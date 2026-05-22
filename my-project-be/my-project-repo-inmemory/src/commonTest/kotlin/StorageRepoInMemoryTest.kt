import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageCreateTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageDeleteTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageReadTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageSearchByIdsTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageSearchTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageUpdateTest

class StorageRepoInMemoryCreateTest : RepoStorageCreateTest() {
    override val repo = RepoStorageInitialized(
        StorageRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class StorageRepoInMemoryDeleteTest : RepoStorageDeleteTest() {
    override val repo = RepoStorageInitialized(
        StorageRepoInMemory(),
        initObjects = initObjects,
    )
}

class StorageRepoInMemoryReadTest : RepoStorageReadTest() {
    override val repo = RepoStorageInitialized(
        StorageRepoInMemory(),
        initObjects = initObjects,
    )
}

class StorageRepoInMemorySearchTest : RepoStorageSearchTest() {
    override val repo = RepoStorageInitialized(
        StorageRepoInMemory(),
        initObjects = initObjects,
    )
}

class StorageRepoInMemoryUpdateTest : RepoStorageUpdateTest() {
    override val repo = RepoStorageInitialized(
        StorageRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}

class StorageRepoInMemorySearchByIdsTest : RepoStorageSearchByIdsTest() {
    override val repo = RepoStorageInitialized(
        StorageRepoInMemory(),
        initObjects = initObjects,
    )
}
