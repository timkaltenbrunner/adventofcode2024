package ch.zkb.t632.kotlin.year24.day15

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day15_test")
    val input = readInput("2024", "Day15")

    check(solve(testInput, false), 10092)
    val resultPart1 = solve(input, false)
    println("Solution of Part1: $resultPart1")

    check(solve(testInput, true), 9021)
    val resultPart2 = solve(input, true)
    println("Solution of Part2: $resultPart2")

}

private fun solve(input: List<String>, scaleX: Boolean): Long = input.parseInputs(scaleX).performMovements()

private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
}

private data class Box(val start: Pos, val end: Pos) {
    operator fun plus(other: Pos) = Box(start + other, end + other)
}

private data class Map(var robot: Pos, val boxes: MutableSet<Box>, val walls: Set<Pos>)


private data class Warehouse(val map: Map, val movements: List<Pos>) {
    fun performMovements(): Long {
        for (movement in movements) {
            move(movement)
        }
        return map.boxes.sumOf { (start, _) -> 100L * start.y + start.x }
    }

    fun move(movement: Pos) {
        val nextPos = map.robot + movement
        if (!map.walls.contains(nextPos)) {
            val box = map.boxes.findBoxAtPos(nextPos)
            if (box == null) {
                map.robot += movement
            } else {
                val boxesToMove = mutableSetOf<Box>()
                if (findBoxes(box, movement, boxesToMove)) {
                    map.robot += movement
                    map.boxes.removeAll(boxesToMove)
                    val movedBoxes = boxesToMove.map { Box(it.start + movement, it.end + movement) }
                    map.boxes.addAll(movedBoxes)
                }
            }
        }
    }

    private fun findBoxes(box: Box, movement: Pos, boxes: MutableSet<Box>): Boolean {
        boxes += box
        val nextBox = box + movement
        if (map.walls.collides(nextBox)) return false
        val nextStartBox = map.boxes.findBoxAtPos(nextBox.start)
        val nextEndBox = map.boxes.findBoxAtPos(nextBox.end)
        var canMove = true
        if (nextStartBox != null && !boxes.contains(nextStartBox)) {
            canMove = canMove && findBoxes(nextStartBox, movement, boxes)
        }
        if (nextEndBox != null && !boxes.contains(nextEndBox)) {
            canMove = canMove && findBoxes(nextEndBox, movement, boxes)
        }
        return canMove
    }
}

private fun Set<Pos>.collides(nextBox: Box): Boolean = contains(nextBox.start) || contains(nextBox.end)

private fun Set<Box>.findBoxAtPos(pos: Pos): Box? = find { box -> box.start == pos || box.end == pos }

private fun List<String>.parseInputs(scaleX: Boolean): Warehouse {
    var robot = Pos(0, 0)
    val boxes: MutableSet<Box> = mutableSetOf()
    val walls: MutableSet<Pos> = mutableSetOf()
    val movements: MutableList<Pos> = mutableListOf()
    val input = this
    for ((y, row) in input.withIndex()) {
        for ((x, ch) in row.withIndex()) {
            when (ch) {
                '@' -> robot = if (scaleX) Pos(x * 2, y) else Pos(x, y)
                '#' -> {
                    if (scaleX) {
                        walls.add(Pos(x * 2, y))
                        walls.add(Pos(x * 2 + 1, y))
                    } else {
                        walls.add(Pos(x, y))
                    }

                }

                'O' -> boxes.add(if (scaleX) Box(Pos(x * 2, y), Pos(x * 2 + 1, y)) else Box(Pos(x, y), Pos(x, y)))
                '>' -> movements.add(Pos(1, 0))
                '<' -> movements.add(Pos(-1, 0))
                '^' -> movements.add(Pos(0, -1))
                'v' -> movements.add(Pos(0, 1))
            }
        }
    }
    return Warehouse(Map(robot, boxes, walls), movements)
}