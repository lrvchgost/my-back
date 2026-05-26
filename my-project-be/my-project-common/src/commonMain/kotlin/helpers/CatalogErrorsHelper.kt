package ru.otus.otuskotlin.lrvch.common.helpers

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.logging.common.LogLevel

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

inline fun CatalogContext.addError(error: CatalogError) = errors.add(error)
inline fun CatalogContext.addErrors(error: Collection<CatalogError>) = errors.addAll(error)

inline fun CatalogContext.fail(error: CatalogError) {
    addError(error)
    state = CatalogState.FAILED
}

inline fun CatalogContext.fail(errors: Collection<CatalogError>) {
    addErrors(errors)
    state = CatalogState.FAILED
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = CatalogError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = CatalogError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)
