package ru.otus.otuskotlin.lrvch.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType

fun Table.paymentTypeEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.PAYMENT_TYPES_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.PAYMENT_TYPE_FREE -> CatalogPaymentType.FREE
            SqlFields.PAYMENT_TYPE_LICENSE -> CatalogPaymentType.LICENSE
            SqlFields.PAYMENT_TYPE_PREPAID -> CatalogPaymentType.PREPAID
            else -> CatalogPaymentType.NONE
        }
    },
    toDb = { value ->
        when (value) {
            CatalogPaymentType.FREE -> PgPaymentTypeFree
            CatalogPaymentType.LICENSE -> PgPaymentTypeLicense
            CatalogPaymentType.PREPAID -> PgPaymentTypePrepaid
            CatalogPaymentType.NONE -> throw Exception("Wrong value of CatalogPaymentType. NONE is unsupported")
        }
    }
)

sealed class PgPaymentTypeValue(enVal: String): PGobject() {
    init {
        type = SqlFields.PAYMENT_TYPES_TYPE
        value = enVal
    }
}

object PgPaymentTypeFree: PgPaymentTypeValue(SqlFields.PAYMENT_TYPE_FREE) {
    private fun readResolve(): Any = PgPaymentTypeFree
}

object PgPaymentTypeLicense: PgPaymentTypeValue(SqlFields.PAYMENT_TYPE_LICENSE) {
    private fun readResolve(): Any = PgPaymentTypeLicense
}

object PgPaymentTypePrepaid: PgPaymentTypeValue(SqlFields.PAYMENT_TYPE_PREPAID) {
    private fun readResolve(): Any = PgPaymentTypePrepaid
}
