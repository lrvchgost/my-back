package ru.otus.otuskotlin.lrvch.common.repo.exeptions

import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock

class RepoConcurrencyException(id: StorageId, expectedLock: StorageLock, actualLock: StorageLock?) : RepoStorageException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
