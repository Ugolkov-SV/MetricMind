import io.ugolkov.metric_mind.kafka.KafkaApp
import io.ugolkov.metric_mind.kafka.KafkaConfig
import io.ugolkov.metric_mind.kafka.strategy.ConsumerStrategyV1

fun main() {
    KafkaApp(
        config = KafkaConfig(),
        consumerStrategies = listOf(ConsumerStrategyV1()),
    )
        .start()
}