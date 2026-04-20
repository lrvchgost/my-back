package ru.otus.otuskotlin.lrvch.biz.validation.validators

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.validation.validateSearchStringLength
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.libs.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = CatalogContext(state = CatalogState.RUNNING, storageFilterValidating = StorageFilter(searchString = ""))
        chain.exec(ctx)
        assertNotEquals(CatalogState.FAILED, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = CatalogContext(state = CatalogState.RUNNING, storageFilterValidating = StorageFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(CatalogState.FAILED, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun shortString() = runTest {
        val ctx = CatalogContext(state = CatalogState.RUNNING, storageFilterValidating = StorageFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(CatalogState.FAILED, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = CatalogContext(state = CatalogState.RUNNING, storageFilterValidating = StorageFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(CatalogState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = CatalogContext(state = CatalogState.RUNNING, storageFilterValidating = StorageFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(CatalogState.FAILED, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
