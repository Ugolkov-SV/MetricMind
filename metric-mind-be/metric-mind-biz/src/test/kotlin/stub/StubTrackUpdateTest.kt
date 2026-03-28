package stub

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.model.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StubTrackUpdateTest {
    private val processor: MmProcessor = MmProcessor(MmCorSettings.NONE)

    @Test
    fun update() = runTest {
        val trackRequest = createTrackRequest()
        val context = MmContext(
            command = MmCommand.UPDATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.SUCCESS,
            trackRequest = trackRequest,
        )

        processor.exec(context)

        with(context.trackResponse.first()) {
            assertEquals(trackRequest.id, this.id)
            assertEquals(trackRequest.title, this.title)
            assertEquals(trackRequest.type, this.type)
            assertEquals(trackRequest.createDate, this.createDate)
            assertEquals(trackRequest.unit, this.unit)
            assertEquals(trackRequest.visibility, this.visibility)
        }
    }

    @Test
    fun badTitle() = runTest {
        val trackRequest = createTrackRequest(title = "")
        val context = MmContext(
            command = MmCommand.UPDATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_TITLE,
            trackRequest = trackRequest,
        )

        processor.exec(context)

        assertTrue(context.trackResponse.isEmpty())
        with(context.errors.first()) {
            assertEquals("validation-title", this.code)
            assertEquals("title", this.field)
            assertEquals("Wrong title field", this.message)
        }
    }

    @Test
    fun badVisibility() = runTest {
        val trackRequest = createTrackRequest()
        val context = MmContext(
            command = MmCommand.UPDATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_VISIBILITY,
            trackRequest = trackRequest,
        )

        processor.exec(context)

        assertTrue(context.trackResponse.isEmpty())
        with(context.errors.first()) {
            assertEquals("validation-visibility", this.code)
            assertEquals("visibility", this.field)
            assertEquals("Wrong visibility field", this.message)
        }
    }

    @Test
    fun dbError() = runTest {
        val trackRequest = createTrackRequest()
        val context = MmContext(
            command = MmCommand.UPDATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.DB_ERROR,
            trackRequest = trackRequest,
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
        val trackRequest = createTrackRequest()
        val context = MmContext(
            command = MmCommand.UPDATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_ID,
            trackRequest = trackRequest,
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