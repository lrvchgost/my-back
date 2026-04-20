package ru.otus.otuskotlin.lrvch.biz.stubs

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker
import ru.otus.otuskotlin.lrvch.logging.common.LogLevel
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub

fun ICorChainDsl<CatalogContext>.stubCreateSuccess(title: String, corSettings: CatalogCoreSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания стораджа
    """.trimIndent()
    on { stubCase == CatalogStubs.SUCCESS && state == CatalogState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateStorageSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = CatalogState.FINISHED
            storageResponse = CatalogStorageStub.prepareResult {
                this.title = storageRequest.title.takeIf { it.isNotBlank() } ?: ""
//                storageRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                this.description = storageRequest.description.takeIf { it.isNotBlank() } ?: ""
//                storageRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                storageRequest.paymentType.takeIf { it != CatalogPaymentType.FREE }?.also { this.paymentType = it }
                storageRequest.readSpeed.takeIf { it != SpeedType.NONE }?.also { this.readSpeed = it }
                storageRequest.id.takeIf { it != CatalogRequestId.NONE }?.also { this.id = it }
            }
        }
    }
}
