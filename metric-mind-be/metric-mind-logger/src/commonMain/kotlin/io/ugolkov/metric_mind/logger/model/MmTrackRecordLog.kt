package io.ugolkov.metric_mind.logger.model

import kotlinx.serialization.Serializable

@Serializable
data class MmTrackRecordLog(
    val trackId: String? = null,
    val value: String? = null,
    val date: String? = null,
)
