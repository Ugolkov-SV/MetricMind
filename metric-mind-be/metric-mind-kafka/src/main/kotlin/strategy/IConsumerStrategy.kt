package io.ugolkov.metric_mind.kafka.strategy

import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.kafka.InputOutputTopics
import io.ugolkov.metric_mind.kafka.KafkaConfig

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy<T : BaseContext> {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: KafkaConfig): InputOutputTopics

    /**
     * Сериализатор для версии API
     */
    fun serialize(source: T): String

    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: T)
}