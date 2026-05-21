package ru.otus.otuskotlin.lrvch.e2e.be

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageRequestDebugMode as StorageRequestDebugModeV2
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageRequestDebugMode as StorageRequestDebugModeV1
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDebug as StorageDebugV2
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDebug as StorageDebugV1
import ru.otus.otuskotlin.lrvch.e2e.be.base.BaseContainerTest
import ru.otus.otuskotlin.lrvch.e2e.be.base.client.Client
import ru.otus.otuskotlin.lrvch.e2e.be.base.client.RestClient
import ru.otus.otuskotlin.lrvch.e2e.be.docker.SpringDockerCompose
import ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1.ScenariosV1
import ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v2.ScenariosV2

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestPgJvm: BaseContainerTest(SpringDockerCompose) {
    private val client: Client = RestClient(compose)
    @Test
    fun info() {
        println("${this::class.simpleName}")
    }

    @Nested
    internal inner class V1: ScenariosV1(client, StorageDebugV1(mode = StorageRequestDebugModeV1.PROD))
    @Nested
    internal inner class V2: ScenariosV2(client, StorageDebugV2(mode = StorageRequestDebugModeV2.PROD))
}
