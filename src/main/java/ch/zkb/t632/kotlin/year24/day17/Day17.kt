package ch.zkb.t632.kotlin.year24.day17

import kotlin.math.min
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
    // check(part2(testInputB), 117440)

    println("Solution of Part2: ${part2(input)}")
}

private fun part1(input: List<String>): String = input.parseInputs().solve()

private fun part2(input: List<String>): Long = input.parseInputs().solveb()

private fun Computer.solveb(): Long {
    val sol = inst.flatMap { listOf(it.opcode, it.operand) }.toList()
    val solReversed = sol.joinToString(",").reversed()
    var search = Long.MAX_VALUE / 2
    var counter = 0
    var registerA: Long
    var minByBinary = Long.MAX_VALUE
    do {
        registerA = search
        val comp = this.copy(a = registerA)
        val current = comp.solveHardcoded()
        val currentRev = current.reversed()
        if (isSmaller(currentRev, solReversed)) search += search / 32 else search -= search / 32
        if (currentRev.take(18) == solReversed.take(18)) {
            minByBinary = min(minByBinary, registerA)
            println("Value $registerA Current: $currentRev")
            println("Value $registerA Solutio: $solReversed")
            println()
            counter++
        }
    } while (currentRev != solReversed && counter < 10)
    println("MinByBinary: " + minByBinary)
    do {
        registerA = minByBinary++
        val comp = this.copy(a = registerA)
        val current = comp.solveHardcoded()
        val currentRev = current.reversed()
    } while (currentRev != solReversed)

    println("Found: $registerA")
    return registerA
}

fun isSmaller(current: String, solution: String): Boolean {
    if (current.length < solution.length) {
        return true
    } else if (current.length > solution.length) {
        return false
    }
    for ((ind, cur) in current.withIndex()) {
        if (cur == solution[ind]) continue
        return cur > solution[ind]
    }
    return true
}

private fun Computer.solve(): String {
    var pointer = 0
    val output = mutableListOf<Int>()
    while (pointer in inst.indices) {
        val cur = inst[pointer]
        when (cur.opcode) {
            //adv
            0 -> a = truncate(a / (2.0.pow(cur.operand.combo(this).toInt()))).toLong()
            //bxl
            1 -> b = b xor cur.operand.toLong()
            //bst
            2 -> b = cur.operand.combo(this) % 8
            //jnz
            3 -> {
                if (a != 0L) {
                    pointer = cur.operand / 2
                    continue
                }
            }
            //bxc
            4 -> b = b xor c
            //out
            5 -> {
                output += cur.operand.combo(this).toInt() % 8
            }
            //bdv
            6 -> b = truncate(a / (2.0.pow(cur.operand.combo(this).toInt()))).toLong()
            //cdv
            7 -> c = truncate(a / (2.0.pow(cur.operand.combo(this).toInt()))).toLong()
        }
        pointer++
    }
    return output.joinToString(",")
}

private fun Int.combo(com: Computer): Long {
    when (this) {
        in 0..3 -> return this.toLong()
        4 -> return com.a
        5 -> return com.b
        6 -> return com.c
        7 -> error("Should not happen")
    }
    error("Should not happen")
}

private data class Inst(val opcode: Int, val operand: Int)

private data class Computer(var a: Long, var b: Long, var c: Long, val inst: List<Inst>)

private fun List<String>.parseInputs(): Computer {
    val register = this[0].split(",").map { it.toLong() }
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


private fun Computer.solveHardcoded(): String {

    val output = mutableListOf<Int>()
    while (true) {
        //2,4 -> b = 0..7
        b = a % 8L
        //1,1 -> b= 1,3,5,7
        b = b xor 1L
        // 7,5
        c = truncate(a / (2.0.pow(b.toInt()))).toLong()
        // 4,3
        b = b xor c
        // 1,6
        b = b xor 6L
        // 5,5,
        output.add((b % 8).toInt())
        //if (output != code.take(output.size))
        //    return "" to false
        //0,3 -> Only Changing operation to a
        a /= 8L
        // 3,0
        if (a == 0L) break
    }
    return output.joinToString(",")
}


