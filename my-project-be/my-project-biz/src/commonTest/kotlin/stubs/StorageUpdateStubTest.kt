package ru.otus.otuskotlin.lrvch.biz.stubs

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class StorageUpdateStubTest {

    private val processor = CatalogProcessor()
    val id = CatalogRequestId("666")
    val title = "title 666"
    val description = "desc 666"
    val paymentType = CatalogPaymentType.FREE
    val readSpeed = SpeedType._100

    @Test
    fun create() = runTest {

        val ctx = CatalogContext(
            command = CatalogCommand.UPDATE,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.SUCCESS,
            storageRequest = Storage(
                id = id,
                title = title,
                description = description,
                paymentType = paymentType,
                readSpeed = readSpeed,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.storageResponse.id)
        assertEquals(title, ctx.storageResponse.title)
        assertEquals(description, ctx.storageResponse.description)
        assertEquals(paymentType, ctx.storageResponse.paymentType)
        assertEquals(readSpeed, ctx.storageResponse.readSpeed)
    }

    @Test
    fun badId() = runTest {
        val ctx = CatalogContext(
            command = CatalogCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = CatalogContext(
            command = CatalogCommand.UPDATE,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.BAD_TITLE,
            storageRequest = Storage(
                id = id,
                title = "",
                description = description,
                paymentType = paymentType,
                readSpeed = readSpeed,
            ),
        )
        processor.exec(ctx)
        assertEquals(Storage(), ctx.storageResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badDescription() = runTest {
        val ctx = CatalogContext(
            command = CatalogCommand.UPDATE,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.BAD_DESCRIPTION,
            storageRequest = Storage(
                id = id,
                title = title,
                description = "",
                paymentType = paymentType,
                readSpeed = readSpeed,
            ),
        )
        processor.exec(ctx)
        assertEquals(Storage(), ctx.storageResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = CatalogContext(
            command = CatalogCommand.UPDATE,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.DB_ERROR,
            storageRequest = Storage(
                id = id,
            ),
        )
        processor.exec(ctx)
        println(ctx)
        assertEquals(Storage(), ctx.storageResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = CatalogContext(
            command = CatalogCommand.UPDATE,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.BAD_SEARCH,
            storageRequest = Storage(
                id = id,
                title = title,
                description = description,
                readSpeed = readSpeed,
                paymentType = paymentType,
            ),
        )
        processor.exec(ctx)
        assertEquals(Storage(), ctx.storageResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
