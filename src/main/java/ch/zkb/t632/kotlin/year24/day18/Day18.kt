package ch.zkb.t632.kotlin.year24.day18

import java.util.ArrayDeque
import java.util.PriorityQueue
import kotlin.math.abs
import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day18_test")
    check(part1(testInput, 6, 12), 22)

    val input = readInput("2024", "Day18")
    println("Solution of Part1: ${part1(input, 70, 1024)}")

    check(part2(testInput, 6, 12), Pos(6,1))
    println("Solution of Part2: ${part2(input, 70, 1024)}")
}

private fun part1(input: List<String>, size: Int, timePassed: Int): Int = input.parseInputs(size, timePassed).solveWithHeuristic()?.cost ?: -1

private fun part2(input: List<String>, size: Int, timePassed: Int): Pos {
    val map = input.parseInputs(size, timePassed)
    var node = map.solveWithHeuristic()
    var paths = node!!.path()
    while (true) {
        val newPos = map.notParsed.remove()
        map.walls.add(newPos.first)
        if (paths.contains(newPos.first)) {
            node = map.solveWithHeuristic()
            if (node == null) {
                return newPos.first
            }
            paths = node.path()
        }
    }
}

private fun Map.solveWithHeuristic(): Node? = aStarPath(
    from = start,
    goal = ::isEnd,
    neighboursWithCost = ::neighboursWithCost,
    size,
    heuristic = ::heuristic
)

private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
    fun neighbourSteps(): Set<Pos> = setOf(Pos(1, 0), Pos(-1, 0), Pos(0, 1), Pos(0, -1))
}

private data class Map(var start: Pos, var end: Pos, val walls: MutableSet<Pos>, val size: Int, val notParsed: ArrayDeque<Pair<Pos, Int>>) {
    fun isEnd(pos: Pos): Boolean = pos == end
    fun heuristic(pos: Pos): Int =
        abs(end.x - pos.x) + abs(end.y - pos.y)

    fun neighboursWithCost(current: Pos, size: Int): Set<Pair<Pos, Int>> = buildSet<Pair<Pos, Int>> {
        for (step in current.neighbourSteps()) {
            val pos = current + step
            if (pos !in walls && pos.x <= size && pos.x >= 0 && pos.y <= size && pos.y >= 0) {
                add(pos to 1)
            }
        }
    }

    fun print(fkt: Map.() -> Node?): Node? {
        val ret = fkt(this)
        if (ret != null) {
            val unique = ret.path()
            println()
            for (y in 0..walls.maxBy { it.y }.y) {
                println()
                for (x in 0..walls.maxBy { it.x }.x) {
                    val pos = Pos(x, y)
                    if (pos in walls) print('#')
                    else if (pos in unique) print('O')
                    else print(' ')
                }
            }
            println()
        }
        return ret;
    }
}

private fun List<String>.parseInputs(size: Int, timePassed: Int): Map {
    val start = Pos(0, 0)
    val end = Pos(size, size)
    val walls = mutableSetOf<Pos>()
    val notParsed = ArrayDeque<Pair<Pos, Int>>()
    for ((y, row) in withIndex()) {
        if (y >= timePassed) {
            val nums = row.split(",").map { it.trim().toInt() }
            notParsed.add(Pos(nums[0], nums[1]) to y)
        } else {
            val nums = row.split(",").map { it.trim().toInt() }
            walls.add(Pos(nums[0], nums[1]))
        }

    }

    return Map(start, end, walls, size, notParsed)
}

private data class Node(
    val parent: Node?,
    val pos: Pos,
    val cost: Int,
    val heuristic: Int,
) {
    fun path(): Set<Pos> = (parent?.path() ?: emptySet()) + pos
}


private fun aStarPath(
    from: Pos,
    goal: (Pos) -> Boolean,
    neighboursWithCost: (Pos, Int) -> Set<Pair<Pos, Int>>,
    size: Int,
    heuristic: (Pos) -> Int = { 0 }
): Node? {
    val visited = mutableSetOf<Pos>()
    val queue = PriorityQueue(compareBy<Node> { it.cost + it.heuristic })
    queue += Node(null, from, 0, heuristic(from))

    while (queue.isNotEmpty()) {
        val current = queue.poll()

        if (goal(current.pos)) return current

        for ((next, cost) in neighboursWithCost(current.pos, size)) {
            if (next in visited) continue
            visited += next

            queue += Node(current, next, current.cost + cost, heuristic(next))
        }
    }
    return null
}


