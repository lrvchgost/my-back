package ru.otus.otuskotlin.lrvch.common.repo

import ru.otus.otuskotlin.lrvch.common.helpers.errorSystem
import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.common.repo.exeptions.RepoConcurrencyException
import ru.otus.otuskotlin.lrvch.common.repo.exeptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: StorageId) = DbStorageResponseErr(
    CatalogError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

fun errorNotFoundByIds(ids: List<StorageId>) = DbStoragesResponseErr(
    CatalogError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Objects with IDs: ${ids.map { it.asString() }} is not Found",
    )
)

val errorEmptyId = DbStorageResponseErr(
    CatalogError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldStorage: Storage,
    expectedLock: StorageLock,
    exception: Exception = RepoConcurrencyException(
        id = oldStorage.id,
        expectedLock = expectedLock,
        actualLock = oldStorage.lock,
    ),
) = DbStorageResponseErrWithData(
    storage = oldStorage,
    err = CatalogError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldStorage.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

data class ConcurrencyMultipleErrorData(
    val oldStorage: Storage,
    val expectedLock: StorageLock,
)

fun errorRepoConcurrencyMultiple(
    data: List<ConcurrencyMultipleErrorData>
) = DbStoragesResponseErrWithData(
    params = data.map {
        DbStorageResponseErrWithDataParams(
            data = it.oldStorage,
            errors = listOf(
                CatalogError(
                    code = "$ERROR_GROUP_REPO-concurrency",
                    group = ERROR_GROUP_REPO,
                    field = "lock",
                    message = "The object with ID ${it.oldStorage.id.asString()} has been changed concurrently by another user or process",
                    exception = RepoConcurrencyException(
                            id = it.oldStorage.id,
                            expectedLock = it.expectedLock,
                            actualLock = it.oldStorage.lock,
                        )
                )
            )
        )
    }
)

fun errorEmptyLock(id: StorageId) = DbStorageResponseErr(
    CatalogError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Ad ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbStorageResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
