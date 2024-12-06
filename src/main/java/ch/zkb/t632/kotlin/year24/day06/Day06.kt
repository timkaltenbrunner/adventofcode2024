package ch.zkb.t632.kotlin.year24.day06

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput1 = readInput("2024", "Day06_test")
    check(part1(testInput1), 41)

    val input = readInput("2024", "Day06")
    println(part1(input))
}

private fun part1(input: List<String>): Int {
    var localInput = input
    var next = localInput.findPosition()
    localInput = input.setX(next)
    println(next)
    print(localInput)

    var steps = 1
    do {
        next = Pair(next.first - 1, next.second)
        val nextChar = localInput.get2D(next.first, next.second)
        when (nextChar) {
            '#' -> {
                next = Pair(next.first + 1, next.second)
                println("Before: " + next)
                next = Pair((input.get(0).length - 1) - next.second, next.first)
                println("After: " + next)
                println("Steps: " + steps)
                localInput = localInput.turnRight()
                print(localInput)
            }

            'X' -> {}
            else -> {
                localInput = localInput.setX(next)
                steps++
            }
        }
    } while (nextChar != null)
    return steps - 1
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

fun List<String>.setX(point: Pair<Int, Int>): List<String> {
    val firstRow = get(0)
    val list = mutableListOf<String>()
    for (y in indices) {
        var line = ""
        for (x in firstRow.indices) {
            if (y == point.first && x == point.second) {
                line += "X"
            } else {
                line += get2D(y, x)
            }

        }
        list.add(line)
    }
    return list;
}