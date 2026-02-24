package ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.lrvch.api.v1.models.*
import ru.otus.otuskotlin.lrvch.e2e.be.base.client.Client
import ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1.base.sendAndReceive
import ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1.base.someCreateStorage
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioUpdateV1(
    private val client: Client,
    private val debug: StorageDebug? = null
) {
    @Test
    fun update() = runBlocking {
        val obj = someCreateStorage
        val resCreate = client.sendAndReceive(
            "storage/create", StorageCreateRequest(
                requestType = "create",
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

        val uObj = StorageUpdateObject(
            id = cObj.id,
            lock = cObj.lock,
            title = cObj.title,
            description = cObj.description,
            capacity = cObj.capacity,
            availability = cObj.availability,
            paymentType = cObj.paymentType,
            readSpeed = cObj.readSpeed,
            writeSpeed = cObj.writeSpeed,
        )
        val resUpdate = client.sendAndReceive(
            "storage/update",
            StorageUpdateRequest(
                requestType = "update",
                debug = debug,
                storage = uObj,
            )
        ) as StorageUpdateResponse

        assertEquals(ResponseResult.SUCCESS, resUpdate.result)

        val ruObj: StorageResponseObject = resUpdate.storage ?: fail("No storage in Update response")
        assertEquals(uObj.title, ruObj.title)
        assertEquals(uObj.description, ruObj.description)
        assertEquals(uObj.capacity, ruObj.capacity)
        assertEquals(uObj.availability, ruObj.availability)
        assertEquals(uObj.paymentType, ruObj.paymentType)
        assertEquals(uObj.readSpeed, ruObj.readSpeed)
        assertEquals(uObj.writeSpeed, ruObj.writeSpeed)

        val resDelete = client.sendAndReceive(
            "storage/delete", StorageDeleteRequest(
                requestType = "delete",
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
    }
}