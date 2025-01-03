package day24

import adventofcode2024.utils.getAoC2024Input
import java.util.*

private val testInput = """
    x00: 1
    x01: 1
    x02: 1
    y00: 0
    y01: 1
    y02: 0

    x00 AND y00 -> z00
    x01 XOR y01 -> z01
    x02 OR y02 -> z02
""".trimIndent()

private val testInput2 = """
    x00: 1
    x01: 0
    x02: 1
    x03: 1
    x04: 0
    y00: 1
    y01: 1
    y02: 1
    y03: 1
    y04: 1

    ntg XOR fgs -> mjb
    y02 OR x01 -> tnw
    kwq OR kpj -> z05
    x00 OR x03 -> fst
    tgd XOR rvg -> z01
    vdt OR tnw -> bfw
    bfw AND frj -> z10
    ffh OR nrd -> bqk
    y00 AND y03 -> djm
    y03 OR y00 -> psh
    bqk OR frj -> z08
    tnw OR fst -> frj
    gnj AND tgd -> z11
    bfw XOR mjb -> z00
    x03 OR x00 -> vdt
    gnj AND wpb -> z02
    x04 AND y00 -> kjc
    djm OR pbm -> qhw
    nrd AND vdt -> hwm
    kjc AND fst -> rvg
    y04 OR y02 -> fgs
    y01 AND x02 -> pbm
    ntg OR kjc -> kwq
    psh XOR fgs -> tgd
    qhw XOR tgd -> z09
    pbm OR djm -> kpj
    x03 XOR y03 -> ffh
    x00 XOR y04 -> ntg
    bfw OR bqk -> z06
    nrd XOR fgs -> wpb
    frj XOR qhw -> z04
    bqk OR frj -> z07
    y03 OR x01 -> nrd
    hwm AND bqk -> z03
    tgd XOR rvg -> z12
    tnw OR pbm -> gnj
""".trimIndent()

fun main() {
    val input = getAoC2024Input("24")

    println(solveFirst(testInput)) // 4
    println(solveFirst(testInput2)) // 2024
    println(solveFirst(input)) // 42883464055378
}

private fun solveFirst(input: String): Long {
    val (values, gates) = parseInput(input)
    val mutableVales = values.toMutableMap()

    val queue: Queue<Gate> = LinkedList(gates)

    while (queue.isNotEmpty()) {
        val gate = queue.poll()
        val (input1, input2, output, operation) = gate

        if (input1 !in mutableVales || input2 !in mutableVales) {
            queue.offer(gate)
            continue
        }

        val i1 = mutableVales[input1]!!
        val i2 = mutableVales[input2]!!

        val value = when (operation) {
            "AND" -> i1 and i2
            "OR" -> i1 or i2
            "XOR" -> i1 xor i2
            else -> error("Unknown OP: $operation")
        }

        mutableVales[output] = value
    }

    return mutableVales.entries
        .filter { it.key.startsWith("z") }
        .sortedBy { it.key }
        .joinToString("") { it.value.toString() }
        .reversed()
        .toLong(2)
}

private data class Gate(val input1: String, val input2: String, val output: String, val operation: String)

private fun parseInput(input: String): Pair<Map<String, Int>, List<Gate>> {
    val (a, b) = input.trim().split("\n\n")
    val wires = a.lines().associate {
        val (name, value) = it.split(": ")
        name to value.toInt()
    }

    val gates = b.lines().map {
        val (inputs, output) = it.split(" -> ")
        val (input1, operation, input2) = inputs.split(" ")
        Gate(input1, input2, output.trim(), operation)
    }

    return wires to gates
}
