package ch.zkb.t632.kotlin.year24.day08

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day08_test")
    check(part1(testInput), 14)

    val input = readInput("2024", "Day08")
    val resultPart1 = part1(input)

    println("Solution of Part1: $resultPart1")
    /*
        check(part2(testInput), 11387)
        val resultPart2 = part2(input)

        println("Solution of Part2: $resultPart2")

     */

}

private fun part1(input: List<String>): Int = solve(input.parseInputs())

private fun solve(parseInputs: AMap): Int {
    var res = mutableSetOf<Pos>()
    for (currSender in parseInputs.sender.values) {
        res += findLocations(currSender).filter { (a, b) -> a in parseInputs.rowRange && b in parseInputs.rowRange }
    }
    return res.count()
}

private fun findLocations(currSender: Set<Pos>): Set<Pos> {
    val localSender = ArrayDeque(currSender)
    var res = mutableSetOf<Pos>()
    while (localSender.isNotEmpty()) {
        val current = localSender.removeFirst()
        for (sender in localSender) {
            var dist = sender - current
            res += current - dist
            res += sender + dist
        }
    }
    return res
}

private fun part2(input: List<String>): Long = 0


private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
    operator fun minus(other: Pos): Pos = Pos(x - other.x, y - other.y)
}

private data class AMap(val sender: Map<Char, Set<Pos>>, val colRange: IntRange, val rowRange: IntRange)

private fun List<String>.parseInputs(): AMap {
    val map: MutableMap<Char, MutableSet<Pos>> = mutableMapOf()
    for ((y, row) in withIndex()) {
        for ((x, ch) in row.withIndex()) {
            if ('.' != ch) {
                map.getOrPut(ch) { mutableSetOf() }.add(Pos(x, y))
            }
        }
    }
    return AMap(map, indices, first().indices)
}


