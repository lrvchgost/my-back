@file:Suppress("unused")

package ru.otus.otuskotlin.lrvch.api.v2

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.lrvch.api.v2.models.IRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.IResponse

@OptIn(ExperimentalSerializationApi::class)
@Suppress("JSON_FORMAT_REDUNDANT_DEFAULT")
val apiV2Mapper = Json {
//    ignoreUnknownKeys = true
    allowTrailingComma = true
//    classDiscriminator = "requestType"
}

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV2RequestDeserialize(json: String) =
    apiV2Mapper.decodeFromString<IRequest>(json) as T

fun apiV2ResponseSerialize(obj: IResponse): String =
    apiV2Mapper.encodeToString(IResponse.serializer(), obj)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV2ResponseDeserialize(json: String) =
    apiV2Mapper.decodeFromString<IResponse>(json) as T

inline fun <reified T : IResponse> apiV2ResponseSimpleDeserialize(json: String) =
    apiV2Mapper.decodeFromString<T>(json)

fun apiV2RequestSerialize(obj: IRequest): String =
    apiV2Mapper.encodeToString(IRequest.serializer(), obj)

inline fun <reified T : IRequest> apiV2RequestSimpleSerialize(obj: T): String =
    apiV2Mapper.encodeToString<T>(obj)
