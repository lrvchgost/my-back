package ru.otus.otuskotlin.lrvch.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class CatalogRequestId(private val id: String) {
    fun asString() = id

    fun asEncoded() = asString()
        .replace("<", "&lt;")
        .replace(">", "&gt;")

    companion object {
        val NONE = CatalogRequestId("")
    }
}