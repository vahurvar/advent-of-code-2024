package day11

import adventofcode2024.utils.getAoC2024Input
import utils.measureAndLogExecutionTime

private const val testInput = "125 17"

fun main() = measureAndLogExecutionTime {
    val input = getAoC2024Input("11")

    val part1Blinks = 25
    println(solve(testInput, part1Blinks)) // 55312
    println(solve(input, part1Blinks)) // 193899

    val part2Blinks = 75
    println(solve(testInput, part2Blinks)) // 65601038650482
    println(solve(input, part2Blinks)) // 229682160383225
}

private fun solve(input: String, blinks: Int): Long = splitInput(input).sumOf { nrOfStonesAfterBlinks(it, blinks) }

private val cache = mutableMapOf<Pair<String, Int>, Long>()

private fun nrOfStonesAfterBlinks(stone: String, blinks: Int): Long {
    if (cache.containsKey(stone to blinks)) return cache[stone to blinks]!!

    val result = when {
        blinks == 0 -> {
            return 1
        }
        stone == "0" -> {
            nrOfStonesAfterBlinks("1", blinks - 1)
        }
        stone.length % 2 == 0 -> {
            val mid = stone.length / 2
            val left = stone.substring(0, mid)
            val right = trimLeadingZeros(stone.substring(mid))
            nrOfStonesAfterBlinks(left, blinks - 1) + nrOfStonesAfterBlinks(right, blinks - 1)
        }
        else -> {
            val multipliedBy2024 = (stone.toLong() * 2024).toString()
            nrOfStonesAfterBlinks(multipliedBy2024, blinks - 1)
        }
    }

    cache[stone to blinks] = result
    return result
}

private fun trimLeadingZeros(input: String): String {
    if (input == "0" || input.isEmpty()) return input

    var index = 0
    while (index < input.length && input[index] == '0') {
        index++
    }

    return if (index == input.length) "0" else input.substring(index)
}

private fun splitInput(input: String): List<String> = input
    .trim()
    .split(" ")
    .map { it.trim() }
