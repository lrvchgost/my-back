package ru.otus.otuskotlin.lrvch.biz.stubs

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker
import ru.otus.otuskotlin.lrvch.logging.common.LogLevel
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub

fun ICorChainDsl<CatalogContext>.stubReadSuccess(title: String, corSettings: CatalogCoreSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для чтения стораджа
    """.trimIndent()
    on { stubCase == CatalogStubs.SUCCESS && state == CatalogState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubReadStorageSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = CatalogState.FINISHED
            storageResponse = CatalogStorageStub.prepareResult {
                storageRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            }
        }
    }
}
