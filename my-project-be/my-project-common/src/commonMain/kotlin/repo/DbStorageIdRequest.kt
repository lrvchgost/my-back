package ru.otus.otuskotlin.lrvch.common.repo

import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock

data class DbStorageIdRequest(
    val id: StorageId,
    val lock: StorageLock = StorageLock.NONE,
) {
    constructor(storage: Storage) : this(storage.id, storage.lock)
}