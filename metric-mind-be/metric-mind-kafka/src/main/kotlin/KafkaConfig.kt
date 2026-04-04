package io.ugolkov.metric_mind.kafka

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.IMmAppSettings
import io.ugolkov.metric_mind.common.IProcessor
import io.ugolkov.metric_mind.common.MmCorSettings

data class KafkaConfig(
    override val corSettings: MmCorSettings = MmCorSettings(),
    override val processor: IProcessor = MmProcessor(corSettings),
    val hosts: List<String> = KAFKA_HOSTS,
    val groupId: String = KAFKA_GROUP_ID,
    private val trackTopicV1: String = TRACK_TOPIC_V1,
    private val trackRecordTopicV1: String = TRACK_RECORD_TOPIC_V1,
    private val trackFilterTopicV1: String = TRACK_FILTER_TOPIC_V1,
) : IMmAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"
        const val TRACK_TOPIC_V1_VAR = "KAFKA_TRACK_TOPIC_V1"
        const val TRACK_RECORD_TOPIC_V1_VAR = "KAFKA_TRACK_RECORD_TOPIC_V1"
        const val TRACK_FILTER_TOPIC_V1_VAR = "KAFKA_TRACK_FILTER_TOPIC_V1"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "localhost:9092").split("\\s*[,; ]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "metric-mind" }
        val TRACK_TOPIC_V1 by lazy { System.getenv(TRACK_TOPIC_V1_VAR) ?: "metric-mind-track-v1" }
        val TRACK_RECORD_TOPIC_V1 by lazy { System.getenv(TRACK_RECORD_TOPIC_V1_VAR) ?: "metric-mind-track-record-v1" }
        val TRACK_FILTER_TOPIC_V1 by lazy { System.getenv(TRACK_FILTER_TOPIC_V1_VAR) ?: "metric-mind-track-filter-v1" }
    }

    val trackTopicInV1: String = "$trackTopicV1-in"
    val trackTopicOutV1: String = "$trackTopicV1-out"

    val trackRecordTopicInV1: String = "$trackRecordTopicV1-in"
    val trackRecordTopicOutV1: String = "$trackRecordTopicV1-out"

    val trackFilterTopicInV1: String = "$trackFilterTopicV1-in"
    val trackFilterTopicOutV1: String = "$trackFilterTopicV1-out"
}
