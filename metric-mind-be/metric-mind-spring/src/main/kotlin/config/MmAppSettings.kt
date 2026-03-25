package io.ugolkov.metric_mind.spring.config

import io.ugolkov.metric_mind.common.IMmAppSettings
import io.ugolkov.metric_mind.common.IProcessor
import io.ugolkov.metric_mind.common.MmCorSettings

data class MmAppSettings(
    override val corSettings: MmCorSettings,
    override val processor: IProcessor,
) : IMmAppSettings
