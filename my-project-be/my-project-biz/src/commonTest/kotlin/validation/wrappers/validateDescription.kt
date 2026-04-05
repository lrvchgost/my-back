package ru.otus.otuskotlin.lrvch.biz.validation.wrappers

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = CatalogStorageStub.get()

fun validationDescriptionCorrect(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = stub.id,
            title = "abc",
            description = "abc",
            lock = StorageLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CatalogState.FAILED, ctx.state)
    assertEquals("abc", ctx.storageValidated.description)
}

fun validationDescriptionTrim(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = stub.id,
            title = "abc",
            description = " \n\t abc \t\n ",
            lock = StorageLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CatalogState.FAILED, ctx.state)
    assertEquals("abc", ctx.storageValidated.description)
}

fun validationDescriptionEmpty(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = stub.id,
            title = "abc",
            description = "",
            lock = StorageLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CatalogState.FAILED, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageRequest = Storage(
            id = stub.id,
            title = "abc",
            description = "!@#$%^&*(),.{}",
            lock = StorageLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CatalogState.FAILED, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
