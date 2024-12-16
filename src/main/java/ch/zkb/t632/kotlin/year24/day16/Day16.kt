package ch.zkb.t632.kotlin.year24.day16

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput
import java.util.*
import kotlin.math.abs

fun main() {
    val testInput = readInput("2024", "Day16_test")
    check(part1(testInput), 11048)

    val input = readInput("2024", "Day16")
    println("Solution of Part1: ${part1(input)}")

    check(part2(testInput), 64)
    println("Solution of Part2: ${part2(input)}")
}

private fun part1(input: List<String>): Int = input.parseInputs().solveWithHeuristic()?.cost ?: -1

private fun part2(input: List<String>): Int =
    input.parseInputs().print(Map::solveWithoutHeuristic)?.uniquePathPos()?.count() ?: -1

private fun Map.solveWithHeuristic(): Node? = aStarPath(
    from = start,
    goal = ::isEnd,
    neighboursWithCost = ::neighboursWithCost,
    heuristic = ::heuristic
)

private fun Map.solveWithoutHeuristic(): Node? = aStarPath(
    from = start,
    goal = ::isEnd,
    neighboursWithCost = ::neighboursWithCost
)

private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
}

private data class PosDir(val pos: Pos, val direction: Direction)

private data class Map(var start: PosDir, var end: Pos, val walls: Set<Pos>) {
    fun isEnd(posDir: PosDir): Boolean = posDir.pos == end
    fun heuristic(posDir: PosDir): Int =
        abs(end.x - posDir.pos.x) + abs(end.y - posDir.pos.y)

    fun neighboursWithCost(current: PosDir): Set<Pair<PosDir, Int>> = buildSet<Pair<PosDir, Int>> {
        for (step in current.direction.neighbourSteps()) {
            val pos = current.pos + step.pos
            if (pos !in walls) {
                add(PosDir(pos, step.direction) to 1 + if (current.direction == step.direction) 0 else 1000)
            }
        }
    }

    fun print(fkt: Map.() -> Node?): Node? {
        val ret = fkt(this)
        if(ret != null) {
            val unique = ret.uniquePathPos()
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

private data class Node(
    val parents: MutableList<Node>,
    val posDir: PosDir,
    val cost: Int,
    val heuristic: Int,
) {
    fun uniquePathPos(): Set<Pos> {
        return buildSet {
            add(posDir.pos)
            for (parent in parents) {
                addAll(parent.uniquePathPos())
            }
        }
    }
}

private fun aStarPath(
    from: PosDir,
    goal: (PosDir) -> Boolean,
    neighboursWithCost: PosDir.() -> Set<Pair<PosDir, Int>>,
    heuristic: (PosDir) -> Int = { 0 },
): Node? {
    val visited = mutableMapOf<PosDir, Node>()
    val queue = PriorityQueue(compareBy<Node> { it.cost + it.heuristic })
    queue += Node(mutableListOf(), from, 0, heuristic(from))
    while (queue.isNotEmpty()) {
        var current = queue.poll()
        if (goal(current.posDir)) return current

        val existingPath = visited[current.posDir]
        var check = existingPath == null
        if (existingPath != null && existingPath.cost <= current.cost && existingPath.parents != current.parents) {
            existingPath.parents += current.parents
            current = existingPath
            check = true
        }
        if (check) {
            visited[current.posDir] = current
            for ((next, cost) in current.posDir.neighboursWithCost()) {
                if (visited[next] == null) {
                    val node = Node(mutableListOf(current), next, current.cost + cost, heuristic(next))
                    queue += node
                }
            }
        }
    }
    return null
}


