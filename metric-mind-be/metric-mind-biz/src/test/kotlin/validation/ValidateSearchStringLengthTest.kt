package validation

import io.ugolkov.metric_mind.biz.validation.validateSearchStringLength
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.common.model.MmTrackFilter
import io.ugolkov.metric_mind.cor.rootChain
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() =
        runTest {
            val ctx = MmContext(state = MmState.RUNNING, trackFilterValidating = MmTrackFilter(searchString = ""))
            chain.exec(ctx)
            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(0, ctx.errors.size)
        }

    @Test
    fun blankString() =
        runTest {
            val ctx = MmContext(state = MmState.RUNNING, trackFilterValidating = MmTrackFilter(searchString = "  "))
            chain.exec(ctx)
            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(0, ctx.errors.size)
        }

    @Test
    fun shortString() =
        runTest {
            val ctx = MmContext(state = MmState.RUNNING, trackFilterValidating = MmTrackFilter(searchString = "12"))
            chain.exec(ctx)
            assertEquals(MmState.FAILING, ctx.state)
            assertEquals(1, ctx.errors.size)
            assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
        }

    @Test
    fun normalString() =
        runTest {
            val ctx = MmContext(state = MmState.RUNNING, trackFilterValidating = MmTrackFilter(searchString = "123"))
            chain.exec(ctx)
            assertEquals(MmState.RUNNING, ctx.state)
            assertEquals(0, ctx.errors.size)
        }

    @Test
    fun longString() =
        runTest {
            val ctx = MmContext(
                state = MmState.RUNNING,
                trackFilterValidating = MmTrackFilter(searchString = "12".repeat(51))
            )
            chain.exec(ctx)
            assertEquals(MmState.FAILING, ctx.state)
            assertEquals(1, ctx.errors.size)
            assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
        }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }
    }
}
