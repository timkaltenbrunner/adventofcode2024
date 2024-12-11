package ch.zkb.t632.kotlin.year24.day11

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day11_test")
    check(part1(testInput, 25), 55312)

    val input = readInput("2024", "Day11")
    val resultPart1 = part1(input, 25)

    println("Solution of Part1: $resultPart1")

}

private fun part1(input: List<String>, blinkTimes: Int): Int = input.parseInputs().solve(blinkTimes)

private data class Position(val height: Int, var reachablePos9: Set<Pair<Int, Int>>, var reachablePos1: Int)


private fun List<Long>.solve(blinkTimes: Int): Int {
    var current = this
    repeat(blinkTimes) {
        // println(current)
        current = buildList {
            for (stone in current) {
                if (stone == 0L) {
                    add(1L)
                } else if (("" + stone).length % 2 == 0) {
                    val (s1, s2) = stone.split()
                    add(s1)
                    add(s2)
                } else {
                    add(stone * 2024)
                }
            }
        }
    }
    return current.count()
}

private fun Long.split(): Pair<Long, Long> {
    val stone = "" + this
    return stone.substring(0, stone.length / 2).toLong() to stone.substring(stone.length / 2, stone.length).toLong()
}


private fun List<String>.parseInputs(): List<Long> {
    val input = this
    return buildList {
        val row = input.first()
        for (num in row.split(" ")) {
            add(num.toLong())
        }
    }
}


