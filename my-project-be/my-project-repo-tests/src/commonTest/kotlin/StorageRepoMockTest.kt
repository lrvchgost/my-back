import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.StorageRepositoryMock
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageFilterRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdsRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageResponseOk
import ru.otus.otuskotlin.lrvch.common.repo.DbStoragesResponseOk
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class StorageRepoMockTest {
    private val repo = StorageRepositoryMock(
        invokeCreateStorage = { DbStorageResponseOk(CatalogStorageStub.prepareResult { title = "create" }) },
        invokeReadStorage = { DbStorageResponseOk(CatalogStorageStub.prepareResult { title = "read" }) },
        invokeUpdateStorage = { DbStorageResponseOk(CatalogStorageStub.prepareResult { title = "update" }) },
        invokeDeleteStorage = { DbStorageResponseOk(CatalogStorageStub.prepareResult { title = "delete" }) },
        invokeSearchStorage = { DbStoragesResponseOk(listOf(CatalogStorageStub.prepareResult { title = "search" })) },
        invokeSearchStoragesByIds = { DbStoragesResponseOk(listOf(CatalogStorageStub.prepareResult { title = "searchByIds" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createStorage(DbStorageRequest(Storage()))
        assertIs<DbStorageResponseOk>(result)
        assertEquals("create", result.data.title)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readStorage(DbStorageIdRequest(Storage()))
        assertIs<DbStorageResponseOk>(result)
        assertEquals("read", result.data.title)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateStorage(DbStorageRequest(Storage()))
        assertIs<DbStorageResponseOk>(result)
        assertEquals("update", result.data.title)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteStorage(DbStorageIdRequest(Storage()))
        assertIs<DbStorageResponseOk>(result)
        assertEquals("delete", result.data.title)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchStorage(DbStorageFilterRequest())
        assertIs<DbStoragesResponseOk>(result)
        assertEquals("search", result.data.first().title)
    }

    @Test
    fun searchStoragesByIds() = runTest {
        val result = repo.searchStoragesByIds(DbStorageIdsRequest( storages = listOf()))
        assertIs<DbStoragesResponseOk>(result)
        assertEquals("searchByIds", result.data.first().title)
    }
}
