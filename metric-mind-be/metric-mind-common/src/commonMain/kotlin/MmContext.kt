package io.ugolkov.metric_mind.common

import io.ugolkov.metric_mind.common.model.*
import kotlin.time.Instant

typealias BaseContext = MmContext<*, *>

sealed class MmContext<I, O>(
    var command: MmCommand = MmCommand.NONE,
    var state: MmState = MmState.NONE,

    var workMode: MmWorkMode = MmWorkMode.REAL,
    var stubCase: MmStubs = MmStubs.NONE,

    var requestId: MmRequestId = MmRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    val errors: MutableList<MmError> = mutableListOf(),
) {
    abstract var request: I
    val response: MutableList<O> = mutableListOf()

    abstract var validating: I
    abstract var validated: I
}

data class TrackContext(
    override var request: MmTrack = MmTrack.NONE,
    override var validating: MmTrack = MmTrack.NONE,
    override var validated: MmTrack = MmTrack.NONE,
) : MmContext<MmTrack, MmTrack>() {
    constructor(
        request: MmTrack = MmTrack.NONE,
        command: MmCommand = MmCommand.NONE,
        state: MmState = MmState.NONE,
        workMode: MmWorkMode = MmWorkMode.REAL,
        stubCase: MmStubs = MmStubs.NONE,
    ) : this(request = request) {
        this.command = command
        this.state = state
        this.workMode = workMode
        this.stubCase = stubCase
    }
}

data class TrackRecordContext(
    override var request: MmTrackRecord = MmTrackRecord.NONE,
    override var validating: MmTrackRecord = MmTrackRecord.NONE,
    override var validated: MmTrackRecord = MmTrackRecord.NONE,
) : MmContext<MmTrackRecord, MmTrackRecord>() {
    constructor(
        request: MmTrackRecord = MmTrackRecord.NONE,
        command: MmCommand = MmCommand.NONE,
        state: MmState = MmState.NONE,
        workMode: MmWorkMode = MmWorkMode.REAL,
        stubCase: MmStubs = MmStubs.NONE,
    ) : this(request = request) {
        this.command = command
        this.state = state
        this.workMode = workMode
        this.stubCase = stubCase
    }
}

data class TrackFilterContext(
    override var request: MmTrackFilter = MmTrackFilter.NONE,
    override var validating: MmTrackFilter = MmTrackFilter.NONE,
    override var validated: MmTrackFilter = MmTrackFilter.NONE,
) : MmContext<MmTrackFilter, MmTrack>() {
    constructor(
        request: MmTrackFilter = MmTrackFilter.NONE,
        command: MmCommand = MmCommand.NONE,
        state: MmState = MmState.NONE,
        workMode: MmWorkMode = MmWorkMode.REAL,
        stubCase: MmStubs = MmStubs.NONE,
    ) : this(request = request) {
        this.command = command
        this.state = state
        this.workMode = workMode
        this.stubCase = stubCase
    }
}

inline fun <reified T : BaseContext> createContext(): T =
    when (T::class) {
        TrackContext::class -> TrackContext()
        TrackRecordContext::class -> TrackRecordContext()
        TrackFilterContext::class -> TrackFilterContext()
        else -> error("Unsupported type ${T::class}")
    } as T