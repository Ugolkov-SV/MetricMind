package stub

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.TrackFilterContext
import io.ugolkov.metric_mind.common.model.MmCommand
import io.ugolkov.metric_mind.common.model.MmStubs
import io.ugolkov.metric_mind.common.model.MmTrackFilter
import io.ugolkov.metric_mind.common.model.MmWorkMode
import io.ugolkov.metric_mind.stubs.MmTrackStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StubTrackSearchTest {
    private val processor: MmProcessor = MmProcessor(MmCorSettings.NONE)

    @Test
    fun search() = runTest {
        val context = TrackFilterContext(
            command = MmCommand.SEARCH,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.SUCCESS,
            request = MmTrackFilter(searchString = "Test track"),
        )

        processor.exec(context)

        with(context.response.first()) {
            val expected = MmTrackStub.get()
            assertEquals(expected.id, this.id)
            assertEquals(expected.title, this.title)
            assertEquals(expected.type, this.type)
            assertEquals(expected.createDate, this.createDate)
            assertEquals(expected.unit, this.unit)
            assertEquals(expected.visibility, this.visibility)
        }
    }

    @Test
    fun dbError() = runTest {
        val context = TrackFilterContext(
            command = MmCommand.SEARCH,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.DB_ERROR,
            request = MmTrackFilter(searchString = "Test track"),
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
        val context = TrackFilterContext(
            command = MmCommand.SEARCH,
            workMode = MmWorkMode.STUB,
            stubCase = MmStubs.BAD_TITLE,
            request = MmTrackFilter(searchString = "Test track"),
        )

        processor.exec(context)

        assertTrue(context.response.isEmpty())
        with(context.errors.first()) {
            assertEquals("validation", this.code)
            assertEquals("stub", this.field)
        }
    }
}