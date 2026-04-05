package ru.otus.otuskotlin.lrvch.biz.validation.biz

import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationIdCorrect
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationIdEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationIdFormat
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationIdTrim
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationLockCorrect
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationLockEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationLockFormat
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationLockTrim
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import kotlin.test.Test

class BizValidateDeleteTest: BaseBizValidationTest() {
    override val command = CatalogCommand.DELETE

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)

}
