package ru.otus.otuskotlin.lrvch.common.helpers

import ru.otus.otuskotlin.lrvch.common.models.CatalogError

fun Throwable.asCatalogError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = CatalogError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
