package ru.otus.otuskotlin.lrvch.app.common

import controllerHelper
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.lrvch.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportStorage
import ru.otus.otuskotlin.lrvch.api.v2.models.IRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.IResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.ResponseResult
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDebug
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageRequestDebugMode
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageRequestDebugStubs
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = StorageCreateRequest(
        storage = StorageCreateObject(
            title = "some storage",
            description = "some description of some storage",
        ),
        debug = StorageDebug(mode = StorageRequestDebugMode.STUB, stub = StorageRequestDebugStubs.SUCCESS)
    )

    private val appSettings: ICatalogAppSettings = object : ICatalogAppSettings {
        override val corSettings: CatalogCoreSettings = CatalogCoreSettings()
        override val processor: CatalogProcessor = CatalogProcessor(corSettings)
    }

    private suspend fun createCatalogSpring(request: StorageCreateRequest): StorageCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportStorage() as StorageCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createStorageKtor(appSettings: ICatalogAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<StorageCreateRequest>()) },
            { toTransportStorage() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createCatalogSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createStorageKtor(appSettings) }
        val res = testApp.res as StorageCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
