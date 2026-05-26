package ru.otus.otuskotlin.lrvch.common.repo.exeptions

import ru.otus.otuskotlin.lrvch.common.models.StorageId

open class RepoStorageException(
    @Suppress("unused")
    val storageId: StorageId,
    msg: String,
): RepoException(msg)
