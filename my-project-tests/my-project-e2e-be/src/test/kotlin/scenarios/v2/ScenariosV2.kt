package ru.otus.otuskotlin.lrvch.e2e.be.scenarios.v2

import org.junit.jupiter.api.Nested
import ru.otus.otuskotlin.lrvch.api.v2.models.StorageDebug
import ru.otus.otuskotlin.lrvch.e2e.be.base.client.Client

@Suppress("unused")
abstract class ScenariosV2(
    private val client: Client,
    private val debug: StorageDebug? = null,
) {
    @Nested
    internal inner class CreateDeleteV2: ScenarioCreateDeleteV2(client, debug)
    @Nested
    internal inner class UpdateV2: ScenarioUpdateV2(client, debug)
    @Nested
    internal inner class ReadV2: ScenarioReadV2(client, debug)
    @Nested
    internal inner class SearchV2: ScenarioSearchV2(client, debug)
    @Nested
    internal inner class OffersV2: ScenarioOptimizeV2(client, debug)
}