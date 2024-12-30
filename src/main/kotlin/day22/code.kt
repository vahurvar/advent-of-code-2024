package day22

import adventofcode2024.utils.getAoC2024Input

private val testInput = """
    1
    10
    100
    2024
""".trimIndent()

fun main() {
    val input = getAoC2024Input("22")

    println(solveFirst(testInput)) // 37327623
    println(solveFirst(input)) // 16299144133
}

private fun solveFirst(input: String): Long {
    return parseInput(input).sumOf { getNextNumbers(it) }
}

private fun getNextNumbers(secret: Long, iterations: Int = 2000): Long {
    var current = secret
    repeat(iterations) {
        current = getNext(current)
    }
    return current
}

private fun getNext(secret: Long): Long {
    var temp = secret
    temp = (temp xor (temp * 64)) % 16777216
    temp = (temp xor (temp / 32)) % 16777216
    temp = (temp xor (temp * 2048)) % 16777216
    return temp % 16777216
}

private fun parseInput(input: String): List<Long> {
    return input.trim().lines().map { it.toLong() }
}
