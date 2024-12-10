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
    return getTrailheads(parsedInput).sumOf { getPathsTo9(parsedInput, it) }
}

private fun solveSecond(input: String): Int {
    val parsedInput = parseInput(input)
    return getTrailheads(parsedInput).sumOf { getAllPathsTo9(parsedInput, it) }
}

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

private fun parseInput(input: String): List<List<Int>> = input
    .lines()
    .map { line -> line.map { it.digitToInt() } }

private fun getPathsTo9(input: List<List<Int>>, head: Point): Int {
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

        point.neighbours()
            .filter { isPointInBounds(it, input) }
            .filter { input[it.x][it.y] - input[point.x][point.y] == 1 }
            .forEach { stack.add(it) }
    }

    return count
}

private fun getAllPathsTo9(input: List<List<Int>>, head: Point): Int {
    val paths = mutableSetOf<Set<Point>>()

    val stack = mutableListOf(setOf(head))

    while (stack.isNotEmpty()) {
        val path = stack.removeLast()
        val point = path.last()

        if (!paths.add(path)) {
            continue
        }

        if (input[point.x][point.y] == 9) {
            paths.add(path)
            continue
        }

        point.neighbours()
            .filter { isPointInBounds(it, input) }
            .filter { input[it.x][it.y] - input[point.x][point.y] == 1 }
            .filter { it !in path }
            .forEach { stack.add(path + it) }
    }

    return paths
        .map { it.last() }
        .count { input[it.x][it.y] == 9 }
}

