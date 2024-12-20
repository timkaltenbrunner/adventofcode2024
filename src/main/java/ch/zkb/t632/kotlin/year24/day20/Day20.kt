package ch.zkb.t632.kotlin.year24.day20

import java.util.PriorityQueue
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

private fun Map.solveAStar(): Node? = aStarPath(
    from = start,
    goal = ::isEnd,
    neighboursWithCost = ::neighboursWithCost,
    heuristic = ::heuristic
)

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
    fun isEnd(pos: Pos): Boolean = pos == end
    fun heuristic(pos: Pos): Int =
        abs(end.x - pos.x) + abs(end.y - pos.y)

    fun neighboursWithCost(current: Pos): Set<Pair<Pos, Int>> = buildSet {
        for (step in current.neighbourSteps()) {
            val pos = current + step
            if (pos !in walls) {
                add(pos to 1)
            }
        }
    }

    fun solve(minSaved: Int, cheatTime: Int): Int {
        val shortest = solveAStar()

        if (shortest != null) {
            val paths = shortest.path()
            return paths.parallelMap {
                it.findPathsInCheatTime(cheatTime, paths, this, minSaved)
            }.sum()
        }
        return -1
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

private data class Node(
    val parent: Node?,
    val pos: Pos,
    val cost: Int,
    val heuristic: Int,
) {
    fun path(): List<Pos> = (parent?.path() ?: emptySet()) + pos
}


private fun aStarPath(
    from: Pos,
    goal: (Pos) -> Boolean,
    neighboursWithCost: (Pos) -> Set<Pair<Pos, Int>>,
    heuristic: (Pos) -> Int = { 0 }
): Node? {
    val visited = mutableSetOf<Pos>()
    val queue = PriorityQueue(compareBy<Node> { it.cost + it.heuristic })
    queue += Node(null, from, 0, heuristic(from))

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        if (goal(current.pos)) return current
        visited += current.pos
        for ((next, cost) in neighboursWithCost(current.pos)) {
            if (next in visited) continue
            val nextNode = queue.find { node -> node.pos == next }
            if (nextNode != null) {
                if (nextNode.cost <= current.cost + cost) continue
                queue -= nextNode
            }
            queue += Node(current, next, current.cost + cost, heuristic(next))
        }
    }
    return null
}


