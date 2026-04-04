package io.ugolkov.metric_mind.cor

interface IExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}

@CorDslMarker
interface IExecDsl<T> {
    var title: String
    var description: String

    fun on(function: suspend T.() -> Boolean)
    fun except(function: suspend T.(e: Throwable) -> Unit)

    fun build(): IExec<T>
}