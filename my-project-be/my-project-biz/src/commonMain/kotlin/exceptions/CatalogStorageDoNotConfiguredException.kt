package ru.otus.otuskotlin.lrvch.biz.exceptions

import ru.otus.otuskotlin.lrvch.common.models.CatalogWorkMode

class CatalogStorageDoNotConfiguredException(val workMode: CatalogWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
