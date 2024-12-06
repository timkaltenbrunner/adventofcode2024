package ch.zkb.t632.kotlin.year24.day06

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day06_test")
    val solutionTest = testInput.toMutable()
    check(part1(solutionTest, testInput.findStart()), 41)

    val input = readInput("2024", "Day06")
    val solutionPart1 = input.toMutable()

    val resultPart1 = part1(solutionPart1, input.findStart())
    solutionPart1.print()
    println("Solution of Part1: $resultPart1")

    check(part2(testInput, solutionTest, testInput.findStart()), 6)
    println("Solution of Part2: ${part2(input, solutionPart1, input.findStart())}")

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


private fun part1(input: List<MutableList<Char>>, start: Pair<Int, Int>): Int? = input.walkWithCycleDetection(start)

private fun pathPoints(input: List<MutableList<Char>>): List<Pair<Int, Int>> = buildList() {
    for (y in input.indices) {
        val row = input.get(y)
        for (x in row.indices) {
            if (row.get(x) == 'X')
                add(Pair(y, x))
        }
    }
}

private fun part2(input: List<String>, solution: List<MutableList<Char>>, start: Pair<Int, Int>): Int = pathPoints(solution).map {
    input.toMutable().setX(it, '#').walkWithCycleDetection(start)
}.count { it == null }


private fun List<MutableList<Char>>.walkWithCycleDetection(start: Pair<Int, Int>): Int? {
    setX(start, 'X')
    var steps = 0
    var nextPos = start
    var direction = Direction.UP
    var samePath = 0;
    do {
        val char = get2D(nextPos)
        when (char) {
            '#' -> {
                nextPos = nextPos.prev(direction)
                direction = direction.rotateRight()
            }

            'X' -> {
                nextPos = nextPos.next(direction)
                samePath++
            }

            else -> {
                samePath = 0
                setX(nextPos, 'X')
                nextPos = nextPos.next(direction)
                steps++
            }
        }
    } while (char != null && samePath < 1000)
    return if (samePath >= 1000) null else steps
}

private fun Pair<Int, Int>.next(direction: Direction): Pair<Int, Int> = when (direction) {
    Direction.UP -> Pair(first - 1, second)
    Direction.DOWN -> Pair(first + 1, second)
    Direction.LEFT -> Pair(first, second - 1)
    Direction.RIGHT -> Pair(first, second + 1)
}

private fun Pair<Int, Int>.prev(direction: Direction): Pair<Int, Int> = next(direction.rotateRight().rotateRight())

fun List<String>.toMutable(): List<MutableList<Char>> = toMutableList().map { it.toMutableList() }

fun List<String>.findStart(): Pair<Int, Int> {
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

fun List<MutableList<Char>>.setX(point: Pair<Int, Int>, c: Char): List<MutableList<Char>> {
    val (y, x) = point
    if (y in indices) {
        val row = get(y)
        if (x in row.indices) {
            row[x] = c
        }
    }
    return this;
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