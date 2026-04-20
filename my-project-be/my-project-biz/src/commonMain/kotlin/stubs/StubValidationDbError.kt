package ru.otus.otuskotlin.lrvch.biz.stubs

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.stubValidationDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == CatalogStubs.DB_ERROR && state == CatalogState.RUNNING }
    handle {
        fail(
            CatalogError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
