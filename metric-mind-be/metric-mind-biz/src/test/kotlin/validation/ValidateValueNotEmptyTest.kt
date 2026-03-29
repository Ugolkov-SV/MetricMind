package validation

import io.ugolkov.metric_mind.biz.validation.validateValueNotEmpty
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmTrackRecord
import io.ugolkov.metric_mind.cor.rootChain
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateValueNotEmptyTest {
    @Test
    fun correct() =
        runTest {
            val ctx = MmContext(state = MmState.RUNNING, trackRecordValidating = MmTrackRecord(value = 20.0))
            rootChain {
                validateValueNotEmpty(title = "Title")
            }.exec(ctx)
            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(0, ctx.errors.size)
        }

    @Test
    fun nan() =
        runTest {
            val ctx = MmContext(state = MmState.RUNNING, trackRecordValidating = MmTrackRecord(value = Double.NaN))
            rootChain {
                validateValueNotEmpty(title = "Title")
            }.exec(ctx)
            assertEquals(MmState.FAILING, ctx.state)
            assertEquals(1, ctx.errors.size)
        }
}