package day17

import adventofcode2024.utils.getAoC2024Input
import utils.measureAndLogExecutionTime
import java.util.*
import java.util.stream.IntStream

private val testInput = """
    Register A: 729
    Register B: 0
    Register C: 0

    Program: 0,1,5,4,3,0
""".trimIndent()

private val testInput2 = """
    Register A: 2024
    Register B: 0
    Register C: 0

    Program: 0,3,5,4,3,0
""".trimIndent()

fun main() = measureAndLogExecutionTime {
    val input = getAoC2024Input("17")

    println(solveFirst(testInput)) // 4,6,3,5,6,3,5,2,1,0
    println(solveFirst(input)) // 2,1,0,4,6,2,4,2

    println(solveSecond(testInput2)) // 117440
    println(solveSecond(input)) //
}

private fun solveFirst(input: String): String {
    val (registers, instructions) = parseInput(input)
    return solve(registers, instructions).joinToString(",")
}

private fun solveSecond(input: String): Int? {
    val (registers, instructions) = parseInput(input)

    return IntStream.range(0, Int.MAX_VALUE)
        .parallel()
        .filter { it != registers[0] }
        .filter { solveOutput(listOf(it, 0, 0), instructions, instructions) ?: false }
        .findFirst()
        .toNullableInt()
}

private fun OptionalInt.toNullableInt(): Int? = if (this.isPresent) this.asInt else null

private fun solve(registers: List<Int>, instructions: List<Int>): List<Int> {
    val output = mutableListOf<Int>()

    var (A, B, C) = registers
    var pointer = 0

    while (pointer < instructions.size) {
        val opCode = instructions[pointer]
        val operand = if (pointer + 1 < instructions.size) instructions[pointer + 1] else null

        fun getComboOperandValue(operand: Int?): Int {
            return when (operand) {
                in 0..3 -> operand!!
                4 -> A
                5 -> B
                6 -> C
                else -> error("Invalid operand: $operand")
            }
        }

        when (opCode) {
            0 -> {
                A /= (1 shl getComboOperandValue(operand))
            }

            1 -> {
                B = B xor operand!!
            }

            2 -> {
                B = getComboOperandValue(operand).mod(8)
            }

            3 -> {
                if (A != 0) {
                    pointer = operand!!
                    continue
                }
            }

            4 -> {
                B = B xor C
            }

            5 -> {
                output.add(getComboOperandValue(operand).mod(8))
            }

            6 -> {
                B = A / (1 shl getComboOperandValue(operand))
            }

            7 -> {
                C = A / (1 shl getComboOperandValue(operand))
            }

            else -> error("Invalid opCode: $opCode")
        }

        pointer += 2
    }

    return output
}

private fun solveOutput(registers: List<Int>, instructions: List<Int>, expectedOutput: List<Int>): Boolean? {
    val output = mutableListOf<Int>()

    var (A, B, C) = registers
    var pointer = 0

    while (pointer < instructions.size) {
        val opCode = instructions[pointer]
        val operand = if (pointer + 1 < instructions.size) instructions[pointer + 1] else null

        fun getComboOperandValue(operand: Int?): Int {
            return when (operand) {
                in 0..3 -> operand!!
                4 -> A
                5 -> B
                6 -> C
                else -> error("Invalid operand: $operand")
            }
        }

        when (opCode) {
            0 -> {
                A /= (1 shl getComboOperandValue(operand))
            }

            1 -> {
                B = B xor operand!!
            }

            2 -> {
                B = getComboOperandValue(operand).mod(8)
            }

            3 -> {
                if (A != 0) {
                    pointer = operand!!
                    continue
                }
            }

            4 -> {
                B = B xor C
            }

            5 -> {
                if (output.size >= expectedOutput.size) {
                    return null
                }

                val currentOut = getComboOperandValue(operand).mod(8)
                if (currentOut != expectedOutput[output.size]) {
                    return null
                }
                output.add(currentOut)
            }

            6 -> {
                B = A / (1 shl getComboOperandValue(operand))
            }

            7 -> {
                C = A / (1 shl getComboOperandValue(operand))
            }

            else -> error("Invalid opCode: $opCode")
        }

        pointer += 2
    }

    return output.joinToString(",") == expectedOutput.joinToString(",")
}

private fun parseInput(input: String): Pair<List<Int>, List<Int>> {
    val lines = input.trim().lines()
    val registerA = lines[0].split(":")[1].trim().toInt()
    val registerB = lines[1].split(":")[1].trim().toInt()
    val registerC = lines[2].split(":")[1].trim().toInt()

    val program = input.lines()[4].split(":")[1].trim().split(",").map { it.toInt() }
    return listOf(registerA, registerB, registerC) to program
}
