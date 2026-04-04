package io.ugolkov.metric_mind.cor

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ChainTest {
    @Test
    fun `chain обрабатывает workers`() =
        runTest {
            fun createWorker(title: String) =
                Worker<TestContext>(
                    title = title,
                    blockOn = { status != TestContext.CorStatus.ERROR },
                    blockHandle = { result += "$title; " }
                )

            val chain = Chain(
                title = "chain",
                execs = listOf(createWorker("worker 1"), createWorker("worker 2")),
            )

            val context = TestContext()
            chain.exec(context)

            assertEquals("worker 1; worker 2; ", context.result)
        }
}