package ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.lrvch.api.v1.models.*
import ru.otus.otuskotlin.lrvch.e2e.be.base.client.Client
import ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1.base.sendAndReceive
import ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1.base.someCreateStorage
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioSearchV1(
    private val client: Client,
    private val debug: StorageDebug? = null
) {
    @Test
    fun search() = runBlocking {
        val objs = listOf(
            someCreateStorage,
            someCreateStorage.copy(title = "Storage 2"),
            someCreateStorage.copy(title = "Storage 3"),
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

        val sObj = StorageSearchFilter(searchString = "public")
        val resSearch = client.sendAndReceive(
            "storage/search",
            StorageSearchRequest(
                requestType = "search",
                debug = debug,
                searchFilter = sObj,
            )
        ) as StorageSearchResponse

        assertEquals(ResponseResult.SUCCESS, resSearch.result)

        val rsObj: List<StorageResponseObject> = resSearch.storages ?: fail("No storages in Search response")
        val titles = rsObj.map { it.title }
        println(titles)
        assertContains(titles, "public")
        assertContains(titles, "public")

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