import ru.otus.otuskotlin.lrvch.api.v1.apiV1Mapper
import ru.otus.otuskotlin.lrvch.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
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
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as StorageCreateRequest

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
