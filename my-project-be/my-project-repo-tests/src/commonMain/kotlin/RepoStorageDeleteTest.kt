package ru.otus.otuskotlin.lrvch.backend.repo.tests

import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErrWithData
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoStorageDeleteTest {
    abstract val repo: IRepoStorage
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = StorageId("ad-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteStorage(DbStorageIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbStorageResponseOk>(result)
        assertEquals(deleteSucc.title, result.data.title)
        assertEquals(deleteSucc.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readStorage(DbStorageIdRequest(notFoundId, lock = lockOld))

        assertIs<DbStorageResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteStorage(DbStorageIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbStorageResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitStorages("delete") {
        override val initObjects: List<Storage> = listOf(
            createInitTestModel("delete").apply { paymentType = CatalogPaymentType.FREE },
            createInitTestModel("deleteLock").apply { paymentType = CatalogPaymentType.FREE },
        )
    }
}
