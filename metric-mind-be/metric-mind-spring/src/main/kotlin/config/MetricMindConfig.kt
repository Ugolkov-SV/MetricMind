package io.ugolkov.metric_mind.spring.config

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.IProcessor
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.logger.base.LoggerProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricMindConfig {
    @Bean
    fun trackProcessor(corSettings: MmCorSettings): IProcessor =
        MmProcessor(corSettings)

    @Bean
    fun corSettings(): MmCorSettings =
        MmCorSettings(loggerProvider())

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider()

    @Bean
    fun appSettings(
        corSettings: MmCorSettings,
        processor: IProcessor,
    ) = MmAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}