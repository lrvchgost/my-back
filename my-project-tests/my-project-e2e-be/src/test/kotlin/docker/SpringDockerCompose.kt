package ru.otus.otuskotlin.lrvch.e2e.be.docker

import ru.otus.otuskotlin.lrvch.e2e.be.base.AbstractDockerCompose

object SpringDockerCompose : AbstractDockerCompose(
    "app-spring", 8080, "docker-compose-spring-pg.yml"
)
