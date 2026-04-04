package validation

import io.ugolkov.metric_mind.common.model.MmCommand
import kotlin.test.Test

class ValidationTrackRecordUpdateTest : BaseValidationTest() {
    override val command = MmCommand.UPDATE

    @Test
    fun correctTrackRecordId() = validationTrackRecordIdCorrect(command, processor)

    @Test
    fun correctTrackId() = validationTrackRecordTrackIdZero(command, processor)

    @Test
    fun zeroTrackRecordId() = validationTrackRecordTrackIdZero(command, processor)

    @Test
    fun correctDate() = validationTrackRecordDateCorrect(command, processor)

    @Test
    fun notFutureDate() = validationTrackRecordDateNotFuture(command, processor)

    @Test
    fun correctValue() = validationValueCorrect(command, processor)

    @Test
    fun notCorrectValue() = validationValue(command, processor)

}