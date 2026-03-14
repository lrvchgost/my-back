package ru.otus.otuskotlin.lrvch.app.common

import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings

interface ICatalogAppSettings {
    val processor: CatalogProcessor
    val corSettings: CatalogCoreSettings
}
