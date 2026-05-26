import ru.otus.otuskotlin.lrvch.common.models.Storage

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class RepoStorageInitialized(
    val repo: IRepoStorageInitializable,
    initObjects: Collection<Storage> = emptyList(),
) : IRepoStorageInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<Storage> = save(initObjects).toList()
}
