package ru.otus.otuskotlin.lrvch.biz

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub

interface ICatalogProcessor {
    suspend fun exec(ctx: CatalogContext)
}

//@Suppress("unused", "RedundantSuspendModifier")
class CatalogProcessor(val corSettings: CatalogCoreSettings): ICatalogProcessor {

    override suspend fun exec(ctx: CatalogContext) {
        ctx.storageResponse = CatalogStorageStub.get()
        ctx.storagesResponse = CatalogStorageStub.prepareSearchList(StorageFilter()).toMutableList()
        ctx.state = CatalogState.RUNNING
    }
}
