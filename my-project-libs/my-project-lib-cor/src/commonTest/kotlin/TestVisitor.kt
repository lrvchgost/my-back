import ru.otus.otuskotlin.lrvch.libs.cor.handlers.CorChain
import ru.otus.otuskotlin.lrvch.libs.cor.handlers.CorWorker
import ru.otus.otuskotlin.lrvch.libs.cor.handlers.IVisitor

class TestVisitor<T>: IVisitor<T> {
    override suspend fun visitForWorker(node: CorWorker<T>, context: T) {
        node.exec(context)
    }

    override suspend fun visitForChain(node: CorChain<T>, context: T) {
        node.execs.forEach { it.exec(context)  }
    }
}
