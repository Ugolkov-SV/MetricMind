package io.ugolkov.metric_mind.api.v1.mappers

import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.TrackFilterContext
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.common.exception.NotValidCommand
import io.ugolkov.metric_mind.common.model.*

fun BaseContext.toTransport(): IResponse =
    when (val cmd = command) {
        MmCommand.CREATE if this is TrackContext -> toTransportTrackCreate()
        MmCommand.READ if this is TrackContext -> toTransportTrackRead()
        MmCommand.UPDATE if this is TrackContext -> toTransportTrackUpdate()
        MmCommand.DELETE if this is TrackContext -> toTransportTrackDelete()
        MmCommand.SEARCH if this is TrackFilterContext -> toTransportTrackSearch()
        MmCommand.CREATE if this is TrackRecordContext -> toTransportTrackRecordCreate()
        MmCommand.READ if this is TrackRecordContext -> toTransportTrackRecordRead()
        MmCommand.UPDATE if this is TrackRecordContext -> toTransportTrackRecordUpdate()
        MmCommand.DELETE if this is TrackRecordContext -> toTransportTrackRecordDelete()
        else -> throw NotValidCommand(cmd)
    }

fun TrackContext.toTransportTrackCreate(): TrackCreateRs =
    TrackCreateRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        track = response.firstOrNull()?.id?.toTransport(),
    )

fun TrackContext.toTransportTrackRead(): TrackReadRs =
    TrackReadRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        track = response.firstOrNull()?.toTransport()
    )

fun TrackContext.toTransportTrackUpdate(): TrackUpdateRs =
    TrackUpdateRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
    )

fun TrackContext.toTransportTrackDelete(): TrackDeleteRs =
    TrackDeleteRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        track = response.firstOrNull()?.id?.toTransport(),
    )

fun TrackFilterContext.toTransportTrackSearch(): TrackSearchRs =
    TrackSearchRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        tracks = response.toTransportTrack(),
    )

fun TrackRecordContext.toTransportTrackRecordCreate(): TrackRecordCreateRs =
    TrackRecordCreateRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        trackRecord = response.firstOrNull()?.trackRecordId?.toTransport(),
    )

fun TrackRecordContext.toTransportTrackRecordRead(): TrackRecordReadRs =
    TrackRecordReadRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        trackRecords = response.toTransportTrackRecord(),
    )

fun TrackRecordContext.toTransportTrackRecordUpdate(): TrackRecordUpdateRs =
    TrackRecordUpdateRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
    )

fun TrackRecordContext.toTransportTrackRecordDelete(): TrackRecordDeleteRs =
    TrackRecordDeleteRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        trackRecord = response.firstOrNull()?.trackRecordId?.toTransport(),
    )

private fun List<MmTrack>.toTransportTrack(): List<TrackReadWithOwner>? =
    this.map { it.toTransport() }
        .takeIf { it.isNotEmpty() }

private fun MmTrack.toTransport(): TrackReadWithOwner =
    TrackReadWithOwner(
        id = id.asLong(),
        title = title,
        type = type.toTransport(),
        unit = unit,
        createDate = createDate,
        visibility = visibility.toTransport(),
        owner = owner.asString(),
    )

private fun List<MmTrackRecord>.toTransportTrackRecord(): List<TrackRecord>? =
    this.map { it.toTransport() }
        .takeIf { it.isNotEmpty() }

internal fun MmTrackRecord.toTransport(): TrackRecord =
    TrackRecord(
        trackRecordId = this.trackRecordId.asLong(),
        trackId = this.trackId.asLong(),
        value = this.value,
        date = this.date,
    )

internal fun MmTrackId.toTransport(): TrackId =
    this.asLong()
        .let(::TrackId)

internal fun MmTrackType.toTransport(): TrackType =
    when (this) {
        MmTrackType.NUMBER -> TrackType.NUMBER
        MmTrackType.INC -> TrackType.INC
        MmTrackType.DURATION -> TrackType.DURATION
        MmTrackType.NONE -> TrackType.NUMBER
    }

internal fun MmVisibility.toTransport(): Visibility =
    when (this) {
        MmVisibility.PRIVATE -> Visibility.PRIVATE
        MmVisibility.INTERNAL -> Visibility.INTERNAL
        MmVisibility.PUBLIC -> Visibility.PUBLIC
        MmVisibility.NONE -> Visibility.PRIVATE
    }

private fun List<MmError>.toTransportErrors(): List<ResponseError>? =
    this.map { it.toTransportError() }
        .toList()
        .takeIf { it.isNotEmpty() }

private fun MmError.toTransportError(): ResponseError =
    ResponseError(
        code = code.takeIf { it.isNotBlank() } ?: "Unknown code",
        message = message.takeIf { it.isNotBlank() } ?: "Unknown error",
        field = field.takeIf { it.isNotBlank() },
    )

private fun MmState.toResult(): ResponseResult =
    when (this) {
        MmState.RUNNING, MmState.FINISHING -> ResponseResult.SUCCESS
        else -> ResponseResult.ERROR
    }
