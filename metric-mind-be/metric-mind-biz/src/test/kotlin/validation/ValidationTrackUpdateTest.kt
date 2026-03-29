package validation

import io.ugolkov.metric_mind.common.model.MmCommand
import kotlin.test.Test

class ValidationTrackUpdateTest : BaseValidationTest() {
    override val command = MmCommand.UPDATE

    @Test
    fun correctTitle() = validationTitleCorrect(command, processor)

    @Test
    fun trimTitle() = validationTitleTrim(command, processor)

    @Test
    fun emptyTitle() = validationTitleEmpty(command, processor)

    @Test
    fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test
    fun correctUnit() = validationUnitCorrect(command, processor)

    @Test
    fun trimUnit() = validationUnitTrim(command, processor)

    @Test
    fun badSymbolsUnit() = validationUnitSymbols(command, processor)

    @Test
    fun correctId() = validationTrackIdCorrect(command, processor)

    @Test
    fun zeroId() = validationTrackIdZero(command, processor)

    @Test
    fun correctDate() = validationTrackDateCorrect(command, processor)

    @Test
    fun notFutureDate() = validationTrackDateNotFuture(command, processor)

}