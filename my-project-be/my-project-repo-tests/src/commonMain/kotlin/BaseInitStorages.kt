package ru.otus.otuskotlin.lrvch.backend.repo.tests

import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock

abstract class BaseInitStorages(private val op: String) : IInitObjects<Storage> {
    open val lockOld: StorageLock = StorageLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: StorageLock = StorageLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        lock: StorageLock = lockOld,
        paymentType: CatalogPaymentType = CatalogPaymentType.NONE,
        readSpeed: SpeedType = SpeedType.NONE,
        writeSpeed: SpeedType = SpeedType.NONE,
        optimizeEnabled: Boolean = false,
        capacity: String = "capacity",
        availability: String = "capacity",
    ) = Storage(
        id = StorageId("storage-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        paymentType = paymentType,
        readSpeed = readSpeed,
        writeSpeed = writeSpeed,
        optimizeEnabled = optimizeEnabled,
        capacity = capacity,
        availability = availability,
        lock = lock,
    )
}
