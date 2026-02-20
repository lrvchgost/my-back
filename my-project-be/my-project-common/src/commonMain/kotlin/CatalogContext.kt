package ru.otus.otuskotlin.lrvch.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs

data class CatalogContext(
    var command: CatalogCommand = CatalogCommand.NONE,
    var state: CatalogState = CatalogState.NONE,
    val errors: MutableList<CatalogError> = mutableListOf(),

    var workMode: CatalogWorkMode = CatalogWorkMode.PROD,
    var stubCase: CatalogStubs = CatalogStubs.NONE,

    var requestId: CatalogRequestId = CatalogRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var storageRequest: Storage = Storage(),
    var storagesRequest: List<Storage> = listOf(),
    var storageFilterRequest: StorageFilter = StorageFilter(),
    var storageResponse: Storage = Storage(),
    var storagesResponse: MutableList<Storage> = mutableListOf(),
)
