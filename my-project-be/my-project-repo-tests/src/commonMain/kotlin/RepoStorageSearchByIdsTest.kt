package ru.otus.otuskotlin.lrvch.backend.repo.tests

import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdsRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import ru.otus.otuskotlin.lrvch.common.repo.IdEntity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoStorageSearchByIdsTest {
    abstract val repo: IRepoStorage

    protected open val initializedObjects: List<Storage> = initObjects

    @Test
    fun searchByIds() = runRepoTest {
        val result = repo.searchStoragesByIds(
            DbStorageIdsRequest(
                storages = listOf(
                    IdEntity(
                        StorageId("storage-repo-searchByIds-1"), StorageLock("1")
                    ),
                    IdEntity(
                        StorageId("storage-repo-searchByIds-2"), StorageLock("2")
                    ),
                    IdEntity(
                        StorageId("storage-repo-searchByIds-3"), StorageLock("3")
                    ),
                )
            )
        )
        assertIs<DbStoragesResponseOk>(result)
        val expected = listOf(initializedObjects[0], initializedObjects[1], initializedObjects[2]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object : BaseInitStorages("searchByIds") {
        override val initObjects: List<Storage> = listOf(
            createInitTestModel(suf = "1"),
            createInitTestModel(suf = "2"),
            createInitTestModel("3"),
            createInitTestModel("4"),
            createInitTestModel("5"),
        )
    }
}
