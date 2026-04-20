package ru.otus.otuskotlin.lrvch.biz.validation.validators

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.validation.validateTitleHasContent
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.libs.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTitleNotEmptyTest {
    @Test
    fun emptyString() = runTest {
        val ctx = CatalogContext(state = CatalogState.RUNNING, storageValidating = Storage(title = ""))
        chain.exec(ctx)
        assertEquals(CatalogState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
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
