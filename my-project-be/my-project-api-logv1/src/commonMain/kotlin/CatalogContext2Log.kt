//package ru.otus.otuskotlin.lrvch.api.logv1.mappers
//
//import ru.otus.otuskotlin.lrvch.common.*
//import ru.otus.otuskotlin.lrvch.api.logv1.models.*
//import kotlinx.datetime.Clock
//import ru.otus.otuskotlin.lrvch.common.models.*
//import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
//import kotlin.String
//
//fun CatalogContext.toLog(logId: String) = CommonLogModel(
//    messageTime = Clock.System.now().toString(),
//    logId = logId,
//    source = "my-project",
//    catalogStorage = toCatalogLog(),
//    errors = errors.map { it.toLog() },
//)
//
//private fun CatalogContext.toCatalogLog(): CatalogStorageLogModel? {
//    val storage = Storage()
//    return CatalogStorageLogModel(
//        requestId = requestId.takeIf { it != CatalogRequestId.NONE }?.asString(),
//        requestStorage = storageRequest.takeIf { it != storage }?.toLog(),
//        requestStorages = storagesRequest.takeIf { it.isNotEmpty() }?.filter { it != storage }?.map { it.toLog() },
//        responseStorage = storageResponse.takeIf { it != storage }?.toLog(),
//        responseStorages = storagesResponse.takeIf { it.isNotEmpty() }?.filter { it != storage }?.map { it.toLog() },
//        requestFilter = storageFilterRequest.takeIf { it != StorageFilter() }?.toLog(),
//    ).takeIf { it != Storage() }
//}
//
//private fun StorageFilter.toLog() = StorageFilterLog(
//    searchString = searchString.takeIf { it.isNotBlank() },
//    capacity = capacity.takeIf { it.isNotBlank() },
//    availability = availability.takeIf { it.isNotBlank() },
//    paymentType = paymentType.takeIf { it != CatalogPaymentType.NONE }?.name,
//    readSpeed = readSpeed.takeIf { it != SpeedType.NONE }?.name,
//    writeSpeed = writeSpeed.takeIf { it != SpeedType.NONE }?.name
//)
//
//private fun CatalogError.toLog() = ErrorLogModel(
//    message = message.takeIf { it.isNotBlank() },
//    field = field.takeIf { it.isNotBlank() },
//    code = code.takeIf { it.isNotBlank() },
//    level = level.name,
//)
//
//private fun Storage.toLog() = StorageLog(
//    id = id.takeIf { it != CatalogRequestId.NONE }?.asString(),
//    title = title.takeIf { it.isNotBlank() },
//    description = description.takeIf { it.isNotBlank() },
//    capacity = capacity.takeIf { it.isNotBlank() },
//    availability = availability.takeIf { it.isNotBlank() },
//    paymentType = paymentType.takeIf { it != CatalogPaymentType.NONE }?.name,
//    readSpeed = readSpeed.takeIf { it != SpeedType.NONE }?.name,
//    writeSpeed = writeSpeed.takeIf { it != SpeedType.NONE }?.name,
//    enableOptimize = optimizeEnabled.toString(),
//    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
//)
