package validation

import io.ugolkov.metric_mind.common.model.MmCommand
import kotlin.test.Test

class ValidationTrackRecordDeleteTest : BaseValidationTest() {
    override val command = MmCommand.DELETE

    @Test
    fun correctId() = validationTrackRecordIdCorrect(command, processor)

    @Test
    fun zeroId() = validationTrackRecordIdZero(command, processor)

}