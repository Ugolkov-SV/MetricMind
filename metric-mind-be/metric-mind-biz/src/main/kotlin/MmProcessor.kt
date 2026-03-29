package io.ugolkov.metric_mind.biz

import io.ugolkov.metric_mind.biz.cor.initStatus
import io.ugolkov.metric_mind.biz.cor.operation
import io.ugolkov.metric_mind.biz.cor.stub
import io.ugolkov.metric_mind.biz.cor.validation
import io.ugolkov.metric_mind.biz.stub.*
import io.ugolkov.metric_mind.biz.validation.*
import io.ugolkov.metric_mind.common.*
import io.ugolkov.metric_mind.common.model.MmCommand
import io.ugolkov.metric_mind.common.model.MmTrackId
import io.ugolkov.metric_mind.cor.Chain
import io.ugolkov.metric_mind.cor.rootChain
import io.ugolkov.metric_mind.cor.worker

@Suppress("unused")
class MmProcessor(val corSettings: MmCorSettings) : IProcessor {

    private val trackChain: Chain<TrackContext> = rootChain {
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

            validation {
                worker("Копируем поля в trackValidating") { validating = request.copy() }
                worker("Очистка id") { validating.id = MmTrackId.NONE }
                worker("Очистка заголовка") { validating.title = validating.title.trim() }
                worker("Очистка единиц измерения") { validating.unit = validating.unit.trim() }
                validateFieldNotEmpty(
                    title = "Проверка, что заголовок не пуст",
                    field = "title",
                    selector = { validating.title },
                )
                validateFieldHasContent(
                    title = "Проверка символов в заголовке",
                    field = "title",
                    selector = { validating.title },
                )
                validateFieldHasContent(
                    title = "Проверка символов в единицах измерения",
                    field = "unit",
                    selector = { validating.unit },
                )
                validateDateNotFuture(
                    title = "Проверка даты",
                    field = "createDate",
                    selector = { validating.createDate },
                )

                finishTrackValidation("Завершение проверок")
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

            validation {
                worker("Копируем поля в trackValidating") { validating = request.copy() }
                validateIdMoreZero(
                    title = "Проверка на наличие id",
                    field = "id",
                    selector = { validating.id },
                )

                finishTrackValidation("Успешное завершение процедуры валидации")
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

            validation {
                worker("Копируем поля в trackValidating") { validating = request.copy() }
                worker("Очистка заголовка") { validating.title = validating.title.trim() }
                worker("Очистка единиц измерения") { validating.unit = validating.unit.trim() }
                validateIdMoreZero(
                    title = "Проверка на наличие id",
                    field = "id",
                    selector = { validating.id },
                )
                validateFieldNotEmpty(
                    title = "Проверка, что заголовок не пуст",
                    field = "title",
                    selector = { validating.title },
                )
                validateFieldHasContent(
                    title = "Проверка символов в заголовке",
                    field = "title",
                    selector = { validating.title },
                )
                validateFieldHasContent(
                    title = "Проверка символов в единицах измерения",
                    field = "unit",
                    selector = { validating.unit },
                )
                validateDateNotFuture(
                    title = "Проверка даты",
                    field = "createDate",
                    selector = { validating.createDate },
                )

                finishTrackValidation("Успешное завершение процедуры валидации")
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

            validation {
                worker("Копируем поля в trackValidating") { validating = request.copy() }
                validateIdMoreZero(
                    title = "Проверка на наличие id",
                    field = "id",
                    selector = { validating.id },
                )

                finishTrackValidation("Успешное завершение процедуры валидации")
            }
        }
    }

    private val trackRecordChain: Chain<TrackRecordContext> = rootChain {
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

            validation {
                worker("Копируем поля в trackRecordValidating") { validating = request.copy() }
                worker("Очистка trackRecordId") { validating.trackRecordId = MmTrackId.NONE }
                validateIdMoreZero(
                    title = "Проверка на наличие trackId",
                    field = "trackId",
                    selector = { validating.trackId },
                )
                validateValueNotEmpty(title = "Проверка, что есть значение для записи")
                validateDateNotFuture(
                    title = "Проверка даты",
                    field = "date",
                    selector = { validating.date },
                )

                finishTrackRecordValidation("Завершение проверок")
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

            validation {
                worker("Копируем поля в trackRecordValidating") { validating = request.copy() }
                validateIdMoreZero(
                    title = "Проверка на наличие trackRecordId",
                    field = "trackRecordId",
                    selector = { validating.trackRecordId },
                )

                finishTrackRecordValidation("Успешное завершение процедуры валидации")
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

            validation {
                worker("Копируем поля в trackRecordValidating") { validating = request.copy() }
                validateIdMoreZero(
                    title = "Проверка на наличие trackRecordId",
                    field = "trackRecordId",
                    selector = { validating.trackRecordId },
                )
                validateIdMoreZero(
                    title = "Проверка на наличие trackId",
                    field = "trackId",
                    selector = { validating.trackId },
                )
                validateValueNotEmpty(title = "Проверка, что есть значение для записи")
                validateDateNotFuture(
                    title = "Проверка даты",
                    field = "date",
                    selector = { validating.date },
                )

                finishTrackRecordValidation("Успешное завершение процедуры валидации")
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

            validation {
                worker("Копируем поля в trackRecordValidating") { validating = request.copy() }
                validateIdMoreZero(
                    title = "Проверка на наличие trackRecordId",
                    field = "trackRecordId",
                    selector = { validating.trackRecordId },
                )

                finishTrackRecordValidation("Успешное завершение процедуры валидации")
            }
        }
    }

    private val trackFilterChain: Chain<TrackFilterContext> = rootChain {
        initStatus("Инициализация")

        operation(
            title = "Поиск трека",
            command = MmCommand.SEARCH,
        ) {
            stub("Обработка стабов") {
                stubTrackSearchSuccess("Успешный поиск трека", corSettings)
                stubDbError("Ошибка работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в trackFilterValidating") { validating = request.copy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishTrackFilterValidation("Успешное завершение процедуры валидации")
            }
        }
    }

    override suspend fun exec(ctx: BaseContext) {
        when (ctx) {
            is TrackContext -> trackChain.exec(ctx)
            is TrackRecordContext -> trackRecordChain.exec(ctx)
            is TrackFilterContext -> trackFilterChain.exec(ctx)
        }
    }
}