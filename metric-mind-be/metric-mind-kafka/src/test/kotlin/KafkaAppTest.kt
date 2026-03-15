import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.api.v1.apiV1RequestSerialize
import io.ugolkov.metric_mind.api.v1.apiV1ResponseDeserialize
import io.ugolkov.metric_mind.kafka.KafkaApp
import io.ugolkov.metric_mind.kafka.KafkaConfig
import io.ugolkov.metric_mind.kafka.strategy.ConsumerStrategyV1
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

        val app = KafkaApp(config, listOf(ConsumerStrategyV1()), consumer, producer)

        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val topic = TopicPartition(inputTopic, PARTITION)

        consumer.schedulePollTask {
            consumer.rebalance(listOf(topic))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
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
            app.close()
        }

        val startOffsets = mutableMapOf(topic to 0L)
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<TrackCreateRs>(message.value())

        assertEquals(outputTopic, message.topic())
        assertNotNull(result.track)
    }

    companion object {
        const val PARTITION = 0
    }
}