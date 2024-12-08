package day07

import adventofcode2024.utils.getAoC2024Input
import utils.measureAndLogExecutionTime

private val testInput = """
    190: 10 19
    3267: 81 40 27
    83: 17 5
    156: 15 6
    7290: 6 8 6 15
    161011: 16 10 13
    192: 17 8 14
    21037: 9 7 18 13
    292: 11 6 16 20
""".trimIndent()

private typealias Operation = (Long, Long) -> Long

fun main() = measureAndLogExecutionTime {
    val input = getAoC2024Input("07")

    val add = { a: Long, b: Long -> a + b }
    val multiply = { a: Long, b: Long -> a * b }
    val concat = { a: Long, b: Long -> "$a$b".toLong() }

    val firstPartOperations = listOf(add, multiply)
    println(solve(testInput, firstPartOperations)) // 3749
    println(solve(input, firstPartOperations)) // 1260333054159

    val secondPartOperations = firstPartOperations + concat
    println(solve(testInput, secondPartOperations)) // 11387
    println(solve(input, secondPartOperations)) // 162042343638683
}

private fun solve(input: String, operations: List<Operation>): Long = parse(input)
    .filter { (target, elements) -> canReachTarget(target, elements, operations) }
    .sumOf { (target) -> target }

private fun canReachTarget(target: Long, elements: List<Long>, operations: List<Operation>): Boolean {
    fun recursion(index: Int, currentValue: Long): Boolean {
        if (index == elements.size) {
            return currentValue == target
        }

        if (currentValue > target) {
            return false
        }

        for (operation in operations) {
            val result = operation(currentValue, elements[index])
            if (recursion(index + 1, result)) {
                return true
            }
        }

        return false
    }

    return recursion(1, elements.first())
}

private fun parse(input: String): List<Pair<Long, List<Long>>> = input.trim()
    .lines()
    .map {
        val (target, values) = it.split(": ")
        val elements = values.split(" ").map { it.toLong() }
        target.toLong() to elements
    }
