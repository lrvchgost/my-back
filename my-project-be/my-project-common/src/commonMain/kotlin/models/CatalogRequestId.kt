package ru.otus.otuskotlin.lrvch.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class CatalogRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CatalogRequestId("")
    }
}