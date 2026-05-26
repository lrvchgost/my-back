package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErrWithData
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == CatalogState.RUNNING }
    handle {
        val request = DbStorageRequest(storageRepoPrepare)
        when(val result = storageRepo.updateStorage(request)) {
            is DbStorageResponseOk -> storageRepoDone = result.data
            is DbStorageResponseErr -> fail(result.errors)
            is DbStorageResponseErrWithData -> {
                fail(result.errors)
                storageRepoDone = result.data
            }
        }
    }
}
