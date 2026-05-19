package ru.otus.otuskotlin.lrvch.backend.repo.postgresql

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "catalog-of-storages-pass",
    val database: String = "catalog-of-storages",
    val schema: String = "public",
    val table: String = "storages",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
