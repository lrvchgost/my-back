package ru.otus.otuskotlin.lrvch.libs.cor.handlers

import ru.otus.otuskotlin.lrvch.libs.cor.CorDslMarker
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.ICorExec
import ru.otus.otuskotlin.lrvch.libs.cor.ICorExecDsl

/**
 * Реализация цепочки (chain), которая исполняет свои вложенные цепочки и рабочие
 */
class CorChain<T>(
    val execs: List<ICorExec<T>>,
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(Throwable) -> Unit = {}
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {

    override suspend fun handle(context: T) {
        execs.forEach {
            it.exec(context)
        }
    }

    override suspend fun visit(visitor: IVisitor<T>, context: T) {
        visitor.visitForChain(this, context)
    }
}

@CorDslMarker
class CorChainDsl<T>() : CorExecDsl<T>(), ICorChainDsl<T> {

    private val workers = mutableListOf<ICorExecDsl<T>>()

    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }

    override fun build(): ICorExec<T> = CorChain(
        title = title,
        description = description,
        execs = workers.map { it.build() },
        blockOn = blockOn,
        blockExcept = blockExcept,
    )
}
