package ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.lrvch.api.v1.models.*
import ru.otus.otuskotlin.lrvch.e2e.be.base.client.Client
import ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1.base.sendAndReceive
import ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1.base.someCreateStorage
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioOptimizeV1(
    private val client: Client,
    private val debug: StorageDebug? = null
) {
    @Test
    fun search() = runBlocking {
        val objs = listOf(
            someCreateStorage,
            someCreateStorage.copy(title = "Some storage 2"),
            someCreateStorage.copy(title = "Some storage 3"),
        ).map { obj ->
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
            cObj
        }

        val sObj = objs.map { StorageOptimizeObject(it.id) }
        val storageResp = client.sendAndReceive(
            "storage/optimize",
            OptimizeStoragesRequest(
                requestType = "optimize",
                debug = debug,
                storages = sObj,
            )
        ) as OptimizeStoragesResponse

        assertEquals(ResponseResult.SUCCESS, storageResp.result)

        val cObj: StorageResponseObject = storageResp.storage ?: fail("No storage in Optimize response")
        println(cObj)
        assertEquals("Change it", cObj.title)
        assertEquals("Change it", cObj.description)
        assertEquals((someCreateStorage.capacity?.toInt()?.times(3) ?: 0).toString(), cObj.capacity)
        assertEquals(someCreateStorage.availability, cObj.availability)
        assertEquals(someCreateStorage.paymentType, cObj.paymentType)
        assertEquals(someCreateStorage.readSpeed, cObj.readSpeed)
        assertEquals(someCreateStorage.writeSpeed, cObj.writeSpeed)

        objs.forEach { obj ->
            val resDelete = client.sendAndReceive(
                "storage/delete", StorageDeleteRequest(
                    requestType = "delete",
                    debug = debug,
                    storage = StorageDeleteObject(obj.id, obj.lock),
                )
            ) as StorageDeleteResponse

            assertEquals(ResponseResult.SUCCESS, resDelete.result)
        }
    }
}