package ru.otus.otuskotlin.lrvch.app.spring.config

import StorageRepoInMemory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.lrvch.app.spring.base.CatalogAppSettings
import ru.otus.otuskotlin.lrvch.biz.CatalogProcessor
import ru.otus.otuskotlin.lrvch.biz.ICatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogCoreSettings
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage
import ru.otus.otuskotlin.lrvch.logging.common.UniformLoggerProvider
import ru.otus.otuskotlin.lrvch.logging.jvm.catalogLoggerLogback

@Suppress("unused")
@Configuration
class CatalogConfig {
    @Bean
    fun processor(corSettings: CatalogCoreSettings) = CatalogProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): UniformLoggerProvider = UniformLoggerProvider { catalogLoggerLogback(it) }

    @Bean
    fun testRepo(): IRepoStorage = StorageRepoInMemory()

    @Bean
    fun prodRepo(): IRepoStorage = StorageRepoInMemory()

    @Bean
    fun corSettings(
        loggerProvider: UniformLoggerProvider,
        testRepo: IRepoStorage,
        prodRepo: IRepoStorage,
    ): CatalogCoreSettings = CatalogCoreSettings(
        loggerProvider = loggerProvider,
        repoTest = testRepo,
        repoProd = testRepo,
    )

    @Bean
    fun appSettings(
        corSettings: CatalogCoreSettings,
        processor: ICatalogProcessor,
    ) = CatalogAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
