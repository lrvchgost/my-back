package ru.otus.otuskotlin.lrvch.common.repo.exeptions

import ru.otus.otuskotlin.lrvch.common.models.StorageId

class RepoEmptyLockException(id: StorageId) : RepoStorageException(
    id,
    "Lock is empty in DB"
)
