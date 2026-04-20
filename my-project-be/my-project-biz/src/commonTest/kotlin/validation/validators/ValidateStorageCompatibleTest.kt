package ru.otus.otuskotlin.lrvch.biz.validation.validators

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.validation.validateStoragesCompatible
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.libs.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateStorageCompatibleTest {
    @Test
    fun allCompatible() = runTest {
        val ctx = CatalogContext(
            state = CatalogState.RUNNING, storagesValidating = mutableListOf(
                Storage(
                    title = "",
                    id = CatalogRequestId(""),
                    paymentType = CatalogPaymentType.FREE,
                    readSpeed = SpeedType._100,
                    writeSpeed = SpeedType._100,
                ),
                Storage(
                    title = "",
                    id = CatalogRequestId("2"),
                    paymentType = CatalogPaymentType.FREE,
                    readSpeed = SpeedType._100,
                    writeSpeed = SpeedType._100,
                )
            )
        )
        chain.exec(ctx)
        assertEquals(CatalogState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun paymentTypeIncompatible() = runTest {
        val ctx = CatalogContext(
            state = CatalogState.RUNNING, storagesValidating = mutableListOf(
                Storage(
                    title = "",
                    id = CatalogRequestId(""),
                    paymentType = CatalogPaymentType.LICENSE,
                    readSpeed = SpeedType._100,
                    writeSpeed = SpeedType._100,
                ),
                Storage(
                    title = "",
                    id = CatalogRequestId("2"),
                    paymentType = CatalogPaymentType.FREE,
                    readSpeed = SpeedType._100,
                    writeSpeed = SpeedType._100,
                )
            )
        )
        chain.exec(ctx)
        assertEquals(CatalogState.FAILED, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("paymentMethod", ctx.errors.firstOrNull()?.field)
    }

    @Test
    fun readSpeedIncompatible() = runTest {
        val ctx = CatalogContext(
            state = CatalogState.RUNNING, storagesValidating = mutableListOf(
                Storage(
                    title = "",
                    id = CatalogRequestId(""),
                    paymentType = CatalogPaymentType.FREE,
                    readSpeed = SpeedType._200,
                    writeSpeed = SpeedType._100,
                ),
                Storage(
                    title = "",
                    id = CatalogRequestId("2"),
                    paymentType = CatalogPaymentType.FREE,
                    readSpeed = SpeedType._100,
                    writeSpeed = SpeedType._100,
                )
            )
        )
        chain.exec(ctx)
        assertEquals(CatalogState.FAILED, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("readSpeed", ctx.errors.firstOrNull()?.field)
    }
    @Test
    fun writeSpeedIncompatible() = runTest {
        val ctx = CatalogContext(
            state = CatalogState.RUNNING, storagesValidating = mutableListOf(
                Storage(
                    title = "",
                    id = CatalogRequestId(""),
                    paymentType = CatalogPaymentType.FREE,
                    readSpeed = SpeedType._100,
                    writeSpeed = SpeedType._200,
                ),
                Storage(
                    title = "",
                    id = CatalogRequestId("2"),
                    paymentType = CatalogPaymentType.FREE,
                    readSpeed = SpeedType._100,
                    writeSpeed = SpeedType._100,
                )
            )
        )
        chain.exec(ctx)
        assertEquals(CatalogState.FAILED, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("writeSpeed", ctx.errors.firstOrNull()?.field)
    }

    companion object {
        val chain = rootChain {
            validateStoragesCompatible("")
        }.build()
    }
}