package ru.otus.otuskotlin.lrvch.backend.repo.tests

import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageFilterRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoStorageSearchTest {
    abstract val repo: IRepoStorage

    protected open val initializedObjects: List<Storage> = initObjects

    @Test
    fun searchTitle() = runRepoTest {
        val result = repo.searchStorage(DbStorageFilterRequest(searchString = searchString))
        assertIs<DbStoragesResponseOk>(result)
        val expected = listOf(initializedObjects[0], initializedObjects[1]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchPaymentType() = runRepoTest {
        val result = repo.searchStorage(DbStorageFilterRequest(paymentType = CatalogPaymentType.FREE))
        assertIs<DbStoragesResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[2]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitStorages("search") {

        const val searchString = "search it"
        override val initObjects: List<Storage> = listOf(
            createInitTestModel(suf=searchString),
            createInitTestModel(suf="$searchString second", paymentType = CatalogPaymentType.FREE),
            createInitTestModel("ad3", paymentType = CatalogPaymentType.FREE),
            createInitTestModel("ad4"),
            createInitTestModel("ad5"),
        )
    }
}
