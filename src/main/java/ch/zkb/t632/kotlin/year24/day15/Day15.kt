package ch.zkb.t632.kotlin.year24.day15

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day15_test")
    check(solve(testInput), 9021)

    val input = readInput("2024", "Day15")
    val resultPart1 = solve(input)

    println("Solution of Part2: $resultPart1")

}

private fun solve(input: List<String>): Long = input.parseInputs().solve()


private data class Warehouse(val map: Map, val movements: List<Pos>) {
    fun canMove(movement: Pos): Boolean {
        var pos = map.robot
        pos += movement
        if (map.walls.contains(pos)) {
            return false
        }
        val box = map.boxes.findBox(pos)
        if (box != null) {
            return canMove(box, movement)
        }
        return true
    }

    private fun canMove(box: Box, movement: Pos, boxes: MutableSet<Box> = mutableSetOf()): Boolean {
        val pos1 = box.start + movement
        val pos2 = box.end + movement
        if (map.walls.contains(pos1)) return false
        if (map.walls.contains(pos2)) return false
        val box1 = map.boxes.findBox(pos1)
        val box2 = map.boxes.findBox(pos2)
        if (box1 != null && !boxes.contains(box1)) {
            boxes.add(box1)
            if (!canMove(box1, movement, boxes)) {
                return false
            }
        }
        if (box2 != null && !boxes.contains(box2)) {
            boxes.add(box2)
            if (!canMove(box2, movement, boxes)) {
                return false
            }
        }
        return true
    }

    fun move(movement: Pos) {
        map.robot += movement
        shiftBoxes(map.robot, movement)
    }

    private fun shiftBoxes(pos: Pos, movement: Pos) {
        val box = map.boxes.findBox(pos)
        if (box != null) {
            val boxes = findBoxes(box, movement)
            map.boxes.removeAll(boxes)
            val movedBoxes = boxes.map { Box(it.start + movement, it.end + movement) }
            map.boxes.addAll(movedBoxes)
        }
    }

    private fun findBoxes(box: Box, movement: Pos, boxes: MutableSet<Box> = mutableSetOf()): Set<Box> {
        boxes += box
        val pos1 = box.start + movement
        val pos2 = box.end + movement
        val box1 = map.boxes.findBox(pos1)
        val box2 = map.boxes.findBox(pos2)
        if (box1 != null && !boxes.contains(box1)) {
            findBoxes(box1, movement, boxes)
        }
        if (box2 != null && !boxes.contains(box2)) {
            findBoxes(box2, movement, boxes)
        }
        return boxes
    }
}

private data class Map(var robot: Pos, val boxes: MutableSet<Box>, val walls: Set<Pos>)

private data class Box(val start: Pos, val end: Pos)

private data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
}

private fun Set<Box>.findBox(pos: Pos): Box? {
    val right = Box(pos, Pos(pos.x + 1, pos.y))
    if (contains(right)) {
        return right
    }
    val left = Box(Pos(pos.x - 1, pos.y), pos)
    if (contains(left)) {
        return left
    }
    return null
}


private fun Warehouse.solve(): Long {
    for (movement in movements) {
        if (canMove(movement)) {
            move(movement)
        }
    }
    return map.boxes.sumOf { (start, _) -> 100L * start.y + start.x }
}


private fun List<String>.parseInputs(): Warehouse {
    var robot = Pos(0, 0)
    val boxes: MutableSet<Box> = mutableSetOf()
    val walls: MutableSet<Pos> = mutableSetOf()
    val movements: MutableList<Pos> = mutableListOf()
    val input = this
    for ((y, row) in input.withIndex()) {
        for ((x, ch) in row.withIndex()) {
            when (ch) {
                '@' -> robot = Pos(x * 2, y)
                '#' -> {
                    walls.add(Pos(x * 2, y))
                    walls.add(Pos(x * 2 + 1, y))
                }
                'O' -> boxes.add(Box(Pos(x * 2, y), Pos(x * 2 + 1, y)))
                '>' -> movements.add(Pos(1, 0))
                '<' -> movements.add(Pos(-1, 0))
                '^' -> movements.add(Pos(0, -1))
                'v' -> movements.add(Pos(0, 1))
            }
        }
    }
    return Warehouse(Map(robot, boxes, walls), movements)
}





