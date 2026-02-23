package ru.otus.otuskotlin.lrvch.e2e.be.docker

import ru.otus.otuskotlin.lrvch.e2e.be.base.AbstractDockerCompose

object KafkaDockerCompose : AbstractDockerCompose(
    "kafka_1", 9091, "docker-compose-kafka.yml"
)
