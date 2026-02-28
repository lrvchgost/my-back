package ru.otus.otuskotlin.lrvch.logging.socket

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.lrvch.logging.common.LogLevel

@Serializable
data class LogData(
    val level: LogLevel,
    val message: String,
//    val data: T
)
