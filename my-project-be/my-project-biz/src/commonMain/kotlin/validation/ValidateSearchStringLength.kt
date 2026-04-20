package ru.otus.otuskotlin.lrvch.biz.validation

import ru.otus.otuskotlin.lrvch.biz.validation.helpers.toLong
import ru.otus.otuskotlin.lrvch.biz.validation.helpers.toShort
import ru.otus.otuskotlin.lrvch.biz.validation.helpers.trimEmptyChars
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.errorValidation
import ru.otus.otuskotlin.lrvch.common.helpers.fail
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import ru.otus.otuskotlin.lrvch.libs.cor.ICorChainDsl
import ru.otus.otuskotlin.lrvch.libs.cor.chain
import ru.otus.otuskotlin.lrvch.libs.cor.worker

fun ICorChainDsl<CatalogContext>.validateSearchStringLength(title: String) = chain {
    this.title = title
    this.description = """
        Валидация длины строки поиска в поисковых фильтрах. Допустимые значения:
        - null - не выполняем поиск по строке
        - 3-100 - допустимая длина
        - больше 100 - слишком длинная строка
    """.trimIndent()
    on { state == CatalogState.RUNNING }
    worker("Обрезка пустых символов") {
        storageFilterValidating.trimEmptyChars()
    }
    worker {
        this.title = "Проверка кейса длины на 1-2 символа"
        this.description = this.title
        on { state == CatalogState.RUNNING && storageFilterValidating.toShort(upperEdge = 2) }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooShort",
                    description = "Search string must contain at least 3 symbols"
                )
            )
        }
    }
    worker {
        this.title = "Проверка кейса длины на более 100 символов"
        this.description = this.title
        on { state == CatalogState.RUNNING && storageFilterValidating.toLong(upperEdge = 100) }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooLong",
                    description = "Search string must be no more than 100 symbols long"
                )
            )
        }
    }
}
