package validation

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.*
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationTitleCorrect(command: MmCommand, processor: MmProcessor) =
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

fun validationTitleTrim(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = MmContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            trackRequest = MmTrack(
                id = MmTrackId(7L),
                title = " \n\t abc \t\n ",
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

fun validationTitleEmpty(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = MmContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            trackRequest = MmTrack(
                id = MmTrackId(7L),
                title = "",
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
        assertEquals("title", error?.field)
        assertContains(error?.message ?: "", "title")
    }

fun validationTitleSymbols(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = MmContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            trackRequest = MmTrack(
                id = MmTrackId(7L),
                title = "!@#$%^&*(),.{}",
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
        assertEquals("title", error?.field)
        assertContains(error?.message ?: "", "title")
    }
