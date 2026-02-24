package ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v2

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.lrvch.api.v2.models.*
import ru.otus.otuskotlin.lrvch.e2e.be.base.client.Client
import ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v2.base.sendAndReceive
import ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v2.base.someCreateStorage
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioReadV2(
    private val client: Client,
    private val debug: StorageDebug? = null
) {
    @Test
    fun read() = runBlocking {
        val obj = someCreateStorage
        val resCreate = client.sendAndReceive(
            "storage/create", StorageCreateRequest(
                debug = debug,
                storage = obj,
            )
        ) as StorageCreateResponse

        assertEquals(ResponseResult.SUCCESS, resCreate.result)

        val cObj: StorageResponseObject = resCreate.storage ?: fail("No storage in Create response")
        assertEquals(obj.title, cObj.title)
        assertEquals(obj.description, cObj.description)
        assertEquals(obj.capacity, cObj.capacity)
        assertEquals(obj.availability, cObj.availability)
        assertEquals(obj.paymentType, cObj.paymentType)
        assertEquals(obj.readSpeed, cObj.readSpeed)
        assertEquals(obj.writeSpeed, cObj.writeSpeed)
        assertEquals(obj.enableOptimize, cObj.enableOptimize)

        val rObj = StorageReadObject(
            id = cObj.id,
        )
        val resRead = client.sendAndReceive(
            "storage/read",
            StorageReadRequest(
                debug = debug,
                storage = rObj,
            )
        ) as StorageReadResponse

        assertEquals(ResponseResult.SUCCESS, resRead.result)

        val rrObj: StorageResponseObject = resRead.storage ?: fail("No storage in Read response")
        assertEquals(obj.title, rrObj.title)
        assertEquals(obj.description, rrObj.description)
        assertEquals(obj.capacity, rrObj.capacity)
        assertEquals(obj.availability, rrObj.availability)
        assertEquals(obj.paymentType, rrObj.paymentType)
        assertEquals(obj.readSpeed, rrObj.readSpeed)
        assertEquals(obj.writeSpeed, rrObj.writeSpeed)
        assertEquals(obj.enableOptimize, rrObj.enableOptimize)

        val resDelete = client.sendAndReceive(
            "storage/delete", StorageDeleteRequest(
                debug = debug,
                storage = StorageDeleteObject(cObj.id, cObj.lock),
            )
        ) as StorageDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)

        val dObj: StorageResponseObject = resDelete.storage ?: fail("No ad in Delete response")
        assertEquals(obj.title, dObj.title)
        assertEquals(obj.description, dObj.description)
        assertEquals(obj.capacity, dObj.capacity)
        assertEquals(obj.availability, dObj.availability)
        assertEquals(obj.paymentType, dObj.paymentType)
        assertEquals(obj.readSpeed, dObj.readSpeed)
        assertEquals(obj.writeSpeed, dObj.writeSpeed)
        assertEquals(obj.enableOptimize, dObj.enableOptimize)
    }
}
