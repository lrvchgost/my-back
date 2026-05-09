import ru.otus.otuskotlin.lrvch.common.models.Storage
import ru.otus.otuskotlin.lrvch.common.repo.IRepoStorage

interface IRepoStorageInitializable: IRepoStorage {
    fun save(storages: Collection<Storage>) : Collection<Storage>
}
