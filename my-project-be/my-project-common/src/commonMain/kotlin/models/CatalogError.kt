package ru.otus.otuskotlin.lrvch.common.models

data class CatalogError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)