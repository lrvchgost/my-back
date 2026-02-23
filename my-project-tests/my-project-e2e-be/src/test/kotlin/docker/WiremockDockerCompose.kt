package ru.otus.otuskotlin.lrvch.e2e.be.docker

import ru.otus.otuskotlin.lrvch.e2e.be.base.AbstractDockerCompose

object WiremockDockerCompose : AbstractDockerCompose(
    "app-wiremock", 8080, "docker-compose-wiremock.yml"
)
