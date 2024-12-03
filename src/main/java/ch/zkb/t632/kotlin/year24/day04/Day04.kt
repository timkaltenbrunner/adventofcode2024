package ch.zkb.t632.kotlin.year24.day04

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput1 = readInput("2024", "Day04_test_part1")
    check(part1(testInput1), 161)


    // Change to 04
    val input = readInput("2024", "Day03")
    println(part1(input))
}

private fun part1(input: List<String>): Int = input.getCommands().filterIsInstance<Mul>().sumOf { it.product }

private sealed interface Command
private data object Do : Command
private data object Dont : Command
private data class Mul(
    val a: Int,
    val b: Int,
) : Command {
    val product = a * b
}

private val pattern = "(do\\(\\)|don't\\(\\)|mul\\(\\d+,\\d+\\))".toRegex()

private fun List<String>.getCommands(): List<Command> {
    return pattern.findAll(joinToString("")).map {
        val text = it.value
        if (text.startsWith("mul")) {
            val (a, b) = text.removePrefix("mul(").removeSuffix(")").split(",")
            Mul(a.toInt(), b.toInt())
        } else if (text == "do()") {
            Do
        } else {
            Dont
        }
    }.toList()
}