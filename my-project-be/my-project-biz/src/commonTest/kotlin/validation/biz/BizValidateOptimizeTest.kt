package ru.otus.otuskotlin.lrvch.biz.validation.biz

import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationOptimizeCorrect
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationOptimizePaymentIncompatible
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationOptimizeReadSpeedIncompatible
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationOptimizeWriteSpeedIncompatible
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import kotlin.test.Test

class BizValidateOptimizeTest: BaseBizValidationTest() {
    override val command = CatalogCommand.OPTIMIZE

    @Test fun correctOptimize() = validationOptimizeCorrect(command, processor)
    @Test fun incorrectPayment() = validationOptimizePaymentIncompatible(command, processor)
    @Test fun incorrectWriteSpeed() = validationOptimizeWriteSpeedIncompatible(command, processor)
    @Test fun incorrectReadSpeed() = validationOptimizeReadSpeedIncompatible(command, processor)
}
