import org.junit.Test
import ru.otus.otuskotlin.lrvch.api.v1.mappers.fromTransport
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransport
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportCreateStorage
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportDeleteStorage
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportOptimizeStorage
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportReadStorage
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportStorage
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportStorageFilter
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportUpdateStorage
import ru.otus.otuskotlin.lrvch.api.v1.models.OptimizeStoragesRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.OptimizeStoragesResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.PaymentType
import ru.otus.otuskotlin.lrvch.api.v1.models.SpeedType
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDebug
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDeleteResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageReadResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageRequestDebugMode
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageRequestDebugStubs
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageSearchResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageUpdateResponse
import  ru.otus.otuskotlin.lrvch.common.models.SpeedType as SpeedTypeModel
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogError
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.CatalogRequestId
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode
import ru.otus.otuskotlin.lrvch.common.models.StorageLock
import ru.otus.otuskotlin.lrvch.common.stubs.CatalogStubs
import ru.otus.otuskotlin.lrvch.stubs.CatalogStorageStub
import kotlin.test.assertEquals

class MapperTest {

    class StorageCreate {
        @Test
        fun fromTransport() {
            val req = StorageCreateRequest(
                debug = StorageDebug(
                    mode = StorageRequestDebugMode.STUB,
                    stub = StorageRequestDebugStubs.SUCCESS
                ),
                storage = CatalogStorageStub.get().toTransportCreateStorage(),
            )

            val expected = CatalogStorageStub.prepareResult {
                id = CatalogRequestId.NONE
                lock = StorageLock.NONE
                permissionsClient.clear()
            }

            val context = CatalogContext()
            context.fromTransport(req)

            assertEquals(CatalogStubs.SUCCESS, context.stubCase)
            assertEquals(CatalogWorkMode.STUB, context.workMode)
            assertEquals(expected, context.storageRequest)
        }

        @Test
        fun toTransport() {
            val context = CatalogContext().apply {
                requestId = CatalogRequestId("1234")
                command = CatalogCommand.CREATE
                storageResponse = CatalogStorageStub.get()
                errors = mutableListOf(
                    CatalogError(
                        code = "err",
                        group = "request",
                        field = "title",
                        message = "wrong title",
                    )
                )
                state = CatalogState.RUNNING
            }

            val res = context.toTransportStorage() as StorageCreateResponse

            assertEquals(res.storage, CatalogStorageStub.get().toTransport())
            assertEquals(1, res.errors?.size)
            assertEquals("err", res.errors?.firstOrNull()?.code)
            assertEquals("request", res.errors?.firstOrNull()?.group)
            assertEquals("title", res.errors?.firstOrNull()?.field)
            assertEquals("wrong title", res.errors?.firstOrNull()?.message)
        }
    }

    class StorageRead {
        @Test
        fun fromTransport() {
            val req = StorageReadRequest(
                debug = StorageDebug(
                    mode = StorageRequestDebugMode.STUB,
                    stub = StorageRequestDebugStubs.SUCCESS
                ),
                storage = CatalogStorageStub.getEmpty().apply {
                    id = CatalogStorageStub.getDefaultId()
                }.toTransportReadStorage(),
            )

            val expected = CatalogStorageStub.prepareResultOnEmpty {
                id = CatalogStorageStub.getDefaultId()
            }

            val context = CatalogContext()
            context.fromTransport(req)

            assertEquals(CatalogStubs.SUCCESS, context.stubCase)
            assertEquals(CatalogWorkMode.STUB, context.workMode)
            assertEquals(expected, context.storageRequest)
        }

        @Test
        fun toTransport() {
            val stubStorage = CatalogStorageStub.get()

            val context = CatalogContext().apply {
                requestId = CatalogRequestId("1234")
                command = CatalogCommand.READ
                storageResponse = stubStorage.copy()
                errors = mutableListOf(
                    CatalogError(
                        code = "err",
                        group = "read",
                        field = "id",
                        message = "no storage found",
                    )
                )
                state = CatalogState.RUNNING
            }

            val res = context.toTransportStorage() as StorageReadResponse

            assertEquals(res.storage, stubStorage.copy().toTransport())
            assertEquals(1, res.errors?.size)
            assertEquals("err", res.errors?.firstOrNull()?.code)
            assertEquals("read", res.errors?.firstOrNull()?.group)
            assertEquals("id", res.errors?.firstOrNull()?.field)
            assertEquals("no storage found", res.errors?.firstOrNull()?.message)
        }
    }

    class StorageUpdate {
        @Test
        fun fromTransport() {
            val req = StorageUpdateRequest(
                debug = StorageDebug(
                    mode = StorageRequestDebugMode.STUB,
                    stub = StorageRequestDebugStubs.SUCCESS
                ),
                storage = CatalogStorageStub.get().toTransportUpdateStorage(),
            )

            val expected = CatalogStorageStub.prepareResult {
                id = CatalogStorageStub.getDefaultId()
                lock = CatalogStorageStub.getDefaultLock()
                permissionsClient.clear()
            }

            val context = CatalogContext()
            context.fromTransport(req)

            assertEquals(CatalogStubs.SUCCESS, context.stubCase)
            assertEquals(CatalogWorkMode.STUB, context.workMode)
            assertEquals(expected, context.storageRequest)
        }

        @Test
        fun toTransport() {
            val context = CatalogContext().apply {
                requestId = CatalogRequestId("1234")
                command = CatalogCommand.UPDATE
                storageResponse = CatalogStorageStub.get()
                errors = mutableListOf(
                    CatalogError(
                        code = "err",
                        group = "update",
                        field = "title",
                        message = "wrong title",
                    )
                )
                state = CatalogState.RUNNING
            }

            val res = context.toTransportStorage() as StorageUpdateResponse

            assertEquals(res.storage, CatalogStorageStub.get().toTransport())
            assertEquals(1, res.errors?.size)
            assertEquals("err", res.errors?.firstOrNull()?.code)
            assertEquals("update", res.errors?.firstOrNull()?.group)
            assertEquals("title", res.errors?.firstOrNull()?.field)
            assertEquals("wrong title", res.errors?.firstOrNull()?.message)
        }
    }

    class StorageDelete {
        @Test
        fun fromTransport() {
            val req = StorageDeleteRequest(
                debug = StorageDebug(
                    mode = StorageRequestDebugMode.STUB,
                    stub = StorageRequestDebugStubs.SUCCESS
                ),
                storage = CatalogStorageStub.getEmpty().apply {
                    id = CatalogStorageStub.getDefaultId()
                    lock = CatalogStorageStub.getDefaultLock()
                }.toTransportDeleteStorage(),
            )

            val expected = CatalogStorageStub.prepareResultOnEmpty {
                id = CatalogStorageStub.getDefaultId()
                lock = CatalogStorageStub.getDefaultLock()
            }

            val context = CatalogContext()
            context.fromTransport(req)

            assertEquals(CatalogStubs.SUCCESS, context.stubCase)
            assertEquals(CatalogWorkMode.STUB, context.workMode)
            assertEquals(expected, context.storageRequest)
        }

        @Test
        fun toTransport() {
            val stubStorage = CatalogStorageStub.get()

            val context = CatalogContext().apply {
                requestId = CatalogRequestId("1234")
                command = CatalogCommand.DELETE
                storageResponse = stubStorage.copy()
                errors = mutableListOf(
                    CatalogError(
                        code = "err",
                        group = "delete",
                        field = "id",
                        message = "no storage found",
                    )
                )
                state = CatalogState.RUNNING
            }

            val res = context.toTransportStorage() as StorageDeleteResponse

            assertEquals(res.storage, stubStorage.copy().toTransport())
            assertEquals(1, res.errors?.size)
            assertEquals("err", res.errors?.firstOrNull()?.code)
            assertEquals("delete", res.errors?.firstOrNull()?.group)
            assertEquals("id", res.errors?.firstOrNull()?.field)
            assertEquals("no storage found", res.errors?.firstOrNull()?.message)
        }
    }

    class StorageSearch {
        @Test
        fun fromTransport() {
            val req = StorageSearchRequest(
                debug = StorageDebug(
                    mode = StorageRequestDebugMode.STUB,
                    stub = StorageRequestDebugStubs.SUCCESS
                ),
                searchFilter = CatalogStorageStub.getDefaultSearchFilter().toTransportStorageFilter(),
            )

            val expected = CatalogStorageStub.getDefaultSearchFilter()

            val context = CatalogContext()
            context.fromTransport(req)

            assertEquals(CatalogStubs.SUCCESS, context.stubCase)
            assertEquals(CatalogWorkMode.STUB, context.workMode)
            assertEquals(expected, context.storageFilterRequest)
        }

        @Test
        fun toTransport() {
            val context = CatalogContext().apply {
                requestId = CatalogRequestId("1234")
                command = CatalogCommand.SEARCH
                storageResponse = CatalogStorageStub.get()
                errors = mutableListOf(
                    CatalogError(
                        code = "err",
                        group = "search",
                        field = "match",
                        message = "unknown property",
                    )
                )
                storagesResponse = CatalogStorageStub.prepareSearchList(CatalogStorageStub.getDefaultSearchFilter())
                state = CatalogState.RUNNING
            }

            val res = context.toTransportStorage() as StorageSearchResponse

            assertEquals(
                res.storages,
                CatalogStorageStub
                    .prepareSearchList(CatalogStorageStub.getDefaultSearchFilter())
                    .toTransport()
            )
            assertEquals(1, res.errors?.size)
            assertEquals("err", res.errors?.firstOrNull()?.code)
            assertEquals("search", res.errors?.firstOrNull()?.group)
            assertEquals("match", res.errors?.firstOrNull()?.field)
            assertEquals("unknown property", res.errors?.firstOrNull()?.message)
        }
    }

    class StorageOptimize {
        @Test
        fun fromTransport() {
            val req = OptimizeStoragesRequest(
                debug = StorageDebug(
                    mode = StorageRequestDebugMode.STUB,
                    stub = StorageRequestDebugStubs.SUCCESS
                ),
                storages = CatalogStorageStub
                    .prepareOptimizeList()
                    .toTransportOptimizeStorage()
            )

            val expected = CatalogStorageStub.prepareOptimizeList()

            val context = CatalogContext()
            context.fromTransport(req)

            assertEquals(CatalogStubs.SUCCESS, context.stubCase)
            assertEquals(CatalogWorkMode.STUB, context.workMode)
            assertEquals(expected, context.storagesRequest)
        }

        @Test
        fun toTransport() {
            val stubStorage = CatalogStorageStub.get()

            val context = CatalogContext().apply {
                requestId = CatalogRequestId("1234")
                command = CatalogCommand.OPTIMIZE
                storageResponse = stubStorage.copy()
                errors = mutableListOf(
                    CatalogError(
                        code = "err",
                        group = "read",
                        field = "id",
                        message = "no storage found",
                    )
                )
                storageResponse = CatalogStorageStub.prepareResult {
                    id = CatalogStorageStub.getDefaultId()
                }
                state = CatalogState.RUNNING
            }

            val res = context.toTransportStorage() as OptimizeStoragesResponse

            assertEquals(res.storage, stubStorage.copy().toTransport())
            assertEquals(1, res.errors?.size)
            assertEquals("err", res.errors?.firstOrNull()?.code)
            assertEquals("read", res.errors?.firstOrNull()?.group)
            assertEquals("id", res.errors?.firstOrNull()?.field)
            assertEquals("no storage found", res.errors?.firstOrNull()?.message)
        }
    }

    class EnumsTest {
        class PaymentTypeTest {
            @Test
            fun fromTransport() {
                assertEquals(PaymentType.FREE.fromTransport(), CatalogPaymentType.FREE)
                assertEquals(PaymentType.PREPAID.fromTransport(), CatalogPaymentType.PREPAID)
                assertEquals(PaymentType.LICENSE.fromTransport(), CatalogPaymentType.LICENSE)
            }

            @Test
            fun toTransport() {
                assertEquals(CatalogPaymentType.FREE.toTransport(), PaymentType.FREE)
                assertEquals(CatalogPaymentType.PREPAID.toTransport(), PaymentType.PREPAID)
                assertEquals(CatalogPaymentType.LICENSE.toTransport(), PaymentType.LICENSE)
            }
        }

        class SpeedTypeTest {
            @Test
            fun fromTransport() {
                assertEquals(SpeedType._100.fromTransport(), SpeedTypeModel._100)
                assertEquals(SpeedType._150.fromTransport(), SpeedTypeModel._150)
                assertEquals(SpeedType._200.fromTransport(), SpeedTypeModel._200)
            }

            @Test
            fun toTransport() {
                assertEquals(SpeedTypeModel._100.toTransport(), SpeedType._100)
                assertEquals(SpeedTypeModel._150.toTransport(), SpeedType._150)
                assertEquals(SpeedTypeModel._200.toTransport(), SpeedType._200)
            }
        }
    }
}