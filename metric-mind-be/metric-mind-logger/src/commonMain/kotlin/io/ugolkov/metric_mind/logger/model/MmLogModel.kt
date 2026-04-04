package io.ugolkov.metric_mind.logger.model

import kotlinx.serialization.Serializable

@Serializable
data class MmLogModel<I, O>(
    val requestId: String? = null,
    val request: I? = null,
    val response: List<O>? = null,
)

typealias MmTrackLogModel = MmLogModel<MmTrackLog, MmTrackLog>
typealias MmTrackRecordLogModel = MmLogModel<MmTrackRecordLog, MmTrackRecordLog>
typealias MmTrackFilterLogModel = MmLogModel<MmTrackFilterLog, MmTrackLog>