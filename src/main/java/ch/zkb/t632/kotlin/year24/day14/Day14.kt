package ch.zkb.t632.kotlin.year24.day14

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day14_test")
    val testBoadSize = Pos(11, 7)
    val waitTime = 1L
    // check(solve(testInput, testBoadSize, waitTime), 12)

    val input = readInput("2024", "Day14")
    val boadSize = Pos(101, 103)
    val resultPart1 = solve(input, boadSize, waitTime)

    println("Solution of Part1: $resultPart1")

}

private fun solve(input: List<String>, boardSize: Pos, seconds: Long): Long =
    input.parseInputs().solve(boardSize, seconds).mulSquares(boardSize)


private fun List<Robot>.solve(boardSize: Pos, seconds: Long): List<Robot> {
    var count = 0
    while (!containsTree()) {
        count++
        for (robot in this) {
            robot.walk(boardSize, seconds)
        }

    }
    println()
    println("Count: $count")
    print(boardSize)
    return this
}

private fun List<Robot>.containsTree(): Boolean {
    val poss = this.map { it.pos }.toSet()
    for (pos in poss) {
        var left = pos
        var right = pos
        var found = true
        for (level in 0..4) {
            left = Pos(left.x + 1, left.y - 1)
            right = Pos(right.x + 1, right.y + 1)
            if (left !in poss || right !in poss) {
                found = false
                continue
            }
        }
        if (found) {
            println("Pos: $pos")
            return true
        }
    }
    return false
}


private fun List<Robot>.mulSquares(boardSize: Pos): Long {
    //print(boardSize)
    var leftUp = 0L
    var rightUp = 0L
    var leftDown = 0L
    var rightDown = 0L
    val xMiddleLine = boardSize.x / 2
    val yMiddleLine = boardSize.y / 2
    for (robot in this) {
        if (robot.pos.x < xMiddleLine) {
            if (robot.pos.y < yMiddleLine) {
                leftUp++
            } else if (robot.pos.y > yMiddleLine) {
                rightUp++
            }
        } else if (robot.pos.x > xMiddleLine) {
            if (robot.pos.y < yMiddleLine) {
                leftDown++
            } else if (robot.pos.y > yMiddleLine) {
                rightDown++
            }

        }
    }
    return leftUp * rightUp * leftDown * rightDown
}

private fun List<Robot>.print(boardSize: Pos) {
    println()
    for (y in 0..<boardSize.y) {
        println()
        for (x in 0..<boardSize.x) {
            val robots = this.filter { it.pos == Pos(x, y) }
            if (robots.isNotEmpty()) {
                print(robots.count())
            } else {
                print(" ")
            }


        }

    }
}

private data class Robot(var pos: Pos, val direction: Pos) {
    fun walk(boardSize: Pos, seconds: Long) {
        pos = (pos + direction.mul(seconds)).mod(boardSize)
    }
}


private data class Pos(val x: Long, val y: Long) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
    fun mul(other: Long): Pos = Pos(x * other, y * other)
    fun mod(boardSize: Pos): Pos {
        var x = x % boardSize.x
        var y = y % boardSize.y
        if (x < 0) x += boardSize.x
        if (y < 0) y += boardSize.y
        return Pos(x, y)
    }
}

private fun List<String>.parseInputs(): List<Robot> {
    val input = this
    return buildList {
        for (line in input) {
            add(line.parse())
        }
    }
}

private fun String.parse(): Robot {
    val reg = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex()
    val matchResult = reg.matchEntire(this)
    return Robot(
        Pos(matchResult!!.groupValues[1].toLong(), matchResult.groupValues[2].toLong()),
        Pos(matchResult.groupValues[3].toLong(), matchResult.groupValues[4].toLong())
    )
}


