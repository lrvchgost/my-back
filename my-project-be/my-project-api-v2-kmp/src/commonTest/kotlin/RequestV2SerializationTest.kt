package ru.otus.otuskotlin.lrvch.api.v2

import ru.otus.otuskotlin.lrvch.api.v2.models.IRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.PaymentType
import ru.otus.otuskotlin.lrvch.api.v2.models.SpeedType
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateObject
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDebug
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageRequestDebugMode
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageRequestDebugStubs
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV2SerializationTest {
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
