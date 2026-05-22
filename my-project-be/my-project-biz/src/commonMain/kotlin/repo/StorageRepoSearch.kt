package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageFilterRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErrWithData
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseErrWithData
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseOk
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker
import kotlin.String

fun ICorChainDsl<CatalogContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == CatalogState.RUNNING }
    handle {
        val request = DbStorageFilterRequest(
            searchString = storageFilterValidated.searchString,
            availability = storageFilterValidated.availability,
            capacity = storageFilterValidated.capacity,
            paymentType = storageFilterValidated.paymentType,
            readSpeed = storageFilterValidated.readSpeed,
            writeSpeed = storageFilterValidated.writeSpeed,
        )
        when(val result = storageRepo.searchStorage(request)) {
            is DbStoragesResponseOk -> storagesRepoDone = result.data.toMutableList()
            is DbStoragesResponseErr -> fail(result.errors)
            is DbStoragesResponseErrWithData -> {}
        }
    }
}
