package ru.otus.otuskotlin.lrvch.stubs

import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.common.models.StoragePermissionClient
import kotlin.String

object CatalogStubStorages {
    val EMPTY: Storage
        get() = Storage()

    val BASE: Storage
        get() = Storage(
            id = CatalogRequestId("111"),
            title = "Some storage",
            description = "Some description",
            paymentType = CatalogPaymentType.FREE,
            readSpeed = SpeedType._100,
            writeSpeed = SpeedType._100,
            optimizeEnabled = false,
            capacity = "100",
            availability = "99",
            lock = StorageLock("123"),
            permissionsClient = mutableSetOf(
                StoragePermissionClient.CREATE,
                StoragePermissionClient.READ,
                StoragePermissionClient.UPDATE,
                StoragePermissionClient.DELETE,
            )
        )

    val DEFAULT_SEARCH_FILTER: StorageFilter = StorageFilter(
        searchString = "search",
        availability = "99",
        capacity = "100",
        paymentType = CatalogPaymentType.FREE,
        writeSpeed = SpeedType._100,
        readSpeed = SpeedType._100,
    )
}