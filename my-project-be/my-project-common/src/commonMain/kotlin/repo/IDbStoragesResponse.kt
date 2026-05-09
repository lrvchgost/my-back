package ru.otus.otuskotlin.lrvch.common.repo

import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.Storage

sealed interface IDbStoragesResponse: IDbResponse<List<Storage>>

data class DbStoragesResponseOk(
    val data: List<Storage>
): IDbStoragesResponse

@Suppress("unused")
data class DbStoragesResponseErr(
    val errors: List<CatalogError> = emptyList()
): IDbStoragesResponse {
    constructor(err: CatalogError): this(listOf(err))
}
