package ch.zkb.t632.kotlin.year24.day06

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput1 = readInput("2024", "Day06_test")
    val solution_test = part1(testInput1)

    val input = readInput("2024", "Day06")
    val solution = part1(input)

    check(part2(testInput1, solution_test), 6)
    println(part2(input, solution))


}

private fun part1(input: List<String>): List<String> {
    var localInput = input
    var next = localInput.findPosition()
    localInput = input.setX(next, 'X')
    println(next)
    print(localInput)
    var steps = 1
    do {
        next = Pair(next.first - 1, next.second)
        val nextChar = localInput.get2D(next.first, next.second)
        when (nextChar) {
            '#' -> {
                next = Pair(next.first + 1, next.second)
                next = Pair((input.get(0).length - 1) - next.second, next.first)
                localInput = localInput.turnRight()

            }

            'X' -> {}
            else -> {
                localInput = localInput.setX(next, 'X')
                steps++
            }
        }
    } while (nextChar != null)
    val fixRot = steps % 4
    repeat(fixRot) {
        localInput = localInput.turnRight()
    }
    return localInput
}

private fun part2(originalInput: List<String>, solution: List<String>): Int {
    var result = 0
    for (y in solution.indices) {
        val row = solution.get(y)
        for (x in row.indices) {
            if (row[x] == 'X') {
                val pair = Pair(y, x)
                if (part2Solver(originalInput.setX(pair, '#'))) {
                    println()
                    println("Pair: " + pair)
                    print(originalInput.setX(pair, 'O'))
                    result++
                }
            }
        }
    }
    return result
}

private fun part2Solver(input: List<String>): Boolean {
    var localInput = input
    var next = localInput.findPosition()
    localInput = input.setX(next, 'X')
    var steps = 1
    var samePath = 0
    do {
        next = Pair(next.first - 1, next.second)
        val nextChar = localInput.get2D(next.first, next.second)
        when (nextChar) {
            '#' -> {
                next = Pair(next.first + 1, next.second)
                localInput = localInput.setX(next, 'X')
                next = Pair((input.get(0).length - 1) - next.second, next.first)
                localInput = localInput.turnRight()

            }

            'X' -> samePath++
            else -> {
                samePath = 0
                localInput = localInput.setX(next, 'X')
                steps++
            }
        }
    } while (nextChar != null && samePath < 100)
    return samePath >= 100
}

private fun print(localInput: List<String>) {
    val firstRow = localInput.get(0)
    for (y in localInput.indices) {
        println()
        for (x in firstRow.indices) {
            print(localInput.get2D(y, x))
        }
    }
}

fun List<String>.findPosition(): Pair<Int, Int> {
    for (y in indices) {
        val row = get(y)
        for (x in row.indices) {
            if (row[x] == '^')
                return Pair(y, x)
        }
    }
    return Pair(-1, -1)
}

fun List<String>.get2D(y: Int, x: Int): Char? {
    if (y in indices) {
        val row = get(y)
        if (x in row.indices) {
            return row[x]
        }
    }
    return null
}

fun List<String>.turnRight(): List<String> {
    val firstRow = get(0)
    val list = mutableListOf<String>()

    for (y in indices.reversed()) {
        var line = ""
        for (x in firstRow.indices) {
            line += get2D(x, y)
        }
        list.add(line)
    }
    return list;
}


fun List<String>.setX(point: Pair<Int, Int>, c: Char): List<String> {
    val firstRow = get(0)
    val list = mutableListOf<String>()
    for (y in indices) {
        var line = ""
        for (x in firstRow.indices) {
            if (y == point.first && x == point.second) {
                line += c
            } else {
                line += get2D(y, x)
            }

        }
        list.add(line)
    }
    return list;
}