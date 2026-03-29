package validation

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.common.model.*
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationTrackIdCorrect(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = TrackContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            request = MmTrack(
                id = MmTrackId(7L),
                title = "abc",
                type = MmTrackType.NUMBER,
                createDate = 2026,
                unit = "kg",
                visibility = MmVisibility.PRIVATE,
            ),
        )
        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(MmState.FAILING, ctx.state)
    }

fun validationTrackIdZero(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = TrackContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            request = MmTrack(
                id = MmTrackId(0L),
                title = "abc",
                type = MmTrackType.NUMBER,
                createDate = 2026,
                unit = "kg",
                visibility = MmVisibility.PRIVATE,
            ),
        )
        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)
        assertEquals(MmState.FAILING, ctx.state)
        val error = ctx.errors.firstOrNull()
        assertEquals("id", error?.field)
        assertContains(error?.message ?: "", "id")
    }

fun validationTrackRecordIdCorrect(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = TrackRecordContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            request = MmTrackRecord(
                trackRecordId = MmTrackId(7L),
                trackId = MmTrackId(7L),
                value = 10.0,
                date = 2026,
            ),
        )
        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(MmState.FAILING, ctx.state)
    }

fun validationTrackRecordIdZero(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = TrackRecordContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            request = MmTrackRecord(
                trackRecordId = MmTrackId(0L),
                trackId = MmTrackId(7L),
                value = 10.0,
                date = 2026,
            ),
        )
        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)
        assertEquals(MmState.FAILING, ctx.state)
        val error = ctx.errors.firstOrNull()
        assertEquals("trackRecordId", error?.field)
        assertContains(error?.message ?: "", "trackRecordId")
    }

fun validationTrackRecordTrackIdZero(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = TrackRecordContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            request = MmTrackRecord(
                trackRecordId = MmTrackId(7L),
                trackId = MmTrackId(0L),
                value = 10.0,
                date = 2026,
            ),
        )
        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)
        assertEquals(MmState.FAILING, ctx.state)
        val error = ctx.errors.firstOrNull()
        assertEquals("trackId", error?.field)
        assertContains(error?.message ?: "", "trackId")
    }