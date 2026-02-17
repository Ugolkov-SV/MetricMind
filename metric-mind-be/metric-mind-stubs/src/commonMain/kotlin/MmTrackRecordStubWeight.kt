package ru.otus.otuskotlin.marketplace.stubs

import io.ugolkov.metric_mind.common.model.MmTrackId
import io.ugolkov.metric_mind.common.model.MmTrackRecord

object MmTrackRecordStubWeight {
    val WEIGHT_RECORD_1: MmTrackRecord
        get() = MmTrackRecord(
            trackId = MmTrackId(123),
            value = 84.5,
            date = 0,
        )
}
