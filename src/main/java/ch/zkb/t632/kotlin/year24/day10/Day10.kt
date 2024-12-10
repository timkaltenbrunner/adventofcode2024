package ch.zkb.t632.kotlin.year24.day10

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day10_test")
    val testSol = part(testInput)
    check(testSol.first, 36)

    val input = readInput("2024", "Day10")
    val result = part(input)

    println("Solution of Part1: ${result.first}")

    check(testSol.second, 81)
    println("Solution of Part2: ${result.second}")

}

private data class Position(val height: Int, var reachablePos9: Set<Pair<Int, Int>>, var reachablePos1: Int)

private fun part(input: List<String>): Pair<Long, Long> = input.parseInputs().solve()


private fun List<List<Position>>.solve(): Pair<Long, Long> {
    var solution1 = 0L
    var solution2 = 0L
    for (cur in 9 downTo 0) {
        for ((y, row) in this.withIndex()) {
            for ((x, pos) in row.withIndex()) {
                if (pos.height == cur) {
                    updateReachablePoints(pos, this, cur - 1, y, x)
                }
            }
        }
    }

    for (row in this) {
        for (pos in row) {
            if (pos.height == 0) {
                solution1 += pos.reachablePos9.size
                solution2 += pos.reachablePos1
            }
        }
    }
    return solution1 to solution2
}

private fun updateReachablePoints(
    pos: Position,
    map: List<List<Position>>,
    toValue: Int,
    y: Int,
    x: Int
) {
    val pos1 = map.get2DValue(y, x - 1, toValue)
    if (pos1 != null) {
        pos1.reachablePos9 += pos.reachablePos9
        pos1.reachablePos1 += pos.reachablePos1
    }
    val pos2 = map.get2DValue(y, x + 1, toValue)
    if (pos2 != null) {
        pos2.reachablePos9 += pos.reachablePos9
        pos2.reachablePos1 += pos.reachablePos1
    }
    val pos3 = map.get2DValue(y - 1, x, toValue)
    if (pos3 != null) {
        pos3.reachablePos9 += pos.reachablePos9
        pos3.reachablePos1 += pos.reachablePos1
    }
    val pos4 = map.get2DValue(y + 1, x, toValue)
    if (pos4 != null) {
        pos4.reachablePos9 += pos.reachablePos9
        pos4.reachablePos1 += pos.reachablePos1
    }
}


private fun List<String>.parseInputs(): List<List<Position>> {
    val input = this
    return buildList {
        for ((y, row) in input.withIndex()) {
            add(buildList {
                for ((x, ch) in row.withIndex()) {
                    val height = ("" + ch).toInt()
                    val reachablePos9 = if (height == 9) setOf(Pair(y, x)) else setOf()
                    val reachablePos1 = if (height == 9) 1 else 0
                    add(Position(height, reachablePos9, reachablePos1))
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


