package io.ugolkov.metric_mind.common.model

data class MmTrackRecord(
    var trackRecordId: MmTrackId = MmTrackId.NONE,
    var trackId: MmTrackId = MmTrackId.NONE,
    var value: Double = Double.NaN,
    var date: Long = 0L,
) {
    fun isEmpty(): Boolean =
        this == NONE

    companion object {
        val NONE = MmTrackRecord()
    }
}