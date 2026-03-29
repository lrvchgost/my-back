package ru.otus.otuskotlin.lrvch.libs.cor.handlers

import kotlin.time.measureTime

class ExecVisitor<T> : IVisitor<T> {
    override suspend fun visitForWorker(node: CorWorker<T>, context: T) {
        node.exec(context)
    }

    override suspend fun visitForChain(node: CorChain<T>, context: T) {
        node.execs.forEach { it.visit(this, context) }
    }
}

class ExecVisitorLogNodeExecDuration<T> : IVisitor<T> {
    override suspend fun visitForWorker(node: CorWorker<T>, context: T) {
        val duration = measureTime {
            node.exec(context)
        }

        println("Executed node ${node.title} $duration")
    }

    override suspend fun visitForChain(node: CorChain<T>, context: T) {
        val duration = measureTime {
            node.execs.forEach { it.visit(this, context) }
        }

        println("Executed node ${node.title} $duration")
    }
}

class PrintGraphVisitor<T> : IVisitor<T> {
    override suspend fun visitForWorker(node: CorWorker<T>, context: T) {
        node.exec(context)
    }

    override suspend fun visitForChain(node: CorChain<T>, context: T) {
        node.execs.forEach { it.exec(context) }
    }
}
