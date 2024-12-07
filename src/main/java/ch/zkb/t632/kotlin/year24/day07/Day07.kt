package ch.zkb.t632.kotlin.year24.day07

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day07_test")
    check(part1(testInput), 3749)

    val input = readInput("2024", "Day07")
    val resultPart1 = part1(input)

    println("Solution of Part1: $resultPart1")

}

private enum class Operator {
    SUM {
        override fun calc(a: Long, b: Long): Long = a + b
        override fun neutral(): Long = 0
    },
    MULTIPLY {
        override fun calc(a: Long, b: Long): Long = a * b
        override fun neutral(): Long = 1
    };

    abstract fun calc(a: Long, b: Long): Long
    abstract fun neutral(): Long
}

private data class Calibration(val testNumber: Long, val inputs: ArrayDeque<Long>)


private fun part1(input: List<String>): Long {
    val calibartions =
        input.map { it.split(": ") }.map { Calibration(it.get(0).toLong(), parseInputs(it.get(1))) }.toList()

    return calculate(calibartions, listOf(Operator.SUM, Operator.MULTIPLY))
}

private fun calculate(calibartions: List<Calibration>, operators: List<Operator>): Long {
    var sum: Long = 0
    for (calibartion in calibartions) {
        calibartion.inputs
        sum += calculate(calibartion, operators, null)
    }
    return sum
}

private fun calculate(cal: Calibration, operators: List<Operator>, currentResult: Long?): Long {
    if (cal.inputs.isNotEmpty()) {
        val current = cal.inputs.removeFirst()
        for (operator in operators) {
            val result = calculate(
                Calibration(cal.testNumber, ArrayDeque(cal.inputs)),
                operators,
                operator.calc(currentResult ?: operator.neutral(), current)
            )
            if (result == cal.testNumber) {
                return result
            }
        }
    }
    return currentResult ?: 0

}


fun parseInputs(get: String): ArrayDeque<Long> = ArrayDeque(get.split(' ').map(String::toLong))

