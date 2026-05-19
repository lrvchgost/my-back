package ru.otus.otuskotlin.lrvch.backend.repo.postgresql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock

class StorageTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val title = text(SqlFields.TITLE).nullable()
    val description = text(SqlFields.DESCRIPTION).nullable()
    val paymentType = paymentTypeEnumeration(SqlFields.PAYMENT_TYPE)
    val readSpeed = speedTypeEnumeration(SqlFields.READ_SPEED)
    val writeSpeed = speedTypeEnumeration(SqlFields.WRITE_SPEED)
    val optimizeEnabled = bool(SqlFields.OPTIMIZE_ENABLED)
    val capacity = text(SqlFields.CAPACITY).nullable()
    val availability = text(SqlFields.AVAILABILITY).nullable()
    val lock = text(SqlFields.LOCK)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = Storage(
        id = StorageId(res[id]),
        title = res[title] ?: "",
        description = res[description] ?: "",
        capacity = res[capacity] ?: "",
        availability = res[availability] ?: "",
        optimizeEnabled = res[optimizeEnabled],
        paymentType = res[paymentType],
        readSpeed = res[readSpeed],
        writeSpeed = res[writeSpeed],
        lock = StorageLock(res[lock]),
    )

    fun UpdateBuilder<*>.to(storage: Storage, randomUuid: () -> String) {
        this[id] = storage.id.takeIf { it != StorageId.NONE }?.asString() ?: randomUuid()
        this[title] = storage.title
        this[description] = storage.description
        this[capacity] = storage.capacity
        this[availability] = storage.availability
        this[optimizeEnabled] = storage.optimizeEnabled
        this[paymentType] = storage.paymentType
        this[readSpeed] = storage.readSpeed
        this[writeSpeed] = storage.writeSpeed
        this[lock] = storage.lock.takeIf { it != StorageLock.NONE }?.asString() ?: randomUuid()
    }
}

