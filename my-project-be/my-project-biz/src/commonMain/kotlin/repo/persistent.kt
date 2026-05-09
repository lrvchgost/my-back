package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.chain

fun ICorChainDsl<CatalogContext>.persistent(block: ICorChainDsl<CatalogContext>.() -> Unit) = chain {
    block()
    title = "Логика сохранения"

    on { state == CatalogState.RUNNING }
}
