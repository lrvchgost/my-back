package ru.otus.otuskotlin.lrvch.biz

import ru.otus.otuskotlin.lrvch.biz.general.initStatus
import ru.otus.otuskotlin.lrvch.biz.general.operation
import ru.otus.otuskotlin.lrvch.biz.general.stubs
import ru.otus.otuskotlin.lrvch.biz.stubs.stubCreateSuccess
import ru.otus.otuskotlin.lrvch.biz.stubs.stubDeleteSuccess
import ru.otus.otuskotlin.lrvch.biz.stubs.stubNoCase
import ru.otus.otuskotlin.lrvch.biz.stubs.stubOptimizeStoragesSuccess
import ru.otus.otuskotlin.lrvch.biz.stubs.stubReadSuccess
import ru.otus.otuskotlin.lrvch.biz.stubs.stubSearchStoragesSuccess
import ru.otus.otuskotlin.lrvch.biz.validation.validateStoragesCompatible
import ru.otus.otuskotlin.lrvch.biz.stubs.stubUpdateSuccess
import ru.otus.otuskotlin.lrvch.biz.stubs.stubValidationBadDescription
import ru.otus.otuskotlin.lrvch.biz.stubs.stubValidationBadId
import ru.otus.otuskotlin.lrvch.biz.stubs.stubValidationBadTitle
import ru.otus.otuskotlin.lrvch.biz.stubs.stubValidationDbError
import ru.otus.otuskotlin.lrvch.biz.validation.finishStorageFilterValidation
import ru.otus.otuskotlin.lrvch.biz.validation.finishStorageValidation
import ru.otus.otuskotlin.lrvch.biz.validation.validateDescriptionHasContent
import ru.otus.otuskotlin.lrvch.biz.validation.validateTitleHasContent
import ru.otus.otuskotlin.lrvch.biz.validation.validateTitleNotEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.validateDescriptionNotEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.validateIdNotEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.validateIdProperFormat
import ru.otus.otuskotlin.lrvch.biz.validation.validateLockNotEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.validateLockProperFormat
import ru.otus.otuskotlin.lrvch.biz.validation.validateSearchStringLength
import ru.otus.otuskotlin.lrvch.biz.validation.validation
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.libs.cor.rootChain
import ru.otus.otuskotlin.lrvch.libs.cor.worker

interface ICatalogProcessor {
    suspend fun exec(ctx: CatalogContext)
}

class CatalogProcessor(
    private val corSettings: CatalogCoreSettings = CatalogCoreSettings.NONE
) : ICatalogProcessor {
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

            validation {
                worker("Копируем поля в storageValidating") { storageValidating = storageRequest.deepCopy() }
                worker("Очистка id") { storageValidating.id = CatalogRequestId.NONE }
                worker("Очистка заголовка") { storageValidating.title = storageValidating.title.trim() }
                worker("Очистка описания") { storageValidating.description = storageValidating.description.trim() }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateTitleHasContent("Проверка символов")
                validateDescriptionNotEmpty("Проверка, что описание не пусто")
                validateDescriptionHasContent("Проверка символов")

                finishStorageValidation("Завершение проверок")
            }
        }

        operation("Получить сторадж", CatalogCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в storageValidating") { storageValidating = storageRequest.deepCopy() }
                worker("Очистка id") { storageValidating.id = CatalogRequestId(storageValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishStorageValidation("Успешное завершение процедуры валидации")
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

            validation {
                worker("Копируем поля в storageValidating") { storageValidating = storageRequest.deepCopy() }
                worker("Очистка id") { storageValidating.id = CatalogRequestId(storageValidating.id.asString().trim()) }
                worker("Очистка lock") {
                    storageValidating.lock =
                        StorageLock(storageValidating.lock.asString().trim())
                }
                worker("Очистка заголовка") { storageValidating.title = storageValidating.title.trim() }
                worker("Очистка описания") { storageValidating.description = storageValidating.description.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateTitleNotEmpty("Проверка на непустой заголовок")
                validateTitleHasContent("Проверка на наличие содержания в заголовке")
                validateDescriptionNotEmpty("Проверка на непустое описание")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")

                finishStorageValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Удалить объявление", CatalogCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в storageValidating") {
                    storageValidating = storageRequest.deepCopy()
                }
                worker("Очистка id") { storageValidating.id = CatalogRequestId(storageValidating.id.asString().trim()) }
                worker("Очистка lock") {
                    storageValidating.lock =
                        StorageLock(storageValidating.lock.asString().trim())
                }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishStorageValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Поиск объявлений", CatalogCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchStoragesSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в storageFilterValidating") { storageFilterValidating = storageFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishStorageFilterValidation("Успешное завершение процедуры валидации")
            }
        }

        operation("Оптимизация стораджей", CatalogCommand.OPTIMIZE) {
            stubs("Обработка стабов") {
                stubOptimizeStoragesSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в storagesValidating") { storagesValidating = (storagesRequest.map{ it.deepCopy()}.toMutableList())}
                validateStoragesCompatible("Валидация что все стораджы совместимы")

                finishStorageValidation("Успешное завершение процедуры валидации")
            }
        }
    }.build()
}
