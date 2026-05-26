package ru.otus.otuskotlin.lrvch.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs

data class CatalogContext(
    var command: CatalogCommand = CatalogCommand.NONE,
    var state: CatalogState = CatalogState.NONE,
    var errors: MutableList<CatalogError> = mutableListOf(),

    var coreSettings: CatalogCoreSettings = CatalogCoreSettings(),
    var workMode: CatalogWorkMode = CatalogWorkMode.PROD,
    var stubCase: CatalogStubs = CatalogStubs.NONE,

    var requestId: CatalogRequestId = CatalogRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var storageRequest: Storage = Storage(),
    var storagesRequest: List<Storage> = listOf(),
    var storageFilterRequest: StorageFilter = StorageFilter(),
    var storageResponse: Storage = Storage(),
    var storagesResponse: MutableList<Storage> = mutableListOf(),

    var storageValidating: Storage = Storage(),
    var storagesValidating: MutableList<Storage> = mutableListOf(),
    var storageFilterValidating: StorageFilter = StorageFilter(),

    var storageValidated: Storage = Storage(),
    var storagesValidated: MutableList<Storage> = mutableListOf(),
    var storageFilterValidated: StorageFilter = StorageFilter(),

    // Тут хранится ссылка на текущий репозиторий
    var storageRepo: IRepoStorage = IRepoStorage.NONE,

    // Для одного стораджа
    var storageRepoRead: Storage = Storage(), // То, что прочитали из репозитория
    var storageRepoPrepare: Storage = Storage(), // То, что готовим для сохранения в БД
    var storageRepoDone: Storage = Storage(),  // Результат, полученный из БД

    // Для списка стораджей
    var storagesRepoRead: MutableList<Storage> = mutableListOf(), // То, что прочитали из репозитория
    var storagesRepoReadTemp: MutableList<Storage> = mutableListOf(), // То, что прочитали из репозитория для временного хранения
    var storagesRepoPrepare: MutableList<Storage> = mutableListOf(), // То, что готовим для сохранения в БД
    var storagesRepoDone: MutableList<Storage> = mutableListOf(),  // Результат, полученный из БД
)