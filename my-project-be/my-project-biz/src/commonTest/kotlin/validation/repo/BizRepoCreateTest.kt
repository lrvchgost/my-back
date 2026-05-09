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
import kotlin.test.assertNotEquals

class BizRepoCreateTest {
    private val command = CatalogCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = StorageRepositoryMock(
        invokeCreateStorage = {
            DbStorageResponseOk(
                data = Storage(
                    id = StorageId(uuid),
                    title = it.storage.title,
                    description = it.storage.description,
                )
            )
        }
    )
    private val settings = CatalogCoreSettings(
        repoTest = repo
    )
    private val processor = CatalogProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = CatalogContext(
            command = command,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.TEST,
            storageRequest = Storage(
                title = "abc",
                description = "abc",
            ),
        )
        processor.exec(ctx)
        assertEquals(CatalogState.FINISHED, ctx.state)
        assertNotEquals(StorageId.NONE, ctx.storageResponse.id)
        assertEquals("abc", ctx.storageResponse.title)
        assertEquals("abc", ctx.storageResponse.description)
    }
}
