package ru.otus.otuskotlin.lrvch.biz.validation

import ru.otus.otuskotlin.lrvch.biz.validation.helpers.descriptionIsEmpty
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.errorValidation
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.validateDescriptionNotEmpty(title: String) = worker {
    this.title = title
    on { descriptionIsEmpty() }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
