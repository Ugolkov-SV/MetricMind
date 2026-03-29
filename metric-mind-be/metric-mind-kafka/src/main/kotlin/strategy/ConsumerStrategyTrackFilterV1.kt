package io.ugolkov.metric_mind.kafka.strategy

import io.ugolkov.api.v1.models.IRequest
import io.ugolkov.metric_mind.api.v1.apiV1RequestDeserialize
import io.ugolkov.metric_mind.api.v1.apiV1ResponseSerialize
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.TrackFilterContext
import io.ugolkov.metric_mind.kafka.InputOutputTopics
import io.ugolkov.metric_mind.kafka.KafkaConfig

class ConsumerStrategyTrackFilterV1 : IConsumerStrategy<TrackFilterContext> {
    override fun topics(config: KafkaConfig): InputOutputTopics =
        InputOutputTopics(config.trackFilterTopicInV1, config.trackFilterTopicOutV1)

    override fun serialize(source: TrackFilterContext): String {
        val response = source.toTransport()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: TrackFilterContext) {
        val request = apiV1RequestDeserialize<IRequest>(value)
        target.fromTransport(request)
    }
}