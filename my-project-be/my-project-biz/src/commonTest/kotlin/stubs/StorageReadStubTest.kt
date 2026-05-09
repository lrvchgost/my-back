package ru.otus.otuskotlin.lrvch.biz.stubs

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub
import kotlin.test.Test
import kotlin.test.assertEquals

class StorageReadStubTest {

    private val processor = CatalogProcessor()
    val id = StorageId("666")

    @Test
    fun read() = runTest {

        val ctx = CatalogContext(
            command = CatalogCommand.READ,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.SUCCESS,
            storageRequest = Storage(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (CatalogStorageStub.get()) {
            assertEquals(id, ctx.storageResponse.id)
            assertEquals(title, ctx.storageResponse.title)
            assertEquals(description, ctx.storageResponse.description)
            assertEquals(paymentType, ctx.storageResponse.paymentType)
            assertEquals(readSpeed, ctx.storageResponse.readSpeed)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = CatalogContext(
            command = CatalogCommand.READ,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.BAD_ID,
            storageRequest = Storage(),
        )
        processor.exec(ctx)
        assertEquals(Storage(), ctx.storageResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = CatalogContext(
            command = CatalogCommand.READ,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.DB_ERROR,
            storageRequest = Storage(
                id = id
            ),
        )
        processor.exec(ctx)
        assertEquals(Storage(), ctx.storageResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = CatalogContext(
            command = CatalogCommand.READ,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.BAD_TITLE,
            storageRequest = Storage(
                id = id
            ),
        )
        processor.exec(ctx)
        assertEquals(Storage(), ctx.storageResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}