package io.ugolkov.metric_mind.kafka.strategy

import io.ugolkov.api.v1.models.IRequest
import io.ugolkov.metric_mind.api.v1.apiV1RequestDeserialize
import io.ugolkov.metric_mind.api.v1.apiV1ResponseSerialize
import io.ugolkov.metric_mind.api.v1.mappers.fromTransport
import io.ugolkov.metric_mind.api.v1.mappers.toTransport
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.kafka.InputOutputTopics
import io.ugolkov.metric_mind.kafka.KafkaConfig

class ConsumerStrategyTrackRecordV1 : IConsumerStrategy<TrackRecordContext> {
    override fun topics(config: KafkaConfig): InputOutputTopics =
        InputOutputTopics(config.trackRecordTopicInV1, config.trackRecordTopicOutV1)

    override fun serialize(source: TrackRecordContext): String {
        val response = source.toTransport()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: TrackRecordContext) {
        val request = apiV1RequestDeserialize<IRequest>(value)
        target.fromTransport(request)
    }
}