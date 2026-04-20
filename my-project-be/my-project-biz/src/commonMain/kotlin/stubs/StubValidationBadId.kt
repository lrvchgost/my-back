package ru.otus.otuskotlin.lrvch.biz.stubs

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для id стораджа
    """.trimIndent()
    on { stubCase == CatalogStubs.BAD_ID && state == CatalogState.RUNNING }
    handle {
        fail(
            CatalogError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
