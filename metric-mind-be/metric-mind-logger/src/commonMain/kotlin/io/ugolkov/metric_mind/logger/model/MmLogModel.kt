package io.ugolkov.metric_mind.logger.model

import kotlinx.serialization.Serializable

@Serializable
data class MmLogModel(
    val requestId: String? = null,
    val requestTrack: MmTrackLog? = null,
    val responseTrack: List<MmTrackLog>? = null,
    val requestTrackRecord: MmTrackRecordLog? = null,
    val responseTrackRecord: List<MmTrackRecordLog>? = null,
    val requestFilter: MmTrackFilterLog? = null,
)
