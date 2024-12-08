package day06

import adventofcode2024.utils.getAoC2024Input
import utils.measureAndLogExecutionTime

private val testInput = """
    ....#.....
    .........#
    ..........
    ..#.......
    .......#..
    ..........
    .#..^.....
    ........#.
    #.........
    ......#...
""".trimIndent()

fun main() = measureAndLogExecutionTime {
    val input = getAoC2024Input("06")

    val parsedTestInput = parseInput(testInput)
    val parsedInput = parseInput(input)

    println(solveFirst(parsedTestInput)) // 41
    println(solveFirst(parsedInput)) // 5067

    println(solveSecond(parsedTestInput)) // 1793
    println(solveSecond(parsedInput)) // 1793
}

private fun solveSecond(input: List<List<Char>>): Int {
    val startPoint = findStartPoint(input)
    val mutableCopy = input.map { it.toMutableList() }

    var looping = 0

    for (p in getVisitedUntilOutOfBounds(input)) {
        val (i, j) = p
        mutableCopy[i][j] = '#'
        if (isLooping(mutableCopy, startPoint)) {
            looping++
        }
        mutableCopy[i][j] = '.'
    }

    return looping
}

private fun isLooping(input: List<List<Char>>, startPoint: Pair<Int, Int>): Boolean {
    val visited = mutableSetOf<Triple<Int, Int, Direction>>()
    var current = startPoint
    var direction = Direction.UP

    while (true) {
        if (!visited.add(Triple(current.first, current.second, direction))) {
            return true
        }

        val next = getNextCoordinate(current, direction)
        val nextValue = getValueOrNull(input, next) ?: break

        if (nextValue == '#') {
            direction = turn90DegreesRight(direction)
        } else {
            current = next
        }
    }

    return false
}

private fun solveFirst(input: List<List<Char>>): Int = getVisitedUntilOutOfBounds(input).size

private fun getVisitedUntilOutOfBounds(input: List<List<Char>>): Set<Pair<Int, Int>> {
    val visited = mutableSetOf<Pair<Int, Int>>()
    var current = findStartPoint(input)
    var direction = Direction.UP

    while (true) {
        visited.add(current)

        val next = getNextCoordinate(current, direction)
        val nextValue = getValueOrNull(input, next) ?: break

        if (nextValue == '#') {
            direction = turn90DegreesRight(direction)
        } else {
            current = next
        }
    }

    return visited
}

private fun getValueOrNull(matrix: List<List<Char>>, current: Pair<Int, Int>): Char? {
    return matrix.getOrNull(current.first)?.getOrNull(current.second)
}

private fun getNextCoordinate(current: Pair<Int, Int>, direction: Direction): Pair<Int, Int> {
    val (x, y) = current
    return when (direction) {
        Direction.UP -> x - 1 to y
        Direction.DOWN -> x + 1 to y
        Direction.LEFT -> x to y - 1
        Direction.RIGHT -> x to y + 1
    }
}

private fun turn90DegreesRight(direction: Direction): Direction = when (direction) {
    Direction.UP -> Direction.RIGHT
    Direction.RIGHT -> Direction.DOWN
    Direction.DOWN -> Direction.LEFT
    Direction.LEFT -> Direction.UP
}

private enum class Direction { UP, DOWN, LEFT, RIGHT }

private fun parseInput(input: String): List<List<Char>> = input.trim().lines().map { it.map { it } }

private fun findStartPoint(input: List<List<Char>>): Pair<Int, Int> {
    for (i in input.indices) {
        for (j in input[i].indices) {
            if (input[i][j] == '^') {
                return i to j
            }
        }
    }
    error("Start point not found")
}
