package ru.otus.otuskotlin.lrvch.biz.validation.biz

import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationSearchCorrect
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationSearchEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationSearchMaxLength
import ru.otus.otuskotlin.lrvch.biz.validation.wrappers.validationSearchMinLength
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import kotlin.test.Test

class BizValidateSearchTest: BaseBizValidationTest() {
    override val command = CatalogCommand.SEARCH

    @Test fun correctSearch() = validationSearchCorrect(command, processor)
    @Test fun correctSearchMinLength() = validationSearchMinLength(command, processor)
    @Test fun correctSearchMaxLength() = validationSearchMaxLength(command, processor)
    @Test fun correctSearchEmpty() = validationSearchEmpty(command, processor)
}
