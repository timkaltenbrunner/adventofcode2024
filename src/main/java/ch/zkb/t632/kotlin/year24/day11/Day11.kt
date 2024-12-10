package ch.zkb.t632.kotlin.year24.day11

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day11_test")
    check(part1(testInput), 2858)

    val input = readInput("2024", "Day11")
    val resultPart1 = part1(input)

    println("Solution of Part1: $resultPart1")

}

private fun part1(input: List<String>): Long = input.parseInputs().solve()

private data class Position(val height: Int, var reachablePos9: Set<Pair<Int, Int>>, var reachablePos1: Int)


private fun List<List<Position>>.solve(): Long {
    var solution = 0L
    for (cur in 9 downTo 0) {
        for ((y, row) in this.withIndex()) {
            for ((x, pos) in row.withIndex()) {
                if (pos.height == cur) {
                    solution++
                }
            }
        }
    }
    return solution
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


