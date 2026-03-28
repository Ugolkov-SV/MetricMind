package io.ugolkov.metric_mind.biz

import io.ugolkov.metric_mind.biz.cor.initStatus
import io.ugolkov.metric_mind.biz.cor.operation
import io.ugolkov.metric_mind.biz.cor.stub
import io.ugolkov.metric_mind.biz.helper.fail
import io.ugolkov.metric_mind.biz.stub.*
import io.ugolkov.metric_mind.common.IProcessor
import io.ugolkov.metric_mind.common.MmContext
import io.ugolkov.metric_mind.common.MmCorSettings
import io.ugolkov.metric_mind.common.model.MmCommand
import io.ugolkov.metric_mind.common.model.MmError
import io.ugolkov.metric_mind.cor.Chain
import io.ugolkov.metric_mind.cor.rootChain

@Suppress("unused")
class MmProcessor(val corSettings: MmCorSettings) : IProcessor {

    private val trackChain: Chain<MmContext> = rootChain {
        initStatus("Инициализация")

        operation(
            title = "Создание трека",
            command = MmCommand.CREATE,
        ) {
            stub("Обработка стабов") {
                stubTrackCreateSuccess("Успешное создание трека", corSettings)
                stubValidationBadTitle("Ошибка валидации заголовка трека")
                stubValidationBadVisibility("Ошибка валидации видимости трека")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation(
            title = "Чтение трека",
            command = MmCommand.READ,
        ) {
            stub("Обработка стабов") {
                stubTrackReadSuccess("Успешное чтение трека", corSettings)
                stubValidationBadId("Ошибка валидации id")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation(
            title = "Обновление трека",
            command = MmCommand.UPDATE,
        ) {
            stub("Обработка стабов") {
                stubTrackUpdateSuccess("Успешное обновление трека", corSettings)
                stubValidationBadTitle("Ошибка валидации заголовка трека")
                stubValidationBadVisibility("Ошибка валидации видимости трека")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation(
            title = "Удаление трека",
            command = MmCommand.DELETE,
        ) {
            stub("Обработка стабов") {
                stubTrackDeleteSuccess("Успешное удаление трека", corSettings)
                stubValidationBadId("Ошибка валидации id")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation(
            title = "Поиск трека",
            command = MmCommand.SEARCH,
        ) {
            stub("Обработка стабов") {
                stubTrackSearchSuccess("Успешный поиск трека", corSettings)
                stubValidationBadId("Ошибка валидации id")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
    }

    private val trackRecordChain: Chain<MmContext> = rootChain {
        initStatus("Инициализация")

        operation(
            title = "Создание записи в треке",
            command = MmCommand.CREATE,
        ) {
            stub("Обработка стабов") {
                stubTrackRecordCreateSuccess("Успешное создание записи трека", corSettings)
                stubValidationBadValue("Ошибка валидации значения записи трека")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation(
            title = "Чтение записи трека",
            command = MmCommand.READ,
        ) {
            stub("Обработка стабов") {
                stubTrackRecordReadSuccess("Успешное чтение записи трека", corSettings)
                stubValidationBadId("Ошибка валидации id")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation(
            title = "Обновление записи трека",
            command = MmCommand.UPDATE,
        ) {
            stub("Обработка стабов") {
                stubTrackRecordUpdateSuccess("Успешное обновление записи трека", corSettings)
                stubValidationBadValue("Ошибка валидации значения записи трека")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation(
            title = "Удаление записи трека",
            command = MmCommand.DELETE,
        ) {
            stub("Обработка стабов") {
                stubTrackRecordDeleteSuccess("Успешное удаление записи трека", corSettings)
                stubValidationBadId("Ошибка валидации id")
                stubDbError("Ошибка работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
    }

    override suspend fun exec(ctx: MmContext) {
        when {
            !ctx.trackRequest.isEmpty() ||
                    !ctx.trackFilterRequest.isEmpty() -> trackChain.exec(ctx)

            !ctx.trackRecordRequest.isEmpty() -> trackRecordChain.exec(ctx)
            else -> ctx.fail(
                MmError(
                    code = "Unknown request",
                    message = "Internal error"
                )
            )
        }
    }
}