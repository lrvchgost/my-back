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
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {
    private val command = CatalogCommand.READ
    private val initAd = Storage(
        id = StorageId("123"),
        title = "abc",
        description = "abc",
    )
    private val repo = StorageRepositoryMock(
        invokeReadStorage = {
            DbStorageResponseOk(
                data = initAd,
            )
        }
    )
    private val settings = CatalogCoreSettings(repoTest = repo)
    private val processor = CatalogProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = CatalogContext(
            command = command,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.TEST,
            storageRequest = Storage(
                id = StorageId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(CatalogState.FINISHED, ctx.state)
        assertEquals(initAd.id, ctx.storageResponse.id)
        assertEquals(initAd.title, ctx.storageResponse.title)
        assertEquals(initAd.description, ctx.storageResponse.description)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
