package ru.otus.otuskotlin.lrvch.app.spring.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.lrvch.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportStorage
import ru.otus.otuskotlin.lrvch.api.v2.models.IRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.IResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.OptimizeStoragesRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.OptimizeStoragesResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDeleteRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDeleteResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageReadRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageReadResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageSearchRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageSearchResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageUpdateRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageUpdateResponse
import ru.otus.otuskotlin.lrvch.app.common.controllerHelper
import ru.otus.otuskotlin.lrvch.app.spring.base.CatalogAppSettings
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v2/storage")
class CatalogControllerV2(
    private val appSettings: CatalogAppSettings
) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: StorageCreateRequest): StorageCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun  read(@RequestBody request: StorageReadRequest): StorageReadResponse =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  update(@RequestBody request: StorageUpdateRequest): StorageUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: StorageDeleteRequest): StorageDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun  search(@RequestBody request: StorageSearchRequest): StorageSearchResponse =
        process(appSettings, request = request, this::class, "search")

    @PostMapping("optimize")
    suspend fun  offers(@RequestBody request: OptimizeStoragesRequest): OptimizeStoragesResponse =
        process(appSettings, request = request, this::class, "optimize")

    companion object {
        suspend inline fun <reified Request : IRequest, reified Response : IResponse> process(
            appSettings: CatalogAppSettings,
            request: Request,
            clazz: KClass<*>,
            logId: String,
        ): Response = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransportStorage() as Response },
            clazz,
            logId,
        )
    }
}
