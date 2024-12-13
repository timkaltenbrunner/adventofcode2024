package ch.zkb.t632.kotlin.year24.day13

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput
import java.awt.Point
import kotlin.math.min

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
        var aCount = 0L
        do {
            val result = clawMachine.prizePos - clawMachine.aButton.mul(++aCount)
            val (bCount, rest) = result.divRest(clawMachine.bButton)
            if (rest == Pos(0L, 0L)) {
                sol += Sol(aCount, bCount, aCount * 3 + bCount)
            }
        } while (result.x > 0 && result.y > 0)
    }
    println(sol)
    return sol
}

private data class ClawMachine(val prizePos: Pos, val aButton: Pos, val bButton: Pos)


private data class Pos(val x: Long, val y: Long) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
    operator fun minus(other: Pos): Pos = Pos(x - other.x, y - other.y)
    fun mul(other: Long): Pos = Pos(x * other, y * other)
    fun divRest(other: Pos): Pair<Long, Pos> {
        val resx = x / other.x
        val resy = y / other.y
        val ret = min(resx, resy)
        return ret to minus(other.mul(ret))
    }
}


/** Greatest common divisor */
fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

private data class Sol(val a: Long, val b: Long, var cost: Long)

private fun List<String>.parseInputs(): List<ClawMachine> {
    val input = this
    return buildList {
        var y = 3
        while (y - 1 in input.indices) {
            val rowA = input[y - 3]
            val rowB = input[y - 2]
            val prize = input[y - 1]
            // add(ClawMachine(prize.parse() + Pos(10000000000000L, 10000000000000), rowA.parse(), rowB.parse()))
            add(ClawMachine(prize.parse(), rowA.parse(), rowB.parse()))
            y += 4
        }
    }
}

private fun String.parse(): Pos {
    val reg = """[^\d]*(\d+), Y[\\+|=](\d+)""".toRegex()
    val matchResult = reg.matchEntire(this)
    return Pos(matchResult!!.groupValues[1].toLong(), matchResult.groupValues[2].toLong())
}


