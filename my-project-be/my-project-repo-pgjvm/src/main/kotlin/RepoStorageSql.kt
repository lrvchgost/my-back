package ru.otus.otuskotlin.lrvch.backend.repo.postgresql

import IRepoStorageInitializable
import com.benasher44.uuid.uuid4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.lrvch.common.helpers.asCatalogError
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageFilterRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdsRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.IDbStorageResponse
import ru.otus.otuskotlin.lrvch.common.repo.IDbStoragesResponse
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import ru.otus.otuskotlin.lrvch.common.repo.errorEmptyId
import ru.otus.otuskotlin.lrvch.common.repo.errorNotFound
import ru.otus.otuskotlin.lrvch.common.repo.errorNotFoundByIds
import ru.otus.otuskotlin.lrvch.common.repo.errorRepoConcurrency

class RepoStorageSql(
    properties: SqlProperties,
    private val randomUuid: () -> String = { uuid4().toString() }
) : IRepoStorage, IRepoStorageInitializable {
    private val storageTable = StorageTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    fun clear(): Unit = transaction(conn) {
        storageTable.deleteAll()
    }

    private fun saveObj(storage: Storage): Storage = transaction(conn) {
        val res = storageTable
            .insert {
                it.to(storage, randomUuid)
            }
            .resultedValues
            ?.map { storageTable.from(it) }
        println("INSERTED ROW ${res?.first()}")
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(
        crossinline block: () -> T,
        crossinline handle: (Exception) -> T
    ): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbStorageResponse): IDbStorageResponse =
        transactionWrapper(block) { DbStorageResponseErr(it.asCatalogError()) }

    override fun save(storages: Collection<Storage>): Collection<Storage> = storages.map { saveObj(it) }

    override suspend fun createStorage(rq: DbStorageRequest): IDbStorageResponse = transactionWrapper {
        DbStorageResponseOk(saveObj(rq.storage))
    }

    private fun read(id: StorageId): IDbStorageResponse {
        val res = storageTable.selectAll().where {
            storageTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbStorageResponseOk(storageTable.from(res))
    }

    override suspend fun readStorage(rq: DbStorageIdRequest): IDbStorageResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: StorageId,
        lock: StorageLock,
        block: (Storage) -> IDbStorageResponse
    ): IDbStorageResponse =
        transactionWrapper {
            if (id == StorageId.NONE) return@transactionWrapper errorEmptyId

            val current = storageTable.selectAll().where { storageTable.id eq id.asString() }
                .singleOrNull()
                ?.let { storageTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    override suspend fun updateStorage(rq: DbStorageRequest): IDbStorageResponse =
        update(rq.storage.id, rq.storage.lock) {
            storageTable.updateReturning(where = { storageTable.id eq rq.storage.id.asString() }) {
                it.to(rq.storage.copy(lock = StorageLock(randomUuid())), randomUuid)
            }.singleOrNull()
                ?.let { DbStorageResponseOk(storageTable.from(it)) }
                ?: errorNotFound(rq.storage.id)
        }

    override suspend fun deleteStorage(rq: DbStorageIdRequest): IDbStorageResponse = update(rq.id, rq.lock) {
        storageTable.deleteWhere { id eq rq.id.asString() }
        DbStorageResponseOk(it)
    }

    override suspend fun searchStorage(rq: DbStorageFilterRequest): IDbStoragesResponse =
        transactionWrapper({
            val res = storageTable.selectAll().where {
                buildList {
//                    add(Op.TRUE)
                    if (rq.searchString.isNotBlank()) {
                        add(
                            (storageTable.title like "%${rq.searchString}%")
                                    or (storageTable.description like "%${rq.searchString}%")
                        )
                    }
                    if (rq.availability.isNotBlank()) {
                        add(
                            (storageTable.availability like "%${rq.availability}%")
                        )
                    }
                    if (rq.capacity.isNotBlank()) {
                        add(
                            (storageTable.capacity like "%${rq.capacity}%")
                        )
                    }
                    if (rq.paymentType != CatalogPaymentType.NONE) {
                        add(storageTable.paymentType eq rq.paymentType)
                    }
                    if (rq.readSpeed != SpeedType.NONE) {
                        add(storageTable.readSpeed eq rq.readSpeed)
                    }

                    if (rq.writeSpeed != SpeedType.NONE) {
                        add(storageTable.writeSpeed eq rq.writeSpeed)
                    }
                }.reduce { a, b -> a and b }
            }

            DbStoragesResponseOk(data = res.map { storageTable.from(it) })
        }, {
            DbStoragesResponseErr(it.asCatalogError())
        })

    override suspend fun searchStoragesByIds(rq: DbStorageIdsRequest): IDbStoragesResponse =
        transactionWrapper({
            val res = storageTable.selectAll().where {
//                println("${storageTable.id inList rq.storages.map { it.id.asString() }}")
                storageTable.id inList rq.storages.map { it.id.asString() }
            }
            val data = res.map { storageTable.from(it) }

            if (data.isEmpty())  return@transactionWrapper errorNotFoundByIds(rq.storages.map { it.id})

            DbStoragesResponseOk(data = res.map { storageTable.from(it) })
        }, {
            DbStoragesResponseErr(it.asCatalogError())
        })
}
