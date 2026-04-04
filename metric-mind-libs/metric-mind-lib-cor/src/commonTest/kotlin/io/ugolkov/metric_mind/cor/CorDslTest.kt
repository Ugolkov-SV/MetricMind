package io.ugolkov.metric_mind.cor

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CorDslTest {
    @Test
    fun `worker выполняет handle`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { result += "w1; " }
            }
        }
        val context = execute(chain)
        assertEquals("w1; ", context.result)
    }

    @Test
    fun `обработка условий в worker`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                on { status == TestContext.CorStatus.ERROR }
                handle { result += "w1; " }
            }
            worker {
                on { status == TestContext.CorStatus.NONE }
                handle {
                    result += "w2; "
                    status = TestContext.CorStatus.FAILING
                }
            }
            worker {
                on { status == TestContext.CorStatus.FAILING }
                handle { result += "w3; " }
            }
        }
        val context = execute(chain)
        assertEquals("w2; w3; ", context.result)
    }

    @Test
    fun `обработка исключений`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { throw RuntimeException("some error") }
                except { result += it.message }
            }
        }
        val context = execute(chain)
        assertEquals("some error", context.result)
    }

    @Test
    fun `выбрасывание исключения`() = runTest {
        val chain = rootChain<TestContext> {
            worker("throw", "Выбрасывает ошибку") { throw RuntimeException("some error") }
        }
        assertFails {
            execute(chain)
        }
    }

    @Test
    fun `сложная цеочка`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                title = "Инициализация статуса"
                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

                on { status == TestContext.CorStatus.NONE }
                handle { status = TestContext.CorStatus.RUNNING }
                except { status = TestContext.CorStatus.ERROR }
            }

            chain {
                on { status == TestContext.CorStatus.RUNNING }

                worker(
                    title = "Лямбда обработчик",
                    description = "Пример использования обработчика в виде лямбды"
                ) {
                    some += 4
                }
            }

            worker(title = "Print example") {
                println("some = $some")
            }
        }

        val context = execute(chain)
        assertEquals(4, context.some)
        println("Complete: $context")
    }

    private suspend fun execute(dsl: IExec<TestContext>): TestContext =
        TestContext()
            .also { dsl.exec(it) }
}