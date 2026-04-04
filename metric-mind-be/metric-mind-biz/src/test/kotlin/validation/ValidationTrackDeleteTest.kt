package validation

import io.ugolkov.metric_mind.common.model.MmCommand
import kotlin.test.Test

class ValidationTrackDeleteTest : BaseValidationTest() {
    override val command = MmCommand.DELETE

    @Test
    fun correctId() = validationTrackIdCorrect(command, processor)

    @Test
    fun zeroId() = validationTrackIdZero(command, processor)

}