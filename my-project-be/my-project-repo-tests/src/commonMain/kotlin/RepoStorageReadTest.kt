package ru.otus.otuskotlin.lrvch.backend.repo.tests

import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseErr
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoStorageReadTest {
    abstract val repo: IRepoStorage
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readStorage(DbStorageIdRequest(readSucc.id))

        assertIs<DbStorageResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.readStorage(DbStorageIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DbStorageResponseErr>(result)
        println("ERRORS: ${result.errors}")
        val error: CatalogError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitStorages("read") {
        override val initObjects: List<Storage> = listOf(
            createInitTestModel("read").apply {
                paymentType = CatalogPaymentType.FREE
            }
        )

        val notFoundId = StorageId("ad-repo-read-notFound")

    }
}
