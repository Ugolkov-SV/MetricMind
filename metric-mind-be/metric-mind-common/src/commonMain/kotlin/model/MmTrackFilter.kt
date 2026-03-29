package io.ugolkov.metric_mind.common.model

data class MmTrackFilter(
    var searchString: String = "",
    var ownerId: MmUserId = MmUserId.NONE,
) {
    fun isNotNone(): Boolean =
        this !== NONE

    companion object {
        val NONE = MmTrackFilter()
    }
}
