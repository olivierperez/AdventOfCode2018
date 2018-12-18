package fr.o80.day15

import java.lang.IllegalStateException

class PathResolver(private val board: Board) {

    fun nextStep(from: Point, to: Point, max: Point): Point {
        lateinit var end: Node
        val nodesList = (Point(0, 0)..max).asSequence()
                .mapNotNull { p ->
                    when {
                        p == from              -> Node(p.x, p.y, NodeType.START)
                        p == to                -> Node(p.x, p.y, NodeType.END).also {end = it}
                        board.isFree(p.x, p.y) -> Node(p.x, p.y, NodeType.EMPTY)
                        else                   -> null
                    }
                }
                .toMutableList()

        val nodes = mutableListOf<Node>().apply { addAll(nodesList) }

        while (nodesList.isNotEmpty()) {
            val current = nodesList.minBy { node -> node.dist }!!
            nodesList.remove(current)

            for (neighbor in current neighborsIn nodes) {
                val alt = current.dist + current.dist(neighbor)
                if (alt < neighbor.dist) {
                    neighbor.dist = alt
                    neighbor.prev = current
                }
            }
        }

        end.prev?.let { prev ->
            return Point(prev.x, prev.y)
        } ?: throw IllegalStateException("Failed to compute distance to the end")
    }

    private infix fun PathResolver.Node.neighborsIn(nodes: List<Node>): List<Node> {
        return nodes.filter { n ->
            (n.y == y && (n.x - x == 1 || n.x - x == -1))
            || (n.x == x && (n.y - y == 1 || n.y - y == -1))
        }
    }

    class Node(
            val x: Int,
            val y: Int,
            var type: NodeType,
            var prev: Node? = null,
            var dist: Int = if (type == NodeType.START) 0 else Int.MAX_VALUE
    ) {

        fun dist(other: Node): Int =
            if (x == other.x || y == other.y) 1 else Int.MAX_VALUE
    }

    enum class NodeType { START, EMPTY, END }
}

class PointIterator(
        start: Point,
        private val endInclusive: Point
) : Iterator<Point> {
    private var current = start

    override fun hasNext(): Boolean = current.x < endInclusive.x || current.y < endInclusive.y

    override fun next(): Point {
        return current.also {
            current = if (current.x < endInclusive.x) {
                Point(current.x + 1, current.y)
            } else {
                Point(1, current.y + 1)
            }
        }
    }

}