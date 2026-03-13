package io.ugolkov.metric_mind.spring.config

import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.spring.common.MmTrackProcessor
import io.ugolkov.metric_mind.spring.common.MmTrackRecordProcessor

data class MmAppSettings(
    val corSettings: MmCorSettings,
    val trackProcessor: MmTrackProcessor,
    val trackRecordProcessor: MmTrackRecordProcessor,
)
