package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == CatalogState.RUNNING }
    handle {
        storageRepoPrepare = storageValidated.deepCopy()
        storagesRepoPrepare = storagesValidated.map { it.deepCopy()}.toMutableList()
        // TODO будет реализовано в занятии по управлению пользвателями
//        storageRepoPrepare.ownerId = MkplAdStub.get().ownerId
    }
}
