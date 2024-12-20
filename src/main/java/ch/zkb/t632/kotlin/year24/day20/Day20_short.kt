package ch.zkb.t632.kotlin.year24.day22

import kotlin.math.abs
import kotlin.math.max
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

    fun findPathsInCheatTime(cheatTime: Int, paths: List<Pos>, map: Map, minSaved: Int): Int {
        var ret = 0
        val currentPos = paths.indexOf(this)
        for ((index, pos) in paths.withIndex()) {
            if (pos != this) {
                val distance = abs(this.x - pos.x) + abs(this.y - pos.y)
                if (index > currentPos) {
                    val savedTime = index - currentPos
                    if (distance <= cheatTime && distance + minSaved <= savedTime) {
                        ret++
                    }
                }
            }
        }
        return ret
    }
}

private data class Map(var start: Pos, var end: Pos, val walls: Set<Pos>, val maxX: Int, val maxY: Int) {
    fun solve(minSaved: Int, cheatTime: Int): Int {
        val path = findPath()
        return path.parallelMap {
            it.findPathsInCheatTime(cheatTime, path, this, minSaved)
        }.sum()
    }

    private fun findPath(): List<Pos> {
        val path = mutableListOf(start)
        while (path.last() != end) {
            for (ns in path.last().neighbourSteps()) {
                val next = path.last() + ns
                val lastIndex = path.size - 2
                if (next !in walls && (lastIndex < 0 || next != path[lastIndex])) {
                    path += next
                }
            }
        }
        return path
    }
}

private fun List<String>.parseInputs(): Map {
    var start = Pos(0, 0)
    var end = Pos(0, 0)
    val walls = mutableSetOf<Pos>()
    var maxX = 0
    var maxY = 0
    for ((y, row) in withIndex()) {
        maxY = max(maxY, y)
        for ((x, ch) in row.withIndex()) {
            maxX = max(maxX, x)
            when (ch) {
                'S' -> start = Pos(x, y)
                'E' -> end = Pos(x, y)
                '#' -> walls.add(Pos(x, y))
            }
        }
    }
    return Map(start, end, walls, maxX, maxY)
}


