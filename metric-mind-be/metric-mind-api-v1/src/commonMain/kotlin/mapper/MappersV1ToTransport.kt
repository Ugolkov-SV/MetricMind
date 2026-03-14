package io.ugolkov.metric_mind.api.v1.mappers

import io.ugolkov.api.v1.models.*
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.exception.NotValidCommand
import io.ugolkov.metric_mind.common.model.*

fun MmContext.toTransport(): IResponse =
    when (val cmd = command) {
        MmCommand.CREATE if !trackRequest.isEmpty() -> toTransportTrackCreate()
        MmCommand.READ if !trackRequest.isEmpty() -> toTransportTrackRead()
        MmCommand.UPDATE if !trackRequest.isEmpty() -> toTransportTrackUpdate()
        MmCommand.DELETE if !trackRequest.isEmpty() -> toTransportTrackDelete()
        MmCommand.SEARCH if !trackFilterRequest.isEmpty() -> toTransportTrackSearch()
        MmCommand.CREATE if !trackRecordRequest.isEmpty() -> toTransportTrackRecordCreate()
        MmCommand.READ if !trackRecordRequest.isEmpty() -> toTransportTrackRecordRead()
        MmCommand.UPDATE if !trackRecordRequest.isEmpty() -> toTransportTrackRecordUpdate()
        MmCommand.DELETE if !trackRecordRequest.isEmpty() -> toTransportTrackRecordDelete()
        else -> throw NotValidCommand(cmd)
    }

fun MmContext.toTransportTrackCreate(): TrackCreateRs =
    TrackCreateRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        track = trackResponse.firstOrNull()?.id?.toTransport(),
    )

fun MmContext.toTransportTrackRead(): TrackReadRs =
    TrackReadRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        track = trackResponse.firstOrNull()?.toTransport()
    )

fun MmContext.toTransportTrackUpdate(): TrackUpdateRs =
    TrackUpdateRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
    )

fun MmContext.toTransportTrackDelete(): TrackDeleteRs =
    TrackDeleteRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        track = trackResponse.firstOrNull()?.id?.toTransport(),
    )

fun MmContext.toTransportTrackSearch(): TrackSearchRs =
    TrackSearchRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        tracks = trackResponse.toTransportTrack(),
    )

fun MmContext.toTransportTrackRecordCreate(): TrackRecordCreateRs =
    TrackRecordCreateRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
    )

fun MmContext.toTransportTrackRecordRead(): TrackRecordReadRs =
    TrackRecordReadRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        trackRecords = trackRecordResponse.toTransportTrackRecord(),
    )

fun MmContext.toTransportTrackRecordUpdate(): TrackRecordUpdateRs =
    TrackRecordUpdateRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
    )

fun MmContext.toTransportTrackRecordDelete(): TrackRecordDeleteRs =
    TrackRecordDeleteRs(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
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
