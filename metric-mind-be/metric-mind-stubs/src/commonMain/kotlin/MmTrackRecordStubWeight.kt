package io.ugolkov.metric_mind.stubs

import io.ugolkov.metric_mind.common.model.MmTrackId
import io.ugolkov.metric_mind.common.model.MmTrackRecord

object MmTrackRecordStubWeight {
    val WEIGHT_RECORD_1: MmTrackRecord
        get() = MmTrackRecord(
            trackRecordId = MmTrackId(7),
            trackId = MmTrackId(123),
            value = 84.5,
            date = 202603,
        )
}
