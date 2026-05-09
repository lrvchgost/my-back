package ru.otus.otuskotlin.lrvch.biz.repo

import ru.otus.otuskotlin.lrvch.biz.exceptions.CatalogStorageDoNotConfiguredException
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.errorSystem
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        storageRepo = when {
            workMode == CatalogWorkMode.TEST -> coreSettings.repoTest
            else -> coreSettings.repoProd
        }
        if (workMode != CatalogWorkMode.STUB && storageRepo == IRepoStorage.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = CatalogStorageDoNotConfiguredException(workMode)
            )
        )
    }
}
