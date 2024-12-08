package day02

import adventofcode2024.utils.getAoC2024Input
import kotlin.math.abs

private val testInput = """
    7 6 4 2 1
    1 2 7 8 9
    9 7 6 2 1
    1 3 2 4 5
    8 6 4 4 1
    1 3 6 7 9
""".trimIndent()

fun main() {
    val input = getAoC2024Input("02")

    println(solveFirst(testInput)) // 2
    println(solveFirst(input)) // 390

    println(solveSecond(testInput)) // 4
    println(solveSecond(input)) // 439
}

private fun solveFirst(input: String): Int = parseInput(input).count { checkIfSafe(it) }
private fun solveSecond(input: String): Int = parseInput(input).count { checkIfSafeWithDamper(it) }

private fun checkIfSafeWithDamper(input: List<Int>): Boolean {
    if (checkIfSafe(input)) return true

    for (i in input.indices) {
        val withOneRemoved = input.toMutableList().apply { removeAt(i) }
        if (checkIfSafe(withOneRemoved)) return true
    }

    return false
}

private fun checkIfSafe(input: List<Int>): Boolean {
    return isDescendingAndHasMaxDiffOfThree(input) || isAscendingAndHasMaxDiffOfThree(input)
}

private fun parseInput(input: String): List<List<Int>> {
    return input.trim()
        .lines()
        .map { it.split(" ").map { s -> s.toInt() } }
}

private fun isDescendingAndHasMaxDiffOfThree(input: List<Int>): Boolean = input
    .windowed(2)
    .all { (x, y) -> x > y && hasMaxDifferenceOfThree(x, y) }

private fun isAscendingAndHasMaxDiffOfThree(input: List<Int>): Boolean = input
    .windowed(2)
    .all { (x, y) -> x < y && hasMaxDifferenceOfThree(x, y) }

private fun hasMaxDifferenceOfThree(a: Int, b: Int): Boolean = abs(a - b) <= 3
