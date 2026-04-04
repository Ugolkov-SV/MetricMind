package validation

import io.ugolkov.metric_mind.common.model.MmCommand
import kotlin.test.Test

class ValidationTrackCreateTest : BaseValidationTest() {
    override val command: MmCommand = MmCommand.CREATE

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
}
