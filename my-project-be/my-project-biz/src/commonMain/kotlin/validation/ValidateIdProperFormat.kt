package ru.otus.otuskotlin.lrvch.biz.validation

import ru.otus.otuskotlin.lrvch.biz.validation.helpers.idHasNotProperFormat
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.errorValidation
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    on { idHasNotProperFormat() }
    handle {
        val encodedId = storageValidating.id.asString()
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
