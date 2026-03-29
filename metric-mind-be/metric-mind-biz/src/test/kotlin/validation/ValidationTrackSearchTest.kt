package validation

import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmCommand
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmTrackFilter
import io.ugolkov.metric_mind.common.model.MmWorkMode
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ValidationTrackSearchTest : BaseValidationTest() {
    override val command = MmCommand.SEARCH

    @Test
    fun correctEmpty() =
        runTest {
            val ctx = MmContext(
                command = command,
                state = MmState.NONE,
                workMode = MmWorkMode.TEST,
                trackFilterRequest = MmTrackFilter()
            )
            processor.exec(ctx)
            assertEquals(0, ctx.errors.size)
            assertNotEquals(MmState.FAILING, ctx.state)
        }
}