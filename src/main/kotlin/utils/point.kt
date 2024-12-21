package utils

data class Point(val x: Int, val y: Int) {
    fun neighboursXY(): List<Point> = listOf(
        Point(x + 1, y),
        Point(x - 1, y),
        Point(x, y + 1),
        Point(x, y - 1)
    )

    fun diagonalNeighbours(): List<Point> = listOf(
        Point(x + 1, y + 1),
        Point(x - 1, y - 1),
        Point(x + 1, y - 1),
        Point(x - 1, y + 1)
    )

    fun allNeighbours(): List<Point> = neighboursXY() + diagonalNeighbours()

    override fun toString(): String = "($x, $y)"
}

fun <T> isPointInBounds(point: Point, input: List<List<T>>): Boolean {
    return point.x in input.indices && point.y in input[point.x].indices
}
