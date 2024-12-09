package ch.zkb.t632.kotlin.year24.day10

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day10_test")
    check(part1(testInput), 2858)

    val input = readInput("2024", "Day10")
    val resultPart1 = part1(input)

    println("Solution of Part1: $resultPart1")

}

private fun part1(input: List<String>): Long = solve(parseInputs(input.first()))


private fun solve(parsedInputs: List<Long>): Long = parsedInputs.sum()


private fun parseInputs(input: String): List<Long> = buildList {
    var currNumber = 0L
    for ((y, ch) in input.withIndex()) {
        add(("" + ch).toLong())
    }
}


