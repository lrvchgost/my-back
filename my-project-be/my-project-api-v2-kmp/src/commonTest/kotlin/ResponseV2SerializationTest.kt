package ru.otus.otuskotlin.lrvch.api.v2

import ru.otus.otuskotlin.lrvch.api.v2.models.IResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.PaymentType
import ru.otus.otuskotlin.lrvch.api.v2.models.SpeedType
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateResponse
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageResponseObject
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
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
