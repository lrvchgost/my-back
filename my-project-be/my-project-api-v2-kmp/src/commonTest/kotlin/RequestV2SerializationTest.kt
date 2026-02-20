package ru.otus.otuskotlin.lrvch.api.v2

import ru.otus.otuskotlin.lrvch.api.v2.models.IRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.OptimizeStoragesRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.PaymentType
import ru.otus.otuskotlin.lrvch.api.v2.models.SpeedType
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDebug
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDeleteObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDeleteRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageOptimizeObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageReadObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageReadRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageRequestDebugMode
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageRequestDebugStubs
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageSearchFilter
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageSearchRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageUpdateObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageUpdateRequest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV2SerializationTest {
    class StorageCreateRequestSerializationTest {
        private val request = StorageCreateRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB,
                stub = StorageRequestDebugStubs.BAD_TITLE
            ),
            storage = StorageCreateObject(
                title = "storage title",
                description = "storage description",
                paymentType = PaymentType.FREE,
                readSpeed = SpeedType._100,
                writeSpeed = SpeedType._100,
                enableOptimize = "1"
            )
        )

        @Test
        fun serialize() {
            val json = apiV2Mapper.encodeToString<IRequest>(request)

            println(json)

            assertContains(json, Regex("\"title\":\\s*\"storage title\""))
            assertContains(json, Regex("\"mode\":\\s*\"stub\""))
            assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
            assertContains(json, Regex("\"enableOptimize\":\\s*\"1\""))
            assertContains(json, Regex("\"requestType\":\\s*\"create\""))
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)
            val obj = apiV2Mapper.decodeFromString<IRequest>(json) as StorageCreateRequest

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"storage": null}
        """.trimIndent()
            val obj = apiV2Mapper.decodeFromString<StorageCreateRequest>(jsonString)

            assertEquals(null, obj.storage)
        }
    }

    class StorageReadRequestSerializationTest {
        private val request = StorageReadRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB,
                stub = StorageRequestDebugStubs.BAD_TITLE
            ),
            storage = StorageReadObject(
                id = "id"
            )
        )

        @Test
        fun serialize() {
            val json = apiV2Mapper.encodeToString<IRequest>(request)

            println(json)

            assertContains(json, Regex("\"id\":\\s*\"id\""))
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)
            val obj = apiV2Mapper.decodeFromString<IRequest>(json) as StorageReadRequest

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"storage": null}
        """.trimIndent()
            val obj = apiV2Mapper.decodeFromString<StorageReadRequest>(jsonString)

            assertEquals(null, obj.storage)
        }
    }

    class StorageUpdateRequestSerializationTest {
        private val request = StorageUpdateRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB,
                stub = StorageRequestDebugStubs.BAD_TITLE
            ),
            storage = StorageUpdateObject(
                title = "storage title",
                description = "storage description",
                paymentType = PaymentType.FREE,
                readSpeed = SpeedType._100,
                writeSpeed = SpeedType._100,
                enableOptimize = "1"
            )
        )

        @Test
        fun serialize() {
            val json = apiV2Mapper.encodeToString<IRequest>(request)

            println(json)

            assertContains(json, Regex("\"title\":\\s*\"storage title\""))
            assertContains(json, Regex("\"mode\":\\s*\"stub\""))
            assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
            assertContains(json, Regex("\"enableOptimize\":\\s*\"1\""))
            assertContains(json, Regex("\"requestType\":\\s*\"update\""))
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)
            val obj = apiV2Mapper.decodeFromString<IRequest>(json) as StorageUpdateRequest

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"storage": null}
        """.trimIndent()
            val obj = apiV2Mapper.decodeFromString<StorageUpdateRequest>(jsonString)

            assertEquals(null, obj.storage)
        }
    }

    class StorageDeleteRequestSerializationTest {
        private val request = StorageDeleteRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB,
                stub = StorageRequestDebugStubs.BAD_TITLE
            ),
            storage = StorageDeleteObject(
                id = "id",
                lock = "lock"
            )
        )

        @Test
        fun serialize() {
            val json = apiV2Mapper.encodeToString<IRequest>(request)

            println(json)

            assertContains(json, Regex("\"id\":\\s*\"id\""))
            assertContains(json, Regex("\"lock\":\\s*\"lock\""))
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)
            val obj = apiV2Mapper.decodeFromString<IRequest>(json) as StorageDeleteRequest

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"storage": null}
        """.trimIndent()
            val obj = apiV2Mapper.decodeFromString<StorageDeleteRequest>(jsonString)

            assertEquals(null, obj.storage)
        }
    }

    class StorageSearchRequestSerializationTest {
        private val request = StorageSearchRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB,
                stub = StorageRequestDebugStubs.BAD_TITLE
            ),
            searchFilter = StorageSearchFilter(
                searchString = "searchString",
                availability = "99.999",
                readSpeed = SpeedType._100,
                writeSpeed = SpeedType._100,
                capacity = "100",
                paymentType = PaymentType.FREE,
            )
        )

        @Test
        fun serialize() {
            val json = apiV2Mapper.encodeToString<IRequest>(request)

            println(json)

            assertContains(json, Regex("\"searchString\":\\s*\"searchString\""))
            assertContains(json, Regex("\"availability\":\\s*\"99.999\""))
            assertContains(json, Regex("\"readSpeed\":\\s*\"100\""))
            assertContains(json, Regex("\"writeSpeed\":\\s*\"100\""))
            assertContains(json, Regex("\"capacity\":\\s*\"100\""))
            assertContains(json, Regex("\"paymentType\":\\s*\"free\""))
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)
            val obj = apiV2Mapper.decodeFromString<IRequest>(json) as StorageSearchRequest

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"searchFilter": null}
        """.trimIndent()
            val obj = apiV2Mapper.decodeFromString<StorageSearchRequest>(jsonString)

            assertEquals(null, obj.searchFilter)
        }
    }

    class StorageOptimizeRequestSerializationTest {
        private val request = OptimizeStoragesRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB, stub = StorageRequestDebugStubs.BAD_TITLE
            ),
            storages = listOf(
                StorageOptimizeObject(
                    id = "id"
                )
            )
        )

        @Test
        fun serialize() {
            val json = apiV2Mapper.encodeToString<IRequest>(request)

            assertContains(json, "\"storages\":[{\"id\":\"id\"}]")
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)
            val obj = apiV2Mapper.decodeFromString<IRequest>(json) as OptimizeStoragesRequest

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"storages": null}
        """.trimIndent()
            val obj = apiV2Mapper.decodeFromString<OptimizeStoragesRequest>(jsonString)

            assertEquals(null, obj.storages)
        }
    }
}
