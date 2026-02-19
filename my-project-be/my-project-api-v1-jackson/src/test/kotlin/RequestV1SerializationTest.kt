import ru.otus.otuskotlin.lrvch.api.v1.apiV1Mapper
import ru.otus.otuskotlin.lrvch.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    class StorageCreateRequestSerialization {
        private val request = StorageCreateRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB, stub = StorageRequestDebugStubs.BAD_TITLE
            ), storage = StorageCreateObject(
                title = "storage title",
                description = "storage description",
                paymentType = PaymentType.FREE,
                readSpeed = SpeedType._100,
                writeSpeed = SpeedType._100
            )
        )

        @Test
        fun serialize() {
            val json = apiV1Mapper.writeValueAsString(request)

            assertContains(json, Regex("\"title\":\\s*\"storage title\""))
            assertContains(json, Regex("\"mode\":\\s*\"stub\""))
            assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
            assertContains(json, Regex("\"requestType\":\\s*\"create\""))
        }

        @Test
        fun deserialize() {
            val json = apiV1Mapper.writeValueAsString(request)
            val obj = apiV1Mapper.readValue(json, StorageCreateRequest::class.java)

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"storage": null}
        """.trimIndent()
            val obj = apiV1Mapper.readValue(jsonString, StorageCreateRequest::class.java)

            assertEquals(null, obj.storage)
        }
    }

    class StorageReadRequestSerialization {
        private val request = StorageReadRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB, stub = StorageRequestDebugStubs.BAD_TITLE
            ), storage = StorageReadObject(
                id = "id"
            )
        )

        @Test
        fun serialize() {
            val json = apiV1Mapper.writeValueAsString(request)

            assertContains(json, Regex("\"id\":\\s*\"id\""))
        }

        @Test
        fun deserialize() {
            val json = apiV1Mapper.writeValueAsString(request)
            val obj = apiV1Mapper.readValue(json, StorageReadRequest::class.java)

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"storage": null}
        """.trimIndent()
            val obj = apiV1Mapper.readValue(jsonString, StorageReadRequest::class.java)

            assertEquals(null, obj.storage)
        }
    }

    class StorageUpdateRequestSerialization {
        private val request = StorageUpdateRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB, stub = StorageRequestDebugStubs.BAD_TITLE
            ), storage = StorageUpdateObject(
                title = "storage title",
                description = "storage description",
                paymentType = PaymentType.FREE,
                readSpeed = SpeedType._100,
                writeSpeed = SpeedType._100
            )
        )

        @Test
        fun serialize() {
            val json = apiV1Mapper.writeValueAsString(request)

            assertContains(json, Regex("\"title\":\\s*\"storage title\""))
            assertContains(json, Regex("\"mode\":\\s*\"stub\""))
            assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
            assertContains(json, Regex("\"requestType\":\\s*\"update\""))
        }

        @Test
        fun deserialize() {
            val json = apiV1Mapper.writeValueAsString(request)
            val obj = apiV1Mapper.readValue(json, StorageUpdateRequest::class.java)

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"storage": null}
        """.trimIndent()
            val obj = apiV1Mapper.readValue(jsonString, StorageUpdateRequest::class.java)

            assertEquals(null, obj.storage)
        }
    }

    class StorageDeleteRequestSerialization {
        private val request = StorageDeleteRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB, stub = StorageRequestDebugStubs.BAD_TITLE
            ), storage = StorageDeleteObject(
                id = "id",
                lock = "lock",
            )
        )

        @Test
        fun serialize() {
            val json = apiV1Mapper.writeValueAsString(request)

            assertContains(json, Regex("\"id\":\\s*\"id\""))
            assertContains(json, Regex("\"lock\":\\s*\"lock\""))
        }

        @Test
        fun deserialize() {
            val json = apiV1Mapper.writeValueAsString(request)
            val obj = apiV1Mapper.readValue(json, StorageDeleteRequest::class.java)

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"storage": null}
        """.trimIndent()
            val obj = apiV1Mapper.readValue(jsonString, StorageDeleteRequest::class.java)

            assertEquals(null, obj.storage)
        }
    }

    class StorageSearchRequestSerialization {
        private val request = StorageSearchRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB, stub = StorageRequestDebugStubs.BAD_TITLE
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
            val json = apiV1Mapper.writeValueAsString(request)

            assertContains(json, Regex("\"searchString\":\\s*\"searchString\""))
            assertContains(json, Regex("\"availability\":\\s*\"99.999\""))
            assertContains(json, Regex("\"readSpeed\":\\s*\"100\""))
            assertContains(json, Regex("\"writeSpeed\":\\s*\"100\""))
            assertContains(json, Regex("\"capacity\":\\s*\"100\""))
            assertContains(json, Regex("\"paymentType\":\\s*\"free\""))
        }

        @Test
        fun deserialize() {
            val json = apiV1Mapper.writeValueAsString(request)
            val obj = apiV1Mapper.readValue(json, StorageSearchRequest::class.java)

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"searchFilter": null}
        """.trimIndent()
            val obj = apiV1Mapper.readValue(jsonString, StorageSearchRequest::class.java)

            assertEquals(null, obj.searchFilter)
        }
    }

    class StorageOptimizeRequestSerialization {
        private val request = OptimizeStoragesRequest(
            debug = StorageDebug(
                mode = StorageRequestDebugMode.STUB, stub = StorageRequestDebugStubs.BAD_TITLE
            ),
            storages = listOf(StorageOptimizeObject(
                id = "id"
            ))
        )

        @Test
        fun serialize() {
            val json = apiV1Mapper.writeValueAsString(request)

            assertContains(json, "\"storages\":[{\"id\":\"id\"}]")
        }

        @Test
        fun deserialize() {
            val json = apiV1Mapper.writeValueAsString(request)
            val obj = apiV1Mapper.readValue(json, OptimizeStoragesRequest::class.java)

            assertEquals(request, obj)
        }

        @Test
        fun deserializeNaked() {
            val jsonString = """
            {"storages": null}
        """.trimIndent()
            val obj = apiV1Mapper.readValue(jsonString, OptimizeStoragesRequest::class.java)

            assertEquals(null, obj.storages)
        }
    }
}
