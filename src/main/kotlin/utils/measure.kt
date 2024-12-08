package utils

fun measureAndLogExecutionTime(block: () -> Unit) {
    val start = System.nanoTime()
    block()
    val seconds = (System.nanoTime() - start) / 1_000_000_000.0
    println("Execution time: $seconds seconds")
}
