package day25

import adventofcode2024.utils.getAoC2024Input

private val testInput = """
    #####
    .####
    .####
    .####
    .#.#.
    .#...
    .....

    #####
    ##.##
    .#.##
    ...##
    ...#.
    ...#.
    .....

    .....
    #....
    #....
    #...#
    #.#.#
    #.###
    #####

    .....
    .....
    #.#..
    ###..
    ###.#
    ###.#
    #####

    .....
    .....
    .....
    #....
    #.#..
    #.#.#
    #####
""".trimIndent()

fun main() {
    val input = getAoC2024Input("25")

    println(solveFirst(testInput)) // 3
    println(solveFirst(input)) // 3136
}

private fun solveFirst(input: String): Int {
    val (keys, locks) = parseInput(input).partition { it is Key }

    return locks.flatMap {
        keys.map { key -> match(key as Key, it as Lock) }
    }.count { it }
}

private fun match(key: Key, lock: Lock): Boolean {
    val keyValues = key.value
    val lockValues = lock.value

    val keyRows = keyValues.size
    val keyCols = keyValues[0].length

    val lockRows = lockValues.size
    val lockCols = lockValues[0].length

    if (keyRows != lockRows || keyCols != lockCols) {
        return false
    }

    val newMatrix = Array(lockRows) { Array(lockCols) { '.' } }

    // Lock
    for (i in 0 until lockRows) {
        for (j in 0 until lockCols) {
            newMatrix[i][j] = lockValues[i][j]
        }
    }

    // Key
    for (i in 0 until keyRows) {
        for (j in 0 until keyCols) {
            val lockValue = newMatrix[i][j]
            val keyValue = keyValues[i][j]
            if (lockValue == '#' && keyValue == '#') {
                return false
            }
            newMatrix[i][j] = keyValue
        }
    }

    return true
}

sealed class Input(open val value: List<String>)
data class Lock(override val value: List<String>): Input(value)
data class Key(override val value: List<String>): Input(value)

private fun parseInput(input: String): List<Input> {
    return input.trim().split("\n\n").map {
        val lines = it.lines()
        if (lines[0].all { it == '#' }) {
            Lock(lines.drop(1))
        } else {
            Key(lines.drop(1))
        }
    }
}
