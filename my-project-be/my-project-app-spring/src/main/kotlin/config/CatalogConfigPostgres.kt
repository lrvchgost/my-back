package ru.otus.otuskotlin.lrvch.app.spring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.otus.otuskotlin.lrvch.backend.repo.postgresql.SqlProperties

@ConfigurationProperties(prefix = "psql")
data class CatalogConfigPostgres(
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "catalog-of-storages-pass",
    var database: String = "catalog-of-storages",
    var schema: String = "public",
    var table: String = "storages",
) {
    val psql: SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table,
    )
}
