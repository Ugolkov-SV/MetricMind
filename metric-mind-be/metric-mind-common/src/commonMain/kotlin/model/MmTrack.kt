package io.ugolkov.metric_mind.common.model

data class MmTrack(
    var id: MmTrackId = MmTrackId.NONE,
    var title: String = "",
    var type: MmTrackType = MmTrackType.NONE,
    var createDate: Long = 0L,
    var unit: String = "",
    var visibility: MmVisibility = MmVisibility.NONE,
    var owner: MmUserId = MmUserId.NONE,
) {
    fun isNotNone(): Boolean =
        this !== NONE

    companion object {
        val NONE = MmTrack()
    }
}
