package io.ugolkov.metric_mind.biz.logger

import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.TrackFilterContext
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.logger.model.*
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents.Formats.RFC_1123
import kotlin.time.Clock
import kotlin.time.Instant

fun <T : BaseContext> T.toLog(logId: String) =
    CommonLogModel(
        messageTime = Clock.System.now().toString(),
        logId = logId,
        source = "metric-mind",
        model = this.toMmLog(),
        errors = errors.map { it.toLog() },
    )

private fun <T : BaseContext> T.toMmLog(): MmLogModel<*, *>? =
    when (this) {
        is TrackContext -> toMmLog()
        is TrackRecordContext -> toMmLog()
        is TrackFilterContext -> toMmLog()
    }

private fun TrackContext.toMmLog(): MmTrackLogModel? =
    MmTrackLogModel(
        requestId = requestId.takeIf { it != MmRequestId.NONE }?.asString(),
        request = request.takeIf { it != MmTrack.NONE }?.toLog(),
        response = response.takeIf { it.isNotEmpty() }
            ?.filter { it != MmTrack.NONE }
            ?.map { it.toLog() },
    )
        .takeIf { it != MmTrackLogModel() }

private fun TrackRecordContext.toMmLog(): MmTrackRecordLogModel? =
    MmTrackRecordLogModel(
        requestId = requestId.takeIf { it != MmRequestId.NONE }?.asString(),
        request = request.takeIf { it != MmTrackRecord.NONE }?.toLog(),
        response = response.takeIf { it.isNotEmpty() }
            ?.filter { it != MmTrackRecord.NONE }
            ?.map { it.toLog() },
    )
        .takeIf { it != MmTrackRecordLogModel() }

private fun TrackFilterContext.toMmLog(): MmTrackFilterLogModel? =
    MmTrackFilterLogModel(
        requestId = requestId.takeIf { it != MmRequestId.NONE }?.asString(),
        request = request.takeIf { it != MmTrackFilter.NONE }?.toLog(),
        response = response.takeIf { it.isNotEmpty() }
            ?.filter { it != MmTrack.NONE }
            ?.map { it.toLog() },
    )
        .takeIf { it != MmTrackRecordLogModel() }

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