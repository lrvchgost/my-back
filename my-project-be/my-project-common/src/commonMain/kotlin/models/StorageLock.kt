package models

import kotlin.jvm.JvmInline

@JvmInline
value class StorageLock(private val id: String) {
    fun asString(): String = id

    companion object {
        val NONE: StorageLock = StorageLock("")
    }
}