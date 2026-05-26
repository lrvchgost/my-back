package ru.otus.otuskotlin.lrvch.common.repo

import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.Storage


sealed interface IDbStorageResponse : IDbResponse<Storage>

data class DbStorageResponseOk(
    val data: Storage,
) : IDbStorageResponse

data class DbStorageResponseErr(
    val errors: List<CatalogError> = emptyList()
) : IDbStorageResponse {
    constructor(err: CatalogError) : this(listOf(err))
}

data class DbStorageResponseErrWithData(
    val data:  Storage,
    val errors: List<CatalogError> = emptyList()
): IDbStorageResponse {
    constructor(storage: Storage, err: CatalogError): this(storage, listOf(err))
}
