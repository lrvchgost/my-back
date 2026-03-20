package ru.otus.otuskotlin.lrvch.app.spring.stub

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportCreate
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportDelete
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportOptimize
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportRead
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportSearch
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportUpdate
import ru.otus.otuskotlin.lrvch.api.v1.models.OptimizeStoragesRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateRequest
import ru.otus.otuskotlin.lrvch.app.spring.config.CatalogConfig
import ru.otus.otuskotlin.lrvch.app.spring.controllers.CatalogControllerV1
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import kotlin.test.Test


// Temporary simple test with stubs
@WebFluxTest(CatalogControllerV1::class, CatalogConfig::class)
internal class CatalogControllerV1Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockitoBean
    private lateinit var processor: CatalogProcessor

    @Test
    fun createStorage() = testStubStorage(
        "/v1/storage/create",
        StorageCreateRequest(),
        CatalogContext().toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readStorage() = testStubStorage(
        "/v1/storage/read",
        StorageReadRequest(),
        CatalogContext().toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateStorage() = testStubStorage(
        "/v1/storage/update",
        StorageUpdateRequest(),
        CatalogContext().toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteStorage() = testStubStorage(
        "/v1/storage/delete",
        StorageDeleteRequest(),
        CatalogContext().toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchStorage() = testStubStorage(
        "/v1/storage/search",
        StorageSearchRequest(),
        CatalogContext().toTransportSearch().copy(responseType = "search")
    )

    @Test
    fun optimize() = testStubStorage(
        "/v1/storage/optimize",
        OptimizeStoragesRequest(),
        CatalogContext().toTransportOptimize().copy(responseType = "optimize")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubStorage(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
