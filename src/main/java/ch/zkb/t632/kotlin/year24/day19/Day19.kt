package ch.zkb.t632.kotlin.year24.day19

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day19_test")
    check(part1(testInput), 6)

    val input = readInput("2024", "Day19")
    println("Solution of Part1: ${part1(input)}")

    check(part2(testInput), 16)
    println("Solution of Part2: ${part2(input)}")


}

private fun part1(input: List<String>): Long = input.parseInputs().solutions()
private fun part2(input: List<String>): Long = input.parseInputs().possibilities()


private data class Puzzle(val towels: List<String>, val combinations: List<String>) {

    fun solutions(): Long {
        var solvable = 0L
        for (comb in combinations) {
            if (isSolvable(comb) > 0) {
                solvable++
            }
        }
        return solvable
    }

    fun possibilities(): Long {
        var solvable = 0L
        for (comb in combinations) {
            solvable += isSolvable(comb)
        }
        return solvable
    }

    private fun isSolvable(comb: String, cache: MutableMap<String, Long> = mutableMapOf()): Long {
        if (comb.isEmpty()) return 1
        return cache.getOrPut(comb) {
            towels.sumOf { towel ->
                val nextComb = comb.removePrefix(towel)
                if (nextComb != comb) isSolvable(nextComb, cache) else 0
            }
        }
    }
}

private fun List<String>.parseInputs(): Puzzle {
    val towels = mutableListOf<String>()
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

    return Puzzle(towels, combinations)
}



