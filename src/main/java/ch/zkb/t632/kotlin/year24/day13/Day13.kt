package ch.zkb.t632.kotlin.year24.day13

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput
import org.yaml.snakeyaml.util.Tuple

fun main() {
    val testInput = readInput("2024", "Day13_test")
    check(part1(testInput), 480)

    val input = readInput("2024", "Day13")
    val resultPart1 = part1(input)
    println("Solution of Part1: $resultPart1")

}

private fun part1(input: List<String>): Long = input.parseInputs().solve().sumOf { it.cost }

private fun List<ClawMachine>.solve(): List<Sol> {
    var sol = mutableListOf<Sol>()
    for (clawMachine in this) {
        var currentSols = mutableSetOf<Sol>()
        for (a in 0..100) {
            for (b in 0..100) {
                if (clawMachine.prizePos == (clawMachine.xButton.mul(a)) + (clawMachine.yButton.mul(b))) {
                    currentSols += Sol(a, b, (a * 3L) + b)
                }
            }
        }
        if (!currentSols.isEmpty()) {
            sol += currentSols.getMin()
        }
    }
    return sol
}

private fun Set<Sol>.getMin(): Sol {
    var min = first()
    for (sol in this) {
        if (sol.cost < min.cost) {
            min = sol
        }
    }
    return min
}

private data class ClawMachine(val prizePos: Pos, val xButton: Pos, val yButton: Pos)


private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
    fun mul(other: Int): Pos = Pos(x * other, y * other)
}

private data class Sol(val a: Int, val b: Int, var cost: Long)

private fun List<String>.parseInputs(): List<ClawMachine> {
    val input = this
    return buildList {
        var y = 3
        while (y - 1 in input.indices) {
            val rowA = input[y - 3]
            val rowB = input[y - 2]
            val prize = input[y - 1]
            add(ClawMachine(prize.parse(), rowA.parse(), rowB.parse()))
            y += 4
        }
    }
}

private fun String.parse(): Pos {
    val reg = """[^\d]*(\d+), Y[\\+|=](\d+)""".toRegex()
    val matchResult = reg.matchEntire(this)
    return Pos(matchResult!!.groupValues[1].toInt(), matchResult.groupValues[2].toInt())
}


