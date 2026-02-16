package ru.otus.otuskotlin.lrvch.api.v1

import ru.otus.otuskotlin.lrvch.api.v1.models.*

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = StorageCreateResponse(
        storage = StorageResponseObject(
            title = "storage title",
            description = "storage description",
            paymentType = PaymentType.FREE,
            readSpeed = SpeedType._100,
            writeSpeed = SpeedType._100,
            id = "0"
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"storage title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, StorageCreateResponse::class.java)

        assertEquals(response, obj)
    }
}
