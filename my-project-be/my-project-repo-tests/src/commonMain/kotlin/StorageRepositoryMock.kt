package ru.otus.otuskotlin.lrvch.backend.repo.tests

import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageFilterRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.IDbStorageResponse
import ru.otus.otuskotlin.lrvch.common.repo.IDbStoragesResponse
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage

class StorageRepositoryMock(
    private val invokeCreateStorage: (DbStorageRequest) -> IDbStorageResponse = { DEFAULT_STORAGE_SUCCESS_EMPTY_MOCK },
    private val invokeReadStorage: (DbStorageIdRequest) -> IDbStorageResponse = { DEFAULT_STORAGE_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateStorage: (DbStorageRequest) -> IDbStorageResponse = { DEFAULT_STORAGE_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteStorage: (DbStorageIdRequest) -> IDbStorageResponse = { DEFAULT_STORAGE_SUCCESS_EMPTY_MOCK },
    private val invokeSearchStorage: (DbStorageFilterRequest) -> IDbStoragesResponse = { DEFAULT_STORAGES_SUCCESS_EMPTY_MOCK },
): IRepoStorage {
    override suspend fun createStorage(rq: DbStorageRequest): IDbStorageResponse {
        return invokeCreateStorage(rq)
    }

    override suspend fun readStorage(rq: DbStorageIdRequest): IDbStorageResponse {
        return invokeReadStorage(rq)
    }

    override suspend fun updateStorage(rq: DbStorageRequest): IDbStorageResponse {
        return invokeUpdateStorage(rq)
    }

    override suspend fun deleteStorage(rq: DbStorageIdRequest): IDbStorageResponse {
        return invokeDeleteStorage(rq)
    }

    override suspend fun searchStorage(rq: DbStorageFilterRequest): IDbStoragesResponse {
        return invokeSearchStorage(rq)
    }

    companion object {
        val DEFAULT_STORAGE_SUCCESS_EMPTY_MOCK = DbStorageResponseOk(Storage())
        val DEFAULT_STORAGES_SUCCESS_EMPTY_MOCK = DbStoragesResponseErr(emptyList())
    }
}