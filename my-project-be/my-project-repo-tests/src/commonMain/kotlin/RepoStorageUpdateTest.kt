package ru.otus.otuskotlin.lrvch.backend.repo.tests

import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.SpeedType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErrWithData
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoStorageUpdateTest {
    abstract val repo: IRepoStorage
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = StorageId("ad-repo-update-not-found")
    protected val lockBad = StorageLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = StorageLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        Storage(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            paymentType = CatalogPaymentType.FREE,
            lock = initObjects.first().lock,
            readSpeed = SpeedType._100,
            writeSpeed = SpeedType._100,
        )
    }
    private val reqUpdateNotFound = Storage(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        paymentType = CatalogPaymentType.FREE,
        lock = initObjects.first().lock,
        readSpeed = SpeedType._100,
        writeSpeed = SpeedType._100,
    )
    private val reqUpdateConc by lazy {
        Storage(
            id = updateConc.id,
            title = "update object not found",
            description = "update object not found description",
            paymentType = CatalogPaymentType.FREE,
            lock = lockBad,
            readSpeed = SpeedType._100,
            writeSpeed = SpeedType._100,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateStorage(DbStorageRequest(reqUpdateSucc))
        println("ERRORS: ${(result as? DbStorageResponseErr)?.errors}")
        println("ERRORSWD: ${(result as? DbStorageResponseErrWithData)?.errors}")
        assertIs<DbStorageResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.title, result.data.title)
        assertEquals(reqUpdateSucc.description, result.data.description)
        assertEquals(reqUpdateSucc.paymentType, result.data.paymentType)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateStorage(DbStorageRequest(reqUpdateNotFound))
        assertIs<DbStorageResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateStorage(DbStorageRequest(reqUpdateConc))
        assertIs<DbStorageResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitStorages("update") {
        override val initObjects: List<Storage> = listOf(
            createInitTestModel("update").apply { paymentType = CatalogPaymentType.FREE },
            createInitTestModel("updateConc").apply { paymentType = CatalogPaymentType.FREE },
        )
    }
}
