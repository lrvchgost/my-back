package ru.otus.otuskotlin.lrvch.api.v1.mappers

import ru.otus.otuskotlin.lrvch.api.v1.models.IRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.OptimizeStoragesRequest
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs
import ru.otus.otuskotlin.lrvch.common.models.SpeedType as SpeedTypeModel
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.api.v1.models.PaymentType
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchFilter
import ru.otus.otuskotlin.lrvch.api.v1.models.SpeedType
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDebug
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageOptimizeObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageRequestDebugMode
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageRequestDebugStubs
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateRequest
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.mapper.v1.exceptions.UnknownRequestClass

fun CatalogContext.fromTransport(request: IRequest) = when (request) {
    is StorageCreateRequest -> fromTransport(request)
    is StorageReadRequest -> fromTransport(request)
    is StorageUpdateRequest -> fromTransport(request)
    is StorageDeleteRequest -> fromTransport(request)
    is StorageSearchRequest -> fromTransport(request)
    is OptimizeStoragesRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

fun CatalogContext.fromTransport(request: StorageCreateRequest) {
    command = CatalogCommand.CREATE
    storageRequest = request.storage?.fromTransport() ?: Storage()
    workMode = request.debug.fromTransportToWorkMode()
    stubCase = request.debug.fromTransportToStubCase()
}

fun CatalogContext.fromTransport(request: StorageReadRequest) {
    command = CatalogCommand.READ
    storageRequest = request.storage?.fromTransport() ?: Storage()
    workMode = request.debug.fromTransportToWorkMode()
    stubCase = request.debug.fromTransportToStubCase()
}

fun CatalogContext.fromTransport(request: StorageUpdateRequest) {
    command = CatalogCommand.UPDATE
    storageRequest = request.storage?.fromTransport() ?: Storage()
    workMode = request.debug.fromTransportToWorkMode()
    stubCase = request.debug.fromTransportToStubCase()
}

fun CatalogContext.fromTransport(request: StorageDeleteRequest) {
    command = CatalogCommand.UPDATE
    storageRequest = request.storage?.fromTransport() ?: Storage()
    workMode = request.debug.fromTransportToWorkMode()
    stubCase = request.debug.fromTransportToStubCase()
}

fun CatalogContext.fromTransport(request: StorageSearchRequest) {
    command = CatalogCommand.SEARCH
    storageFilterRequest = request.searchFilter.fromTransport()
    workMode = request.debug.fromTransportToWorkMode()
    stubCase = request.debug.fromTransportToStubCase()
}

fun CatalogContext.fromTransport(request: OptimizeStoragesRequest) {
    command = CatalogCommand.OPTIMIZE
    storagesRequest = request.storages?.fromTransport() ?: listOf()
    workMode = request.debug.fromTransportToWorkMode()
    stubCase = request.debug.fromTransportToStubCase()
}

private fun StorageCreateObject.fromTransport(): Storage = Storage(
    title = this.title ?: "",
    description = this.description ?: "",
    paymentType = this.paymentType.fromTransport(),
    capacity = this.capacity ?: "",
    availability = this.availability ?: "",
    readSpeed = this.readSpeed.fromTransport(),
    writeSpeed = this.readSpeed.fromTransport(),
    optimizeEnabled = false
)

private fun StorageReadObject?.fromTransport(): Storage = Storage(
    id = this?.id.toStorageId()
)

private fun StorageUpdateObject.fromTransport(): Storage = Storage(
    id = this.id.toStorageId(),
    title = this.title ?: "",
    description = this.description ?: "",
    paymentType = this.paymentType.fromTransport(),
    capacity = this.capacity ?: "",
    availability = this.availability ?: "",
    readSpeed = this.readSpeed.fromTransport(),
    writeSpeed = this.readSpeed.fromTransport(),
    optimizeEnabled = false,
    lock = lock.toStorageLock()
)

private fun StorageDeleteObject?.fromTransport(): Storage = Storage(
    id = this?.id.toStorageId(),
    lock = this?.lock.toStorageLock()
)

internal fun SpeedType?.fromTransport(): SpeedTypeModel = when (this) {
    SpeedType._100 -> SpeedTypeModel._100
    SpeedType._150 -> SpeedTypeModel._150
    SpeedType._200 -> SpeedTypeModel._200
    null -> SpeedTypeModel.NONE
}

internal fun PaymentType?.fromTransport(): CatalogPaymentType = when (this) {
    PaymentType.FREE -> CatalogPaymentType.FREE
    PaymentType.PREPAID -> CatalogPaymentType.PREPAID
    PaymentType.LICENSE -> CatalogPaymentType.LICENSE
    null -> CatalogPaymentType.NONE
}

private fun StorageDebug?.fromTransportToWorkMode(): CatalogWorkMode = when (this?.mode) {
    StorageRequestDebugMode.PROD -> CatalogWorkMode.PROD
    StorageRequestDebugMode.TEST -> CatalogWorkMode.TEST
    StorageRequestDebugMode.STUB -> CatalogWorkMode.STUB
    null -> CatalogWorkMode.PROD
}

private fun StorageDebug?.fromTransportToStubCase(): CatalogStubs = when (this?.stub) {
    StorageRequestDebugStubs.BAD_DESCRIPTION -> CatalogStubs.BAD_DESCRIPTION
    StorageRequestDebugStubs.BAD_ID -> CatalogStubs.BAD_ID
    StorageRequestDebugStubs.BAD_PAYMENT_METHOD -> CatalogStubs.BAD_PAYMENT_METHOD
    StorageRequestDebugStubs.BAD_SEARCH_STRING -> CatalogStubs.BAD_SEARCH
    StorageRequestDebugStubs.BAD_SPEED_TYPE -> CatalogStubs.BAD_SPEED_TYPE
    StorageRequestDebugStubs.BAD_TITLE -> CatalogStubs.BAD_TITLE
    StorageRequestDebugStubs.SUCCESS -> CatalogStubs.SUCCESS
    StorageRequestDebugStubs.NOT_FOUND -> CatalogStubs.NOT_FOUND
    StorageRequestDebugStubs.CANNOT_DELETE -> CatalogStubs.CANNOT_DELETE
    null -> CatalogStubs.BAD_ID
}

private fun StorageSearchFilter?.fromTransport(): StorageFilter = StorageFilter(
    searchString = this?.searchString ?: "",
    availability = this?.availability ?: "",
    capacity = this?.capacity ?: "",
    paymentType = this?.paymentType.fromTransport(),
    readSpeed = this?.readSpeed.fromTransport(),
    writeSpeed = this?.writeSpeed.fromTransport(),
)

private fun List<StorageOptimizeObject>.fromTransport(): List<Storage> =
    this.map { it.id.toStorageWithId() }

private fun String?.toStorageId() = this?.let { CatalogRequestId(it) } ?: CatalogRequestId.NONE
private fun String?.toStorageLock() = this?.let { StorageLock(it) } ?: StorageLock.NONE
private fun String?.toStorageWithId() = Storage(id = this.toStorageId())
