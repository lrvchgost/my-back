package ru.otus.otuskotlin.lrvch.biz.validation.repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.StorageRepositoryMock
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {
    private val command = CatalogCommand.SEARCH
    private val initAd = Storage(
        id = StorageId("123"),
        title = "abc",
        description = "abc",
    )
    private val repo = StorageRepositoryMock(
        invokeSearchStorage = {
            DbStoragesResponseOk(
                data = listOf(initAd),
            )
        }
    )
    private val settings = CatalogCoreSettings(repoTest = repo)
    private val processor = CatalogProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = CatalogContext(
            command = command,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.TEST,
            storageFilterRequest = StorageFilter(
                searchString = "abc",
            ),
        )
        processor.exec(ctx)
        assertEquals(CatalogState.FINISHED, ctx.state)
        assertEquals(1, ctx.storagesResponse.size)
    }
}
