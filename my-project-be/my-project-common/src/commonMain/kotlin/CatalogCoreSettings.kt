package ru.otus.otuskotlin.lrvch.common

import ru.otus.otuskotlin.lrvch.logging.common.UniformLoggerProvider

data class CatalogCoreSettings(
    val loggerProvider: UniformLoggerProvider = UniformLoggerProvider(),
) {
    companion object {
        val NONE = CatalogCoreSettings()
    }
}
