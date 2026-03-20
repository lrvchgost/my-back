package ru.otus.otuskotlin.lrvch.app.kafka

import ru.otus.otuskotlin.lrvch.common.CatalogContext

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun toTransport(source: CatalogContext): String
    /**
     * Десериализатор для версии API
     */
    fun fromTransport(value: String, target: CatalogContext)
}
