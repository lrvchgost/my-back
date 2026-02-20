package ru.otus.otuskotlin.lrvch.common.models

data class Storage(
    var id: CatalogRequestId = CatalogRequestId.NONE,
    var title: String = "",
    var description: String = "",
    var paymentType: CatalogPaymentType = CatalogPaymentType.NONE,
    var readSpeed: SpeedType = SpeedType.NONE,
    var writeSpeed: SpeedType = SpeedType.NONE,
    var optimizeEnabled: Boolean = false,
    var capacity: String = "",
    var availability: String = "",
    var lock: StorageLock = StorageLock.NONE,
    var permissionsClient: MutableSet<StoragePermissionClient> = mutableSetOf()
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Storage()
    }

}