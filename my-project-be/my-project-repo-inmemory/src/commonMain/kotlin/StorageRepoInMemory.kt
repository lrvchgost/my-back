import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageFilterRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.IDbStorageResponse
import ru.otus.otuskotlin.lrvch.common.repo.IDbStoragesResponse
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import ru.otus.otuskotlin.lrvch.common.repo.StorageRepoBase
import ru.otus.otuskotlin.lrvch.common.repo.errorDb
import ru.otus.otuskotlin.lrvch.common.repo.errorEmptyId
import ru.otus.otuskotlin.lrvch.common.repo.errorEmptyLock
import ru.otus.otuskotlin.lrvch.common.repo.errorNotFound
import ru.otus.otuskotlin.lrvch.common.repo.errorRepoConcurrency
import ru.otus.otuskotlin.lrvch.common.repo.exeptions.RepoEmptyLockException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class StorageRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : StorageRepoBase(), IRepoStorage, IRepoStorageInitializable {
    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, StorageEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(storages: Collection<Storage>) = storages.map { storage ->
        val entity = StorageEntity(storage)
        require(entity.id != null)
        cache.put(entity.id, entity)
        storage
    }

    override suspend fun createStorage(rq: DbStorageRequest): IDbStorageResponse = tryStorageMethod {
        val key = randomUuid()
        val ad = rq.storage.copy(id = StorageId(key), lock = StorageLock(randomUuid()))
        val entity = StorageEntity(ad)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbStorageResponseOk(ad)
    }

    override suspend fun readStorage(rq: DbStorageIdRequest): IDbStorageResponse = tryStorageMethod {
        val key = rq.id.takeIf { it != StorageId.NONE }?.asString() ?: return@tryStorageMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbStorageResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateStorage(rq: DbStorageRequest): IDbStorageResponse = tryStorageMethod {
        val rqStorage = rq.storage
        val id = rqStorage.id.takeIf { it != StorageId.NONE } ?: return@tryStorageMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqStorage.lock.takeIf { it != StorageLock.NONE } ?: return@tryStorageMethod errorEmptyLock(id)

        mutex.withLock {
            val oldStorage = cache.get(key)?.toInternal()
            when {
                oldStorage == null -> errorNotFound(id)
                oldStorage.lock == StorageLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldStorage.lock != oldLock -> errorRepoConcurrency(oldStorage, oldLock)
                else -> {
                    val newStorage = rqStorage.copy(lock = StorageLock(randomUuid()))
                    val entity = StorageEntity(newStorage)
                    cache.put(key, entity)
                    DbStorageResponseOk(newStorage)
                }
            }
        }
    }

    override suspend fun deleteStorage(rq: DbStorageIdRequest): IDbStorageResponse = tryStorageMethod {
        val id = rq.id.takeIf { it != StorageId.NONE } ?: return@tryStorageMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != StorageLock.NONE } ?: return@tryStorageMethod errorEmptyLock(id)

        mutex.withLock {
            val oldStorage = cache.get(key)?.toInternal()
            when {
                oldStorage == null -> errorNotFound(id)
                oldStorage.lock == StorageLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldStorage.lock != oldLock -> errorRepoConcurrency(oldStorage, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbStorageResponseOk(oldStorage)
                }
            }
        }
    }

    /**
     * Поиск стораджа c  по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchStorage(rq: DbStorageFilterRequest): IDbStoragesResponse = tryStoragesMethod {
        val r = cache.asMap().values;
        val result: List<Storage> = cache.asMap().asSequence()
            .filter { entry ->
                rq.searchString.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it, ignoreCase = true) ?: false
                } ?: true
            }
            .filter { entry ->
                rq.availability.takeIf { it.isNotBlank() }?.let {
                    entry.value.availability?.contains(it, ignoreCase = true) ?: false
                } ?: true
            }
            .filter { entry ->
                rq.capacity.takeIf { it.isNotBlank() }?.let {
                    entry.value.capacity?.contains(it, ignoreCase = true) ?: false
                } ?: true
            }
            .filter { entry ->
                rq.paymentType.takeIf { it != CatalogPaymentType.NONE }?.let {
                    it.name == entry.value.paymentType
                } ?: true
            }
            .filter { entry ->
                rq.readSpeed.takeIf { it != SpeedType.NONE }?.let {
                    it.name == entry.value.readSpeed
                } ?: true
            }
            .filter { entry ->
                rq.writeSpeed.takeIf { it != SpeedType.NONE }?.let {
                    it.name == entry.value.writeSpeed
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbStoragesResponseOk(result)
    }
}
