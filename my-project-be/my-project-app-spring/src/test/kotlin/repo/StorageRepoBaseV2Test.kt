package ru.otus.otuskotlin.lrvch.app.spring.repo

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportOptimize
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportOptimizeStorage
import ru.otus.otuskotlin.lrvch.api.v2.apiV2RequestSerialize
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportCreate
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportCreateStorage
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportDelete
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportDeleteStorage
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportRead
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportReadStorage
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportSearch
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportUpdate
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportUpdateStorage
import ru.otus.otuskotlin.lrvch.api.v2.models.IRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.IResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.OptimizeStoragesRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.PaymentType
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDebug
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDeleteRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageReadRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageRequestDebugMode
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageSearchFilter
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageSearchRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageSearchResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageUpdateRequest
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.models.StorageFilter
import ru.otus.otuskotlin.lrvch.common.models.StorageId
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub
import kotlin.test.Test

internal abstract class StorageRepoBaseV2Test {
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
    )

    @Test
    open fun updateStorage() = testRepoStorage(
        "update",
        StorageUpdateRequest(
            storage = CatalogStorageStub.prepareResult { title = "add" }.toTransportUpdateStorage(),
            debug = debug,
        ),
        prepareCtx(CatalogStorageStub.prepareResult { title = "add"; lock = StorageLock(uuidNew) })
            .toTransportUpdate()
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
            .toTransportSearch()
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
    )

    private fun prepareCtx(storage: Storage) = CatalogContext(
        state = CatalogState.RUNNING,
        storageResponse = storage.apply {
            // Пока не реализована эта функциональность
            permissionsClient.clear()
        },
    )

    private inline fun <reified Req : IRequest, reified Res : IResponse> testRepoStorage(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v2/storage/$url")
            .contentType(MediaType.APPLICATION_JSON)
//            .body(BodyInserters.fromValue(requestObj))
            .bodyValue(apiV2RequestSerialize(requestObj))
            .exchange()
            .expectStatus().isOk
//            .expectBody(Res::class.java)
            .expectBody<IResponse>()
            .consumeWith { response ->
                val body = response.responseBody
                println("RESPONSE: $body")

                val sortedResp: IResponse = when (body) {
                    is StorageSearchResponse -> body.copy(storages = body.storages?.sortedBy { it.id })
                    null -> throw RuntimeException("Null response")
                    else -> body
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
//            .value {
//                println("RESPONSE: $it")
//                val sortedResp: IResponse = when (it) {
//                    is StorageSearchResponse -> it.copy(storages = it.storages?.sortedBy { it.id })
//                    null -> throw RuntimeException("Null response")
//                    else -> it
//                }
//                assertThat(sortedResp).isEqualTo(expectObj)
//            }
    }
}
