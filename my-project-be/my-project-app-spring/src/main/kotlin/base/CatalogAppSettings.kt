package ru.otus.otuskotlin.lrvch.app.spring.base

import ru.otus.otuskotlin.lrvch.app.common.ICatalogAppSettings
import ru.otus.otuskotlin.lrvch.biz.ICatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings

data class CatalogAppSettings(
    override val corSettings: CatalogCoreSettings,
    override val processor: ICatalogProcessor,
): ICatalogAppSettings
