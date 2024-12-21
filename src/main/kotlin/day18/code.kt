package day18

import adventofcode2024.utils.getAoC2024Input
import utils.Point
import java.util.*

private val testInput = """
    5,4
    4,2
    4,5
    3,0
    2,1
    6,3
    2,4
    1,5
    0,6
    3,3
    2,6
    5,1
    1,2
    5,5
    2,5
    6,5
    1,4
    0,4
    6,4
    1,1
    6,1
    1,0
    0,5
    1,6
    2,0
""".trimIndent()

fun main() {
    val testDimensions = 6
    val testFinish = Point(6, 6)

    val dimensions = 70
    val finish = Point(70, 70)

    val input = getAoC2024Input("18")

    println(solveFirst(testInput, testDimensions, testFinish, 12)) // 22
    println(solveFirst(input, dimensions, finish, 1024)) // 330

    println(solveSecond(testInput, testDimensions, testFinish)) // 6,1
    println(solveSecond(input, dimensions, finish)) // 10,38
}

private fun parseObstacles(input: String, limit: Int? = null): List<Point> {
    val bytes = input
        .trim()
        .split("\n")
        .map { it.split(",") }
        .map { Point(it[0].toInt(), it[1].toInt()) }

    return if (limit != null) bytes.take(limit) else bytes
}

private fun solveFirst(input: String, dimensions: Int, finish: Point, limit: Int): Int {
    val start = Point(0, 0)

    val obstacles = parseObstacles(input, limit).toSet()
    val distances = dijkstra(dimensions, start, obstacles)

    return distances[finish]!!
}

private fun solveSecond(input: String, dimensions: Int, finish: Point): String {
    val start = Point(0, 0)

    val allObstacles = parseObstacles(input)

    var i = 0
    var currentObstacles = allObstacles.subList(0, i).toSet()
    while (i < allObstacles.size) {
        val distances = dijkstra(dimensions, start, currentObstacles)
        if (distances[finish] == Int.MAX_VALUE) {
            val previous = allObstacles[i - 1]
            return "${previous.x},${previous.y}"
        }
        i++
        currentObstacles = allObstacles.subList(0, i).toSet()
    }

    error("No solution found")
}

private fun dijkstra(dimensions: Int, start: Point, obstacles: Set<Point>): Map<Point, Int> {
    data class Path(val current: Point, val path: Set<Point>, val distance: Int)

    val distances = initDistances(dimensions, start)
    val visited = mutableSetOf<Point>()

    val queue = PriorityQueue<Path>(compareBy { it.distance })
    queue.add(Path(start, setOf(start), 0))

    while (queue.isNotEmpty()) {
        val (current, path, distance) = queue.poll()

        if (current in visited) continue

        visited.add(current)
        val newDistance = minOf(distances[current]!!, distance)
        distances[current] = newDistance

        val neighbours = current.neighboursXY()
            .filter { isValidPoint(it, dimensions) }
            .filter { it !in obstacles }

        neighbours.forEach {
            queue.add(Path(it, path + it, newDistance + 1))
        }
    }

    return distances
}

private fun isValidPoint(point: Point, dimensions: Int): Boolean {
    return point.x in 0..dimensions && point.y in 0..dimensions
}

private fun initDistances(dimensions: Int, start: Point): MutableMap<Point, Int> {
    val distances = mutableMapOf<Point, Int>()
    for (i in 0..dimensions) {
        for (j in 0..dimensions) {
            distances[Point(i, j)] = Int.MAX_VALUE
        }
    }
    distances[start] = 0
    return distances
}
