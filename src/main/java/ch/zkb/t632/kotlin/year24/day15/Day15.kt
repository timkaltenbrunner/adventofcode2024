package ch.zkb.t632.kotlin.year24.day15

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day15_test")
    check(solve(testInput), 10092)

    val input = readInput("2024", "Day15")
    val resultPart1 = solve(input)

    println("Solution of Part1: $resultPart1")

}

private fun solve(input: List<String>): Long = input.parseInputs().solve()


private data class Warehouse(val map: Map, val movements: List<Pos>) {
    fun canMove(movement: Pos): Boolean {
        var pos = map.robot
        do {
            pos = pos + movement
            if (map.walls.contains(pos)) {
                return false
            }
        } while (map.boxes.contains(pos))
        return true
    }

    fun move(movement: Pos) {
        map.robot += movement
        shiftBoxes(map.robot, movement)
    }

    private fun shiftBoxes(pos: Pos, movement: Pos) {
        if (pos in map.boxes) {
            var next = pos
            while (next in map.boxes) {
                next += movement
            }
            map.boxes.remove(pos)
            map.boxes.add(next)
        }
    }
}

private data class Map(var robot: Pos, val boxes: MutableSet<Pos>, val walls: Set<Pos>, val maxX: Int, val maxY: Int)

private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
}

private fun Warehouse.solve(): Long {
    for (movement in movements) {
        if (canMove(movement)) {
            move(movement)
        }
    }
    return map.boxes.sumOf { (x, y) -> 100L * y + x }
}


private fun List<String>.parseInputs(): Warehouse {
    var robot = Pos(0, 0)
    val boxes: MutableSet<Pos> = mutableSetOf()
    val walls: MutableSet<Pos> = mutableSetOf()
    val movements: MutableList<Pos> = mutableListOf()
    var maxX = 0
    var maxY = 0
    val input = this
    for ((y, row) in input.withIndex()) {
        for ((x, ch) in row.withIndex()) {
            when (ch) {
                '#' -> {
                    maxX = kotlin.math.max(maxX, x)
                    maxY = kotlin.math.max(maxY, y)
                    walls.add(Pos(x, y))
                }

                'O' -> boxes.add(Pos(x, y))
                '>' -> movements.add(Pos(1, 0))
                '<' -> movements.add(Pos(-1, 0))
                '^' -> movements.add(Pos(0, -1))
                'v' -> movements.add(Pos(0, 1))
                '@' -> robot = Pos(x, y)
            }
        }
    }
    return Warehouse(Map(robot, boxes, walls, maxX, maxY), movements)
}





