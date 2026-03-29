package validation

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.stubs.MmTrackStub
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = MmTrackStub.get()

fun validationUnitCorrect(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = MmContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            trackRequest = MmTrack(
                id = stub.id,
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
        assertEquals("kg", ctx.trackValidated.unit)
    }

fun validationUnitTrim(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = MmContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            trackRequest = MmTrack(
                id = stub.id,
                title = "abc",
                type = MmTrackType.NUMBER,
                createDate = 2026,
                unit = " \n\tkg \n\t",
                visibility = MmVisibility.PRIVATE,
            ),
        )
        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(MmState.FAILING, ctx.state)
        assertEquals("kg", ctx.trackValidated.unit)
    }

fun validationUnitSymbols(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = MmContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            trackRequest = MmTrack(
                id = stub.id,
                title = "abc",
                type = MmTrackType.NUMBER,
                createDate = 2026,
                unit = "!@#$%^&*(),.{}",
                visibility = MmVisibility.PRIVATE,
            ),
        )
        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)
        assertEquals(MmState.FAILING, ctx.state)
        val error = ctx.errors.firstOrNull()
        assertEquals("unit", error?.field)
        assertContains(error?.message ?: "", "unit")
    }