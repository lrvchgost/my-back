package ru.otus.otuskotlin.lrvch.biz.validation

import ru.otus.otuskotlin.lrvch.biz.validation.helpers.notAllStoragesHaveTheSamePaymentMethod
import ru.otus.otuskotlin.lrvch.biz.validation.helpers.notAllStoragesHaveTheSameReadSpeed
import ru.otus.otuskotlin.lrvch.biz.validation.helpers.notAllStoragesHaveTheSameWriteSpeed
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.errorValidation
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.chain
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.validateStoragesCompatible(title: String) = chain {
    this.title = title
    this.description = """
        Валидация что стораджи имеют одинаковые параметры:
        - readSpeed
        - writeSpeed
        - paymentType
    """.trimIndent()
    on { state == CatalogState.RUNNING }
    worker {
        this.title = "Стораджи имееют одинаковый тариф"
        this.description = this.title
        on { state == CatalogState.RUNNING && notAllStoragesHaveTheSamePaymentMethod() }
        handle {
            fail(
                errorValidation(
                    field = "paymentMethod",
                    violationCode = "notAllStoragesHaveTheSamePaymentMethod",
                    description = "All storages must have the same paymentMethod"
                )
            )
        }
    }
    worker {
        this.title = "Стораджи имееют одинаковую скорость записи"
        this.description = this.title
        on { state == CatalogState.RUNNING && notAllStoragesHaveTheSameWriteSpeed() }
        handle {
            fail(
                errorValidation(
                    field = "writeSpeed",
                    violationCode = "notAllStoragesHaveTheSameWriteSpeed",
                    description = "All storages must have the same writeSpeed"
                )
            )
        }
    }
    worker {
        this.title = "Стораджи имееют одинаковую скорость чтения"
        this.description = this.title
        on { state == CatalogState.RUNNING && notAllStoragesHaveTheSameReadSpeed() }
        handle {
            fail(
                errorValidation(
                    field = "readSpeed",
                    violationCode = "notAllStoragesHaveTheSameReadSpeed",
                    description = "All storages must have the same readSpeed"
                )
            )
        }
    }
}
