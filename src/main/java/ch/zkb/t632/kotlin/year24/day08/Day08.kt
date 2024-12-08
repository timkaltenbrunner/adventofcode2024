package ch.zkb.t632.kotlin.year24.day08

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day08_test")
    check(part1(testInput), 14)

    val input = readInput("2024", "Day08")
    val resultPart1 = part1(input)

    println("Solution of Part1: $resultPart1")
    check(part2(testInput), 34)
    val resultPart2 = part2(input)

    println("Solution of Part2: $resultPart2")


}

private fun part1(input: List<String>): Int = solve(input.parseInputs(), ::findLocations)

private fun part2(input: List<String>): Int = solve(input.parseInputs(), ::findAllLocations)

private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
    operator fun minus(other: Pos): Pos = Pos(x - other.x, y - other.y)
}

private data class AMap(val sender: Map<Char, Set<Pos>>, val colRange: IntRange, val rowRange: IntRange)

private fun solve(parseInputs: AMap, locationFinder : (Set<Pos>, IntRange, IntRange) -> Set<Pos>): Int = parseInputs.sender.values.flatMap { locationFinder(it, parseInputs.colRange, parseInputs.rowRange) }.toSet().count()

private fun findLocations(currSender: Set<Pos>, colRange: IntRange, rowRange: IntRange): Set<Pos> {
    val localSender = ArrayDeque(currSender)
    var res = mutableSetOf<Pos>()
    while (localSender.isNotEmpty()) {
        val current = localSender.removeFirst()
        for (sender in localSender) {
            val dist = sender - current
            val posA = current - dist
            if (posA.y in colRange && posA.x in rowRange) {
                res += posA
            }
            val posB = sender + dist
            if (posB.y in colRange && posB.x in rowRange) {
                res += posB
            }
        }
    }
    return res
}

private fun findAllLocations(currSender: Set<Pos>, colRange: IntRange, rowRange: IntRange): Set<Pos> {
    val localSender = ArrayDeque(currSender)
    var res = mutableSetOf<Pos>()
    while (localSender.isNotEmpty()) {
        val current = localSender.removeFirst()
        for (sender in localSender) {
            var dist = sender - current
            var next = current
            while (next.y in colRange && next.x in rowRange) {
                res += next
                next = next - dist
            }
            next = current
            while (next.y in colRange && next.x in rowRange) {
                res += next
                next = next + dist
            }
        }
    }
    return res
}

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


