package ru.otus.otuskotlin.lrvch.common

import kotlinx.datetime.Instant
import models.CatalogCommand
import models.CatalogError
import models.CatalogRequestId
import models.CatalogState
import models.CatalogWorkMode
import models.Storage
import models.StorageFilter
import stubs.CatalogStubs

data class CatalogContext(
    var command: CatalogCommand = CatalogCommand.NONE,
    var state: CatalogState = CatalogState.NONE,
    val errors: MutableList<CatalogError> = mutableListOf(),

    var workMode: CatalogWorkMode = CatalogWorkMode.PROD,
    var stubCase: CatalogStubs = CatalogStubs.NONE,

    var requestId: CatalogRequestId = CatalogRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var storageRequest: Storage = Storage(),
    var storageFilterRequest: StorageFilter = StorageFilter(),
    var storageResponse: Storage = Storage(),
    var storagesResponse: MutableList<Storage> = mutableListOf(),
)
