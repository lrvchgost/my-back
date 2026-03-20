package ru.otus.otuskotlin.lrvch.app.kafka

import ru.otus.otuskotlin.lrvch.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.lrvch.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.lrvch.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.lrvch.api.v2.mappers.toTransportStorage
import ru.otus.otuskotlin.lrvch.api.v2.models.IRequest
import ru.otus.otuskotlin.lrvch.api.v2.models.IResponse
import ru.otus.otuskotlin.lrvch.common.CatalogContext

class ConsumerStrategyV2 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

    override fun toTransport(source: CatalogContext): String {
        val response: IResponse = source.toTransportStorage()
        return apiV2ResponseSerialize(response)
    }

    override fun fromTransport(value: String, target: CatalogContext) {
        val request: IRequest = apiV2RequestDeserialize(value)
        target.fromTransport(request)
    }
}
