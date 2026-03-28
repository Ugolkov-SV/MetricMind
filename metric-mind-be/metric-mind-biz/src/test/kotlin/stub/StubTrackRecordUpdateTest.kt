package stub

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.model.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StubTrackRecordUpdateTest {
    private val processor: MmProcessor = MmProcessor(MmCorSettings.NONE)

    @Test
    fun update() = runTest {
        val trackRecordRequest = createTrackRecordRequest()
        val context = MmContext(
            command = MmCommand.UPDATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.SUCCESS,
            trackRecordRequest = trackRecordRequest,
        )

        processor.exec(context)

        with(context.trackRecordResponse.first()) {
            assertEquals(trackRecordRequest.trackRecordId, this.trackRecordId)
            assertEquals(trackRecordRequest.trackId, this.trackId)
            assertEquals(trackRecordRequest.value, this.value)
            assertEquals(trackRecordRequest.date, this.date)
        }
    }

    @Test
    fun badValue() = runTest {
        val trackRecordRequest = createTrackRecordRequest(Double.NaN)
        val context = MmContext(
            command = MmCommand.UPDATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_VALUE,
            trackRecordRequest = trackRecordRequest,
        )

        processor.exec(context)

        assertTrue(context.trackResponse.isEmpty())
        with(context.errors.first()) {
            assertEquals("validation-value", this.code)
            assertEquals("value", this.field)
            assertEquals("Wrong value field", this.message)
        }
    }

    @Test
    fun dbError() = runTest {
        val trackRecordRequest = createTrackRecordRequest()
        val context = MmContext(
            command = MmCommand.UPDATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.DB_ERROR,
            trackRecordRequest = trackRecordRequest,
        )

        processor.exec(context)

        assertTrue(context.trackResponse.isEmpty())
        with(context.errors.first()) {
            assertEquals("internal-db", this.code)
            assertEquals("Internal error", this.message)
        }
    }

    @Test
    fun noCase() = runTest {
        val trackRecordRequest = createTrackRecordRequest()
        val context = MmContext(
            command = MmCommand.UPDATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_ID,
            trackRecordRequest = trackRecordRequest,
        )

        processor.exec(context)

        assertTrue(context.trackResponse.isEmpty())
        with(context.errors.first()) {
            assertEquals("validation", this.code)
            assertEquals("stub", this.field)
        }
    }

    private fun createTrackRecordRequest(
        value: Double = 3.0,
    ): MmTrackRecord =
        MmTrackRecord(
            trackRecordId = MmTrackId(7),
            trackId = MmTrackId(1L),
            value = value,
            date = 2026L,
        )
}