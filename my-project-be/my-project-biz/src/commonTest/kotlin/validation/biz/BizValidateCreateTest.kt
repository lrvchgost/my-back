package ru.otus.otuskotlin.lrvch.biz.validation.biz

import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationDescriptionCorrect
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationDescriptionEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationDescriptionSymbols
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationDescriptionTrim
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationTitleCorrect
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationTitleEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationTitleSymbols
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationTitleTrim
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import kotlin.test.Test

class BizValidateCreateTest: BaseBizValidationTest() {
    override val command: CatalogCommand = CatalogCommand.CREATE

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)
}
