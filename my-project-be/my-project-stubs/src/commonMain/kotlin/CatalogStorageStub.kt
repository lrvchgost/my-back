package ru.otus.otuskotlin.lrvch.stubs

import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.stubs.CatalogStubStorages.BASE
import ru.otus.otuskotlin.lrvch.stubs.CatalogStubStorages.DEFAULT_SEARCH_FILTER
import ru.otus.otuskotlin.lrvch.stubs.CatalogStubStorages.EMPTY

object CatalogStorageStub {
    private const val defaultIdValue = "111"
    private const val defaultLockValue = "123"

    fun getDefaultId() = CatalogRequestId(defaultIdValue)

    fun getDefaultLock() = StorageLock(defaultLockValue)

    fun getDefaultSearchFilter(): StorageFilter = DEFAULT_SEARCH_FILTER

    fun get(): Storage = BASE.apply {
        lock = getDefaultLock()
        id = getDefaultId()
    }.copy()

    fun getEmpty(): Storage = EMPTY.copy()

    fun prepareResult(block: Storage.() -> Unit) = get().apply(block)

    fun prepareResultOnEmpty(block: Storage.() -> Unit) = getEmpty().apply(block)

    fun prepareSearchList(filter: StorageFilter) = mutableListOf(
        makeSearchableStorage("1", BASE, filter = filter),
        makeSearchableStorage("2", BASE, filter = filter),
        makeSearchableStorage("3", BASE, filter = filter),
        makeSearchableStorage("4", BASE, filter = filter),
        makeSearchableStorage("5", BASE, filter = filter),
    )

    fun prepareOptimizeList() = mutableListOf(
        EMPTY.copy(id = CatalogRequestId("1")),
        EMPTY.copy(id = CatalogRequestId("2")),
        EMPTY.copy(id = CatalogRequestId("3")),
        EMPTY.copy(id = CatalogRequestId("4")),
        EMPTY.copy(id = CatalogRequestId("5"))
    )

    fun makeSearchableStorage(id: String, base: Storage, filter: StorageFilter) = base.copy(
        id = CatalogRequestId(id),
        title = "$id ${filter.searchString}",
        availability = filter.availability,
        capacity = filter.capacity,
        paymentType = filter.paymentType,
        readSpeed = filter.readSpeed,
        writeSpeed = filter.writeSpeed,
    )
}