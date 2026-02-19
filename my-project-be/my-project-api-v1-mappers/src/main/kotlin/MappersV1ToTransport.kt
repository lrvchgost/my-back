package ru.otus.otuskotlin.lrvch.api.v1.mappers

import ru.otus.otuskotlin.lrvch.api.v1.models.Error
import ru.otus.otuskotlin.lrvch.api.v1.models.IResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.OptimizeStoragesResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.PaymentType
import ru.otus.otuskotlin.lrvch.api.v1.models.ResponseResult
import ru.otus.otuskotlin.lrvch.api.v1.models.SpeedType
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StoragePermissions
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageResponseObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateResponse
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.exceptions.UnknownCatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.SpeedType as CatalogSpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.common.models.StoragePermissionClient

fun CatalogContext.toTransportStorage(): IResponse = when (val cmd = command) {
    CatalogCommand.CREATE -> toTransportCreate()
    CatalogCommand.READ -> toTransportRead()
    CatalogCommand.UPDATE -> toTransportUpdate()
    CatalogCommand.DELETE -> toTransportDelete()
    CatalogCommand.SEARCH -> toTransportSearch()
    CatalogCommand.OPTIMIZE -> toTransportOptimize()
    CatalogCommand.NONE -> throw UnknownCatalogCommand(command)
}

fun CatalogContext.toTransportCreate() = StorageCreateResponse(
    result = state.toTransport(),
    errors = errors.toTransportError(),
    storage = storageResponse.toTransport()
)

fun CatalogContext.toTransportRead() = StorageReadResponse(
    result = state.toTransport(),
    errors = errors.toTransportError(),
    storage = storageResponse.toTransport()
)

fun CatalogContext.toTransportUpdate() = StorageUpdateResponse(
    result = state.toTransport(),
    errors = errors.toTransportError(),
    storage = storageResponse.toTransport()
)

fun CatalogContext.toTransportDelete() = StorageDeleteResponse(
    result = state.toTransport(),
    errors = errors.toTransportError(),
    storage = storageResponse.toTransport()
)

fun CatalogContext.toTransportSearch() = StorageSearchResponse(
    result = state.toTransport(),
    errors = errors.toTransportError(),
    storages = storagesResponse.toTransport()
)

fun CatalogContext.toTransportOptimize() = OptimizeStoragesResponse(
    result = state.toTransport(),
    errors = errors.toTransportError(),
    storage = storageResponse.toTransport()
)

private fun Storage.toTransport(): StorageResponseObject = StorageResponseObject(
    id = id.toTransport(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    capacity = capacity.takeIf { it.isNotBlank() },
    availability = availability.takeIf { it.isNotBlank() },
    paymentType = paymentType.toTransport(),
    readSpeed = readSpeed.toTransport(),
    writeSpeed = readSpeed.toTransport(),
    lock = lock.takeIf { it != StorageLock.NONE }?.asString(),
    permissions = permissionsClient.toTransport(),
)

private fun CatalogState.toTransport(): ResponseResult? = when (this) {
    CatalogState.RUNNING, CatalogState.FINISHED -> ResponseResult.SUCCESS
    CatalogState.FAILED -> ResponseResult.ERROR
    CatalogState.NONE -> null
}

private fun CatalogError.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

internal fun CatalogRequestId.toTransport() = takeIf { it != CatalogRequestId.NONE }?.asString()

internal fun CatalogPaymentType.toTransport(): PaymentType? = when (this) {
    CatalogPaymentType.LICENSE -> PaymentType.LICENSE
    CatalogPaymentType.PREPAID -> PaymentType.PREPAID
    CatalogPaymentType.FREE -> PaymentType.FREE
    CatalogPaymentType.NONE -> null
}

internal fun CatalogSpeedType.toTransport(): SpeedType? = when (this) {
    CatalogSpeedType._100 -> SpeedType._100
    CatalogSpeedType._150 -> SpeedType._150
    CatalogSpeedType._200 -> SpeedType._200
    CatalogSpeedType.NONE -> null
}

private fun StoragePermissionClient.toTransport(): StoragePermissions = when (this) {
    StoragePermissionClient.CREATE -> StoragePermissions.CREATE
    StoragePermissionClient.READ -> StoragePermissions.READ
    StoragePermissionClient.UPDATE -> StoragePermissions.UPDATE
    StoragePermissionClient.DELETE -> StoragePermissions.DELETE
}

private fun Set<StoragePermissionClient>.toTransport(): Set<StoragePermissions>? = this
    .map { it.toTransport() }
    .toSet()
    .takeIf { isNotEmpty() }

private fun List<Storage>.toTransport(): List<StorageResponseObject>? = this
    .map { it.toTransport() }
    .takeIf { it.isNotEmpty() }

private fun List<CatalogError>.toTransportError(): List<Error>? = this
    .map { it.toTransport() }
    .takeIf { it.isNotEmpty() }
