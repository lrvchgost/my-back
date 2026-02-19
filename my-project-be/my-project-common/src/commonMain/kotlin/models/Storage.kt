package models

data class Storage(
    var id: CatalogRequestId = CatalogRequestId.NONE,
    var title: String = "",
    var description: String = "",
    var paymentType: PaymentType = PaymentType.NONE,
    var readSpeed: SpeedType = SpeedType.NONE,
    var writeSpeed: SpeedType = SpeedType.NONE,
    var optimizeEnabled: Boolean = false,
    var lock: StorageLock = StorageLock.NONE,
    var permissionsClient: MutableSet<StoragePermissionClient> = mutableSetOf()
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Storage()
    }

}