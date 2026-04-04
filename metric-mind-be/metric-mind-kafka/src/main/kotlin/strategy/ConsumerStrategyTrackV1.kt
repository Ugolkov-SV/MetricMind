package io.ugolkov.metric_mind.kafka.strategy

import io.ugolkov.api.v1.models.IRequest
import io.ugolkov.metric_mind.api.v1.apiV1RequestDeserialize
import io.ugolkov.metric_mind.api.v1.apiV1ResponseSerialize
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.kafka.InputOutputTopics
import io.ugolkov.metric_mind.kafka.KafkaConfig

class ConsumerStrategyTrackV1 : IConsumerStrategy<TrackContext> {
    override fun topics(config: KafkaConfig): InputOutputTopics =
        InputOutputTopics(config.trackTopicInV1, config.trackTopicOutV1)

    override fun serialize(source: TrackContext): String {
        val response = source.toTransport()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: TrackContext) {
        val request = apiV1RequestDeserialize<IRequest>(value)
        target.fromTransport(request)
    }
}