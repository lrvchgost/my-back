package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdsRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseErrWithData
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.IdEntity
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.repoReadByIds(title: String) = worker {
    this.title = title
    description = "Чтение стораджей из БД"
    on { state == CatalogState.RUNNING }
    handle {
        val request = DbStorageIdsRequest(storagesValidated.map { IdEntity(it.id, it.lock) })
        when(val result = storageRepo.searchStoragesByIds(request)) {
            is DbStoragesResponseOk -> storagesRepoRead = result.data.toMutableList()
            is DbStoragesResponseErr -> fail(result.errors)
            is DbStoragesResponseErrWithData -> {
                fail(result.toErrors())
                storagesRepoDone = result.toData().toMutableList()
            }
        }
    }
}