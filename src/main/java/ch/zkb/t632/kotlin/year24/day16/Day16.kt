package ch.zkb.t632.kotlin.year24.day16

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput
import java.util.*
import kotlin.math.abs

fun main() {
    val testInput = readInput("2024", "Day16_test")
    check(part1(testInput), 64)

    val input = readInput("2024", "Day16")
    val resultPart1 = part1(input)
    println("Solution of Part1: $resultPart1")
}

private fun part1(input: List<String>): Int = input.parseInputs().solve()

private fun Map.solve(): Int {
    val node = aStar(
        from = start,
        goal = ::isEnd,
        neighboursWithCost = ::neighboursWithCost
    )
    if (node != null) {
        return node.path().count()
    }
    return -1
}

private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
}

private data class PosDir(val pos: Pos, val direction: Direction) {
    fun rotationCosts(end: Pos): Int = when (direction) {
        Direction.UP -> if (pos.y < end.y) 2000 else if (pos.x != end.x) 1000 else 0
        Direction.DOWN -> if (pos.y > end.y) 2000 else if (pos.x != end.x) 1000 else 0
        Direction.RIGHPosDir -> if (pos.x > end.x) 2000 else if (pos.y != end.y) 1000 else 0
        Direction.LEFPosDir -> if (pos.x < end.x) 2000 else if (pos.y != end.y) 1000 else 0
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
    LEFPosDir,
    RIGHPosDir;

    fun neighbourSteps(): Set<PosDir> = when (this) {
        UP -> setOf(PosDir(Pos(-1, 0), LEFPosDir), PosDir(Pos(1, 0), RIGHPosDir), PosDir(Pos(0, -1), UP))
        DOWN -> setOf(PosDir(Pos(-1, 0), LEFPosDir), PosDir(Pos(1, 0), RIGHPosDir), PosDir(Pos(0, 1), DOWN))
        RIGHPosDir -> setOf(PosDir(Pos(0, -1), UP), PosDir(Pos(0, 1), DOWN), PosDir(Pos(1, 0), RIGHPosDir))
        LEFPosDir -> setOf(PosDir(Pos(0, -1), UP), PosDir(Pos(0, 1), DOWN), PosDir(Pos(-1, 0), LEFPosDir))
    }
}


private fun List<String>.parseInputs(): Map {
    var start = PosDir(Pos(0, 0), Direction.RIGHPosDir)
    var end = Pos(0, 0)
    val walls = mutableSetOf<Pos>()
    for ((y, row) in withIndex()) {
        for ((x, ch) in row.withIndex()) {
            when (ch) {
                'S' -> start = PosDir(Pos(x, y), Direction.RIGHPosDir)
                'E' -> end = Pos(x, y)
                '#' -> walls.add(Pos(x, y))
            }
        }
    }
    return Map(start, end, walls)
}

private data class Node(
    val parents: MutableList<Node>,
    val value: PosDir,
    val cost: Int,
    val heuristic: Int,
) {
    fun path(): Set<Pos> {
        return buildSet {
            add(value.pos)
            for (parent in parents) {
                addAll(parent.path())
            }
        }
    }
}

private fun aStar(
    from: PosDir,
    goal: (PosDir) -> Boolean,
    neighboursWithCost: PosDir.() -> Set<Pair<PosDir, Int>>,
    heuristic: (PosDir) -> Int = { 0 },
): Node? {
    val visited = mutableMapOf<Pos, Node>()
    val queue = PriorityQueue(compareBy<Node> { it.cost + it.heuristic })
    queue += Node(mutableListOf(), from, 0, heuristic(from))
    var found: Node? = null
    while (queue.isNotEmpty()) {
        val current = queue.poll()
        if (goal(current.value)) found = current else {
            for ((next, cost) in current.value.neighboursWithCost()) {
                val otherPath = visited[next.pos]
                if (otherPath != null) {
                    if (otherPath.cost <= current.cost) {
                        current.parents += otherPath
                    }
                } else {
                    val node = Node(mutableListOf(current), next, current.cost + cost, heuristic(next))
                    visited[next.pos] = node
                    queue += node
                }
            }
        }
    }
    return found
}


