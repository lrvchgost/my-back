package ru.otus.otuskotlin.lrvch.biz.validation.validators

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.validation.validateIdNotEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.validateLockNotEmpty
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.libs.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateLockNotEmptyTest {
    @Test
    fun emptyLock() = runTest {
        val ctx = CatalogContext(state = CatalogState.RUNNING, storageValidating = Storage(lock = StorageLock("")))
        chain.exec(ctx)
        assertEquals(CatalogState.FAILED, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-lock-empty", ctx.errors.first().code)
    }

    @Test
    fun normalLock() = runTest {
        val ctx = CatalogContext(state = CatalogState.RUNNING, storageValidating = Storage(title = "123"))
        ValidateDescriptionHasContentTest.chain.exec(ctx)
        assertEquals(CatalogState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateLockNotEmpty("")
        }.build()
    }
}
