package ru.otus.otuskotlin.lrvch.app.spring.stub

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportCreate
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportDelete
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportOptimize
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportRead
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportSearch
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportUpdate
import ru.otus.otuskotlin.lrvch.api.v2.models.OptimizeStoragesRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDeleteRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageReadRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageSearchRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageUpdateRequest
import ru.otus.otuskotlin.lrvch.app.spring.config.CatalogConfig
import ru.otus.otuskotlin.lrvch.app.spring.controllers.CatalogControllerV2
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import kotlin.test.Test


// Temporary simple test with stubs
@WebFluxTest(CatalogControllerV2::class, CatalogConfig::class)
internal class CatalogControllerV2Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockitoBean
    private lateinit var processor: CatalogProcessor

    @Test
    fun createStorage() = testStubStorage(
        "/v2/storage/create",
        StorageCreateRequest(),
        CatalogContext().toTransportCreate().copy()
    )

    @Test
    fun readStorage() = testStubStorage(
        "/v2/storage/read",
        StorageReadRequest(),
        CatalogContext().toTransportRead().copy()
    )

    @Test
    fun updateStorage() = testStubStorage(
        "/v2/storage/update",
        StorageUpdateRequest(),
        CatalogContext().toTransportUpdate().copy()
    )

    @Test
    fun deleteStorage() = testStubStorage(
        "/v2/storage/delete",
        StorageDeleteRequest(),
        CatalogContext().toTransportDelete().copy()
    )

    @Test
    fun searchStorage() = testStubStorage(
        "/v2/storage/search",
        StorageSearchRequest(),
        CatalogContext().toTransportSearch().copy()
    )

    @Test
    fun optimize() = testStubStorage(
        "/v2/storage/optimize",
        OptimizeStoragesRequest(),
        CatalogContext().toTransportOptimize().copy()
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
