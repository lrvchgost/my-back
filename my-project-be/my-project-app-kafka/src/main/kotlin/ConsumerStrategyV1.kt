package ru.otus.otuskotlin.lrvch.app.kafka

import ru.otus.otuskotlin.lrvch.api.v1.apiV1RequestDeserialize
import ru.otus.otuskotlin.lrvch.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.lrvch.api.v1.mappers.fromTransport
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportStorage
import ru.otus.otuskotlin.lrvch.api.v1.models.IRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.IResponse
import ru.otus.otuskotlin.lrvch.common.CatalogContext

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun toTransport(source: CatalogContext): String {
        val response: IResponse = source.toTransportStorage()
        return apiV1ResponseSerialize(response)
    }

    override fun fromTransport(value: String, target: CatalogContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}
