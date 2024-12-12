package ch.zkb.t632.kotlin.year24.day12

import ch.zkb.t632.kotlin.check
import ch.zkb.t632.kotlin.readInput

fun main() {
    val testInput = readInput("2024", "Day12_test")
    check(part1(testInput), 1930)

    val input = readInput("2024", "Day12")
    val resultPart1 = part1(input)
    println("Solution of Part1: $resultPart1")

    check(part2(testInput), 1206)
    val resultPart2 = part2(input)
    println("Solution of Part2: $resultPart2")

}

private fun part1(input: List<String>): Long = input.parseInputs().gardens().sumOf { it.calculateEdgeFence() }

private fun part2(input: List<String>): Long = input.parseInputs().gardens().sumOf { it.calculateCornerFence() }

private data class Pos(val x: Int, val y: Int)

private data class Area(val type: Char, val pos: Pos) {
    fun up(): Area = Area(type, Pos(pos.x, pos.y + 1))
    fun down(): Area = Area(type, Pos(pos.x, pos.y - 1))
    fun right(): Area = Area(type, Pos(pos.x + 1, pos.y))
    fun left(): Area = Area(type, Pos(pos.x - 1, pos.y))
}

private data class Garden(val type: Char, val areas: MutableSet<Area>) {
    fun calculateEdgeFence(): Long = areas.count() * edgesWithoutNeighbour()

    fun calculateCornerFence(): Long = areas.count() * cornerWithoutNeighbour()

    private fun edgesWithoutNeighbour(): Long {
        var fence = 0L
        for (area in areas) {
            if (area.up() !in areas) {
                fence++
            }
            if (area.down() !in areas) {
                fence++
            }
            if (area.right() !in areas) {
                fence++
            }
            if (area.left() !in areas) {
                fence++
            }

        }
        return fence
    }

    private fun cornerWithoutNeighbour(): Long {
        var fence = 0L
        for (area in areas) {
            if (area.right() !in areas && area.up() !in areas) {
                fence++
            }
            if (area.right() !in areas && area.down() !in areas) {
                fence++
            }
            if (area.left() !in areas && area.down() !in areas) {
                fence++
            }
            if (area.left() !in areas && area.up() !in areas) {
                fence++
            }

            if (area.right() in areas && area.up() in areas && area.right().up() !in areas) {
                fence++
            }
            if (area.right() in areas && area.down() in areas && area.right().down() !in areas) {
                fence++
            }
            if (area.left() in areas && area.down() in areas && area.left().down() !in areas) {
                fence++
            }
            if (area.left() in areas && area.up() in areas && area.left().up() !in areas) {
                fence++
            }
        }
        return fence
    }
}

private fun List<List<Area>>.gardens(): Set<Garden> {
    val foundAreas = mutableSetOf<Area>()
    val gardens = mutableSetOf<Garden>()
    for (row in this) {
        for (area in row) {
            if (!foundAreas.contains(area)) {
                val garden = Garden(area.type, mutableSetOf(area))
                findAllNeighbours(area, garden)
                foundAreas += garden.areas
                gardens += garden
            }
        }
    }
    return gardens
}

private fun List<List<Area>>.findAllNeighbours(area: Area, garden: Garden) {
    val neighbours = findNeighbours(area)
    for (neighbour in neighbours) {
        if (!garden.areas.contains(neighbour)) {
            garden.areas += neighbour
            findAllNeighbours(neighbour, garden)
        }
    }
}


private fun List<List<Area>>.findNeighbours(area: Area): Set<Area> {
    val map = this
    return buildSet {
        val up = map.get2DValue(area.up())
        if (up != null) {
            add(up)
        }
        val down = map.get2DValue(area.down())
        if (down != null) {
            add(down)
        }
        val right = map.get2DValue(area.right())
        if (right != null) {
            add(right)
        }
        val left = map.get2DValue(area.left())
        if (left != null) {
            add(left)
        }
    }
}

private fun List<List<Area>>.get2DValue(area: Area): Area? {
    var pos = area.pos
    if (pos.y in indices) {
        val row = get(pos.y)
        if (pos.x in row.indices) {
            val ret = row[pos.x]
            return if (ret.type == area.type) ret else null
        }
    }
    return null
}


private fun List<String>.parseInputs(): List<List<Area>> {
    val input = this
    return buildList {
        for ((y, row) in input.withIndex()) {
            add(buildList {
                for ((x, ch) in row.withIndex()) {
                    add(Area(ch, Pos(x, y)))
                }
            })
        }
    }
}


