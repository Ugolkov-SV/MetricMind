package io.ugolkov.metric_mind.spring.config

import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.logger.base.LoggerProvider
import io.ugolkov.metric_mind.spring.common.MmTrackProcessor
import io.ugolkov.metric_mind.spring.common.MmTrackRecordProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricMindConfig {
    @Bean
    fun trackProcessor(corSettings: MmCorSettings): MmTrackProcessor =
        MmTrackProcessor(corSettings)

    @Bean
    fun trackRecordProcessor(corSettings: MmCorSettings): MmTrackRecordProcessor =
        MmTrackRecordProcessor(corSettings)

    @Bean
    fun corSettings(): MmCorSettings =
        MmCorSettings(loggerProvider())

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider()

    @Bean
    fun appSettings(
        corSettings: MmCorSettings,
        trackProcessor: MmTrackProcessor,
        trackRecordProcessor: MmTrackRecordProcessor,
    ) = MmAppSettings(
        corSettings = corSettings,
        trackProcessor = trackProcessor,
        trackRecordProcessor = trackRecordProcessor,
    )
}