package ru.otus.otuskotlin.lrvch.common

import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import ru.otus.otuskotlin.lrvch.logging.common.UniformLoggerProvider

data class CatalogCoreSettings(
    val loggerProvider: UniformLoggerProvider = UniformLoggerProvider(),
    val repoTest: IRepoStorage = IRepoStorage.NONE,
    val repoProd: IRepoStorage = IRepoStorage.NONE,
) {
    companion object {
        val NONE = CatalogCoreSettings()
    }
}
