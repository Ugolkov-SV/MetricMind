package io.ugolkov.metric_mind.biz.logger

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.logger.model.*
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents.Formats.RFC_1123
import kotlin.time.Clock
import kotlin.time.Instant

fun MmContext.toLog(logId: String) =
    CommonLogModel(
        messageTime = Clock.System.now().toString(),
        logId = logId,
        source = "metric-mind",
        track = toMmLog(),
        trackRecord = toMmLog(),
        errors = errors.map { it.toLog() },
    )

private fun MmContext.toMmLog(): MmLogModel? {
    val trackNone = MmTrack()
    val trackRecordNone = MmTrackRecord()
    return MmLogModel(
        requestId = requestId.takeIf { it != MmRequestId.NONE }?.asString(),
        requestTrack = trackRequest.takeIf { it != trackNone }?.toLog(),
        responseTrack = trackResponse.takeIf { it.isNotEmpty() }
            ?.filter { it != trackNone }
            ?.map { it.toLog() },
        requestTrackRecord = trackRecordRequest.takeIf { it != trackRecordNone }?.toLog(),
        responseTrackRecord = trackRecordResponse.takeIf { it.isNotEmpty() }
            ?.filter { it != trackNone }
            ?.map { it.toLog() },
        requestFilter = trackFilterRequest.takeIf { it != MmTrackFilter() }?.toLog(),
    ).takeIf { it != MmLogModel() }
}

private fun MmTrack.toLog(): MmTrackLog =
    MmTrackLog(
        id = this.id.takeIf { it != MmTrackId.NONE }?.asString(),
        title = this.title.takeIf { it.isNotEmpty() },
        type = this.type.name,
        createDate = Instant.fromEpochSeconds(this.createDate).format(RFC_1123),
        unit = this.unit.takeIf { it.isNotEmpty() },
        visibility = this.visibility.name,
        owner = this.owner.asString(),
    )

private fun MmTrackRecord.toLog(): MmTrackRecordLog =
    MmTrackRecordLog(
        trackId = this.trackId.takeIf { it != MmTrackId.NONE }?.asString(),
        value = this.value.takeIf { !it.isNaN() }.toString(),
        date = Instant.fromEpochSeconds(this.date).format(RFC_1123),
    )

private fun MmTrackFilter.toLog(): MmTrackFilterLog =
    MmTrackFilterLog(
        searchString = this.searchString.takeIf { it.isNotBlank() },
        ownerId = this.ownerId.takeIf { it != MmUserId.NONE }?.asString(),
    )

private fun MmError.toLog(): ErrorLogModel =
    ErrorLogModel(
        message = message.takeIf { it.isNotBlank() },
        field = field.takeIf { it.isNotBlank() },
        code = code.takeIf { it.isNotBlank() },
    )