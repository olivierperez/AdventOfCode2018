package fr.o80.week3

import java.util.*

class SortedList<T>(elements: Array<out T>, private val comparator: Comparator<T>) : SortedMutableList<T> {

    private val inner = LinkedList<T>()

    init {
        elements.forEach { add(it) }
    }

    override val size: Int
        get() = inner.size

    override fun add(element: T) {
        if (size == 0) {
            inner.add(element)
        } else {
            var min = 0
            var max = size - 1

            while (min < max) {
                val position = min + ((max - min) / 2)
                val middleElement = inner[position]

                val comparison = comparator.compare(element, middleElement)
                when {
                    comparison == 0 -> {
                        inner.add(position, element)
                        return
                    }
                    comparison < 0 -> max = position - 1
                    else -> min = position + 1
                }
            }

            if (min >= inner.size) {
                inner.add(element)
            } else {
                val lastCompared = inner[min]
                val comparison = comparator.compare(element, lastCompared)
                if (comparison >= 0) {
                    inner.add(min + 1, element)
                } else {
                    inner.add(min, element)
                }
            }
        }
    }

    override fun remove(element: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(index: Int): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun contains(element: T): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun iterator(): Iterator<T> =
            inner.iterator()

    override fun toString(): String {
        return inner.toString()
    }

}

interface SortedMutableList<T> : Iterable<T> {
    val size: Int
    fun add(element: T)
    fun remove(element: T)
    operator fun get(index: Int): T
    operator fun contains(element: T): Boolean
}

fun <T : Comparable<T>> sortedMutableListOf(vararg elements: T): SortedMutableList<T> = SortedList(elements, compareBy { it })

fun <T> sortedMutableListOf(comparator: Comparator<T>, vararg elements: T): SortedMutableList<T> = SortedList(elements, comparator)