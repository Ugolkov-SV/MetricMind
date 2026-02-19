package io.ugolkov.metric_mind.common.model

import kotlin.jvm.JvmInline

@JvmInline
value class MmRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MmRequestId("")
    }
}
