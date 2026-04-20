package ru.otus.otuskotlin.lrvch.biz.validation.validators

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.validation.validateTitleHasContent
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.libs.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTitleHasContentTest {
    @Test
    fun noContent() = runTest {
        val ctx = CatalogContext(state = CatalogState.RUNNING, storageValidating = Storage(title = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(CatalogState.FAILED, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = CatalogContext(state = CatalogState.RUNNING, storageValidating = Storage(title = "Ж"))
        chain.exec(ctx)
        assertEquals(CatalogState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTitleHasContent("")
        }.build()
    }
}
