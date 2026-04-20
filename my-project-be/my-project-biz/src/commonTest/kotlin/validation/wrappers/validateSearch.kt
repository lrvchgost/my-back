package ru.otus.otuskotlin.lrvch.biz.validation.wrappers

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationSearchEmpty(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageFilterRequest = StorageFilter(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CatalogState.FAILED, ctx.state)
}

fun validationSearchMinLength(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageFilterRequest = StorageFilter("12"),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CatalogState.FAILED, ctx.state)
}

fun validationSearchMaxLength(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageFilterRequest = StorageFilter("1".repeat(101)),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CatalogState.FAILED, ctx.state)
}

fun validationSearchCorrect(command: CatalogCommand, processor: CatalogProcessor) = runTest {
    val ctx = CatalogContext(
        command = command,
        state = CatalogState.NONE,
        workMode = CatalogWorkMode.TEST,
        storageFilterRequest = StorageFilter("Search string"),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CatalogState.FAILED, ctx.state)
}
