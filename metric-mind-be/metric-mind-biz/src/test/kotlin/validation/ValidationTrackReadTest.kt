package validation

import io.ugolkov.metric_mind.common.model.MmCommand
import kotlin.test.Test

class ValidationTrackReadTest : BaseValidationTest() {
    override val command = MmCommand.READ

    @Test
    fun correctId() = validationTrackIdCorrect(command, processor)

    @Test
    fun zeroId() = validationTrackIdZero(command, processor)

}