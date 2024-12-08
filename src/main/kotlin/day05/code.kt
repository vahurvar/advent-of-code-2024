package day05

import adventofcode2024.utils.getAoC2024Input

private val testInput = """
    47|53
    97|13
    97|61
    97|47
    75|29
    61|13
    75|53
    29|13
    97|29
    53|29
    61|53
    97|53
    61|29
    47|13
    75|47
    97|75
    47|61
    75|61
    47|29
    75|13
    53|13
    
    75,47,61,53,29
    97,61,53,29,13
    75,29,13
    75,97,47,61,53
    61,13,29
    97,13,75,29,47
""".trimIndent()


fun main() {
    val input = getAoC2024Input("05")

    println(solveFirst(testInput)) // 143
    println(solveFirst(input)) // 5091

    println(solveSecond(testInput)) // 123
    println(solveSecond(input)) // 4681
}

private fun solveFirst(input: String): Int {
    val (rules, updates) = parseInput(input)

    return updates
        .filter { isCorrectlyOrdered(it, getRules(rules)) }
        .sumOf { it[it.size / 2] }
}

private fun solveSecond(input: String): Int {
    val (rules, updates) = parseInput(input)
    val rulesMap = getRules(rules)

    return updates
        .filter { !isCorrectlyOrdered(it, rulesMap) }
        .map { sort(it, rulesMap) }
        .sumOf { it[it.size / 2] }
}

private fun getRules(rules: List<Pair<Int, Int>>): Map<Int, Set<Int>> = rules
    .groupBy { it.second }
    .mapValues { it.value.map { it.first }.toSet() }

private fun sort(update: List<Int>, rulesMap: Map<Int, Set<Int>>): List<Int> = update
    .sortedWith(Comparator { a: Int, b: Int ->
        val bRules = rulesMap[b] ?: emptySet()

        if (bRules.contains(a)) {
            return@Comparator -1
        }

        return@Comparator 0
    })

private fun isCorrectlyOrdered(update: List<Int>, rules: Map<Int, Set<Int>>): Boolean {
    val visited = mutableSetOf<Int>()

    for (current in update.reversed()) {
        val rule = rules[current] ?: emptySet()
        if (visited.intersect(rule).isNotEmpty()) {
            return false
        }
        visited.add(current)
    }

    return true
}

private fun parseInput(input: String): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
    val (rules, updates) = input.split("\n\n")

    val rulesList = rules.split("\n")
        .map { rule ->
            val (a, b) = rule.split("|").map { it.toInt() }
            a to b
        }

    val updatesList = updates.split("\n")
        .map { it.split(",").map { it.toInt() } }

    return rulesList to updatesList
}
