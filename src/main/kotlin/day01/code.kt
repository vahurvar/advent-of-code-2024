package day01

import adventofcode2024.utils.getAoC2024Input
import kotlin.math.abs

private val testInput = """
3   4
4   3
2   5
1   3
3   9
3   3
""".trimIndent()

fun main() {
    val input = getAoC2024Input("01")

    println(solveFirst(testInput)) // 11
    println(solveFirst(input)) // 2 904 518

    println(solveSecond(testInput)) // 31
    println(solveSecond(input)) // 18 650 129
}

private fun solveFirst(input: String): Int {
    val (left, right) = getInputLists(input)

    val leftSorted = left.sorted()
    val rightSorted = right.sorted()

    return leftSorted.zip(rightSorted)
        .sumOf { (l, r) -> abs(l - r) }
}

private fun solveSecond(input: String): Int {
    val (left, right) = getInputLists(input)

    val rightCounts = right.groupingBy { it }
        .eachCount()

    return left.sumOf { it * (rightCounts[it] ?: 0) }
}

private fun getInputLists(input: String): Pair<List<Int>, List<Int>> {
    val lines = input.trim().lines().map { it.split("  ").map { it.trim() } }

    val left = lines.map { it[0].toInt() }
    val right = lines.map { it[1].toInt() }

    return left to right
}
