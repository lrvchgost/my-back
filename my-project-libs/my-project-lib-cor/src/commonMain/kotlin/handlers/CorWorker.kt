package ru.otus.otuskotlin.lrvch.libs.cor.handlers

import ru.otus.otuskotlin.lrvch.libs.cor.CorDslMarker
import ru.otus.otuskotlin.lrvch.libs.cor.ICorExec
import ru.otus.otuskotlin.lrvch.libs.cor.ICorWorkerDsl

/**
 * Реализация воркера (worker)
 */
class CorWorker<T>(
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    private val blockHandle: suspend T.() -> Unit = {},
    blockExcept: suspend T.(Throwable) -> Unit = {}
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {

    override suspend fun handle(context: T) {
        blockHandle(context)
    }

    override suspend fun visit(visitor: IVisitor<T>, context: T) {
        visitor.visitForWorker(this, context)
    }
}

@CorDslMarker
class CorWorkerDsl<T> : CorExecDsl<T>(), ICorWorkerDsl<T> {

    private var blockHandle: suspend T.() -> Unit = {}

    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): ICorExec<T> = CorWorker<T>(
        title = title,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockExcept = blockExcept,
    )
}
