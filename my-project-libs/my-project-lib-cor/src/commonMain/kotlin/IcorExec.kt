package ru.otus.otuskotlin.lrvch.libs.cor

import ru.otus.otuskotlin.lrvch.libs.cor.handlers.IVisitor

/**
 * Блок кода, который обрабатывает контекст. Имеет имя и описание
 */
interface ICorExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
    suspend fun visit(visitor: IVisitor<T>, context: T)
}