package day12

import adventofcode2024.utils.getAoC2024Input
import utils.Point
import utils.isPointInBounds

private val testInput = """
    AAAA
    BBCD
    BBCC
    EEEC
""".trimIndent()

private val testInput2 = """
    RRRRIICCFF
    RRRRIICCCF
    VVRRRCCFFF
    VVRCCCJFFF
    VVVVCJJCFE
    VVIVCCJJEE
    VVIIICJJEE
    MIIIIIJJEE
    MIIISIJEEE
    MMMISSJEEE
""".trimIndent()

private val testInput3 = """
    EEEEE
    EXXXX
    EEEEE
    EXXXX
    EEEEE
""".trimIndent()

private val testInput4 = """
    AAAAAA
    AAABBA
    AAABBA
    ABBAAA
    ABBAAA
    AAAAAA
""".trimIndent()

fun main() {
    val input = getAoC2024Input("12")

    println(solveFirst(testInput)) // 140
    println(solveFirst(testInput2)) // 1930
    println(solveFirst(input)) // 1370258

    println()
    println(solveSecond(testInput)) // 80
    println(solveSecond(testInput2)) // 1206
    println(solveSecond(testInput3)) // 236
    println(solveSecond(testInput4)) // 368
    println(solveSecond(input)) // 805814
}

private fun solveFirst(input: String): Int = findAllRegions(parseInput(input)).sumOf { it.size * getNrOfBoundaries(it) }
private fun solveSecond(input: String): Int = findAllRegions(parseInput(input)).sumOf { it.size * calculateNrSides(it) }

private fun calculateNrSides(region: Set<Point>): Int {
    var corners = 0 // Nr of corners == nr of sides

    for (point in getBoundaryPoints(region)) {
        val topPointInRegion = Point(point.x - 1, point.y) in region
        val topLeftPointInRegion = Point(point.x - 1, point.y - 1) in region
        val leftPointInRegion = Point(point.x, point.y - 1) in region
        val bottomLeftPointInRegion = Point(point.x + 1, point.y - 1) in region
        val bottomPointInRegion = Point(point.x + 1, point.y) in region
        val bottomRightPointInRegion = Point(point.x + 1, point.y + 1) in region
        val rightPointInRegion = Point(point.x, point.y + 1) in region
        val topRightPointInRegion = Point(point.x - 1, point.y + 1) in region

        // All cases with 3 neighbours where point must be in corner
        // External corners
        if (!topPointInRegion && !topLeftPointInRegion && !leftPointInRegion) {
            corners++
        }
        if (!topPointInRegion && !topRightPointInRegion && !rightPointInRegion) {
            corners++
        }
        if (!rightPointInRegion && !bottomRightPointInRegion && !bottomPointInRegion) {
            corners++
        }
        if (!bottomPointInRegion && !bottomLeftPointInRegion && !leftPointInRegion) {
            corners++
        }

        // Internal corners
        if (leftPointInRegion && bottomPointInRegion && !bottomLeftPointInRegion) {
            corners++
        }
        if (bottomPointInRegion && rightPointInRegion && !bottomRightPointInRegion) {
            corners++
        }
        if (rightPointInRegion && topPointInRegion && !topRightPointInRegion) {
            corners++
        }
        if (topPointInRegion && leftPointInRegion && !topLeftPointInRegion) {
            corners++
        }

        // Area inside area
        if (!bottomPointInRegion && !rightPointInRegion && bottomRightPointInRegion) {
            corners++
        }
        if (!leftPointInRegion && !topPointInRegion && topLeftPointInRegion) {
            corners++
        }
        if (!topPointInRegion && !rightPointInRegion && topRightPointInRegion) {
            corners++
        }
        if (!leftPointInRegion && !bottomPointInRegion && bottomLeftPointInRegion) {
            corners++
        }
    }

    return corners
}

private fun parseInput(input: String): List<List<Char>> = input.trim().lines().map { it.toList() }

private fun findAllRegions(grid: List<List<Char>>): List<Set<Point>> {
    val pointsToVisit = grid.indices.flatMap { y -> grid[y].indices.map { x -> Point(x, y) } }
    val allVisited = mutableSetOf<Point>()

    val regions = mutableListOf<Set<Point>>()

    for (point in pointsToVisit) {
        if (point in allVisited) {
            continue
        }

        val region = findRegion(grid, point)

        regions.add(region)
        allVisited.addAll(region)
    }

    return regions
}

private fun findRegion(grid: List<List<Char>>, point: Point): Set<Point> {
    val value = grid[point.x][point.y]

    val region = mutableSetOf<Point>()

    val stack = mutableListOf(point)

    while (stack.isNotEmpty()) {
        val current = stack.removeLast()
        if (current in region) {
            continue
        }
        region.add(current)

        current.neighbours()
            .filter { isPointInBounds(it, grid) }
            .filter { grid[it.x][it.y] == value }
            .forEach { stack.add(it) }
    }

    return region
}

private fun getNrOfBoundaries(region: Set<Point>): Int = region.sumOf {
    it.neighbours().count { neighbour -> neighbour !in region }
}

private fun getBoundaryPoints(region: Set<Point>): Set<Point> = region
    .filter { it.allNeighbours().any { neighbour -> neighbour !in region } }
    .toSet()

