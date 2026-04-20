package ru.otus.otuskotlin.lrvch.libs.cor.handlers

interface IVisitor<T> {
    suspend fun visitForWorker(node: CorWorker<T>, context: T)
    suspend fun visitForChain(node: CorChain<T>, context: T)
}