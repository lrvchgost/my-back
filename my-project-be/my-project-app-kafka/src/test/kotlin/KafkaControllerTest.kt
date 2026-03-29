package ru.otus.otuskotlin.lrvch.app.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.otuskotlin.lrvch.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.lrvch.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.lrvch.api.v1.mappers.toTransportCreateStorage
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateRequest
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageCreateResponse
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDebug
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageRequestDebugMode
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageRequestDebugStubs
import ru.otus.otuskotlin.lrvch.biz.ICatalogProcessor
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.models.Storage
import java.util.*
import kotlin.test.assertEquals


class KafkaControllerTest {
    val storage = Storage(
        title = "Обычный сторадж",
        description = "Обычное описаниe",
        capacity = "100"
    )

    val request = StorageCreateRequest(
        storage = storage.toTransportCreateStorage(),
        debug = StorageDebug(
            mode = StorageRequestDebugMode.STUB,
            stub = StorageRequestDebugStubs.SUCCESS,
        ),
    )

    val processor = object : ICatalogProcessor {
        override suspend fun exec(ctx: CatalogContext) {
            ctx.storageResponse = storage
        }
    }

    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig(processor = processor)
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        request
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<StorageCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("Обычный сторадж", result.storage?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}


