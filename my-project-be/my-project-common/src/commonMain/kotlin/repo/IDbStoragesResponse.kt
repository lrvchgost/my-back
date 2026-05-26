package ru.otus.otuskotlin.lrvch.common.repo

import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.Storage

sealed interface IDbStoragesResponse : IDbResponse<List<Storage>>

data class DbStoragesResponseOk(
    val data: List<Storage>
) : IDbStoragesResponse

data class DbStoragesResponseErr(
    val errors: List<CatalogError> = emptyList()
) : IDbStoragesResponse {
    constructor(err: CatalogError) : this(listOf(err))
}

data class DbStorageResponseErrWithDataParams(
    val data: Storage,
    val errors: List<CatalogError> = emptyList()
)

data class DbStoragesResponseErrWithData(
    val params: List<DbStorageResponseErrWithDataParams>
) : IDbStoragesResponse {
    constructor(storage: Storage, err: CatalogError) : this(
        listOf(
            DbStorageResponseErrWithDataParams(
                storage,
                listOf(err)
            )
        )
    )

    fun toErrors() = params.flatMap { it.errors }

    fun toData() = params.map { it.data }
}

