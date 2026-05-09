import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageCreateTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageDeleteTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageReadTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageSearchTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageUpdateTest

class StorageRepoInMemoryCreateTest : RepoStorageCreateTest() {
    override val repo = RepoStorageInitialized(
        StorageRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryDeleteTest : RepoStorageDeleteTest() {
    override val repo = RepoStorageInitialized(
        StorageRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryReadTest : RepoStorageReadTest() {
    override val repo = RepoStorageInitialized(
        StorageRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemorySearchTest : RepoStorageSearchTest() {
    override val repo = RepoStorageInitialized(
        StorageRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryUpdateTest : RepoStorageUpdateTest() {
    override val repo = RepoStorageInitialized(
        StorageRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
