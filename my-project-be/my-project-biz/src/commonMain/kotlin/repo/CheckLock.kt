package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.repo.ConcurrencyMultipleErrorData
import ru.otus.otuskotlin.lrvch.common.repo.errorRepoConcurrency
import ru.otus.otuskotlin.lrvch.common.repo.errorRepoConcurrencyMultiple
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == CatalogState.RUNNING && storageValidated.lock != storageRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(storageRepoRead, storageValidated.lock).errors)
    }
}

fun ICorChainDsl<CatalogContext>.checkLocks(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == CatalogState.RUNNING }
    handle {
        val source = storagesRequest.sortedBy { it.id.asString()}
        val target = storagesValidated.sortedBy { it.id.asString() }
        val paired = source.zip(target)

        val hasLockErrors = paired.any { it.first.lock != it.second.lock }

        if (hasLockErrors) {
            fail(errorRepoConcurrencyMultiple(paired.map {
                ConcurrencyMultipleErrorData(
                    it.first,
                    it.second.lock
                )
            }).toErrors())
        }
    }
}
