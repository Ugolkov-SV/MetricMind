package ru.otus.otuskotlin.marketplace.stubs

import io.ugolkov.metric_mind.common.model.MmTrack
import ru.otus.otuskotlin.marketplace.stubs.MmTrackStubWeight.WEIGHT_1

object MmTrackStub {
    fun get(): MmTrack = WEIGHT_1.copy()

    fun prepareResult(block: MmTrack.() -> Unit): MmTrack =
        get().apply(block)

}
