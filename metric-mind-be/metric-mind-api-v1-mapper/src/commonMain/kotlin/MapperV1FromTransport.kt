package io.ugolkov.metric_mind.api.v1.mappers

import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.*

fun MmContext.fromTransport(request: IRequest) =
    when (request) {
        is TrackCreateRq -> fromTransport(request)
        is TrackReadRq -> fromTransport(request)
        is TrackUpdateRq -> fromTransport(request)
        is TrackDeleteRq -> fromTransport(request)
        is TrackSearchRq -> fromTransport(request)
        is TrackRecordCreateRq -> fromTransport(request)
        is TrackRecordReadRq -> fromTransport(request)
        is TrackRecordUpdateRq -> fromTransport(request)
        is TrackRecordDeleteRq -> fromTransport(request)
    }

private fun MmContext.fromTransport(request: TrackCreateRq) {
    command = MmCommand.CREATE
    trackRequest = request.track.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MmContext.fromTransport(request: TrackReadRq) {
    command = MmCommand.READ
    trackRequest = request.track.asTrack()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MmContext.fromTransport(request: TrackUpdateRq) {
    command = MmCommand.UPDATE
    trackRequest = request.track.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MmContext.fromTransport(request: TrackDeleteRq) {
    command = MmCommand.DELETE
    trackRequest = request.track.asTrack()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MmContext.fromTransport(request: TrackSearchRq) {
    command = MmCommand.SEARCH
    trackFilterRequest = request.filter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MmContext.fromTransport(request: TrackRecordCreateRq) {
    command = MmCommand.CREATE
    trackRecordRequest = request.trackRecord.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MmContext.fromTransport(request: TrackRecordReadRq) {
    command = MmCommand.READ
    trackRecordRequest = request.track.asTrackRecord()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MmContext.fromTransport(request: TrackRecordUpdateRq) {
    command = MmCommand.UPDATE
    trackRecordRequest = request.trackRecord.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MmContext.fromTransport(request: TrackRecordDeleteRq) {
    command = MmCommand.DELETE
    trackRecordRequest = request.trackRecord.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
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
        trackId = this.trackId.toTrackId(),
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
        Stubs.DB_ERROR -> MmStubs.DB_ERROR
        null -> MmStubs.NONE
    }