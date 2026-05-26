package ru.otus.otuskotlin.lrvch.backend.repo.postgresql

import IRepoStorageInitializable
import RepoStorageInitialized
import com.benasher44.uuid.uuid4
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageCreateTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageDeleteTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageReadTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageSearchByIdsTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageSearchTest
import ru.otus.otuskotlin.lrvch.backend.repo.tests.RepoStorageUpdateTest
import ru.otus.otuskotlin.lrvch.common.models.Storage
import java.io.File
import java.time.Duration
import kotlin.test.AfterTest
import kotlin.test.Ignore


private fun IRepoStorageInitializable.clear() {
    val pgRepo = (this as RepoStorageInitialized).repo as RepoStorageSql
    pgRepo.clear()
}

@RunWith(Enclosed::class)
class RepoStorageSQLTest {

    class RepoStorageSQLCreateTest : RepoStorageCreateTest() {
        override val repo = repoUnderTestContainer(
            initObjects,
            randomUuid = { uuidNew.asString() },
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoStorageSQLReadTest : RepoStorageReadTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoStorageSQLUpdateTest : RepoStorageUpdateTest() {
        override val repo = repoUnderTestContainer(
            initObjects,
            randomUuid = { lockNew.asString() },
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoStorageSQLDeleteTest : RepoStorageDeleteTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoStorageSQLSearchTest : RepoStorageSearchTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoStorageSQLSearchByIdsTest : RepoStorageSearchByIdsTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    @Ignore
    companion object {
        private const val PG_SERVICE = "psql"
        private const val MG_SERVICE = "liquibase"

        // val LOGGER = org.slf4j.LoggerFactory.getLogger(ComposeContainer::class.java)
        private val container: ComposeContainer by lazy {
            val res = this::class.java.classLoader.getResource("docker-compose-pg.yml")
                ?: throw Exception("No resource found")
            val file = File(res.toURI())
            //  val logConsumer = Slf4jLogConsumer(LOGGER)
            ComposeContainer(
                file,
            )
                .withExposedService(PG_SERVICE, 5432)
                .withStartupTimeout(Duration.ofSeconds(300))
//                .withLogConsumer(MG_SERVICE, logConsumer)
//                .withLogConsumer(PG_SERVICE, logConsumer)
                .waitingFor(
                    MG_SERVICE,
                    Wait.forLogMessage(".*Liquibase command 'update' was executed successfully.*", 1)
                )
        }

        private const val HOST = "localhost"
        private const val USER = "postgres"
        private const val PASS = "catalog-of-storages-pass"
        private val PORT by lazy {
            container.getServicePort(PG_SERVICE, 5432) ?: 5432
        }

        fun repoUnderTestContainer(
            initObjects: Collection<Storage> = emptyList(),
            randomUuid: () -> String = { uuid4().toString() },
        ): IRepoStorageInitializable = RepoStorageInitialized(
            repo = RepoStorageSql(
                SqlProperties(
                    host = HOST,
                    user = USER,
                    password = PASS,
                    port = PORT,
                ),
                randomUuid = randomUuid
            ),
            initObjects = initObjects,
        )

        @JvmStatic
        @BeforeClass
        fun start() {
            container.start()
        }

        @JvmStatic
        @AfterClass
        fun finish() {
            container.stop()
        }
    }
}

