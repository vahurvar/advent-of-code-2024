package day04

import adventofcode2024.utils.getAoC2024Input

private val testInput = """
    MMMSXXMASM
    MSAMXMSMSA
    AMXSXMAAMM
    MSAMASMSMX
    XMASAMXAMM
    XXAMMXXAMA
    SMSMSASXSS
    SAXAMASAAA
    MAMMMXMMMM
    MXMXAXMASX
""".trimIndent()

fun main() {
    val input = getAoC2024Input("04")

    println(solveFirst(testInput)) // 18
    println(solveFirst(input)) // 2500

    println(solveSecond(testInput)) // 9
    println(solveSecond(input)) // 1933
}

private fun solveFirst(input: String): Int = getAllStrings(parseInput(input))
        .flatMap { getAllOccurrences("XMAS", it) }
        .count()

private fun solveSecond(input: String): Int {
    val parsedInput = parseInput(input)

    return parsedInput.flatMapIndexed { i, row ->
        row.mapIndexed { j, _ -> isValidXMas(parsedInput, i, j) }
    }.count { it }
}

private fun isValidXMas(input: List<String>, i: Int, j: Int): Boolean {
    if (getLocationOrNull(input, i, j) != 'A') {
        return false
    }

    val leftTop = getLocationOrNull(input, i - 1, j - 1) ?: return false
    val rightTop = getLocationOrNull(input, i - 1, j + 1) ?: return false
    val leftBottom = getLocationOrNull(input, i + 1, j - 1) ?: return false
    val rightBottom = getLocationOrNull(input, i + 1, j + 1) ?: return false

    if (!(leftTop == 'M' && rightBottom == 'S' || leftTop == 'S' && rightBottom == 'M')) {
        return false
    }

    if (!(rightTop == 'M' && leftBottom == 'S' || rightTop == 'S' && leftBottom == 'M')) {
        return false
    }

    return true
}

private fun getLocationOrNull(input: List<String>, i: Int, j: Int): Char? = input.getOrNull(i)?.getOrNull(j)
private fun parseInput(input: String): List<String> = input.trim().lines()

private fun getAllStrings(input: List<String>): List<String> {
    val allDirections = mutableListOf<String>()

    // Left to right and reverse
    for (i in input.indices) {
        allDirections.add(input[i])
        allDirections.add(input[i].reversed())
    }

    // Top to bottom and reverse
    for (i in input[0].indices) {
        val sb = StringBuilder()
        for (j in input.indices) {
            sb.append(input[j][i])
        }
        allDirections.add(sb.toString())
        allDirections.add(sb.reversed().toString())
    }

    // Top-left to bottom-right diagonals and reverse
    for (n in 0 until 2 * input.size - 1) {
        val sb = StringBuilder()
        for (j in 0..n) {
            val i = n - j
            if (i in input.indices && j in input.indices) {
                sb.append(input[i][j])
            }
        }
        allDirections.add(sb.toString())
        allDirections.add(sb.reversed().toString())
    }

    // Bottom-left to top-right diagonals and reverse
    for (n in 0 until 2 * input.size - 1) {
        val sb = StringBuilder()
        for (j in 0..n) {
            val i = input.size - 1 - n + j
            if (i in input.indices && j in input.indices) {
                sb.append(input[i][j])
            }
        }
        allDirections.add(sb.toString())
        allDirections.add(sb.reversed().toString())
    }

    return allDirections
}

private fun getAllOccurrences(pattern: String, text: String): List<Int> {
    val occurrences = mutableListOf<Int>()

    var i = 0
    while (i < text.length) {
        val index = text.indexOf(pattern, i)
        if (index == -1) break

        occurrences.add(index)
        i = index + 1
    }

    return occurrences
}
