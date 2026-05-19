package ru.otus.otuskotlin.lrvch.common.models

enum class CatalogPaymentType {
    NONE,
    FREE,
    PREPAID,
    LICENSE
}

fun CatalogPaymentType.toNameOrNull(): String? = when(this) {
    CatalogPaymentType.NONE -> null
    CatalogPaymentType.FREE -> CatalogPaymentType.FREE.name
    CatalogPaymentType.LICENSE -> CatalogPaymentType.LICENSE.name
    CatalogPaymentType.PREPAID -> CatalogPaymentType.PREPAID.name
}