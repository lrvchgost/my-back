package ru.otus.otuskotlin.lrvch.biz.validation

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.finishStorageValidation(title: String) = worker {
    this.title = title
    on { state == CatalogState.RUNNING }
    handle {
        storageValidated = storageValidating
        storagesValidated = storagesValidating
    }
}

fun ICorChainDsl<CatalogContext>.finishStorageFilterValidation(title: String) = worker {
    this.title = title
    on { state == CatalogState.RUNNING }
    handle {
        storageFilterValidated = storageFilterValidating
    }
}
