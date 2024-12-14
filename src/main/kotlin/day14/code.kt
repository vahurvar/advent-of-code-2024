package day14

import adventofcode2024.utils.getAoC2024Input
import utils.Point

private val testInput = """
    p=0,4 v=3,-3
    p=6,3 v=-1,-3
    p=10,3 v=-1,2
    p=2,0 v=2,-1
    p=0,0 v=1,3
    p=3,0 v=-2,-2
    p=7,6 v=-1,-3
    p=3,0 v=-1,-2
    p=9,3 v=2,3
    p=7,3 v=-1,2
    p=2,4 v=2,-3
    p=9,5 v=-3,-3
""".trimIndent()

private data class Robot(val start: Point, val vector: Point)
private data class Dimensions(val width: Int, val height: Int)

fun main() {
    val testDimension = Dimensions(11, 7)
    val dimensions = Dimensions(101, 103)

    val input = getAoC2024Input("14")

    println(solveFirst(testInput, testDimension)) // 12
    println(solveFirst(input, dimensions)) // 216772608

    println(solveSecond(input, dimensions)) // 6888
}

private fun solveSecond(input: String, dimensions: Dimensions): Int {
    val robots = parseInput(input)
    var step = 1

    while (true) {
        val locations = robots.map { calculateRobotLocationAtStep(it, step, dimensions) }

        if (isPossibleTree(locations, dimensions)) {
            val grid = getCurrentLocationsGrid(locations, dimensions)
            println(grid.joinToString("\n") { it.joinToString(", ") })
            return step
        }

        step++
    }
}

private fun isPossibleTree(locations: List<Point>, dimensions: Dimensions): Boolean {
    val grid = getCurrentLocationsGrid(locations, dimensions)

    // Check if any point has robots in all directions for 5 steps
    for (x in 0 until dimensions.width) {
        for (y in 0 until dimensions.height) {
            if (grid[x][y] != '#') {
                continue
            }

            val topFivePoints = (1..5).map { Point(x, (y - it)) }
                .filter { it.x in 0 until dimensions.width && it.y in 0 until dimensions.height }

            if (topFivePoints.size != 5) continue

            val bottomFivePoints = (1..5).map { Point(x, (y + it)) }
                .filter { it.x in 0 until dimensions.width && it.y in 0 until dimensions.height }

            if (bottomFivePoints.size != 5) continue

            val leftFivePoints = (1..5).map { Point((x - it), y) }
                .filter { it.x in 0 until dimensions.width && it.y in 0 until dimensions.height }

            if (leftFivePoints.size != 5) continue

            val rightFivePoints = (1..5).map { Point((x + it), y) }
                .filter { it.x in 0 until dimensions.width && it.y in 0 until dimensions.height }

            if (rightFivePoints.size != 5) continue

            val allValuesMarked = (topFivePoints + bottomFivePoints + leftFivePoints + rightFivePoints)
                .all { grid[it.x][it.y] == '#' }

            if (allValuesMarked) {
                return true
            }
        }
    }

    return false
}

private fun getCurrentLocationsGrid(locations: List<Point>, dimensions: Dimensions): Array<Array<Char>> {
    val grid = Array(dimensions.width) { Array(dimensions.height) { '.' } }

    locations.forEach {
        val (x, y) = it
        grid[x][y] = '#'
    }

    return grid
}

private fun solveFirst(input: String, dimensions: Dimensions): Int {
    val robots = parseInput(input)

    val quadrantCounts = robots.map { calculateRobotLocationAtStep(it, 100, dimensions) }
        .mapNotNull { mapToQuadrants(it, dimensions) }
        .groupingBy { it }
        .eachCount()

    return quadrantCounts.values.reduce { acc, i -> acc * i }
}

private fun mapToQuadrants(point: Point, dimensions: Dimensions): Int? {
    val (x, y) = point
    val xMid = dimensions.width / 2
    val yMid = dimensions.height / 2

    if (x == xMid || y == yMid) {
        return null
    }

    return when {
        x < xMid && y < yMid -> 1
        x > xMid && y < yMid -> 2
        x > xMid && y > yMid -> 3
        else -> 4
    }
}

private fun calculateRobotLocationAtStep(robot: Robot, steps: Int, dimensions: Dimensions): Point {
    val x = (robot.start.x + robot.vector.x * steps).mod(dimensions.width)
    val y = (robot.start.y + robot.vector.y * steps).mod(dimensions.height)
    return Point(x, y)
}

private fun parseInput(input: String): List<Robot> {
    return input.trim().lines().map {
        val parts = it.split(" ")

        val pointParts = parts[0].split("=")[1].split(",")
        val point = Point(pointParts[0].toInt(), pointParts[1].toInt())

        val vectorParts = parts[1].split("=")[1].split(",")
        val vector = Point(vectorParts[0].toInt(), vectorParts[1].toInt())

        Robot(point, vector)
    }
}
