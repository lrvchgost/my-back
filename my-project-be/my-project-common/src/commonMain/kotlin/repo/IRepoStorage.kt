package ru.otus.otuskotlin.lrvch.common.repo

interface IRepoStorage {
    suspend fun createStorage(rq: DbStorageRequest): IDbStorageResponse
    suspend fun readStorage(rq: DbStorageIdRequest): IDbStorageResponse
    suspend fun updateStorage(rq: DbStorageRequest): IDbStorageResponse
    suspend fun deleteStorage(rq: DbStorageIdRequest): IDbStorageResponse
    suspend fun searchStorage(rq: DbStorageFilterRequest): IDbStoragesResponse
//    suspend fun searchStoragesByIds(rq: DbStorageFilterRequest): IDbStoragesResponse
    companion object {
        val NONE = object : IRepoStorage {
            override suspend fun createStorage(rq: DbStorageRequest): IDbStorageResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readStorage(rq: DbStorageIdRequest): IDbStorageResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateStorage(rq: DbStorageRequest): IDbStorageResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteStorage(rq: DbStorageIdRequest): IDbStorageResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchStorage(rq: DbStorageFilterRequest): IDbStoragesResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
