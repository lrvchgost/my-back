package ru.otus.otuskotlin.lrvch.biz.stubs

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class StorageSearchStubTest {

    private val processor = CatalogProcessor()
    val filter = StorageFilter(searchString = "central")

    @Test
    fun read() = runTest {

        val ctx = CatalogContext(
            command = CatalogCommand.SEARCH,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.SUCCESS,
            storageFilterRequest = filter,
        )

        processor.exec(ctx)

        assertTrue(ctx.storagesResponse.size > 1)

        val first = ctx.storagesResponse.firstOrNull() ?: fail("Empty response list")

        println(first)

        assertTrue(first.title.contains(filter.searchString))
//        assertTrue(first.description.contains(filter.searchString))
        with (CatalogStorageStub.get()) {
            assertEquals(paymentType, first.paymentType)
            assertEquals(readSpeed, first.readSpeed)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = CatalogContext(
            command = CatalogCommand.SEARCH,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.BAD_ID,
            storageFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Storage(), ctx.storageResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = CatalogContext(
            command = CatalogCommand.SEARCH,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.DB_ERROR,
            storageFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Storage(), ctx.storageResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = CatalogContext(
            command = CatalogCommand.SEARCH,
            state = CatalogState.NONE,
            workMode = CatalogWorkMode.STUB,
            stubCase = CatalogStubs.BAD_TITLE,
            storageFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Storage(), ctx.storageResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
