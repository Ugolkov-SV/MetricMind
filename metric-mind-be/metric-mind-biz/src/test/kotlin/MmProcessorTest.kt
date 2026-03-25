import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.model.*
import io.ugolkov.metric_mind.stubs.MmTrackRecordStub
import io.ugolkov.metric_mind.stubs.MmTrackStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MmProcessorTest {

    private val processor: MmProcessor = MmProcessor(MmCorSettings())

    @Test
    fun trackProcessor() =
        runTest {
            val ctx = MmContext()
            ctx.trackRequest = MmTrack(
                id = MmTrackId(1),
            )
            processor.exec(ctx)

            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(listOf(MmTrackStub.get()), ctx.trackResponse)
        }

    @Test
    fun trackSearchProcessor() =
        runTest {
            val ctx = MmContext()
            ctx.trackFilterRequest = MmTrackFilter(
                searchString = "text",
            )
            processor.exec(ctx)

            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(listOf(MmTrackStub.get()), ctx.trackResponse)
        }

    @Test
    fun trackRecordProcessor() =
        runTest {
            val ctx = MmContext()
            ctx.trackRecordRequest = MmTrackRecord(
                trackRecordId = MmTrackId(1),
            )
            processor.exec(ctx)

            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(listOf(MmTrackRecordStub.get()), ctx.trackRecordResponse)
        }

    @Test
    fun processorFailed() =
        runTest {
            val ctx = MmContext()
            processor.exec(ctx)

            assertEquals(MmState.FAILING, ctx.state)
        }
}