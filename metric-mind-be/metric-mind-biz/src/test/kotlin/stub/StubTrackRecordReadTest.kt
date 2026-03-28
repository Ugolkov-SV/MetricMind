package stub

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.stubs.MmTrackRecordStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StubTrackRecordReadTest {
    private val processor: MmProcessor = MmProcessor(MmCorSettings.NONE)

    @Test
    fun read() = runTest {
        val context = MmContext(
            command = MmCommand.READ,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.SUCCESS,
            trackRecordRequest = MmTrackRecord(
                trackRecordId = MmTrackId(7L),
                trackId = MmTrackId(3L),
            ),
        )

        processor.exec(context)

        with(context.trackRecordResponse.first()) {
            val expected = MmTrackRecordStub.get()
            assertEquals(7L, this.trackRecordId.asLong())
            assertEquals(3L, this.trackId.asLong())
            assertEquals(expected.value, this.value)
            assertEquals(expected.date, this.date)
        }
    }

    @Test
    fun badId() = runTest {
        val context = MmContext(
            command = MmCommand.READ,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_ID,
            trackRecordRequest = MmTrackRecord(
                trackRecordId = MmTrackId(0L),
                trackId = MmTrackId(0L),
            ),
        )

        processor.exec(context)

        assertTrue(context.trackResponse.isEmpty())
        with(context.errors.first()) {
            assertEquals("validation-id", this.code)
            assertEquals("id", this.field)
            assertEquals("Wrong id field", this.message)
        }
    }

    @Test
    fun dbError() = runTest {
        val trackRecordRequest = createTrackRecordRequest()
        val context = MmContext(
            command = MmCommand.READ,
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
            command = MmCommand.READ,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_VALUE,
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