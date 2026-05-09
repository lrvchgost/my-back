package ru.otus.otuskotlin.lrvch.common.repo

import ru.otus.otuskotlin.lrvch.common.models.CatalogPaymentType
import ru.otus.otuskotlin.lrvch.common.models.SpeedType

data class DbStorageFilterRequest(
    var searchString: String = "",
    var availability: String = "",
    var capacity: String = "",
    var paymentType: CatalogPaymentType = CatalogPaymentType.NONE,
    var readSpeed: SpeedType = SpeedType.NONE,
    var writeSpeed: SpeedType = SpeedType.NONE
)
