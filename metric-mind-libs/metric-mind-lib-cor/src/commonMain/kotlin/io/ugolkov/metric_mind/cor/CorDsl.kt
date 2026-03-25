package io.ugolkov.metric_mind.cor

fun <T> rootChain(block: IChainDsl<T>.() -> Unit): Chain<T> =
    ChainDsl<T>()
        .apply(block)
        .build()

fun <T> IChainDsl<T>.chain(block: IChainDsl<T>.() -> Unit) =
    ChainDsl<T>()
        .apply(block)
        .let(::add)

fun <T> IChainDsl<T>.worker(block: IWorkerDsl<T>.() -> Unit) =
    WorkerDsl<T>()
        .apply(block)
        .let(::add)

fun <T> IChainDsl<T>.worker(
    title: String,
    description: String = "",
    blockHandle: suspend T.() -> Unit
) =
    WorkerDsl<T>()
        .apply {
            this.title = title
            this.description = description
            this.handle(blockHandle)
        }
        .let(::add)
