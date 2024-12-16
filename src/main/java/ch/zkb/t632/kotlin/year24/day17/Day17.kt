package ch.zkb.t632.kotlin.year24.day17

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day17_test")
    check(part1(testInput), 11048)

    val input = readInput("2024", "Day17")
    println("Solution of Part1: ${part1(input)}")

    check(part2(testInput), 64)
    println("Solution of Part2: ${part2(input)}")
}

private fun part1(input: List<String>): Int = input.parseInputs().solve()

private fun part2(input: List<String>): Int = input.parseInputs().solve()

private fun Map.solve(): Int = 0

private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
}

private data class Map(var start: Pos, val walls: Set<Pos>) {

}

private fun List<String>.parseInputs(): Map {
    var start = Pos(0, 0)
    val walls = mutableSetOf<Pos>()
    for ((y, row) in withIndex()) {
        for ((x, ch) in row.withIndex()) {
            when (ch) {
                'S' -> start = Pos(x, y)
                '#' -> walls.add(Pos(x, y))
            }
        }
    }
    return Map(start, walls)
}


