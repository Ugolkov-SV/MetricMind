package io.ugolkov.metric_mind.common

interface IProcessor {
    suspend fun exec(ctx: BaseContext)
}