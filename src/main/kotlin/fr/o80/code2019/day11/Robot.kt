package fr.o80.code2019.day11

import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min

typealias Color = Int

data class Point(val x: Int, val y: Int)

private const val BLACK: Color = 0
private const val WHITE: Color = 1
private const val LEFT = 0
private const val RIGHT = 1

class Robot(input: String) {

    private val map = mutableMapOf<Point, Color>()

    private val parsed = parseInput(input)

    private val intcode = Intcode(this::provideInt, this::handleOutput)

    private var currentPosition = Point(0, 0)
    private var currentDirection = Direction.UP

    private var readingColor: ReadingMod = ReadingMod.COLOR
    private var topLeft = currentPosition
    private var bottomRight = currentPosition

    fun parseInput(s: String): MutableList<BigInteger> =
        s.split(",")
            .map(String::toBigInteger)
            .toMutableList()

    private fun provideInt(): Int {
        //Thread.sleep(100L)
        val color = map.getOrDefault(currentPosition, BLACK)
        //println("----")
        //println("Providing color: $color")
        return color
    }

    private fun handleOutput(value: BigInteger) {
        when (readingColor) {
            ReadingMod.COLOR -> {
                //println("color: $value")
                paint(value.toInt())
                readingColor = ReadingMod.MOVE
            }
            ReadingMod.MOVE -> {
                //println("move: $value")
                move(value.toInt())
                readingColor = ReadingMod.COLOR
            }
        }
    }

    private fun paint(value: Int) {
        map[currentPosition] = value
    }

    private fun move(value: Int) {
        when (value) {
            LEFT -> {
                currentDirection = currentDirection.turnLeft()
                currentPosition = currentDirection(currentPosition)
            }
            RIGHT -> {
                currentDirection = currentDirection.turnRight()
                currentPosition = currentDirection(currentPosition)
            }
        }
        //println("currentDirection: $currentDirection")
        //println("currentPosition: $currentPosition")

        topLeft = Point(min(currentPosition.x, topLeft.x), min(currentPosition.y, topLeft.y))
        bottomRight = Point(max(currentPosition.x, bottomRight.x), max(currentPosition.y, bottomRight.y))
    }

    fun compute(): Int {
        map[currentPosition] = BLACK
        intcode.compute(parsed)
        return map.size
    }

    fun draw() {
        map[currentPosition] = WHITE
        intcode.compute(parsed)

        for (y in topLeft.y..bottomRight.y) {
            for (x in topLeft.x..bottomRight.x) {
                if (map.getOrDefault(Point(x, y), BLACK) == WHITE) {
                    print("#")
                } else {
                    print(" ")
                }
            }
            println("")
        }
    }
}

enum class ReadingMod {
    COLOR,
    MOVE
}

enum class Direction {
    UP {
        override fun turnLeft(): Direction = LEFT
        override fun turnRight(): Direction = RIGHT
        override fun invoke(currentPosition: Point): Point =
            Point(currentPosition.x, currentPosition.y - 1)
    },
    RIGHT {
        override fun turnLeft(): Direction = UP
        override fun turnRight(): Direction = DOWN
        override fun invoke(currentPosition: Point): Point =
            Point(currentPosition.x + 1, currentPosition.y)
    },
    DOWN {
        override fun turnLeft(): Direction = RIGHT
        override fun turnRight(): Direction = LEFT
        override fun invoke(currentPosition: Point): Point =
            Point(currentPosition.x, currentPosition.y + 1)
    },
    LEFT {
        override fun turnLeft(): Direction = DOWN
        override fun turnRight(): Direction = UP
        override fun invoke(currentPosition: Point): Point =
            Point(currentPosition.x - 1, currentPosition.y)
    };

    abstract fun turnLeft(): Direction
    abstract fun turnRight(): Direction
    abstract operator fun invoke(currentPosition: Point): Point
}