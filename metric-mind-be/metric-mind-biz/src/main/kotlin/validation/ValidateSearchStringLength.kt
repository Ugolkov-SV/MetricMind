package io.ugolkov.metric_mind.biz.validation

import io.ugolkov.metric_mind.biz.helper.errorValidation
import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.model.MmState
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.chain
import io.ugolkov.metric_mind.cor.worker

internal fun IChainDsl<MmContext>.validateSearchStringLength(title: String) =
    chain {
        this.title = title
        this.description = """
        Валидация длины строки поиска в поисковых фильтрах. Допустимые значения:
        - null - не выполняем поиск по строке
        - 3-100 - допустимая длина
        - больше 100 - слишком длинная строка
    """.trimIndent()
        on { state == MmState.RUNNING }
        worker("Обрезка пустых символов") {
            trackFilterValidating.searchString = trackFilterValidating.searchString.trim()
        }
        worker {
            this.title = "Проверка кейса длины на 0-2 символа"
            this.description = this.title
            on { state == MmState.RUNNING && trackFilterValidating.searchString.length in (1..2) }
            handle {
                fail(
                    errorValidation(
                        field = "searchString",
                        violationCode = "tooShort",
                        description = "Search string must contain at least 3 symbols"
                    )
                )
            }
        }
        worker {
            this.title = "Проверка кейса длины на более 100 символов"
            this.description = this.title
            on { state == MmState.RUNNING && trackFilterValidating.searchString.length > 100 }
            handle {
                fail(
                    errorValidation(
                        field = "searchString",
                        violationCode = "tooLong",
                        description = "Search string must be no more than 100 symbols long"
                    )
                )
            }
        }
    }
