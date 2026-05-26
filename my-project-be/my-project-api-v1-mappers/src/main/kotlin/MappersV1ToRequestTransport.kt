//package ru.otus.otuskotlin.lrvch.api.v1.mappers
//
//import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateObject
//import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteObject
//import ru.otus.otuskotlin.lrvch.api.v1.models.StorageOptimizeObject
//import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadObject
//import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchFilter
//import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateObject
//import ru.otus.otuskotlin.lrvch.common.models.Storage
//import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
//import ru.otus.otuskotlin.lrvch.common.models.StorageLock
//
//fun Storage.toTransportCreate() = StorageCreateObject(
//    title = title.takeIf { it.isNotBlank() },
//    description = description.takeIf { it.isNotBlank() },
//    paymentType = paymentType.toTransport(),
//    readSpeed = readSpeed.toTransport(),
//    writeSpeed = writeSpeed.toTransport(),
//    capacity = capacity.takeIf { it.isNotBlank() },
//    availability = availability.takeIf { it.isNotBlank() },
//)
//
//fun Storage.toTransportRead() = StorageReadObject(
//    id = id.toTransport(),
//)
//
//fun Storage.toTransportDelete() = StorageDeleteObject(
//    id = id.toTransport(),
//    lock = lock.toTransport(),
//)
//
//fun Storage.toTransportUpdateStorage() = StorageUpdateObject(
//    id = id.toTransport(),
//    title = title,
//    description = description,
//    paymentType = paymentType.toTransport(),
//    readSpeed = readSpeed.toTransport(),
//    writeSpeed = writeSpeed.toTransport(),
//    capacity = capacity,
//    availability = availability,
//    lock = lock.toTransport()
//)
//
//fun Storage.toTransportOptimizeStorage() = StorageOptimizeObject(
//    id = id.toTransport()
//)
//
//fun List<Storage>.toTransportOptimizeStorage(): List<StorageOptimizeObject>? =
//    map { it.toTransportOptimizeStorage() }.takeIf { it.isNotEmpty() }
//
//fun StorageFilter.toTransportStorageFilter() = StorageSearchFilter(
//    searchString = searchString,
//    availability = availability,
//    capacity = capacity,
//    paymentType = paymentType.toTransport(),
//    readSpeed = readSpeed.toTransport(),
//    writeSpeed = writeSpeed.toTransport(),
//)
