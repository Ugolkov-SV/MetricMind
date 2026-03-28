package stub

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.stubs.MmTrackStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StubTrackReadTest {
    private val processor: MmProcessor = MmProcessor(MmCorSettings.NONE)

    @Test
    fun read() = runTest {
        val context = MmContext(
            command = MmCommand.READ,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.SUCCESS,
            trackRequest = MmTrack(
                id = MmTrackId(7L),
            ),
        )

        processor.exec(context)

        with(context.trackResponse.first()) {
            val expected = MmTrackStub.get()
            assertEquals(7L, this.id.asLong())
            assertEquals(expected.title, this.title)
            assertEquals(expected.type, this.type)
            assertEquals(expected.createDate, this.createDate)
            assertEquals(expected.unit, this.unit)
            assertEquals(expected.visibility, this.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val context = MmContext(
            command = MmCommand.READ,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_ID,
            trackRequest = MmTrack(id = MmTrackId(0L)),
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
        val context = MmContext(
            command = MmCommand.READ,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.DB_ERROR,
            trackRequest = MmTrack(id = MmTrackId(7L)),
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
        val context = MmContext(
            command = MmCommand.READ,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_TITLE,
            trackRequest = MmTrack(id = MmTrackId(7L)),
        )

        processor.exec(context)

        assertTrue(context.trackResponse.isEmpty())
        with(context.errors.first()) {
            assertEquals("validation", this.code)
            assertEquals("stub", this.field)
        }
    }

    private fun createTrackRequest(
        title: String = "Test track",
    ): MmTrack =
        MmTrack(
            id = MmTrackId(7),
            title = title,
            type = MmTrackType.NUMBER,
            createDate = 2026L,
            unit = "kg",
            visibility = MmVisibility.PRIVATE,
        )
}