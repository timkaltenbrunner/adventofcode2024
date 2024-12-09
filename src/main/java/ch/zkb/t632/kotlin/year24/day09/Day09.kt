package ch.zkb.t632.kotlin.year24.day09

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day09_test")
    check(part1(testInput), 1928)

    val input = readInput("2024", "Day09")
    val resultPart1 = part1(input)

    println("Solution of Part1: $resultPart1")


    /*
    check(part2(testInput), 34)
    val resultPart2 = part2(input)

    println("Solution of Part2: $resultPart2")
     */
}

private fun part1(input: List<String>): Long = solve(parseInputs(input.first()))

private fun part2(input: List<String>): Long = 0

private fun solve(parsedInputs: List<Long>): Long =
    multiply(compact(parsedInputs).print()).print().map { it.toLong() }.sum()

private fun List<Long>.print(): List<Long> {
    println(this)
    return this
}


private fun compact(input: List<Long>): List<Long> = buildList {
    var revIndex = input.size - 1
    for ((index, value) in input.withIndex()) {
        if (value == -1L) {
            var revVal = input[revIndex--]
            while (revVal == -1L) {
                if (index >= revIndex) return@buildList
                revVal = input[revIndex--]
            }
            add(revVal)
        } else {
            add(value)
        }
        if (index >= revIndex) break
    }
}

private fun multiply(input: List<Long>): List<Long> = buildList {
    for ((index, value) in input.withIndex()) add(value * index)
}


private fun parseInputs(input: String): List<Long> = buildList {
    var currNumber = 0L
    for ((y, ch) in input.withIndex()) {
        if (y % 2 == 1) addAll(addNumTimes(-1, ("" + ch).toInt()))
        else addAll(addNumTimes(currNumber++, ("" + ch).toInt()))
    }
}

fun addNumTimes(num: Long, times: Int): List<Long> = buildList {
    for (j in 1..times) {
        add(num)
    }
}


