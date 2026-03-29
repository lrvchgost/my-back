package ru.otus.otuskotlin.lrvch.libs.cor

import TestVisitor
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.libs.cor.handlers.CorWorker
import kotlin.test.Test
import kotlin.test.assertEquals

class CorWorkerTest {

    @Test
    fun `worker should execute handle`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { history += "w1; " }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("w1; ", ctx.history)
    }

    @Test
    fun `worker should not execute when off`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockOn = { status == TestContext.CorStatuses.ERROR },
            blockHandle = { history += "w1; " }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("", ctx.history)
    }

    @Test
    fun `worker should handle exception`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { throw RuntimeException("some error") },
            blockExcept = { e -> history += e.message }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("some error", ctx.history)
    }

    @Test
    fun `worker should be run with visitor`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { history += "w1; " }
        )
        val ctx = TestContext()
        val visitor = TestVisitor<TestContext>()

        worker.visit(visitor, ctx)

        assertEquals("w1; ", ctx.history)
    }
}
