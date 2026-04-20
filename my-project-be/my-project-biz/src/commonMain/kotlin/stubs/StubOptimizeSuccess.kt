package ru.otus.otuskotlin.lrvch.biz.stubs

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.worker
import ru.otus.otuskotlin.lrvch.logging.common.LogLevel
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub

fun ICorChainDsl<CatalogContext>.stubOptimizeStoragesSuccess(title: String, corSettings: CatalogCoreSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для получения списка стораджей для оптимизации
    """.trimIndent()
    on { stubCase == CatalogStubs.SUCCESS && state == CatalogState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOptimizeStoragesSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = CatalogState.FINISHED
            storageResponse = CatalogStorageStub.prepareResult {
                this.title = ""
                this.description = ""
                storagesRequest.first().paymentType.takeIf { it != CatalogPaymentType.FREE }?.also { this.paymentType = it }
                storagesRequest.first().readSpeed.takeIf { it != SpeedType.NONE }?.also { this.readSpeed = it }
                this.id = CatalogStorageStub.getDefaultUuid()
            }
        }
    }
}
