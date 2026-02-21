package ru.otus.otuskotlin.lrvch.api.v1.mappers

import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageOptimizeObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchFilter
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateObject
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.common.models.StoragePermissionClient

fun Storage.toTransportCreateStorage() = StorageCreateObject(
    title = title,
    description = description,
    paymentType = paymentType.toTransport(),
    readSpeed = readSpeed.toTransport(),
    writeSpeed = writeSpeed.toTransport(),
    capacity = capacity,
    availability = availability,
    //optimizeEnabled = optimizeEnabled,
)

fun Storage.toTransportReadStorage() = StorageReadObject(
    id = id.toTransport(),
)

internal fun StorageLock.toTransport() = takeIf { it != StorageLock.NONE }?.asString()

fun Storage.toTransportDeleteStorage() = StorageDeleteObject(
    id = id.toTransport(),
    lock = lock.toTransport(),
)

fun Storage.toTransportUpdateStorage() = StorageUpdateObject(
    id = id.toTransport(),
    title = title,
    description = description,
    paymentType = paymentType.toTransport(),
    readSpeed = readSpeed.toTransport(),
    writeSpeed = writeSpeed.toTransport(),
    capacity = capacity,
    availability = availability,
    lock = lock.toTransport()
    // optimizeEnabled = false,
)

fun Storage.toTransportOptimizeStorage() = StorageOptimizeObject(
    id = id.toTransport()
)

fun List<Storage>.toTransportOptimizeStorage(): List<StorageOptimizeObject>? =
    map { it.toTransportOptimizeStorage() }.takeIf { it.isNotEmpty() }

fun StorageFilter.toTransportStorageFilter() = StorageSearchFilter(
    searchString = searchString,
    availability = availability,
    capacity = capacity,
    paymentType = paymentType.toTransport(),
    readSpeed = readSpeed.toTransport(),
    writeSpeed = writeSpeed.toTransport(),
)