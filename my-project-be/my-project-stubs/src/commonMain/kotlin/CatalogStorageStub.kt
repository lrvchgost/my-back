package ru.otus.otuskotlin.lrvch.stubs

import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.stubs.CatalogStubStorages.BASE
import ru.otus.otuskotlin.lrvch.stubs.CatalogStubStorages.DEFAULT_SEARCH_FILTER
import ru.otus.otuskotlin.lrvch.stubs.CatalogStubStorages.EMPTY

object CatalogStorageStub {
    private const val defaultIdValue = "111"
    private const val defaultLockValue = "123"
    private const val defaultIdUuidValue = "508885e1-41ed-401b-b4d7-df0aa6617094"

    fun getDefaultId() = StorageId(defaultIdValue)

    fun getDefaultUuid() = StorageId(defaultIdUuidValue)

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
        EMPTY.copy(id = StorageId("1")),
        EMPTY.copy(id = StorageId("2")),
        EMPTY.copy(id = StorageId("3")),
        EMPTY.copy(id = StorageId("4")),
        EMPTY.copy(id = StorageId("5"))
    )

    fun prepareOptimizeListNotEmpty() = mutableListOf(
        BASE.copy(id = StorageId("1-not-empty")).apply { paymentType = CatalogPaymentType.PREPAID },
        BASE.copy(id = StorageId("2-not-empty")).apply { paymentType = CatalogPaymentType.PREPAID },
        BASE.copy(id = StorageId("3-not-empty")).apply { paymentType = CatalogPaymentType.PREPAID },
        BASE.copy(id = StorageId("4-not-empty")).apply { paymentType = CatalogPaymentType.PREPAID },
        BASE.copy(id = StorageId("5-not-empty")).apply { paymentType = CatalogPaymentType.PREPAID }
    )

    fun optimizedStorage() =
       BASE.copy(id = StorageId("1")).apply {
           paymentType = CatalogPaymentType.PREPAID
           capacity = "500"
           title = "Change it"
           description = "Change it"
       }

    fun makeSearchableStorage(id: String, base: Storage, filter: StorageFilter) = base.copy(
        id = StorageId(id),
        title = "$id ${filter.searchString}",
        availability = filter.availability,
        capacity = filter.capacity,
        paymentType = filter.paymentType.takeIf { it != CatalogPaymentType.NONE } ?: base.paymentType,
        readSpeed = filter.readSpeed.takeIf { it != SpeedType.NONE } ?: base.readSpeed,
        writeSpeed = filter.writeSpeed.takeIf { it != SpeedType.NONE } ?: base.writeSpeed
    )
}