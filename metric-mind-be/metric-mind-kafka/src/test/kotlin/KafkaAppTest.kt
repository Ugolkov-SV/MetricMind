import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.apiV1RequestSerialize
import io.ugolkov.metric_mind.api.v1.apiV1ResponseDeserialize
import io.ugolkov.metric_mind.kafka.KafkaApp
import io.ugolkov.metric_mind.kafka.KafkaConfig
import io.ugolkov.metric_mind.kafka.strategy.ConsumerStrategyTrackFilterV1
import io.ugolkov.metric_mind.kafka.strategy.ConsumerStrategyTrackRecordV1
import io.ugolkov.metric_mind.kafka.strategy.ConsumerStrategyTrackV1
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.internals.AutoOffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class KafkaAppTest {
    @Test
    fun runKafkaApp() {
        val config = KafkaConfig()
        val consumer = MockConsumer<String, String>(AutoOffsetResetStrategy.EARLIEST.name())
        val producer = MockProducer(true, null, StringSerializer(), StringSerializer())

        val app = KafkaApp(
            config,
            listOf(
                ConsumerStrategyTrackV1(),
                ConsumerStrategyTrackRecordV1(),
                ConsumerStrategyTrackFilterV1(),
            ),
            consumer,
            producer,
        )

        val trackTopic = TopicPartition(config.trackTopicInV1, PARTITION)
        val trackRecordTopic = TopicPartition(config.trackRecordTopicInV1, PARTITION)
        val trackFilterTopic = TopicPartition(config.trackFilterTopicInV1, PARTITION)

        consumer.schedulePollTask {
            consumer.rebalance(listOf(trackTopic, trackRecordTopic, trackFilterTopic))
            consumer.addRecord(
                ConsumerRecord(
                    config.trackTopicInV1,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        TrackCreateRq(
                            track = TrackWrite(
                                title = "Вес",
                                type = TrackType.NUMBER,
                                createDate = 0L,
                                visibility = Visibility.PRIVATE,
                            ),
                            debug = Debug(
                                mode = Mode.STUB,
                                stub = Stubs.SUCCESS,
                            )
                        )
                    )
                )
            )
            consumer.addRecord(
                ConsumerRecord(
                    config.trackRecordTopicInV1,
                    PARTITION,
                    1L,
                    "test-2",
                    apiV1RequestSerialize(
                        TrackRecordCreateRq(
                            trackRecord = TrackRecord(
                                trackRecordId = 7L,
                                trackId = 7L,
                                value = 10.0,
                                date = 2026L,
                            ),
                            debug = Debug(
                                mode = Mode.STUB,
                                stub = Stubs.SUCCESS,
                            )
                        )
                    )
                )
            )
            app.close()
        }

        val startOffsets = mutableMapOf(
            trackTopic to 0L,
            trackRecordTopic to 0L,
            trackFilterTopic to 0L,
        )
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<TrackCreateRs>(message.value())

        assertEquals(config.trackTopicOutV1, message.topic())
        assertNotNull(result.track)
    }

    companion object {
        const val PARTITION = 0
    }
}