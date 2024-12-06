package ch.zkb.t632.kotlin.year24.day06

import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput1 = readInput("2024", "Day06_test")
    val solution_test = part1(testInput1.toMutable())

    val input = readInput("2024", "Day06")
    val solution = part1(input.toMutable())

}

enum class Direction() {
    UP {
        override fun rotateRight() = RIGHT
    },
    DOWN {
        override fun rotateRight() = LEFT
    },
    LEFT {
        override fun rotateRight() = UP
    },
    RIGHT {
        override fun rotateRight() = DOWN
    };

    abstract fun rotateRight(): Direction
}


private fun part1(input: List<MutableList<Char>>): List<MutableList<Char>> {
    println("Number of Steps: " + input.walk())
    input.print()
    return input
}

private fun List<MutableList<Char>>.walk(): Int {
    val pos = this.findStart()
    setX(pos, 'X')
    var steps = 0
    var nextPos = pos
    var direction = Direction.UP
    do {
        val char = get2D(nextPos)
        when (char) {
            '#' -> {
                nextPos = nextPos.prev(direction)
                direction = direction.rotateRight()
            }

            'X' -> {
                nextPos = nextPos.next(direction)
            }

            else -> {
                setX(nextPos, 'X')
                nextPos = nextPos.next(direction)
                steps++
            }
        }
    } while (char != null)
    return steps
}

private fun Pair<Int, Int>.next(direction: Direction): Pair<Int, Int> = when (direction) {
    Direction.UP -> Pair(first - 1, second)
    Direction.DOWN -> Pair(first + 1, second)
    Direction.LEFT -> Pair(first, second - 1)
    Direction.RIGHT -> Pair(first, second + 1)
}

private fun Pair<Int, Int>.prev(direction: Direction): Pair<Int, Int> = when (direction) {
    Direction.UP -> Pair(first + 1, second)
    Direction.DOWN -> Pair(first - 1, second)
    Direction.LEFT -> Pair(first, second + 1)
    Direction.RIGHT -> Pair(first, second - 1)
}

private fun List<MutableList<Char>>.print() {
    val firstRow = get(0)
    for (y in indices) {
        println()
        for (x in firstRow.indices) {
            print(get2D(Pair(y, x)))
        }
    }
    println()
}

fun List<String>.toMutable(): List<MutableList<Char>> = toMutableList().map { it.toMutableList() }


fun List<MutableList<Char>>.findStart(): Pair<Int, Int> {
    for (y in indices) {
        val row = get(y)
        for (x in row.indices) {
            if (row[x] == '^')
                return Pair(y, x)
        }
    }
    return Pair(-1, -1)
}

fun List<MutableList<Char>>.get2D(point: Pair<Int, Int>): Char? {
    val (y, x) = point
    if (y in indices) {
        val row = get(y)
        if (x in row.indices) {
            return row[x]
        }
    }
    return null
}

fun List<MutableList<Char>>.setX(point: Pair<Int, Int>, c: Char) {
    val (y, x) = point
    if (y in indices) {
        val row = get(y)
        if (x in row.indices) {
            row[x] = c
        }
    }
}