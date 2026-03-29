package validation

import io.ugolkov.metric_mind.biz.MmProcessor
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.model.MmCommand

abstract class BaseValidationTest {
    protected abstract val command: MmCommand
    protected val processor by lazy { MmProcessor(MmCorSettings()) }
}
