package ch.zkb.t632.kotlin.year24.day04

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput1 = readInput("2024", "Day04_test_part1")
    check(part1(testInput1), 161)

    val input = readInput("2024", "Day04")
    println(part1(input))
}

private fun part1(input: List<String>): Int = input.search()


private fun List<String>.search(): Int {
    return 0;
}