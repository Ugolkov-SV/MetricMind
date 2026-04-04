package stub

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.common.model.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StubTrackRecordDeleteTest {
    private val processor: MmProcessor = MmProcessor(MmCorSettings.NONE)

    @Test
    fun delete() = runTest {
        val context = TrackRecordContext(
            command = MmCommand.DELETE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.SUCCESS,
            request = MmTrackRecord(
                trackRecordId = MmTrackId(7L),
                trackId = MmTrackId(3L),
            ),
        )

        processor.exec(context)

        with(context.response.first()) {
            assertEquals(7L, this.trackRecordId.asLong())
        }
    }

    @Test
    fun badId() = runTest {
        val context = TrackRecordContext(
            command = MmCommand.DELETE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_ID,
            request = MmTrackRecord(
                trackRecordId = MmTrackId(0L),
                trackId = MmTrackId(0L),
            ),
        )

        processor.exec(context)

        assertTrue(context.response.isEmpty())
        with(context.errors.first()) {
            assertEquals("validation-id", this.code)
            assertEquals("id", this.field)
            assertEquals("Wrong id field", this.message)
        }
    }

    @Test
    fun dbError() = runTest {
        val trackRecordRequest = createTrackRecordRequest()
        val context = TrackRecordContext(
            command = MmCommand.DELETE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.DB_ERROR,
            request = trackRecordRequest,
        )

        processor.exec(context)

        assertTrue(context.response.isEmpty())
        with(context.errors.first()) {
            assertEquals("internal-db", this.code)
            assertEquals("Internal error", this.message)
        }
    }

    @Test
    fun noCase() = runTest {
        val trackRecordRequest = createTrackRecordRequest()
        val context = TrackRecordContext(
            command = MmCommand.DELETE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_VALUE,
            request = trackRecordRequest,
        )

        processor.exec(context)

        assertTrue(context.response.isEmpty())
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