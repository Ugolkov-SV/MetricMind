package io.ugolkov.metric_mind.cor

internal class Worker<T>(
    title: String = "",
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(e: Throwable) -> Unit = {},
    private val blockHandle: suspend T.() -> Unit = {},
) : Exec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(context: T) {
        context.blockHandle()
    }
}

@CorDslMarker
interface IWorkerDsl<T> : IExecDsl<T> {
    fun handle(function: suspend T.() -> Unit)
}

@CorDslMarker
internal class WorkerDsl<T> : ExecDsl<T>(), IWorkerDsl<T> {

    private var blockHandle: suspend T.() -> Unit = { }

    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): Worker<T> =
        Worker(
            title = title,
            description = description,
            blockOn = blockOn,
            blockExcept = blockExcept,
            blockHandle = blockHandle,
        )
}