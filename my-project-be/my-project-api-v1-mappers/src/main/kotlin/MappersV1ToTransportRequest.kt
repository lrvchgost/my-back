package ru.otus.otuskotlin.lrvch.api.v1.mappers

import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageOptimizeObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadObject
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchFilter
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateObject
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.models.StorageLock

fun Storage.toTransportCreateStorage() = StorageCreateObject(
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    paymentType = paymentType.toTransport(),
    readSpeed = readSpeed.toTransport(),
    writeSpeed = writeSpeed.toTransport(),
    capacity = capacity.takeIf { it.isNotBlank() },
    availability = availability.takeIf { it.isNotBlank() },
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
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    paymentType = paymentType.toTransport(),
    readSpeed = readSpeed.toTransport(),
    writeSpeed = writeSpeed.toTransport(),
    capacity = capacity.takeIf { it.isNotBlank() },
    availability = availability.takeIf { it.isNotBlank() },
    lock = lock.toTransport()
)

fun Storage.toTransportOptimizeStorage() = StorageOptimizeObject(
    id = id.toTransport(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    paymentType = paymentType.toTransport(),
    readSpeed = readSpeed.toTransport(),
    writeSpeed = writeSpeed.toTransport(),
    capacity = capacity.takeIf { it.isNotBlank() },
    availability = availability.takeIf { it.isNotBlank() },
    lock = lock.toTransport()
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