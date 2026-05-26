import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.common.models.toNameOrNull

data class StorageEntity(
    val id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var paymentType: String? = null,
    var readSpeed: String? = null,
    var writeSpeed: String? = null,
    var optimizeEnabled: Boolean? = null,
    var capacity: String? = null,
    var availability: String? = null,
    var lock: String? = null,
) {
    constructor(model: Storage): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        lock = model.lock.asString().takeIf { it.isNotBlank() },
//        paymentType = model.paymentType.toNameOrNull(),
        paymentType = model.paymentType.takeIf { it != CatalogPaymentType.NONE }?.name,
        readSpeed = model.readSpeed.takeIf { it != SpeedType.NONE }?.name,
        writeSpeed = model.writeSpeed.takeIf { it != SpeedType.NONE }?.name,
        optimizeEnabled = model.optimizeEnabled,
        capacity = model.capacity.takeIf { it.isNotBlank() },
        availability = model.availability.takeIf { it.isNotBlank() },
    )

    fun toInternal() = Storage(
        id = id?.let { StorageId(it) }?: StorageId.NONE,
        title = title?: "",
        description = description?: "",
        lock = lock?.let { StorageLock(it) } ?: StorageLock.NONE,
        paymentType = paymentType?.let { CatalogPaymentType.valueOf(it) }?: CatalogPaymentType.NONE,
        readSpeed = readSpeed?.let { SpeedType.valueOf(it) }?: SpeedType.NONE,
        writeSpeed = writeSpeed?.let { SpeedType.valueOf(it) }?: SpeedType.NONE,
        optimizeEnabled = optimizeEnabled?: false,
        capacity = capacity?: "",
        availability = availability?: "",
    )
}
