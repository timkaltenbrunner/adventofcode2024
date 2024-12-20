package ch.zkb.t632.kotlin.year24.day20_short

import kotlin.math.abs
import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.parallelMap
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day20_test")
    check(solve(testInput, 1), 44)

    val input = readInput("2024", "Day20")
    println("Solution of Part1: ${solve(input, 100)}")

    check(solve(testInput, 50, 20), 285)
    println("Solution of Part2: ${solve(input, 100, 20)}")
}

private fun solve(input: List<String>, minSaved: Int, cheatTime: Int = 2): Int = input.parseInputs().solve(minSaved, cheatTime)

private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
    fun neighbourSteps(): Set<Pos> = setOf(Pos(1, 0), Pos(-1, 0), Pos(0, 1), Pos(0, -1))

    fun findPathsInCheatTime(cheatTime: Int, remainingPath: List<Pos>, minSaved: Int): Int = remainingPath.mapIndexed { index, pos ->
        val distance = abs(this.x - pos.x) + abs(this.y - pos.y)
        val savedTime = index + 1
        if (distance <= cheatTime && distance + minSaved <= savedTime) 1 else 0
    }.sum()
}

private data class Map(var start: Pos, var end: Pos, val walls: Set<Pos>) {
    fun solve(minSaved: Int, cheatTime: Int): Int {
        val path = findPath()
        return path.parallelMap {
            it.findPathsInCheatTime(cheatTime, path.drop(path.indexOf(it) + 1), minSaved)
        }.sum()
    }

    private fun findPath(): List<Pos> = buildList {
        add(start)
        while (last() != end) {
            for (ns in last().neighbourSteps()) {
                val next = last() + ns
                val lastIndex = size - 2
                if (next !in walls && (lastIndex < 0 || next != get(lastIndex))) {
                    add(next)
                }
            }
        }
    }
}

private fun List<String>.parseInputs(): Map {
    var start = Pos(0, 0)
    var end = Pos(0, 0)
    val walls = mutableSetOf<Pos>()
    for ((y, row) in withIndex()) {
        for ((x, ch) in row.withIndex()) {
            when (ch) {
                'S' -> start = Pos(x, y)
                'E' -> end = Pos(x, y)
                '#' -> walls.add(Pos(x, y))
            }
        }
    }
    return Map(start, end, walls)
}


