package ru.otus.otuskotlin.lrvch.biz

import ru.otus.otuskotlin.lrvch.biz.general.initStatus
import ru.otus.otuskotlin.lrvch.biz.general.operation
import ru.otus.otuskotlin.lrvch.biz.general.stubs
import ru.otus.otuskotlin.lrvch.biz.stubs.stubCreateSuccess
import ru.otus.otuskotlin.lrvch.biz.stubs.stubDeleteSuccess
import ru.otus.otuskotlin.lrvch.biz.stubs.stubNoCase
import ru.otus.otuskotlin.lrvch.biz.stubs.stubReadSuccess
import ru.otus.otuskotlin.lrvch.biz.stubs.stubSearchStoragesSuccess
import ru.otus.otuskotlin.lrvch.biz.stubs.stubUpdateSuccess
import ru.otus.otuskotlin.lrvch.biz.stubs.stubValidationBadDescription
import ru.otus.otuskotlin.lrvch.biz.stubs.stubValidationBadId
import ru.otus.otuskotlin.lrvch.biz.stubs.stubValidationBadTitle
import ru.otus.otuskotlin.lrvch.biz.stubs.stubValidationDbError
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.libs.cor.rootChain
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub

interface ICatalogProcessor {
    suspend fun exec(ctx: CatalogContext)
}

class CatalogProcessor(
    private val corSettings: CatalogCoreSettings = CatalogCoreSettings.NONE
): ICatalogProcessor {
    override suspend fun exec(ctx: CatalogContext) = businessChain.exec(ctx.also { it.coreSettings = corSettings })

    private val businessChain = rootChain<CatalogContext> {
        initStatus("Инициализация статуса")

        operation("Создание стораджа", CatalogCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubValidationDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Получить сторадж", CatalogCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Изменить сторадж", CatalogCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubValidationDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Удалить объявление", CatalogCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Поиск объявлений", CatalogCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchStoragesSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
    }.build()
}
