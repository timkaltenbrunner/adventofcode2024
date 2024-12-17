package ch.zkb.t632.kotlin.year24.day17

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput
import kotlin.math.pow
import kotlin.math.truncate

fun main() {
    val testInput_n1 = readInput("2024", "Day17_test4")
    part1(testInput_n1)


    val testInput = readInput("2024", "Day17_test")
    check(part1(testInput), "4,6,3,5,6,3,5,2,1,0")

    val testInput2 = readInput("2024", "Day17_test2")
    check(part1(testInput2), "0,1,2")

    val testInput3 = readInput("2024", "Day17_test3")
    check(part1(testInput3), "4,2,5,6,7,7,7,7,3,1,0")

    val input = readInput("2024", "Day17")
    println("Solution of Part1: ${part1(input)}")

    val testInputB = readInput("2024", "Day17_testb")
    check(part2(testInputB), 117440)

    println("Solution of Part2: ${part2(input)}")
}

private fun part1(input: List<String>): String = input.parseInputs().solve().first

private fun part2(input: List<String>): Int = input.parseInputs().solveb()

private fun Computer.solveb(): Int {
    val sol = inst.flatMap { listOf(it.opcode, it.operand) }.toList()
    var registerA = -1
    do {
        val comp = this.copy(a = ++registerA)
        if (registerA < 0) {
            return -1
        }
    } while (!comp.solve(sol).second)
    println("Found: $registerA")
    return registerA
}

private fun Computer.solve(code: List<Int>? = null): Pair<String, Boolean> {
    var pointer = 0
    val output = mutableListOf<Int>()
    while (pointer in inst.indices) {
        val cur = inst[pointer]
        when (cur.opcode) {
            //adv
            0 -> a = truncate(a / (2.0.pow(cur.operand.combo(this)))).toInt()
            //bxl
            1 -> b = b xor cur.operand
            //bst
            2 -> b = cur.operand.combo(this) % 8
            //jnz
            3 -> {
                if (a != 0) {
                    pointer = cur.operand / 2
                    continue
                }
            }
            //bxc
            4 -> b = b xor c
            //out
            5 -> {
                output += cur.operand.combo(this) % 8
                if (code != null) {
                    for (index in output.indices) {
                        if(code == output) {
                            return "" to true
                        }
                        if (index !in code.indices || output[index] != code[index]) {
                            return "" to false
                        }
                    }
                    if(output.size > 8)
                        println("Found partial: " + output.joinToString(","))
                }
            }
            //bdv
            6 -> b = truncate(a / (2.0.pow(cur.operand.combo(this)))).toInt()
            //cdv
            7 -> c = truncate(a / (2.0.pow(cur.operand.combo(this)))).toInt()
        }
        pointer++
    }
    return output.joinToString(",") to (output == code)
}

private fun Int.combo(com: Computer): Int {
    when (this) {
        in 0..3 -> return this
        4 -> return com.a
        5 -> return com.b
        6 -> return com.c
        7 -> error("Should not happen")
    }
    error("Should not happen")
}

private data class Inst(val opcode: Int, val operand: Int) {

}

private data class Computer(var a: Int, var b: Int, var c: Int, val inst: List<Inst>) {

}

private fun List<String>.parseInputs(): Computer {
    val register = this[0].split(",").map { it.toInt() }
    val insts = this[1].split(",").map { it.toInt() }
    val instructions = buildList<Inst> {
        for (i in insts.indices) {
            if (i % 2 == 1) {
                add(Inst(insts[i - 1], insts[i]))
            }
        }
    }
    return Computer(register[0], register[1], register[2], instructions)
}


