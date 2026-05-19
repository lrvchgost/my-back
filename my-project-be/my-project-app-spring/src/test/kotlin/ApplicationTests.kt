package ru.otus.otuskotlin.lrvch.app.spring

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.otus.otuskotlin.lrvch.app.spring.config.CatalogConfigPostgres

@SpringBootTest
class ApplicationTests {
    @Autowired
    var pgConf: CatalogConfigPostgres = CatalogConfigPostgres()

    @Test
    fun contextLoads() {
        assertEquals(5433, pgConf.psql.port)
        assertEquals("test_db", pgConf.psql.database)
    }
}
