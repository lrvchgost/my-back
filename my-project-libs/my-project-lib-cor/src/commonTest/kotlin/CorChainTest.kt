package ru.otus.otuskotlin.lrvch.libs.cor

import TestVisitor
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.libs.cor.handlers.CorChain
import ru.otus.otuskotlin.lrvch.libs.cor.handlers.CorWorker
import kotlin.test.Test
import kotlin.test.assertEquals

class CorChainTest {

    @Test
    fun `chain should execute workers`() = runTest {
        val createWorker = { title: String ->
            CorWorker<TestContext>(
                title = title,
                blockOn = { status == TestContext.CorStatuses.NONE },
                blockHandle = { history += "$title; " }
            )
        }
        val chain = CorChain<TestContext>(
            execs = listOf(createWorker("w1"), createWorker("w2")),
            title = "chain",
        )
        val ctx = TestContext()
        chain.exec(ctx)
        assertEquals("w1; w2; ", ctx.history)
    }

    @Test
    fun `chain should be executed with visitor`() = runTest {
        val createWorker = { title: String ->
            CorWorker<TestContext>(
                title = title,
                blockOn = { status == TestContext.CorStatuses.NONE },
                blockHandle = { history += "$title; " }
            )
        }
        val chain = CorChain<TestContext>(
            execs = listOf(createWorker("w1"), createWorker("w2")),
            title = "chain",
        )
        val ctx = TestContext()
        val visitor = TestVisitor<TestContext>()

        chain.visit(visitor, ctx)

        assertEquals("w1; w2; ", ctx.history)
    }
}
