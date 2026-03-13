package io.ugolkov.metric_mind.spring.common

import io.ugolkov.metric_mind.common.MmContext

interface MmProcessor {
    suspend fun exec(ctx: MmContext)
}