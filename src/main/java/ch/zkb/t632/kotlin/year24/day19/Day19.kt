package ch.zkb.t632.kotlin.year24.day19

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day19_test")
    check(part1(testInput), 6)

    val input = readInput("2024", "Day19")
    println("Solution of Part1: ${part1(input)}")

}

private fun part1(input: List<String>): Int = input.parseInputs().solve()


private data class Puzzle(val towels: Set<String>, val combinations: List<String>, val maxTowelSize: Int) {

    fun solve(): Int {
        var solvable = 0;
        for (comb in combinations) {
            if (isSolvable(comb)) {
                solvable++
            }
        }
        return solvable
    }

    private fun isSolvable(comb: String, solution: String = ""): Boolean {
        if (comb == "") {
            return true
        }
        for (cur in 1 .. maxTowelSize) {
            if (comb.length >= cur) {
                val junk = comb.take(cur)
                if (towels.contains(junk)) {
                    if(isSolvable(comb.drop(cur), solution + junk)) {
                        return true
                    }
                }
            }
        }
        return false
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


