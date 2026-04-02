package ru.otus.otuskotlin.lrvch.biz.general

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.chain

fun ICorChainDsl<CatalogContext>.operation(
    title: String,
    command: CatalogCommand,
    block: ICorChainDsl<CatalogContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == CatalogState.RUNNING }
}
