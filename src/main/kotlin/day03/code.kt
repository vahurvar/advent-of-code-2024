package day03

import adventofcode2024.utils.getAoC2024Input

private const val testInput = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
private const val testInput2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

private val regex = """mul\((-?\d+),(-?\d+)\)""".toRegex()

fun main() {
    val input = getAoC2024Input("03")

    println(solveFirst(testInput)) // 161
    println(solveFirst(input)) // 162 813 399

    println(solveSecond(testInput2)) // 48
    println(solveSecond(input)) // 53783319
}

private fun solveFirst(input: String): Int = regex.findAll(input)
    .sumOf {
        val (a, b) = it.destructured
        a.toInt() * b.toInt()
    }

private fun solveSecond(input: String): Int {
    val enabled = listOf(0) + findAllIndexes(input, "do()")
    val disabled = findAllIndexes(input, "don't()")

    return regex.findAll(input)
        .filter { findLastIndexBefore(it.range.first, enabled) > findLastIndexBefore(it.range.first, disabled) }
        .sumOf {
            val (a, b) = it.destructured
            a.toInt() * b.toInt()
        }
}

private fun findLastIndexBefore(currentIndex: Int, indexes: List<Int>): Int {
    return indexes.lastOrNull { it < currentIndex } ?: -1
}

private fun findAllIndexes(input: String, pattern: String): List<Int> {
    val indexes = mutableListOf<Int>()
    var index = input.indexOf(pattern)
    while (index >= 0) {
        indexes.add(index)
        index = input.indexOf(pattern, index + 1)
    }
    return indexes
}
