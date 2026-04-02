package ru.otus.otuskotlin.lrvch.biz.general

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.chain

fun ICorChainDsl<CatalogContext>.stubs(title: String, block: ICorChainDsl<CatalogContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == CatalogWorkMode.STUB && state == CatalogState.RUNNING }
}