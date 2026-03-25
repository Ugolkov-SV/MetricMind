package io.ugolkov.metric_mind.logger.base

enum class LogLevel(
    val level: Int,
) {
    ERROR(40),
    WARN(30),
    INFO(20),
    DEBUG(10),
    TRACE(0);

    override fun toString(): String =
        name
}
