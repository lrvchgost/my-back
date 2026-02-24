package ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1.base

import ru.otus.otuskotlin.lrvch.api.v1.models.*

val someCreateStorage = StorageCreateObject(
    title = "Some storage",
    description = "Хранилище для пользователей без почты",
    capacity = "100",
    availability = "99.99",
    paymentType = PaymentType.FREE,
    readSpeed = SpeedType._100,
    writeSpeed = SpeedType._100,
)
