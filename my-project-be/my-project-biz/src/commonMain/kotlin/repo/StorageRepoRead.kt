package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErrWithData
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение стораджа из БД"
    on { state == CatalogState.RUNNING }
    handle {
        val request = DbStorageIdRequest(storageValidated)
        when(val result = storageRepo.readStorage(request)) {
            is DbStorageResponseOk -> storageRepoRead = result.data
            is DbStorageResponseErr -> fail(result.errors)
            is DbStorageResponseErrWithData -> {
                fail(result.errors)
                storageRepoRead = result.data
            }
        }
    }
}

fun ICorChainDsl<CatalogContext>.repoReadComplete(title: String) = worker {
    this.title = title
    description = "Подготовка ответа для Read"
    on { state == CatalogState.RUNNING }
    handle { storageRepoDone = storageRepoRead }
}
