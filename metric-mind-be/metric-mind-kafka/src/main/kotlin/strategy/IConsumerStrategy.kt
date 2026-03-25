package io.ugolkov.metric_mind.kafka.strategy

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.kafka.InputOutputTopics
import io.ugolkov.metric_mind.kafka.KafkaConfig

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: KafkaConfig): InputOutputTopics

    /**
     * Сериализатор для версии API
     */
    fun serialize(source: MmContext): String

    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: MmContext)
}