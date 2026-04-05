package ru.otus.otuskotlin.lrvch.common.models

data class StorageFilter(
    var searchString: String = "",
    var availability: String = "",
    var capacity: String = "",
    var paymentType: CatalogPaymentType = CatalogPaymentType.NONE,
    var readSpeed: SpeedType = SpeedType.NONE,
    var writeSpeed: SpeedType = SpeedType.NONE
) {
    fun deepCopy(): StorageFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = StorageFilter()
    }
}