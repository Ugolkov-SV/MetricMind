package io.ugolkov.metric_mind.common

import io.ugolkov.metric_mind.logger.base.LoggerProvider

data class MmCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
) {
    companion object {
        val NONE = MmCorSettings()
    }
}
