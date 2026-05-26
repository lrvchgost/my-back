package ru.otus.otuskotlin.lrvch.app.spring.repo

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportCreate
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportCreateStorage
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportDelete
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportDeleteStorage
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportOptimize
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportOptimizeStorage
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportRead
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportReadStorage
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportSearch
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportUpdate
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportUpdateStorage
import ru.otus.otuskotlin.lrvch.api.v1.models.IResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.OptimizeStoragesRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.PaymentType
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDebug
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageRequestDebugMode
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchFilter
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateRequest
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub
import kotlin.test.Test
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType

internal abstract class StorageRepoBaseV1Test {
    protected abstract var webClient: WebTestClient
    private val debug = StorageDebug(mode = StorageRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createStorage() = testRepoStorage(
        "create",
        StorageCreateRequest(
            storage = CatalogStorageStub.get().toTransportCreateStorage(),
            debug = debug,
        ),
        prepareCtx(CatalogStorageStub.prepareResult {
            id = StorageId(uuidNew)
            lock = StorageLock(uuidNew)
        })
            .toTransportCreate()
            .copy(responseType = "create")
    )

    @Test
    open fun readStorage() = testRepoStorage(
        "read",
        StorageReadRequest(
            storage = CatalogStorageStub.get().toTransportReadStorage(),
            debug = debug,
        ),
        prepareCtx(CatalogStorageStub.get())
            .toTransportRead()
            .copy(responseType = "read")
    )

    @Test
    open fun updateStorage() = testRepoStorage(
        "update",
        StorageUpdateRequest(
            storage = CatalogStorageStub.prepareResult { title = "add" }.toTransportUpdateStorage(),
            debug = debug,
        ),
        prepareCtx(CatalogStorageStub.prepareResult { title = "add"; lock = StorageLock(uuidNew) })
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    open fun deleteStorage() = testRepoStorage(
        "delete",
        StorageDeleteRequest(
            storage = CatalogStorageStub.get().toTransportDeleteStorage(),
            debug = debug,
        ),
        prepareCtx(CatalogStorageStub.get())
            .toTransportDelete()
            .copy(responseType = "delete")
    )

    @Test
    open fun searchStorage() = testRepoStorage(
        "search",
        StorageSearchRequest(
            searchFilter = StorageSearchFilter(paymentType = PaymentType.LICENSE),
            debug = debug,
        ),
        CatalogContext(
            state = CatalogState.RUNNING,
            storagesResponse = CatalogStorageStub.prepareSearchList(StorageFilter(paymentType = CatalogPaymentType.LICENSE))
                .onEach { it.permissionsClient.clear() }
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch().copy(responseType = "search")
    )

    @Test
    open fun optimizeStorages() = testRepoStorage(
        "optimize",
        OptimizeStoragesRequest(
            storages = CatalogStorageStub.prepareOptimizeListNotEmpty().toTransportOptimizeStorage(),
            debug = debug,
        ),
        prepareCtx(CatalogStorageStub.optimizedStorage().apply {
            id = StorageId(uuidNew)
            lock = StorageLock(uuidNew)
        })
            .toTransportOptimize()
            .copy(responseType = "optimize")
    )

    private fun prepareCtx(storage: Storage) = CatalogContext(
        state = CatalogState.RUNNING,
        storageResponse = storage.apply {
            // Пока не реализована эта функциональность
            permissionsClient.clear()
        },
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoStorage(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v1/storage/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is StorageSearchResponse -> it.copy(storages = it.storages?.sortedBy { it.id })
                    null -> throw RuntimeException("Null response")
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
