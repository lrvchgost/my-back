package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErrWithData
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.repoOptimize(title: String) = worker {
    this.title = title
    description = "Создание одного объединенного стораджа в БД"
    on { state == CatalogState.RUNNING }
    handle {
        val baseStorage = storagesRepoPrepare.first()
        val capacityTotal = storagesRepoPrepare.sumOf{ it.capacity.toInt() }.toString()
        val optimizedStorage = Storage().apply {
            this.title = "Change it"
            description = "Change it"
            paymentType = baseStorage.paymentType
            readSpeed = baseStorage.readSpeed
            writeSpeed = baseStorage.writeSpeed
            optimizeEnabled = baseStorage.optimizeEnabled
            capacity = capacityTotal
            availability = baseStorage.availability
        }

        val request = DbStorageRequest(optimizedStorage)
        when(val result = storageRepo.createStorage(request)) {
            is DbStorageResponseOk -> storageRepoDone = result.data
            is DbStorageResponseErr -> fail(result.errors)
            is DbStorageResponseErrWithData -> {
                fail(result.errors)
                storageRepoDone = result.data
            }
        }
    }
}
