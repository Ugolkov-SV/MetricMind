package io.ugolkov.metric_mind.logger.model

import kotlinx.serialization.Serializable

@Serializable
data class MmTrackFilterLog(
    val searchString: String? = null,
    val ownerId: String? = null,
)