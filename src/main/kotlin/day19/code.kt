package day19

import adventofcode2024.utils.getAoC2024Input

private val testInput = """
    r, wr, b, g, bwu, rb, gb, br

    brwrr
    bggr
    gbbr
    rrbgbr
    ubwu
    bwurrg
    brgr
    bbrgwb
""".trimIndent()

fun main() {
    val input = getAoC2024Input("19")

    println(solveFirst(testInput)) // 6
    println(solveFirst(input)) // 242

    println(solveSecond(testInput)) // 16
    println(solveSecond(input)) // 595975512785325
}

private fun solveFirst(input: String): Int {
    val (patterns, towels) = parseInput(input)
    return towels.count { countWays(it, patterns) > 0 }
}

private fun solveSecond(input: String): Long {
    val (patterns, towels) = parseInput(input)
    return towels.sumOf { countWays(it, patterns) }
}

private val cache = mutableMapOf<Pair<String, List<String>>, Long>()

fun countWays(text: String, patterns: List<String>): Long {
    fun recursive(currentText: String): Long {
        val key = currentText to patterns

        if (key in cache) {
            return cache[key]!!
        }

        if (currentText.isEmpty()) {
            return 1L
        }

        val totalWays = patterns.filter { currentText.startsWith(it) }
            .sumOf { recursive(currentText.substring(it.length)) }

        cache[key] = totalWays
        return totalWays
    }

    return recursive(text)
}

private fun parseInput(input: String): Pair<List<String>, List<String>> {
    val (rules, messages) = input.trim().split("\n\n")
    val splitRules = rules.lines().flatMap { it.split(",") }.map { it.trim() }

    return splitRules to messages.lines()
}
