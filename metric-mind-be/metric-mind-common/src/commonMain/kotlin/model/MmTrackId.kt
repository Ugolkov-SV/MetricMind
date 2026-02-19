package io.ugolkov.metric_mind.common.model

import kotlin.jvm.JvmInline

@JvmInline
value class MmTrackId(private val id: Long) {
    fun asLong(): Long = id

    companion object {
        val NONE = MmTrackId(-1L)
    }
}

fun Long?.toTrackId(): MmTrackId =
    this?.let(::MmTrackId) ?: MmTrackId.NONE
