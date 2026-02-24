package ru.otus.otuskotlin.lrvch.api.v2.mappers

import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDeleteObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageOptimizeObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageReadObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageSearchFilter
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageUpdateObject
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.models.StorageLock

fun Storage.toTransportCreateStorage() = StorageCreateObject(
    title = title,
    description = description,
    paymentType = paymentType.toTransport(),
    readSpeed = readSpeed.toTransport(),
    writeSpeed = writeSpeed.toTransport(),
    capacity = capacity,
    availability = availability,
    enableOptimize = optimizeEnabled.toTransportOptimizeEnabled(),
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
    lock = lock.toTransport(),
    enableOptimize = optimizeEnabled.toTransportOptimizeEnabled(),
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

private fun Boolean?.toTransportOptimizeEnabled()  = when (this) {
    true -> "1"
    false -> "0"
    null -> "0"
}