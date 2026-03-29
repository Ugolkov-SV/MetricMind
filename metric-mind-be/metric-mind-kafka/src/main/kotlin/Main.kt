import io.ugolkov.metric_mind.kafka.KafkaApp
import io.ugolkov.metric_mind.kafka.KafkaConfig
import io.ugolkov.metric_mind.kafka.strategy.ConsumerStrategyTrackFilterV1
import io.ugolkov.metric_mind.kafka.strategy.ConsumerStrategyTrackRecordV1
import io.ugolkov.metric_mind.kafka.strategy.ConsumerStrategyTrackV1

fun main() {
    KafkaApp(
        config = KafkaConfig(),
        consumerStrategies = listOf(
            ConsumerStrategyTrackV1(),
            ConsumerStrategyTrackRecordV1(),
            ConsumerStrategyTrackFilterV1(),
        ),
    )
        .start()
}