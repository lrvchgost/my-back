package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == CatalogState.RUNNING }
    handle {
        storageRepoPrepare = storageValidated.deepCopy()
        storagesRepoPrepare = storagesValidated.map { it.deepCopy() }.toMutableList()
    }
}
