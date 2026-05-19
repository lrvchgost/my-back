package ru.otus.otuskotlin.lrvch.backend.repo.postgresql

object SqlFields {
    const val ID = "id"
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val PAYMENT_TYPE = "payment_type"
    const val WRITE_SPEED = "write_speed"
    const val READ_SPEED = "read_speed"
    const val OPTIMIZE_ENABLED = "optimize_enabled"
    const val CAPACITY = "capacity"
    const val AVAILABILITY = "availability"
    const val LOCK = "lock"
    const val LOCK_OLD = "lock_old"

    // enum payment_types_type
    const val PAYMENT_TYPES_TYPE = "payment_types_type"
    const val PAYMENT_TYPE_FREE = "free"
    const val PAYMENT_TYPE_PREPAID = "prepaid"
    const val PAYMENT_TYPE_LICENSE = "license"

    // enum speed_type
    const val SPEED_TYPE = "speed_type"
    const val SPEED_TYPE_100 = "100"
    const val SPEED_TYPE_150 = "150"
    const val SPEED_TYPE_200 = "200"

    const val FILTER_TITLE = TITLE
//    const val FILTER_AD_TYPE = AD_TYPE

    const val DELETE_OK = "DELETE_OK"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, TITLE, DESCRIPTION, PAYMENT_TYPE, WRITE_SPEED, READ_SPEED, OPTIMIZE_ENABLED, CAPACITY, AVAILABILITY, LOCK,
    )
}
