package day23

import adventofcode2024.utils.getAoC2024Input


private val testInput = """
    kh-tc
    qp-kh
    de-cg
    ka-co
    yn-aq
    qp-ub
    cg-tb
    vc-aq
    tb-ka
    wh-tc
    yn-cg
    kh-ub
    ta-co
    de-co
    tc-td
    tb-wq
    wh-td
    ta-ka
    td-qp
    aq-cg
    wq-ub
    ub-vc
    de-ta
    wq-aq
    wq-vc
    wh-yn
    ka-de
    kh-ta
    co-tc
    wh-qp
    tb-vc
    td-yn
""".trimIndent()

fun main() {
    val input = getAoC2024Input("23")

    println(solveFirst(testInput)) // 7
    println(solveFirst(input)) // 1437

    println(solveSecond(testInput)) // co,de,ka,ta
    println(solveSecond(input)) // da,do,gx,ly,mb,ns,nt,pz,sc,si,tp,ul,vl
}

private fun solveSecond(input: String): String {
    val map = parseInput(input)
    val cliques = findCliques(map)
    return cliques.maxBy { it.size }
        .sorted()
        .joinToString(",")
}

private fun solveFirst(input: String): Int {
    return findTriangles(parseInput(input)).count {
        it.any { it.startsWith("t") }
    }
}

fun findTriangles(map: Map<String, Set<String>>): Set<Set<String>> {
    val triangles: MutableSet<Set<String>> = HashSet()

    for ((node, connections) in map) {
        if (connections.size < 2) continue

        for (conn1 in connections) {
            for (conn2 in connections) {
                if (conn1 != conn2 && map[conn1]!!.contains(conn2)) {
                    triangles.add(setOf(node, conn1, conn2))
                }
            }
        }
    }

    return triangles
}

private fun findCliques(graph: Map<String, Set<String>>): Set<Set<String>> {
    val cliques = mutableSetOf<Set<String>>()
    bronKerbosch(graph, mutableSetOf(), graph.keys.toMutableSet(), mutableSetOf(), cliques)
    return cliques
}

private fun bronKerbosch(
    graph: Map<String, Set<String>>,
    currentClique: MutableSet<String>,
    candidates: MutableSet<String>,
    excluded: MutableSet<String>,
    cliques: MutableSet<Set<String>>
) {
    if (candidates.isEmpty() && excluded.isEmpty()) {
        cliques.add(currentClique.toSet()) // Found a maximal clique
        return
    }

    val candidatesCopy = candidates.toSet() // Avoid modifying the original set during iteration
    for (node in candidatesCopy) {
        currentClique.add(node)

        val neighbors = graph[node] ?: emptySet()
        val newCandidates = candidates.intersect(neighbors).toMutableSet()
        val newExcluded = excluded.intersect(neighbors).toMutableSet()

        bronKerbosch(graph, currentClique, newCandidates, newExcluded, cliques)

        currentClique.remove(node)
        candidates.remove(node)
        excluded.add(node)
    }
}

private fun parseInput(input: String): Map<String, Set<String>> {
    val map = mutableMapOf<String, Set<String>>()

    for (line in input.trim().lines()) {
        val (from, to) = line.split("-")
        map[from] = map.getOrDefault(from, emptySet()) + to
        map[to] = map.getOrDefault(to, emptySet()) + from
    }

    return map
}
