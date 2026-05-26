package ru.otus.otuskotlin.lrvch.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import ru.otus.otuskotlin.lrvch.common.models.SpeedType

fun Table.speedTypeEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.SPEED_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.SPEED_TYPE_100 -> SpeedType._100
            SqlFields.SPEED_TYPE_150 -> SpeedType._150
            SqlFields.SPEED_TYPE_200 -> SpeedType._200
            else -> SpeedType.NONE
        }
    },
    toDb = { value ->
        when (value) {
            SpeedType._100 -> PgSeedType100
            SpeedType._150 -> PgSpeedType150
            SpeedType._200 -> PgSpeedType200
            SpeedType.NONE -> throw Exception("Wrong value of SpeedType NONE is unsupported")
        }
    }
)

sealed class PgSpeedTypeValue(enVal: String): PGobject() {
    init {
        type = SqlFields.SPEED_TYPE
        value = enVal
    }
}

object PgSeedType100: PgSpeedTypeValue(SqlFields.SPEED_TYPE_100) {
    private fun readResolve(): Any = PgSeedType100
}

object PgSpeedType150: PgSpeedTypeValue(SqlFields.SPEED_TYPE_150) {
    private fun readResolve(): Any = PgSpeedType150
}

object PgSpeedType200: PgSpeedTypeValue(SqlFields.SPEED_TYPE_200) {
    private fun readResolve(): Any = PgSpeedType200
}
