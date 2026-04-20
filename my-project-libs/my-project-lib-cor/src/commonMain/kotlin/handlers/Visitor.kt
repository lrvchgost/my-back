package ru.otus.otuskotlin.lrvch.libs.cor.handlers

import ru.otus.otuskotlin.lrvch.libs.cor.ICorExec
import kotlin.time.measureTime

class ExecVisitor<T> : IVisitor<T> {
    override suspend fun visitForWorker(node: CorWorker<T>, context: T) {
        node.exec(context)
    }

    override suspend fun visitForChain(node: CorChain<T>, context: T) {
        node.execs.forEach { it.visit(this, context) }
    }
}

class ExecVisitorLogNodeExecDuration<T> : IVisitor<T> {
    override suspend fun visitForWorker(node: CorWorker<T>, context: T) {
        val duration = measureTime {
            node.exec(context)
        }

        println("Executed node ${node.title} $duration")
    }

    override suspend fun visitForChain(node: CorChain<T>, context: T) {
        val duration = measureTime {
            node.execs.forEach { it.visit(this, context) }
        }

        println("Executed node ${node.title} $duration")
    }
}

interface INode<T> {
    val children: MutableList<INode<T>>
    val task: T
    val id: Int
}

data class Node<T>(
    override val task: T
) : INode<T> {
    override val children = mutableListOf<INode<T>>()
    override val id = idCount++

    companion object {
        var idCount = 0;
    }
}

data class Tree<T>(val name: String) {
    var root: INode<out ICorExec<T>>? = null;
    val stack = mutableListOf<INode<CorChain<T>>>()

    fun push(node: INode<CorChain<T>>) {
        stack.add(node)
    }

    fun link(node: INode<out ICorExec<T>>) {
        val parent = stack[stack.size - 1]
        parent.children.add(node as INode<CorChain<T>>)
    }

    fun pop() {
        stack.removeLast()
    }
}

class CopyTreeAndPrintTasksGraphVisitor<T> : IVisitor<T> {
    val tree = Tree<T>("my")

    override suspend fun visitForWorker(node: CorWorker<T>, context: T) {
        val child = Node(node)

        tree.link(child)
    }

    override suspend fun visitForChain(node: CorChain<T>, context: T) {
        val nodeElement = Node(node)

        if (tree.root == null) {
            tree.root = nodeElement
        } else {
            tree.link(nodeElement)
        }

        tree.push(nodeElement)

        node.execs.forEach {
            it.visit(this, context)
        }

        tree.pop()
    }

    fun getGraph(): String {
        println("tasks tree")
        println("root: id ${tree.root?.id} children size ${tree.root?.children?.size}")
        println("child: ${tree.root?.children?.get(1)?.children?.size}")

        walk(tree.root, 0)

        println("==========================")

        println("tasks order")
        taskOrder(tree.root, 0)

        return "=========================="
    }

    fun walk(node: INode<out ICorExec<T>>?, level: Int) {
        println("    ".repeat(level) + node?.task?.title + " id ${node?.id} children size ${tree.root?.children?.size}")

        node?.children?.forEach {
            walk(it, level + 1)
        }
    }

    fun taskOrder(node: INode<out ICorExec<T>>?, level: Int) {
        node?.children?.forEach {
            taskOrder(it, level + 1)
        }
        println(node?.task?.title + " id ${node?.id} children size ${tree.root?.children?.size}")
    }
}

class PrintTasksOrderVisitor<T> : IVisitor<T> {
    val rows = mutableListOf<String>()
    var level = 0;

    override suspend fun visitForWorker(node: CorWorker<T>, context: T) {}

    override suspend fun visitForChain(node: CorChain<T>, context: T) {
        if (level == 0) {
            rows.add("\n[node]: ${node.title}")
        }

        level += 1;
        node.execs.forEach {
            rows.add("      ".repeat(level) + " child: ${it.title}")
            it.visit(this, context)
        }
        level -= 1;
    }

    fun getTasks(): String {
        return "tasks :\n ${rows.joinToString("\n")}"
    }
}
