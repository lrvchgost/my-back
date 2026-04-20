package ru.otus.otuskotlin.lrvch.biz.validation.biz

import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationDescriptionCorrect
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationDescriptionEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationDescriptionTrim
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationTitleCorrect
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationTitleEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationTitleSymbols
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationTitleTrim
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationDescriptionSymbols
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

class BizValidateUpdateTest: BaseBizValidationTest() {
    override val command = CatalogCommand.UPDATE

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)

}
