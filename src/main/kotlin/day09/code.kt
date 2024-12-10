package day09

import adventofcode2024.utils.getAoC2024Input
import utils.measureAndLogExecutionTime

private const val testInput = "2333133121414131402"

fun main() = measureAndLogExecutionTime {
    val input = getAoC2024Input("09")

    println(solveFirst(testInput)) // 1928
    println(solveFirst(input)) // 6398252054886

    println(solveSecond(testInput)) // 2858
    println(solveSecond(input)) // 6415666220005
}

private fun solveFirst(input: String): Long = checkSum(compress(parse(input)))

private fun solveSecond(input: String): Long = checkSum(compressFiles(parse(input)))

private fun parse(input: String): List<String> {
    val list = mutableListOf<String>()

    var idCounter = 0

    for ((i, c) in input.withIndex()) {
        val cInt = c.digitToInt()

        if (i % 2 == 0) {
            repeat(cInt) { list.add(idCounter.toString()) }
            idCounter++
        } else {
            repeat(cInt) { list.add(".") }
        }
    }
    return list
}

private fun compressFiles(input: List<String>): List<String> {
    // Didn't bother to implement a more efficient solution, runs in 2 seconds
    val files = input.filter { it != "." }
        .distinct()
        .map { it.toInt() }
        .sortedDescending()

    val compressed = input.toMutableList()

    for (file in files) {
        val asString = file.toString()

        val fileIdx = compressed.indexOf(asString)
        val fileEndIdx = compressed.lastIndexOf(asString)

        val fileLength = fileEndIdx - fileIdx + 1

        val whitespaceIdx = findWhitespace(compressed, fileLength)
        if (whitespaceIdx == -1 || whitespaceIdx > fileIdx) {
            continue
        }

        for (i in whitespaceIdx until whitespaceIdx + fileLength) {
            compressed[i] = asString
        }

        for (i in fileIdx..fileEndIdx) {
            compressed[i] = "."
        }

    }

    return compressed
}

private fun findWhitespace(input: List<String>, size: Int): Int {
    for (i in 0 until input.size - size) {
        if (input.subList(i, i + size).all { it == "." }) {
            return i
        }
    }
    return -1
}

private fun compress(input: List<String>): List<String> {
    val arr = input.toMutableList()

    var freeSpaceIdx = search(arr) { it == "." }
    var lastItemIdx = searchRight(arr) { it != "." }

    while (freeSpaceIdx < lastItemIdx) {
        arr[freeSpaceIdx] = arr[lastItemIdx]
        arr[lastItemIdx] = "."

        freeSpaceIdx = search(arr, freeSpaceIdx + 1) { it == "." }
        lastItemIdx = searchRight(arr, lastItemIdx - 1) { it != "." }
    }

    return arr
}

private fun checkSum(input: List<String>): Long =
    input.foldIndexed(0L) { i, acc, c -> acc + i * (c.toLongOrNull() ?: 0) }

private fun search(input: List<String>, start: Int = 0, target: (String) -> Boolean): Int {
    for (i in start until input.size) {
        if (target(input[i])) {
            return i
        }
    }
    return -1
}

private fun searchRight(input: List<String>, end: Int = input.size - 1, target: (String) -> Boolean): Int {
    for (i in end downTo 0) {
        if (target(input[i])) {
            return i
        }
    }
    return -1
}
