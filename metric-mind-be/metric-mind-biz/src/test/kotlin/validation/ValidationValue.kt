package validation

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.*
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationValueCorrect(command: MmCommand, processor: MmProcessor) =
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

fun validationValue(command: MmCommand, processor: MmProcessor) =
    runTest {
        val ctx = MmContext(
            command = command,
            state = MmState.NONE,
            workMode = MmWorkMode.TEST,
            trackRecordRequest = MmTrackRecord(
                trackRecordId = MmTrackId(7L),
                trackId = MmTrackId(7L),
                value = Double.NaN,
                date = 2026,
            ),
        )
        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)
        assertEquals(MmState.FAILING, ctx.state)
        val error = ctx.errors.firstOrNull()
        assertEquals("value", error?.field)
        assertContains(error?.message ?: "", "value")
    }