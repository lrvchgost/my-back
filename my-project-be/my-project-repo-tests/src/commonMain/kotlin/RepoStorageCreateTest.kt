package ru.otus.otuskotlin.lrvch.backend.repo.tests

import IRepoStorageInitializable
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

abstract class RepoStorageCreateTest {
    abstract val repo: IRepoStorageInitializable
    protected open val uuidNew = StorageId("10000000-0000-0000-0000-000000000001")

    private val createObj = Storage(
        title = "create object",
        description = "create object description",
        paymentType = CatalogPaymentType.FREE,
        availability = "create availability"
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createStorage(DbStorageRequest(createObj))
        val expected = createObj
        assertIs<DbStorageResponseOk>(result)
        assertNotEquals(StorageId.NONE, result.data.id)
        assertEquals(uuidNew.asString(), result.data.lock.asString())
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.description, result.data.description)
        assertEquals(expected.paymentType, result.data.paymentType)
        assertEquals(expected.availability, result.data.availability)
    }

    companion object : BaseInitStorages("create") {
        override val initObjects: List<Storage> = emptyList()
    }
}
