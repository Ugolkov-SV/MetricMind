package io.ugolkov.metric_mind.api.v1.mappers

import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.TrackFilterContext
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.common.model.*

fun BaseContext.fromTransport(request: IRequest) =
    when (request) {
        is TrackCreateRq if this is TrackContext -> fromTransport(request)
        is TrackReadRq if this is TrackContext -> fromTransport(request)
        is TrackUpdateRq if this is TrackContext -> fromTransport(request)
        is TrackDeleteRq if this is TrackContext -> fromTransport(request)
        is TrackSearchRq if this is TrackFilterContext -> fromTransport(request)
        is TrackRecordCreateRq if this is TrackRecordContext -> fromTransport(request)
        is TrackRecordReadRq if this is TrackRecordContext -> fromTransport(request)
        is TrackRecordUpdateRq if this is TrackRecordContext -> fromTransport(request)
        is TrackRecordDeleteRq if this is TrackRecordContext -> fromTransport(request)
        else -> Unit
    }

private fun TrackContext.fromTransport(request: TrackCreateRq) {
    this.command = MmCommand.CREATE
    this.workMode = request.debug.transportToWorkMode()
    this.stubCase = request.debug.transportToStubCase()
    this.request = request.track.toInternal()
}

private fun TrackContext.fromTransport(request: TrackReadRq) {
    this.command = MmCommand.READ
    this.workMode = request.debug.transportToWorkMode()
    this.stubCase = request.debug.transportToStubCase()
    this.request = request.track.asTrack()
}

private fun TrackContext.fromTransport(request: TrackUpdateRq) {
    this.command = MmCommand.UPDATE
    this.workMode = request.debug.transportToWorkMode()
    this.stubCase = request.debug.transportToStubCase()
    this.request = request.track.toInternal()
}

private fun TrackContext.fromTransport(request: TrackDeleteRq) {
    this.command = MmCommand.DELETE
    this.workMode = request.debug.transportToWorkMode()
    this.stubCase = request.debug.transportToStubCase()
    this.request = request.track.asTrack()
}

private fun TrackFilterContext.fromTransport(request: TrackSearchRq) {
    this.command = MmCommand.SEARCH
    this.workMode = request.debug.transportToWorkMode()
    this.stubCase = request.debug.transportToStubCase()
    this.request = request.filter.toInternal()
}

private fun TrackRecordContext.fromTransport(request: TrackRecordCreateRq) {
    this.command = MmCommand.CREATE
    this.workMode = request.debug.transportToWorkMode()
    this.stubCase = request.debug.transportToStubCase()
    this.request = request.trackRecord.toInternal()
}

private fun TrackRecordContext.fromTransport(request: TrackRecordReadRq) {
    this.command = MmCommand.READ
    this.workMode = request.debug.transportToWorkMode()
    this.stubCase = request.debug.transportToStubCase()
    this.request = request.track.asTrackRecord()
}

private fun TrackRecordContext.fromTransport(request: TrackRecordUpdateRq) {
    this.command = MmCommand.UPDATE
    this.workMode = request.debug.transportToWorkMode()
    this.stubCase = request.debug.transportToStubCase()
    this.request = request.trackRecord.toInternal()
}

private fun TrackRecordContext.fromTransport(request: TrackRecordDeleteRq) {
    this.command = MmCommand.DELETE
    this.workMode = request.debug.transportToWorkMode()
    this.stubCase = request.debug.transportToStubCase()
    this.request = request.trackRecord.toInternal()
}

private fun TrackWrite.toInternal(): MmTrack =
    MmTrack(
        title = this.title,
        type = this.type.toInternal(),
        createDate = this.createDate,
        unit = this.unit.orEmpty(),
        visibility = this.visibility.toInternal(),
    )

private fun TrackId.asTrack(): MmTrack =
    MmTrack(id = id.toTrackId())

private fun TrackId.asTrackRecord(): MmTrackRecord =
    MmTrackRecord(trackId = id.toTrackId())

private fun TrackUpdateRqAllOfTrack.toInternal(): MmTrack =
    MmTrack(
        id = this.id.toTrackId(),
        title = this.title.orEmpty(),
        unit = this.unit.orEmpty(),
        visibility = this.visibility.toInternal(),
    )

private fun TrackSearchRqAllOfFilter.toInternal(): MmTrackFilter =
    MmTrackFilter(
        searchString = this.searchTrack,
        ownerId = this.owner.toUserId(),
    )

private fun TrackType?.toInternal(): MmTrackType =
    when (this) {
        TrackType.NUMBER -> MmTrackType.NUMBER
        TrackType.INC -> MmTrackType.INC
        TrackType.DURATION -> MmTrackType.DURATION
        null -> MmTrackType.NONE
    }

private fun Visibility?.toInternal(): MmVisibility =
    when (this) {
        Visibility.PRIVATE -> MmVisibility.PRIVATE
        Visibility.INTERNAL -> MmVisibility.INTERNAL
        Visibility.PUBLIC -> MmVisibility.PUBLIC
        null -> MmVisibility.NONE
    }

private fun TrackRecord.toInternal(): MmTrackRecord =
    MmTrackRecord(
        trackId = this.trackId.toTrackId(),
        value = this.value,
        date = this.date,
    )

private fun TrackRecordDeleteRqAllOfTrackRecord.toInternal(): MmTrackRecord =
    MmTrackRecord(
        trackRecordId = this.trackRecordId.toTrackId(),
        date = this.date,
    )

private fun Debug?.transportToWorkMode(): MmWorkMode =
    when (this?.mode) {
        Mode.REAL -> MmWorkMode.REAL
        Mode.STUB -> MmWorkMode.STUB
        null -> MmWorkMode.REAL
    }

private fun Debug?.transportToStubCase(): MmStubs =
    when (this?.stub) {
        Stubs.SUCCESS -> MmStubs.SUCCESS
        Stubs.NOT_FOUND -> MmStubs.NOT_FOUND
        Stubs.BAD_ID -> MmStubs.BAD_ID
        Stubs.BAD_TITLE -> MmStubs.BAD_TITLE
        Stubs.BAD_VISIBILITY -> MmStubs.BAD_VISIBILITY
        Stubs.BAD_SEARCH_STRING -> MmStubs.BAD_SEARCH_STRING
        Stubs.BAD_VALUE -> MmStubs.BAD_VALUE
        Stubs.DB_ERROR -> MmStubs.DB_ERROR
        null -> MmStubs.NONE
    }