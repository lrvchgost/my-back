package ru.otus.otuskotlin.lrvch.common.repo

import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock

data class IdEntity(
    val id: StorageId,
    val lock: StorageLock = StorageLock.NONE,
)

data class DbStorageIdsRequest(
    val storages: List<IdEntity>
) {
//    constructor(inStorages: List<Storage>) : this(storages = inStorages.map { IdEntity(it.id, it.lock) })
}
