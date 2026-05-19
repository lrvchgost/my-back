package ru.otus.otuskotlin.lrvch.app.spring.repo

import StorageRepoInMemory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage

@TestConfiguration
class RepoInMemoryConfig {
    @Suppress("unused")
    @Bean()
    @Primary
    fun prodRepo(): IRepoStorage = StorageRepoInMemory()
}
