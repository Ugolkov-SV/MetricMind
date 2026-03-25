package io.ugolkov.metric_mind.logger.model

import kotlinx.serialization.Serializable

@Serializable
data class CommonLogModel(
    val messageTime: String? = null,
    val logId: String? = null,
    val source: String? = null,
    val track: MmLogModel? = null,
    val trackRecord: MmLogModel? = null,
    val errors: List<ErrorLogModel>? = null
)
