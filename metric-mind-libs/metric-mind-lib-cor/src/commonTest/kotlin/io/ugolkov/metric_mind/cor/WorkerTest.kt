package io.ugolkov.metric_mind.cor

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WorkerTest {
    @Test
    fun `worker выполняет handle`() =
        runTest {
            val worker = Worker<TestContext>(
                title = "worker 1",
                blockHandle = { result += "history from worker 1" }
            )
            val context = TestContext()
            worker.exec(context)

            assertEquals("history from worker 1", context.result)
        }

    @Test
    fun `worker не выполняет handle`() =
        runTest {
            val worker = Worker<TestContext>(
                title = "worker 1",
                blockOn = { false },
                blockHandle = { result += "history from worker 1" }
            )
            val context = TestContext()
            worker.exec(context)

            assertEquals("", context.result)
        }

    @Test
    fun `worker отлавливает ошибки`() =
        runTest {
            val worker = Worker<TestContext>(
                title = "worker 1",
                blockHandle = { throw IllegalStateException("Test error") },
                blockExcept = { result += it.message }
            )
            val context = TestContext()
            worker.exec(context)

            assertEquals("Test error", context.result)
        }
}