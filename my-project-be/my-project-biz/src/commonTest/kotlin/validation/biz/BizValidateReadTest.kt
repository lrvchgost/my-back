package ru.otus.otuskotlin.lrvch.biz.validation.biz

import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationIdCorrect
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationIdEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationIdFormat
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationIdTrim
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import kotlin.test.Test

class BizValidateReadTest: BaseBizValidationTest() {
    override val command = CatalogCommand.READ

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}
