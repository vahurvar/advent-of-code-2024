package day08

import adventofcode2024.utils.getAoC2024Input

private val testInput = """
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
""".trimIndent()

private typealias Point = Pair<Int, Int>

fun main() {
    val input = getAoC2024Input("08")

    println(solveFirst(testInput)) // 14
    println(solveFirst(input)) // 398

    println(solveSecond(testInput)) // 34
    println(solveSecond(input)) // 1333
}

private fun solveSecond(input: String): Int {
    val parsedInput = parseInput(input)
    val pointsPerFrequency = getUniqueFrequencies(parsedInput).map { findAllPointsOfType(parsedInput, it) }

    val allAntiNodeLocations =
        pointsPerFrequency.flatMap { getAllAntiNodeLocationsWithHarmonics(parsedInput, it) }.toSet()
    val allPoints = pointsPerFrequency.flatten()

    return (allAntiNodeLocations + allPoints).size
}

private fun solveFirst(input: String): Int {
    val parsedInput = parseInput(input)
    return getUniqueFrequencies(parsedInput)
        .map { findAllPointsOfType(parsedInput, it) }
        .flatMap { getAllAntiNodeLocation(parsedInput, it) }
        .toSet().count()
}

private fun getAllAntiNodeLocation(input: List<List<Char>>, points: List<Point>): List<Point> {
    val allAntiNodeLocations = mutableListOf<Point>()

    for ((i, point) in points.withIndex()) {
        for (otherPoint in points.subList(i + 1, points.size)) {
            val locations = getAntiNodeLocation(point, otherPoint)

            if (isInBounds(input, locations.first)) {
                allAntiNodeLocations.add(locations.first)
            }
            if (isInBounds(input, locations.second)) {
                allAntiNodeLocations.add(locations.second)
            }
        }
    }

    return allAntiNodeLocations
}

private fun getAllAntiNodeLocationsWithHarmonics(input: List<List<Char>>, points: List<Point>): List<Point> {
    val allAntiNodeLocations = mutableListOf<Point>()

    for ((i, point) in points.withIndex()) {
        for (otherPoint in points.subList(i + 1, points.size)) {
            allAntiNodeLocations.addAll(getAntiNodeLocationWithHarmonics(input, point, otherPoint))
        }
    }

    return allAntiNodeLocations
}

private fun getAntiNodeLocationWithHarmonics(
    input: List<List<Char>>,
    first: Point,
    second: Point,
): List<Point> {
    val (x1, y1) = first
    val (x2, y2) = second

    val vector = x2 - x1 to y2 - y1
    val reversedVector = -vector.first to -vector.second

    return mutableListOf<Point>().apply {
        addAll(getAllHarmonicsPoints(x2 to y2, vector, input))
        addAll(getAllHarmonicsPoints(x1 to y1, reversedVector, input))
    }
}

private fun getAllHarmonicsPoints(
    point: Point,
    vector: Point,
    input: List<List<Char>>,
): List<Point> {
    return generateSequence(point) { (x, y) -> x + vector.first to y + vector.second }
        .takeWhile { isInBounds(input, it) }
        .toList()
}

private fun getAntiNodeLocation(first: Point, second: Point): Pair<Point, Point> {
    val (x1, y1) = first
    val (x2, y2) = second

    val vector = x2 - x1 to y2 - y1
    val reversedVector = -vector.first to -vector.second

    val firstAntiNode = x2 + vector.first to y2 + vector.second
    val secondAntiNode = x1 + reversedVector.first to y1 + reversedVector.second

    return firstAntiNode to secondAntiNode
}

private fun findAllPointsOfType(input: List<List<Char>>, target: Char): List<Point> = input
    .flatMapIndexed { i, row -> row.mapIndexed { j, char -> char to (i to j) } }
    .filter { it.first == target }
    .map { it.second }

private fun getUniqueFrequencies(input: List<List<Char>>): Set<Char> = input.flatten().filter { it != '.' }.toSet()

private fun parseInput(input: String): List<List<Char>> = input.lines().map { it.toList() }

private fun isInBounds(input: List<List<Char>>, point: Point): Boolean {
    val (x, y) = point
    return x in input.indices && y in input[x].indices
}
