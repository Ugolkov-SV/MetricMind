package io.ugolkov.metric_mind.kafka.strategy

import io.ugolkov.api.v1.models.IRequest
import io.ugolkov.metric_mind.api.v1.apiV1RequestDeserialize
import io.ugolkov.metric_mind.api.v1.apiV1ResponseSerialize
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.kafka.InputOutputTopics
import io.ugolkov.metric_mind.kafka.KafkaConfig

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: KafkaConfig): InputOutputTopics =
        InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)

    override fun serialize(source: MmContext): String {
        val response = source.toTransport()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: MmContext) {
        val request = apiV1RequestDeserialize<IRequest>(value)
        target.fromTransport(request)
    }
}