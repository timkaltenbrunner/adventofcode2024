package ch.zkb.t632.kotlin.year24.day16

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput
import kotlin.math.abs

fun main() {
    val testInput = readInput("2024", "Day16_test")
    check(part1(testInput), 11048)

    val input = readInput("2024", "Day16")
    val resultPart1 = part1(input)
    println("Solution of Part1: $resultPart1")
}

private fun part1(input: List<String>): Int = input.parseInputs().solve()

private fun Map.solve(): Int {
    val node = aStar(
        from = start,
        goal = ::isEnd,
        neighboursWithCost = ::neighboursWithCost,
        heuristic = ::heuristic
    )
    if (node == null) {
        return -1
    }
    return node.cost
}

private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
}

private data class PosDir(val pos: Pos, val direction: Direction) {
    fun rotationCosts(end: Pos): Int = when (direction) {
        Direction.UP -> if (pos.y < end.y) 2000 else if (pos.x != end.x) 1000 else 0
        Direction.DOWN -> if (pos.y > end.y) 2000 else if (pos.x != end.x) 1000 else 0
        Direction.RIGHT -> if (pos.x > end.x) 2000 else if (pos.y != end.y) 1000 else 0
        Direction.LEFT -> if (pos.x < end.x) 2000 else if (pos.y != end.y) 1000 else 0
    }
}

private data class Map(var start: PosDir, var end: Pos, val walls: Set<Pos>) {
    fun isEnd(posDir: PosDir): Boolean = posDir.pos == end
    fun heuristic(posDir: PosDir): Int =
        abs(end.x - posDir.pos.x) + abs(end.y - posDir.pos.y) + posDir.rotationCosts(end)

    fun neighboursWithCost(current: PosDir): Set<Pair<PosDir, Int>> {
        val steps = mutableSetOf<Pair<PosDir, Int>>()
        for (step in current.direction.neighbourSteps()) {
            val pos = current.pos + step.pos
            if (pos !in walls) {
                steps += PosDir(pos, step.direction) to 1 + if (current.direction == step.direction) 0 else 1000
            }
        }
        return steps
    }

}

private enum class Direction() {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    fun neighbourSteps(): Set<PosDir> = when (this) {
        UP -> setOf(PosDir(Pos(-1, 0), LEFT), PosDir(Pos(1, 0), RIGHT), PosDir(Pos(0, -1), UP))
        DOWN -> setOf(PosDir(Pos(-1, 0), LEFT), PosDir(Pos(1, 0), RIGHT), PosDir(Pos(0, 1), DOWN))
        RIGHT -> setOf(PosDir(Pos(0, -1), UP), PosDir(Pos(0, 1), DOWN), PosDir(Pos(1, 0), RIGHT))
        LEFT -> setOf(PosDir(Pos(0, -1), UP), PosDir(Pos(0, 1), DOWN), PosDir(Pos(-1, 0), LEFT))
    }
}


private fun List<String>.parseInputs(): Map {
    var start = PosDir(Pos(0, 0), Direction.RIGHT)
    var end = Pos(0, 0)
    val walls = mutableSetOf<Pos>()
    for ((y, row) in withIndex()) {
        for ((x, ch) in row.withIndex()) {
            when (ch) {
                'S' -> start = PosDir(Pos(x, y), Direction.RIGHT)
                'E' -> end = Pos(x, y)
                '#' -> walls.add(Pos(x, y))
            }
        }
    }
    return Map(start, end, walls)
}


