package stub

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.stubs.MmTrackStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StubTrackCreateTest {
    private val processor: MmProcessor = MmProcessor(MmCorSettings.NONE)

    @Test
    fun create() = runTest {
        val trackRequest = createTrackRequest()
        val context = TrackContext(
            command = MmCommand.CREATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.SUCCESS,
            request = trackRequest,
        )

        processor.exec(context)

        with(context.response.first()) {
            assertEquals(MmTrackStub.get().id, this.id)
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
        val context = TrackContext(
            command = MmCommand.CREATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_TITLE,
            request = trackRequest,
        )

        processor.exec(context)

        assertTrue(context.response.isEmpty())
        with(context.errors.first()) {
            assertEquals("validation-title", this.code)
            assertEquals("title", this.field)
            assertEquals("Wrong title field", this.message)
        }
    }

    @Test
    fun badVisibility() = runTest {
        val trackRequest = createTrackRequest()
        val context = TrackContext(
            command = MmCommand.CREATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_VISIBILITY,
            request = trackRequest,
        )

        processor.exec(context)

        assertTrue(context.response.isEmpty())
        with(context.errors.first()) {
            assertEquals("validation-visibility", this.code)
            assertEquals("visibility", this.field)
            assertEquals("Wrong visibility field", this.message)
        }
    }

    @Test
    fun dbError() = runTest {
        val trackRequest = createTrackRequest()
        val context = TrackContext(
            command = MmCommand.CREATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.DB_ERROR,
            request = trackRequest,
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
        val trackRequest = createTrackRequest()
        val context = TrackContext(
            command = MmCommand.CREATE,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_ID,
            request = trackRequest,
        )

        processor.exec(context)

        assertTrue(context.response.isEmpty())
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