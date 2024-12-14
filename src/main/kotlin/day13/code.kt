package day13

import adventofcode2024.utils.getAoC2024Input

private val testInput = """
    Button A: X+94, Y+34
    Button B: X+22, Y+67
    Prize: X=8400, Y=5400

    Button A: X+26, Y+66
    Button B: X+67, Y+21
    Prize: X=12748, Y=12176

    Button A: X+17, Y+86
    Button B: X+84, Y+37
    Prize: X=7870, Y=6450

    Button A: X+69, Y+23
    Button B: X+27, Y+71
    Prize: X=18641, Y=10279
""".trimIndent()

fun main() {
    val input = getAoC2024Input("13")

    println(solveFirst(testInput)) // 480
    println(solveFirst(input)) // 29711

    println(solveSecond(testInput)) // 875318608908
    println(solveSecond(input)) // 94955433618919
}

private fun solveFirst(input: String): Long = parseInput(input).mapNotNull { solve(it) }.sum()

private fun solveSecond(input: String): Long {
    val part2ToAdd = 10000000000000

    return parseInput(input)
        .map {
            val newPrize = Prize(it.prize.x + part2ToAdd, it.prize.y + part2ToAdd)
            it.copy(prize = newPrize)
        }
        .mapNotNull { solve(it) }
        .sum()
}

private fun solve(game: Game): Long? {
    val (aPushes, bPushes) = solveLinearEquation(game) ?: return null
    return calculateTokens(aPushes, bPushes)
}

private fun calculateTokens(aPushes: Long, bPushes: Long): Long = aPushes * ButtonType.A.cost + bPushes * ButtonType.B.cost

private fun solveLinearEquation(game: Game): Pair<Long, Long>? {
    val a1 = game.aButton.x
    val b1 = game.bButton.x
    val c1 = game.prize.x

    val a2 = game.aButton.y
    val b2 = game.bButton.y
    val c2 = game.prize.y

    val determinant = a1 * b2 - a2 * b1

    if (determinant == 0L) {
        return null
    }

    val x = (c1 * b2 - c2 * b1).toDouble() / determinant
    val y = (a1 * c2 - a2 * c1).toDouble() / determinant

    if (!isExact(x) || !isExact(y)) {
        return null
    }

    return x.toLong() to y.toLong()
}

private fun parseInput(input: String): List<Game> {
    fun parseButton(line: String): Button {
        val name = when {
            line.startsWith("Button A") -> ButtonType.A
            line.startsWith("Button B") -> ButtonType.B
            else -> error("Invalid button name")
        }
        val x = line.split("X+")[1].split(",")[0].toLong()
        val y = line.split("Y+")[1].toLong()
        return Button(name, x, y)
    }

    fun parsePrize(line: String): Prize {
        val x = line.split("X=")[1].split(",")[0].toLong()
        val y = line.split("Y=")[1].toLong()
        return Prize(x, y)
    }

    return input.trim().split("\n\n").map {
        val lines = it.lines()
        val aButton = parseButton(lines[0])
        val bButton = parseButton(lines[1])
        val prize = parsePrize(lines[2])
        Game(aButton, bButton, prize)
    }
}

private enum class ButtonType(val cost: Long) { A(3), B(1) }
private data class Button(val name: ButtonType, val x: Long, val y: Long)
private data class Prize(val x: Long, val y: Long)
private data class Game(val aButton: Button, val bButton: Button, val prize: Prize)

private fun isExact(n: Double): Boolean = n == n.toLong().toDouble()
