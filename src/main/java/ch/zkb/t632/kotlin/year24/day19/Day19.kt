package ch.zkb.t632.kotlin.year24.day19

import java.util.PriorityQueue
import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day19_test")
    check(part1(testInput), 6)

    val input = readInput("2024", "Day19")
    println("Solution of Part1: ${part1(input)}")

    check(part2(testInput), 16)
    println("Solution of Part1: ${part2(input)}")

}

private fun part1(input: List<String>): Int = input.parseInputs().solve()

private fun part2(input: List<String>): Int = input.parseInputs().solve2()


fun neighboursWithCost(comb: String, maxTowelSize: Int, towels: Set<String>): Set<Pair<String, Int>> {
    val neighboursWithCost = mutableSetOf<Pair<String, Int>>()
    for (cur in 1..maxTowelSize) {
        if (comb.length >= cur) {
            val junk = comb.take(cur)
            if (towels.contains(junk)) {
                neighboursWithCost.add(comb.drop(cur) to junk.length)
            }
        }
    }
    return neighboursWithCost
}

private data class Puzzle(val towels: Set<String>, val combinations: List<String>, val maxTowelSize: Int) {

    fun solve(): Int {
        var solvable = 0;
        for (comb in combinations) {
            val node = aStarPath(
                from = comb,
                goal = { it == "" },
                neighboursWithCost = ::neighboursWithCost,
                maxTowelSize = maxTowelSize,
                towels = towels,
                heuristic = { 0 }
            )

            if (node != null) {
                solvable++
            }
        }
        return solvable
    }

    fun solve2(): Int {
        var solvable = 0;
        for (comb in combinations) {
            val node = aStarPath(
                from = comb,
                goal = { it == "" },
                neighboursWithCost = ::neighboursWithCost,
                maxTowelSize = maxTowelSize,
                towels = towels,
                heuristic = { 0 }
            )

            if (node != null) {
                solvable += node.numPath()
            }
        }
        return solvable
    }
}


private fun List<String>.parseInputs(): Puzzle {
    val towels = mutableSetOf<String>()
    val combinations = mutableListOf<String>()
    var parseTowels = true
    for (row in this) {
        if (row == "") {
            parseTowels = false
            continue
        }
        if (parseTowels) {
            towels.addAll(row.split(", "))
        } else {
            combinations.add(row)
        }
    }

    return Puzzle(towels, combinations, towels.maxOf { it.length })
}

private data class Node(
    val parents: MutableList<Node>,
    val comb: String,
    val cost: Int,
    val heuristic: Int,
) {
    fun numPath(): Int {
        if (parents.isEmpty()) {
            return 1
        }
        var path = 0
        for (parent in parents) {
            path += parent.numPath()
        }
        return path
    }
}


private fun aStarPath(
    from: String,
    goal: (String) -> Boolean,
    neighboursWithCost: (String, Int, Set<String>) -> Set<Pair<String, Int>>,
    maxTowelSize: Int,
    towels: Set<String>,
    heuristic: (String) -> Int
): Node? {
    val visited = mutableMapOf<String, Node>()
    val queue = PriorityQueue(compareBy<Node> { it.cost + it.heuristic })
    queue += Node(mutableListOf(), from, 0, heuristic(from))

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        if (goal(current.comb)) return current
        visited[current.comb] = current
        for ((next, cost) in neighboursWithCost(current.comb, maxTowelSize, towels)) {
            val alrVis = visited[next]
            if (alrVis != null) {
                if (alrVis.cost >= current.cost + cost) {
                    alrVis.parents += current
                }
                continue
            }
            val nextNode = queue.find { node -> node.comb == next }
            if (nextNode != null) {
                if (nextNode.cost <= current.cost + cost) continue
                queue -= nextNode
            }
            queue += Node(mutableListOf(current), next, current.cost + cost, heuristic(next))
        }
    }
    return null
}


