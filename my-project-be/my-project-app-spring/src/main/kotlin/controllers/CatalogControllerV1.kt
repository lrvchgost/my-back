package ru.otus.otuskotlin.lrvch.app.spring.controllers

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.lrvch.api.v1.mappers.fromTransport
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportStorage
import ru.otus.otuskotlin.lrvch.api.v1.models.IRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.IResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.OptimizeStoragesRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.OptimizeStoragesResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateResponse
import ru.otus.otuskotlin.lrvch.app.common.controllerHelper
import ru.otus.otuskotlin.lrvch.app.spring.base.CatalogAppSettings
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/storage")
class CatalogControllerV1(
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
