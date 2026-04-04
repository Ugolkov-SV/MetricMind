package validation

import io.ugolkov.metric_mind.common.model.MmCommand
import kotlin.test.Test

class ValidationTrackRecordReadTest : BaseValidationTest() {
    override val command = MmCommand.READ

    @Test
    fun correctId() = validationTrackRecordIdCorrect(command, processor)

    @Test
    fun zeroId() = validationTrackRecordIdZero(command, processor)

}