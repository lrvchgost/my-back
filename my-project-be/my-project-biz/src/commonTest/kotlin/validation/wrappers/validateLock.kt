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
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: CatalogCommand, processor: CatalogProcessor) = runTest {
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

fun validationLockTrim(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = CatalogRequestId("123-234-abc-ABC"),
            title = "abc",
            description = " \n\t abc \t\n ",
            lock = StorageLock(" \n\t 123-234-abc-ABC \n\t "),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CatalogState.FAILED, ctx.state)
}

fun validationLockEmpty(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = CatalogRequestId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            lock = StorageLock(""),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CatalogState.FAILED, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "lock")
}

fun validationLockFormat(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = CatalogRequestId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            lock = StorageLock("!@#\$%^&*(),.{}"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CatalogState.FAILED, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "lock")
}
