package ru.otus.otuskotlin.lrvch.biz.validation.biz

import RepoStorageInitialized
import StorageRepoInMemory
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub

abstract class BaseBizValidationTest {
    private val repo = RepoStorageInitialized(
        repo = StorageRepoInMemory(),
        initObjects = listOf(
            CatalogStorageStub.get(),
        ),
    )
    protected abstract val command: CatalogCommand
    private val settings by lazy { CatalogCoreSettings(repoTest = repo) }
    protected val processor by lazy { CatalogProcessor(settings) }
}