package validation

import io.ugolkov.metric_mind.common.model.MmCommand
import kotlin.test.Test

class ValidationTrackRecordCreateTest : BaseValidationTest() {
    override val command: MmCommand = MmCommand.CREATE

    @Test
    fun correctTrackRecordId() = validationTrackRecordIdCorrect(command, processor)

    @Test
    fun zeroTrackId() = validationTrackRecordTrackIdZero(command, processor)

    @Test
    fun correctDate() = validationTrackRecordDateCorrect(command, processor)

    @Test
    fun notFutureDate() = validationTrackRecordDateNotFuture(command, processor)

    @Test
    fun correctValue() = validationValueCorrect(command, processor)

    @Test
    fun notCorrectValue() = validationValue(command, processor)
}
