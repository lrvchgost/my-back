package ru.otus.otuskotlin.lrvch.biz.validation.repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.StorageRepositoryMock
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoOptimizeTest {
    private val command = CatalogCommand.OPTIMIZE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val searchStorage1 = Storage(
        id = StorageId("123"),
        title = "abc1",
        description = "abc",
        capacity = "100",
        availability = "100",
        paymentType = CatalogPaymentType.FREE,
        readSpeed = SpeedType._200,
        writeSpeed = SpeedType._200,
    )
    private val searchStorage2 = Storage(
        id = StorageId("456"),
        title = "abc1",
        description = "abc",
        capacity = "100",
        availability = "100",
        paymentType = CatalogPaymentType.FREE,
        readSpeed = SpeedType._200,
        writeSpeed = SpeedType._200,
    )
    private val repo = StorageRepositoryMock(
        invokeSearchStoragesByIds = {
            DbStoragesResponseOk(
                data = listOf(searchStorage1, searchStorage2),
            )
        },
        invokeCreateStorage = {
            DbStorageResponseOk(
                data = Storage(
                    id = StorageId(uuid),
                    title = it.storage.title,
                    description = it.storage.description,
                    availability = it.storage.availability,
                    capacity = it.storage.capacity,
                    paymentType = it.storage.paymentType,
                    readSpeed = it.storage.readSpeed,
                    writeSpeed = it.storage.writeSpeed,
                    optimizeEnabled = it.storage.optimizeEnabled,
                )
            )
        },
        invokeDeleteStorage = {
            if (it.id == searchStorage1.id)
                return@StorageRepositoryMock DbStorageResponseOk(
                    data = searchStorage1
                )
            if (it.id == searchStorage2.id)
                return@StorageRepositoryMock DbStorageResponseOk(
                    data = searchStorage2
                )
            else DbStorageResponseErr(CatalogError(message = "test error"))
        }
    )
    private val settings = CatalogCoreSettings(repoTest = repo)
    private val processor = CatalogProcessor(settings)

    @Test
    fun repoOptimizeSuccessTest() = runTest {
        val ctx = CatalogContext(
            command = command,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.TEST,
            storagesRequest = listOf(searchStorage1, searchStorage2),
        )
        processor.exec(ctx)
        assertEquals(CatalogState.FINISHED, ctx.state)
        assertNotEquals(Storage(), ctx.storageResponse)
        assertNotEquals(StorageId.NONE, ctx.storageResponse.id)
        assertEquals(0, ctx.errors.size)
        assertEquals("Change it", ctx.storageResponse.title)
        assertEquals("Change it", ctx.storageResponse.description)
    }
}
