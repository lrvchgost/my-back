package ru.otus.otuskotlin.lrvch.api.v2

import ru.otus.otuskotlin.lrvch.api.v2.models.IResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.OptimizeStoragesResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.PaymentType
import ru.otus.otuskotlin.lrvch.api.v2.models.SpeedType
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDeleteResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageReadResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageResponseObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageSearchResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageUpdateResponse
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    class StorageCreateResponseSerializationTest {
        private val response = StorageCreateResponse(
            storage = StorageResponseObject(
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
//        val json = apiV2Mapper.encodeToString(IResponse.serializer(), response)
            val json = apiV2Mapper.encodeToString<IResponse>(response)

            println(json)

            assertContains(json, Regex("\"title\":\\s*\"storage title\""))
            assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString<IResponse>(response)
            val obj = apiV2Mapper.decodeFromString<IResponse>(json) as StorageCreateResponse

            println(json)

            assertEquals(response, obj)
        }
    }

    class StorageReadResponseSerializationTest {
        private val response = StorageReadResponse(
            storage = StorageResponseObject(
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
            val json = apiV2Mapper.encodeToString<IResponse>(response)

            println(json)

            assertContains(json, Regex("\"title\":\\s*\"storage title\""))
            assertContains(json, Regex("\"responseType\":\\s*\"read\""))
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString<IResponse>(response)
            val obj = apiV2Mapper.decodeFromString<IResponse>(json) as StorageReadResponse

            println(json)

            assertEquals(response, obj)
        }
    }

    class StorageUpdateResponseSerializationTest {
        private val response = StorageUpdateResponse(
            storage = StorageResponseObject(
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
            val json = apiV2Mapper.encodeToString<IResponse>(response)

            println(json)

            assertContains(json, Regex("\"title\":\\s*\"storage title\""))
            assertContains(json, Regex("\"responseType\":\\s*\"update\""))
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString<IResponse>(response)
            val obj = apiV2Mapper.decodeFromString<IResponse>(json) as StorageUpdateResponse

            println(json)

            assertEquals(response, obj)
        }
    }

    class StorageDeleteResponseSerializationTest {
        private val response = StorageDeleteResponse(
            storage = StorageResponseObject(
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
            val json = apiV2Mapper.encodeToString<IResponse>(response)

            println(json)

            assertContains(json, Regex("\"title\":\\s*\"storage title\""))
            assertContains(json, Regex("\"responseType\":\\s*\"delete\""))
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString<IResponse>(response)
            val obj = apiV2Mapper.decodeFromString<IResponse>(json) as StorageDeleteResponse

            println(json)

            assertEquals(response, obj)
        }
    }

    class StorageSearchResponseSerializationTest {
        private val response = StorageSearchResponse(
            storages = listOf(StorageResponseObject(
                title = "storage title",
                description = "storage description",
                paymentType = PaymentType.FREE,
                readSpeed = SpeedType._100,
                writeSpeed = SpeedType._100,
                enableOptimize = "1"
            ))
        )

        @Test
        fun serialize() {
            val json = apiV2Mapper.encodeToString<IResponse>(response)

            println(json)

            assertContains(json, "\"storages\":[{\"title\":\"storage title\"")
            assertContains(json, Regex("\"responseType\":\\s*\"search\""))
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString<IResponse>(response)
            val obj = apiV2Mapper.decodeFromString<IResponse>(json) as StorageSearchResponse

            println(json)

            assertEquals(response, obj)
        }
    }

    class StorageOptimizeResponseSerializationTest {
        private val response = OptimizeStoragesResponse(
            storage = StorageResponseObject(
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
            val json = apiV2Mapper.encodeToString<IResponse>(response)

            println(json)

            assertContains(json, Regex("\"title\":\\s*\"storage title\""))
            assertContains(json, Regex("\"responseType\":\\s*\"optimize\""))
        }

        @Test
        fun deserialize() {
            val json = apiV2Mapper.encodeToString<IResponse>(response)
            val obj = apiV2Mapper.decodeFromString<IResponse>(json) as OptimizeStoragesResponse

            println(json)

            assertEquals(response, obj)
        }
    }
}
