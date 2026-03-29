package validation

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.*
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationTrackDateCorrect(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = MmContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            trackRequest = MmTrack(
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
        assertEquals("abc", ctx.trackValidated.title)
    }

fun validationTrackDateNotFuture(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = MmContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            trackRequest = MmTrack(
                id = MmTrackId(7L),
                title = "abc",
                type = MmTrackType.NUMBER,
                createDate = Long.MAX_VALUE,
                unit = "kg",
                visibility = MmVisibility.PRIVATE,
            ),
        )
        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)
        assertEquals(MmState.FAILING, ctx.state)
        val error = ctx.errors.firstOrNull()
        assertEquals("createDate", error?.field)
        assertContains(error?.message ?: "", "createDate")
    }

fun validationTrackRecordDateCorrect(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = MmContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            trackRecordRequest = MmTrackRecord(
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

fun validationTrackRecordDateNotFuture(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = MmContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            trackRecordRequest = MmTrackRecord(
                trackRecordId = MmTrackId(7L),
                trackId = MmTrackId(7L),
                value = 10.0,
                date = Long.MAX_VALUE,
            ),
        )
        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)
        assertEquals(MmState.FAILING, ctx.state)
        val error = ctx.errors.firstOrNull()
        assertEquals("date", error?.field)
        assertContains(error?.message ?: "", "date")
    }