package ch.zkb.t632.kotlin.year24.day11

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day11_test")
    check(parts(testInput, 25), 55312)

    val input = readInput("2024", "Day11")
    val resultPart1 = parts(input, 25)

    println("Solution of Part1: $resultPart1")

    val resultPart2 = parts(input, 75)

    println("Solution of Part2: $resultPart2")

}

private fun parts(input: List<String>, blinkTimes: Int): Long = input.parseInputs().solve(blinkTimes)

private fun List<Long>.solve(blinkTimes: Int): Long {
    var current: Map<Long, Long> = this.associateWith { 1 }
    repeat(blinkTimes) {
        current = buildMap {
            for ((stone, count) in current) {
                if (stone == 0L) {
                    putSum(1L, count)
                } else if (("" + stone).length % 2 == 0) {
                    val (s1, s2) = stone.split()
                    putSum(s1, count)
                    putSum(s2, count)
                } else {
                    val s1 = stone * 2024
                    putSum(s1, count)
                }
            }
        }
    }
    return current.values.sum()
}

private fun MutableMap<Long, Long>.putSum(s1: Long, count: Long) {
    put(s1, getOrDefault(s1, 0) + count)
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


