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
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoUpdateTest {
    private val command = CatalogCommand.UPDATE
    private val initAd = Storage(
        id = StorageId("123"),
        title = "abc",
        description = "abc",
        lock = StorageLock("123-234-abc-ABC"),
    )
    private val repo = StorageRepositoryMock(
        invokeReadStorage = {
            DbStorageResponseOk(
                data = initAd,
            )
        },
        invokeUpdateStorage = {
            DbStorageResponseOk(
                data = Storage(
                    id = StorageId("123"),
                    title = "xyz",
                    description = "xyz",
                )
            )
        }
    )
    private val settings = CatalogCoreSettings(repoTest = repo)
    private val processor = CatalogProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val storageToUpdate = Storage(
            id = StorageId("123"),
            title = "xyz",
            description = "xyz",
            lock = StorageLock("123-234-abc-ABC"),
        )
        val ctx = CatalogContext(
            command = command,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.TEST,
            storageRequest = storageToUpdate,
        )
        processor.exec(ctx)

        assertEquals(CatalogState.FINISHED, ctx.state)
        assertEquals(storageToUpdate.id, ctx.storageResponse.id)
        assertEquals(storageToUpdate.title, ctx.storageResponse.title)
        assertEquals(storageToUpdate.description, ctx.storageResponse.description)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
