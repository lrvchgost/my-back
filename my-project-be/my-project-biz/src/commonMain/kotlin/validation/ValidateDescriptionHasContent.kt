package ru.otus.otuskotlin.lrvch.biz.validation

import ru.otus.otuskotlin.lrvch.biz.validation.helpers.hasNoContent
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.errorValidation
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

// пример обработки ошибки в рамках бизнес-цепочки
fun ICorChainDsl<CatalogContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    on { hasNoContent() }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
