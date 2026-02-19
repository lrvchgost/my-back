package ru.otus.otuskotlin.lrvch.common.models

data class StorageFilter(
    val searchString: String = "",
    val availability: String = "",
    val capacity: String = "",
    val paymentType: CatalogPaymentType = CatalogPaymentType.NONE,
    val readSpeed: SpeedType = SpeedType.NONE,
    val writeSpeed: SpeedType = SpeedType.NONE,
)