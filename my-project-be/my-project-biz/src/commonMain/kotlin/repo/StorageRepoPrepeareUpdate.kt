package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == CatalogState.RUNNING }
    handle {
        storageRepoPrepare = storageRepoRead.deepCopy().apply {
            this.title = storageValidated.title
            description = storageValidated.description
            paymentType = storageValidated.paymentType
            readSpeed = storageValidated.readSpeed
            writeSpeed = storageValidated.writeSpeed
            optimizeEnabled = storageValidated.optimizeEnabled
            capacity = storageValidated.capacity
            availability = storageValidated.availability
            lock = storageValidated.lock
        }
    }
}
