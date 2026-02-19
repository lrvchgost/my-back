package models

data class StorageFilter(
    val searchString: String = "",
    val availability: String = "",
    val capacity: String = "",
    val paymentType: PaymentType = PaymentType.NONE,
    val readSpeed: SpeedType = SpeedType.NONE,
    val writeSpeed: SpeedType = SpeedType.NONE,
)