package ru.otus.otuskotlin.lrvch.biz.validation.validators

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.validation.validateIdNotEmpty
import ru.otus.otuskotlin.lrvch.biz.validation.validateIdProperFormat
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.libs.cor.rootChain
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateIdProperFormatTest {
    @Test
    fun properFormat() = runTest {
        val ctx = CatalogContext(
            state = CatalogState.RUNNING, storageValidating = Storage(
                id =
                    CatalogRequestId("123)")
            )
        )
        chain.exec(ctx)
        assertEquals(CatalogState.FAILED, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-id-badFormat", ctx.errors.first().code)
    }

    @Test
    fun notProperFormat() = runTest {
        val ctx = CatalogContext(
            state = CatalogState.RUNNING, storageValidating = Storage(
                id =
                    CatalogStorageStub.getDefaultUuid()
            )
        )
        ValidateDescriptionHasContentTest.chain.exec(ctx)
        assertEquals(CatalogState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateIdProperFormat("")
        }.build()
    }
}
