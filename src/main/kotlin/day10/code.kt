package day10

import adventofcode2024.utils.getAoC2024Input
import utils.Point
import utils.isPointInBounds

private val testInput = """
    89010123
    78121874
    87430965
    96549874
    45678903
    32019012
    01329801
    10456732
""".trimIndent()

fun main() {
    val input = getAoC2024Input("10")

    println(solveFirst(testInput)) // 36
    println(solveFirst(input)) // 638

    println(solveSecond(testInput)) // 81
    println(solveSecond(input)) // 1289
}

private fun solveFirst(input: String): Int {
    val parsedInput = parseInput(input)
    return getTrailheads(parsedInput).sumOf { getNumberOfPathsTo9SingleVisit(parsedInput, it) }
}

private fun solveSecond(input: String): Int {
    val parsedInput = parseInput(input)
    return getTrailheads(parsedInput).sumOf { getNrOfAllPathsTo9(parsedInput, it) }
}

private fun parseInput(input: String): List<List<Int>> = input
    .lines()
    .map { line -> line.map { it.digitToInt() } }

private fun getTrailheads(input: List<List<Int>>): Set<Point> {
    val trailheads = mutableSetOf<Point>()

    for (i in input.indices) {
        for (j in input[i].indices) {
            if (input[i][j] == 0) {
                trailheads.add(Point(i, j))
            }
        }
    }

    return trailheads
}

private fun getNumberOfPathsTo9SingleVisit(input: List<List<Int>>, head: Point): Int {
    var count = 0

    val visited = mutableSetOf<Point>()
    val stack = mutableListOf(head)

    while (stack.isNotEmpty()) {
        val point = stack.removeLast()

        if (!visited.add(point)) continue

        if (input[point.x][point.y] == 9) {
            count++
            continue
        }

        point.neighboursXY()
            .filter { isPointInBounds(it, input) }
            .filter { input[it.x][it.y] - input[point.x][point.y] == 1 }
            .forEach { stack.add(it) }
    }

    return count
}

private fun getNrOfAllPathsTo9(input: List<List<Int>>, head: Point): Int {
    var paths = 0

    val visitedPaths = mutableSetOf<Set<Point>>()

    val stack = mutableListOf(setOf(head))

    while (stack.isNotEmpty()) {
        val path = stack.removeLast()

        if (!visitedPaths.add(path)) {
            continue
        }

        val point = path.last()

        if (input[point.x][point.y] == 9) {
            paths++
            continue
        }

        point.neighboursXY()
            .filter { isPointInBounds(it, input) }
            .filter { input[it.x][it.y] - input[point.x][point.y] == 1 }
            .forEach { stack.add(path + it) }
    }

    return paths
}

