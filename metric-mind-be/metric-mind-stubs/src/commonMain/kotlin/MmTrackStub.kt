package io.ugolkov.metric_mind.stubs

import io.ugolkov.metric_mind.common.model.MmTrack
import io.ugolkov.metric_mind.stubs.MmTrackStubWeight.WEIGHT_1

object MmTrackStub {
    fun get(): MmTrack = WEIGHT_1.copy()

    fun prepareResult(block: MmTrack.() -> Unit): MmTrack =
        get().apply(block)

}
