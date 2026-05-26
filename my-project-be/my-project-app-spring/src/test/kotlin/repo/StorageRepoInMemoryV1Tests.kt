package ru.otus.otuskotlin.lrvch.app.spring.repo

import RepoStorageInitialized
import StorageRepoInMemory
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import ru.otus.otuskotlin.lrvch.app.spring.config.CatalogConfig
import ru.otus.otuskotlin.lrvch.app.spring.controllers.CatalogControllerV1
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageFilterRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageIdsRequest
import ru.otus.otuskotlin.lrvch.common.repo.DbStorageRequest
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(CatalogControllerV1::class, CatalogConfig::class)
internal class StorageRepoInMemoryV1Tests : StorageRepoBaseV1Test() {
    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoStorage

    @BeforeEach
    fun tearUp() {
        val slotStorage = slot<DbStorageRequest>()
        val slotId = slot<DbStorageIdRequest>()
        val slotIds = slot<DbStorageIdsRequest>()
        val slotFl = slot<DbStorageFilterRequest>()
        val repo = RepoStorageInitialized(
            repo = StorageRepoInMemory(randomUuid = { uuidNew }),
            initObjects =
                CatalogStorageStub.prepareSearchList(StorageFilter(paymentType = CatalogPaymentType.LICENSE))
                        + CatalogStorageStub.get()
                        + CatalogStorageStub.prepareOptimizeListNotEmpty()

        )
        coEvery { testTestRepo.createStorage(capture(slotStorage)) } coAnswers { repo.createStorage(slotStorage.captured) }
        coEvery { testTestRepo.readStorage(capture(slotId)) } coAnswers { repo.readStorage(slotId.captured) }
        coEvery { testTestRepo.updateStorage(capture(slotStorage)) } coAnswers { repo.updateStorage(slotStorage.captured) }
        coEvery { testTestRepo.deleteStorage(capture(slotId)) } coAnswers { repo.deleteStorage(slotId.captured) }
        coEvery { testTestRepo.searchStorage(capture(slotFl)) } coAnswers { repo.searchStorage(slotFl.captured) }
        coEvery { testTestRepo.searchStoragesByIds(capture(slotIds)) } coAnswers { repo.searchStoragesByIds(slotIds.captured) }
    }

    @Test
    override fun createStorage() = super.createStorage()

    @Test
    override fun readStorage() = super.readStorage()

    @Test
    override fun updateStorage() = super.updateStorage()

    @Test
    override fun deleteStorage() = super.deleteStorage()

    @Test
    override fun searchStorage() = super.searchStorage()

    @Test
    override fun optimizeStorages() = super.optimizeStorages()
}
