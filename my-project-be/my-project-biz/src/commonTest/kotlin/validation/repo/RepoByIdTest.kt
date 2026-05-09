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
import ru.otus.otuskotlin.lrvch.common.repo.errorNotFound
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initAd = Storage(
    id = StorageId("123"),
    title = "abc",
    description = "abc",
)
private val repo = StorageRepositoryMock(
    invokeReadStorage = {
        if (it.id == initAd.id) {
            DbStorageResponseOk(
                data = initAd,
            )
        } else errorNotFound(it.id)
    }
)
private val settings = CatalogCoreSettings(repoTest = repo)
private val processor = CatalogProcessor(settings)

fun repoNotFoundTest(command: CatalogCommand) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = StorageId("12345"),
            title = "xyz",
            description = "xyz",
            lock = StorageLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(CatalogState.FAILED, ctx.state)
    assertEquals(Storage(), ctx.storageResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
