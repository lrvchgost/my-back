package ru.otus.otuskotlin.lrvch.common.models

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.logging.common.LogLevel

data class CatalogError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)

inline fun CatalogContext.addError(vararg error: CatalogError) = errors.addAll(error)

inline fun CatalogContext.fail(error: CatalogError) {
    addError(error)
    state = CatalogState.FAILED
}
