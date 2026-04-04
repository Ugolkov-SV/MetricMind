package io.ugolkov.metric_mind.biz.validation

import io.ugolkov.metric_mind.biz.helper.errorValidation
import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.common.BaseContext
import io.ugolkov.metric_mind.cor.IChainDsl
import io.ugolkov.metric_mind.cor.worker

internal fun <T : BaseContext> IChainDsl<T>.validateFieldHasContent(
    title: String,
    field: String,
    selector: T.() -> String,
) =
    worker {
        this.title = title
        this.description = """
        Проверяем, что у нас есть какие-то слова в заголовке.
        Отказываем в публикации заголовков, в которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
        val regExp = Regex("\\p{L}")
        on {
            val text = selector()
            text.isNotBlank() && text.contains(regExp).not()
        }
        handle {
            fail(
                errorValidation(
                    field = field,
                    violationCode = "noContent",
                    description = "field must contain letters"
                )
            )
        }
    }
