package io.ugolkov.metric_mind.cor

abstract class Exec<T>(
    final override val title: String,
    final override val description: String,
    private val blockOn: suspend T.() -> Boolean,
    private val blockExcept: suspend T.(e: Throwable) -> Unit
) : IExec<T> {
    protected abstract suspend fun handle(context: T)

    final override suspend fun exec(context: T) {
        if (context.blockOn()) {
            try {
                handle(context)
            } catch (e: Throwable) {
                context.blockExcept(e)
            }
        }
    }
}

@CorDslMarker
internal abstract class ExecDsl<T> : IExecDsl<T> {
    override var title: String = ""

    override var description: String = ""

    protected var blockOn: suspend T.() -> Boolean = { true }

    protected var blockExcept: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e }

    override fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    override fun except(function: suspend T.(e: Throwable) -> Unit) {
        blockExcept = function
    }
}