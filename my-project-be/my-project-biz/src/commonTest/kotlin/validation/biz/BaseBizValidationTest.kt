package ru.otus.otuskotlin.lrvch.biz.validation.biz

import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand

abstract class BaseBizValidationTest {
    protected abstract val command: CatalogCommand
    private val settings by lazy { CatalogCoreSettings() }
    protected val processor by lazy { CatalogProcessor(settings) }
}