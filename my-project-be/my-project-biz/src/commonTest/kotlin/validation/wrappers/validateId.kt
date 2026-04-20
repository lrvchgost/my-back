package ru.otus.otuskotlin.lrvch.biz.validation.wrappers

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = CatalogRequestId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            lock = StorageLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CatalogState.FAILED, ctx.state)
}

fun validationIdTrim(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = CatalogRequestId(" \n\t 123-234-abc-ABC \n\t "),
            title = "abc",
            description = " \n\t abc \t\n ",
            lock = StorageLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CatalogState.FAILED, ctx.state)
}

fun validationIdEmpty(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = CatalogRequestId(""),
            title = "abc",
            description = "abc",
            lock = StorageLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CatalogState.FAILED, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = CatalogRequestId("!@#\$%^&*(),.{}"),
            title = "abc",
            description = "abc",
            lock = StorageLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CatalogState.FAILED, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
