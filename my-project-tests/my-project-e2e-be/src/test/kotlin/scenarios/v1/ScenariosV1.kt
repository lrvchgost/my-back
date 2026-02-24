package ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v1

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import ru.otus.otuskotlin.lrvch.api.v1.models.StorageDebug
import ru.otus.otuskotlin.lrvch.e2e.be.base.client.Client

@Suppress("unused")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ScenariosV1(
    private val client: Client,
    private val debug: StorageDebug? = null
) {
    @Nested
    internal inner class CreateDeleteV1: ScenarioCreateDeleteV1(client, debug)
    @Nested
    internal inner class UpdateV1: ScenarioUpdateV1(client, debug)
    @Nested
    internal inner class ReadV1: ScenarioReadV1(client, debug)
    @Nested
    internal inner class SearchV1: ScenarioSearchV1(client, debug)
    @Nested
    internal inner class OffersV1: ScenarioOptimizeV1(client, debug)
}