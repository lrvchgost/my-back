package ru.otus.otuskotlin.lrvch.common.repo

import ru.otus.otuskotlin.lrvch.common.helpers.errorSystem

abstract class StorageRepoBase: IRepoStorage {

    protected suspend fun tryStorageMethod(block: suspend () -> IDbStorageResponse) = try {
        block()
    } catch (e: Throwable) {
        DbStorageResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryStoragesMethod(block: suspend () -> IDbStoragesResponse) = try {
        block()
    } catch (e: Throwable) {
        DbStoragesResponseErr(errorSystem("methodException", e = e))
    }

}
