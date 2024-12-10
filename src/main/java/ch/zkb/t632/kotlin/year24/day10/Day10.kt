package ch.zkb.t632.kotlin.year24.day10

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day10_test")
    check(part1(testInput), 81)

    val input = readInput("2024", "Day10")
    val resultPart1 = part1(input)

    println("Solution of Part1: $resultPart1")

}

private data class Position(val height: Int, var reachablePos1: Int)

private fun part1(input: List<String>): Long = input.parseInputs().solve()


private fun List<List<Position>>.solve(): Long {
    var ret = 0L
    for (cur in 9 downTo 0) {
        for ((y, row) in this.withIndex()) {
            for ((x, pos) in row.withIndex()) {
                if (pos.height == cur) {
                    updateReachablePoints(pos.reachablePos1, this, cur - 1, y, x)
                }
            }
        }
    }
    this.print()
    for (row in this) {
        for (pos in row) {
            if (pos.height == 0) {
                ret += pos.reachablePos1
            }
        }
    }
    return ret
}

private fun List<List<Position>>.print() {
    println()
    for (y in indices) {
        println()
        val row = get(y)
        for (x in row.indices) {
            print(row[x].reachablePos1)
        }
    }
    println()
}

private fun updateReachablePoints(reachablePos1: Int, map: List<List<Position>>, toValue: Int, y: Int, x: Int) {
    val pos1 = map.get2DValue(y, x - 1, toValue)
    if (pos1 != null) {
        pos1.reachablePos1 += reachablePos1
    }
    val pos2 = map.get2DValue(y, x + 1, toValue)
    if (pos2 != null) {
        pos2.reachablePos1 += reachablePos1
    }
    val pos3 = map.get2DValue(y - 1, x, toValue)
    if (pos3 != null) {
        pos3.reachablePos1 += reachablePos1
    }
    val pos4 = map.get2DValue(y + 1, x, toValue)
    if (pos4 != null) {
        pos4.reachablePos1 += reachablePos1
    }
}


private fun List<String>.parseInputs(): List<List<Position>> {
    val input = this
    return buildList {
        for (row in input) {
            add(buildList {
                for (ch in row) {
                    val height = ("" + ch).toInt()
                    val reachablePos1 = if (height == 9) 1 else 0
                    add(Position(height, reachablePos1))
                }
            })
        }
    }
}

private fun List<List<Position>>.get2DValue(y: Int, x: Int, toValue: Int): Position? {
    if (y in indices) {
        val row = get(y)
        if (x in row.indices) {
            val ret = row[x]
            return if (ret.height == toValue) row[x] else null
        }
    }
    return null
}


