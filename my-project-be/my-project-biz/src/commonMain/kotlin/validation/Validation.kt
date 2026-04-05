package ru.otus.otuskotlin.lrvch.biz.validation

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.chain

fun ICorChainDsl<CatalogContext>.validation(block: ICorChainDsl<CatalogContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == CatalogState.RUNNING }
}