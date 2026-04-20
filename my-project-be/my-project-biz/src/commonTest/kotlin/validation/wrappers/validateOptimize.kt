package ru.otus.otuskotlin.lrvch.biz.validation.wrappers

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
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationOptimizeCorrect(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storagesRequest = mutableListOf(
            Storage(
                id = CatalogRequestId("1"),
                title = "abc",
                description = "abc",
                paymentType = CatalogPaymentType.FREE,
                writeSpeed = SpeedType._100,
                readSpeed = SpeedType._100,
            ),
            Storage(
                id = CatalogRequestId("1"),
                title = "abc",
                description = "abc",
                paymentType = CatalogPaymentType.FREE,
                writeSpeed = SpeedType._100,
                readSpeed = SpeedType._100,
            ),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CatalogState.FAILED, ctx.state)
    assertEquals(2, ctx.storagesValidated.size)
}

fun validationOptimizePaymentIncompatible(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storagesRequest = mutableListOf(
            Storage(
                id = CatalogRequestId("1"),
                title = "abc",
                description = "abc",
                paymentType = CatalogPaymentType.LICENSE,
                writeSpeed = SpeedType._100,
                readSpeed = SpeedType._100,
            ),
            Storage(
                id = CatalogRequestId("1"),
                title = "abc",
                description = "abc",
                paymentType = CatalogPaymentType.FREE,
                writeSpeed = SpeedType._100,
                readSpeed = SpeedType._100,
            ),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CatalogState.FAILED, ctx.state)
}

fun validationOptimizeWriteSpeedIncompatible(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storagesRequest = mutableListOf(
            Storage(
                id = CatalogRequestId("1"),
                title = "abc",
                description = "abc",
                paymentType = CatalogPaymentType.FREE,
                writeSpeed = SpeedType._200,
                readSpeed = SpeedType._100,
            ),
            Storage(
                id = CatalogRequestId("1"),
                title = "abc",
                description = "abc",
                paymentType = CatalogPaymentType.FREE,
                writeSpeed = SpeedType._100,
                readSpeed = SpeedType._100,
            ),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CatalogState.FAILED, ctx.state)
}

fun validationOptimizeReadSpeedIncompatible(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storagesRequest = mutableListOf(
            Storage(
                id = CatalogRequestId("1"),
                title = "abc",
                description = "abc",
                paymentType = CatalogPaymentType.FREE,
                writeSpeed = SpeedType._200,
                readSpeed = SpeedType._200,
            ),
            Storage(
                id = CatalogRequestId("1"),
                title = "abc",
                description = "abc",
                paymentType = CatalogPaymentType.FREE,
                writeSpeed = SpeedType._100,
                readSpeed = SpeedType._100,
            ),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CatalogState.FAILED, ctx.state)
}
