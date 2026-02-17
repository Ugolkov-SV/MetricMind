package ru.otus.otuskotlin.marketplace.stubs

import io.ugolkov.metric_mind.common.model.MmTrackRecord
import ru.otus.otuskotlin.marketplace.stubs.MmTrackRecordStubWeight.WEIGHT_RECORD_1

object MmTrackRecordStub {
    fun get(): MmTrackRecord = WEIGHT_RECORD_1.copy()

    fun prepareResult(block: MmTrackRecord.() -> Unit): MmTrackRecord =
        get().apply(block)

}
