package ru.otus.otuskotlin.lrvch.biz.validation.helpers

import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock

internal fun CatalogContext.hasNoContent(): Boolean {
    val regExp = Regex("\\p{L}")
    return storageValidating.description.isNotEmpty() && !storageValidating.description.contains(regExp)
}

internal fun CatalogContext.descriptionIsEmpty(): Boolean {
    return storageValidating.description.isEmpty()
}

internal fun CatalogContext.idIsEmpty(): Boolean {
    return storageValidating.id.asString().isEmpty()
}

internal fun CatalogContext.idHasNotProperFormat(): Boolean {
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    return storageValidating.id != StorageId.NONE && !storageValidating.id.asString().matches(regExp)
}

internal fun CatalogContext.lockIsEmpty(): Boolean {
    return storageValidating.lock.asString().isEmpty()
}

internal fun CatalogContext.lockHasNotProperFormat(): Boolean {
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    return storageValidating.lock != StorageLock.NONE && !storageValidating.lock.asString().matches(regExp)
}

internal fun StorageFilter.toShort(upperEdge: Int): Boolean {
    return searchString.length in (1..upperEdge)
}

internal fun StorageFilter.trimEmptyChars(): Unit {
    searchString.trim()
}

internal fun StorageFilter.toLong(upperEdge: Int): Boolean {
    return searchString.length > upperEdge
}

internal fun CatalogContext.titleHasNoContent(): Boolean {
    val regExp = Regex("\\p{L}")
    return storageValidating.title.isNotEmpty() && !storageValidating.title.contains(regExp)
}

internal fun CatalogContext.titleIsEmpty(): Boolean {
    return storageValidating.title.isEmpty()
}

internal fun CatalogContext.notAllStoragesHaveTheSamePaymentMethod(): Boolean {
    return storagesValidating.map { it.paymentType }.distinct().size > 1
}

internal fun CatalogContext.notAllStoragesHaveTheSameReadSpeed(): Boolean {
    return storagesValidating.map { it.readSpeed }.distinct().size > 1
}

internal fun CatalogContext.notAllStoragesHaveTheSameWriteSpeed(): Boolean {
    return storagesValidating.map { it.writeSpeed }.distinct().size > 1
}
