package io.ugolkov.metric_mind.common

import io.ugolkov.metric_mind.common.model.*
import kotlin.time.Instant

data class MmContext(
    var command: MmCommand = MmCommand.NONE,
    var state: MmState = MmState.NONE,
    val errors: MutableList<MmError> = mutableListOf(),

    var workMode: MmWorkMode = MmWorkMode.REAL,
    var stubCase: MmStubs = MmStubs.NONE,

    var requestId: MmRequestId = MmRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var trackRequest: MmTrack = MmTrack.NONE,
    var trackRecordRequest: MmTrackRecord = MmTrackRecord.NONE,
    var trackFilterRequest: MmTrackFilter = MmTrackFilter.NONE,

    var trackResponse: MutableList<MmTrack> = mutableListOf(),
    var trackRecordResponse: MutableList<MmTrackRecord> = mutableListOf(),
)
