package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.addErrors
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErrWithData
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление стораджа из БД по ID"
    on { state == CatalogState.RUNNING }
    handle {
        val request = DbStorageIdRequest(storageRepoPrepare)
        when (val result = storageRepo.deleteStorage(request)) {
            is DbStorageResponseOk -> storageRepoDone = result.data
            is DbStorageResponseErr -> {
                fail(result.errors)
                storageRepoDone = storageRepoRead
            }

            is DbStorageResponseErrWithData -> {
                fail(result.errors)
                storageRepoDone = result.data
            }
        }
    }
}

fun ICorChainDsl<CatalogContext>.repoDeleteByIds(title: String) = worker {
    this.title = title
    description = "Удаление стораджей из БД по ID"
    on { state == CatalogState.RUNNING }
    handle {
        storagesRepoPrepare.forEach { storage ->
            val request = DbStorageIdRequest(storage)
            when (val result = storageRepo.deleteStorage(request)) {
                is DbStorageResponseOk -> {}
                is DbStorageResponseErr -> {
                    addErrors(result.errors)
                }
                is DbStorageResponseErrWithData -> {
                    addErrors(result.errors)
                }
            }
        }
    }
}
