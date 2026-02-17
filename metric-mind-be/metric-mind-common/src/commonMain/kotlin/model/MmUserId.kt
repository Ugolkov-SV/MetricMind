package io.ugolkov.metric_mind.common.model

import kotlin.jvm.JvmInline

@JvmInline
value class MmUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MmUserId("")
    }
}

fun String?.toUserId(): MmUserId =
    this?.let(::MmUserId) ?: MmUserId.NONE
