package io.ugolkov.metric_mind.common.model

@JvmInline
value class MmTrackId(private val id: Long) {
    fun asLong(): Long = id

    fun asString(): String = id.toString()

    companion object {
        val NONE = MmTrackId(-1L)
    }
}

fun Long?.toTrackId(): MmTrackId =
    this?.let(::MmTrackId) ?: MmTrackId.NONE
