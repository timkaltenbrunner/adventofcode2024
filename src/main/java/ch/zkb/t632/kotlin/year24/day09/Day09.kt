package ch.zkb.t632.kotlin.year24.day09

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day09_test")
    check(part2(testInput), 2858)

    val input = readInput("2024", "Day09")
    val resultPart1 = part2(input)

    println("Solution of Part2: $resultPart1")

}

private fun part2(input: List<String>): Long = solve(parseInputs(input.first()))


private fun solve(parsedInputs: List<Long>): Long =
    multiply(compact(parsedInputs).print()).print().map { it.toLong() }.sum()

private fun List<Long>.print(): List<Long> {
    println(this)
    return this
}


private fun compact(input: List<Long>): List<Long> {
    var revIndex = input.size - 1
    val copy = input.toMutableList()
    do {
        val (localRevVal, revSize, localRevIndex) = getRevBlock(copy, revIndex)
        revIndex = localRevIndex
        var index = 0
        while (index < revIndex) {
            val value = copy[index]
            if (value == -1L) {
                val emptyBlockSize = calcEmptyBlockSize(copy, index)
                index += emptyBlockSize
                if (emptyBlockSize >= revSize) {
                    updateNumTimes(copy, revSize, revIndex + 1, 0)
                    updateNumTimes(copy, revSize, index - emptyBlockSize, localRevVal)
                    break
                }

            }
            index++
        }
    } while (revIndex > 0)
    return copy.map { if (it < 0) 0 else it }.toList()
}


fun calcEmptyBlockSize(longs: MutableList<Long>, index: Int): Int {
    var size = 0
    while (index + ++size in longs.indices && longs.get(index + size) == -1L) {
    }
    return size
}

private fun getRevBlock(copy: MutableList<Long>, revIndex: Int): Triple<Long, Int, Int> {
    var localRevSize = 0
    var localRevVal = copy[revIndex]
    var localRevIndex = revIndex
    while (localRevVal == -1L && localRevIndex - 1 in copy.indices) {
        localRevVal = copy[--localRevIndex]
    }
    var tempRevValue = localRevVal
    while (localRevVal == tempRevValue && localRevIndex - 1 in copy.indices) {
        localRevSize++
        tempRevValue = copy[--localRevIndex]
    }
    return Triple(localRevVal, localRevSize, localRevIndex)

}

private fun multiply(input: List<Long>): List<Long> = buildList {
    for ((index, value) in input.withIndex()) add(value * index)
}


private fun parseInputs(input: String): List<Long> = buildList {
    var currNumber = 0L
    for ((y, ch) in input.withIndex()) {
        if (y % 2 == 1) addAll(addNumTimes(-1, ("" + ch).toInt()))
        else addAll(addNumTimes(currNumber++, ("" + ch).toInt()))
    }
}

fun addNumTimes(num: Long, times: Int): List<Long> = buildList {
    for (j in 1..times) {
        add(num)
    }
}

fun updateNumTimes(copy: MutableList<Long>, revSize: Int, index: Int, value: Long) {
    for (j in index..<(index + revSize)) {
        copy[j] = value
    }
}


