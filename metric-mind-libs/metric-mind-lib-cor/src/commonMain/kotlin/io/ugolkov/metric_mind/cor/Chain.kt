package io.ugolkov.metric_mind.cor

class Chain<T>(
    title: String = "",
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(e: Throwable) -> Unit = {},
    private val execs: List<IExec<T>>,
) : Exec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(context: T) {
        execs.forEach {
            it.exec(context)
        }
    }
}

@CorDslMarker
interface IChainDsl<T> : IExecDsl<T> {
    fun add(exec: IExecDsl<T>)
}

@CorDslMarker
internal class ChainDsl<T> : ExecDsl<T>(), IChainDsl<T> {
    private val execs: MutableList<IExecDsl<T>> = mutableListOf()

    override fun add(exec: IExecDsl<T>) {
        execs.add(exec)
    }

    override fun build(): Chain<T> =
        Chain(
            title = title,
            description = description,
            blockOn = blockOn,
            blockExcept = blockExcept,
            execs = execs.map { it.build() },
        )
}