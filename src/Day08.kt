import kotlin.math.max

private enum class Direction(private val dr: Int = 0, private val dc: Int = 0) {
    U(dr = -1), D(dr = 1), L(dc = -1), R(dc = 1);

    fun nextFunction(pos: Pair<Int, Int>): Pair<Int, Int> {
        val (r, c) = pos
        return (r + dr) to (c + dc)
    }
}

fun main() {
    fun part1(): Int {
        val input = readInput("Day08")
        val forest: List<List<Int>> = input.map { it.map { num -> num - '0' } }
        val visible: List<MutableList<Int>> = List(forest.size) { MutableList(forest[0].size) { 0 } }

        fun scanFunction(h: Int, pos: Pair<Int, Int>): Int {
            val (r, c) = pos
            if (forest[r][c] > h) {
                visible[r][c]++
            }
            return max(h, forest[r][c])
        }

        repeat(forest[0].size) {
            generateSequence(0 to it, Direction.D::nextFunction)
                .takeWhile { (r, c) -> r in forest.indices && c in forest[0].indices }
                .fold(-1) { h, pos -> scanFunction(h, pos) }
            generateSequence(forest.size - 1 to it, Direction.U::nextFunction)
                .takeWhile { (r, c) -> r in forest.indices && c in forest[0].indices }
                .fold(-1) { h, pos -> scanFunction(h, pos) }
        }
        repeat(forest.size) {
            generateSequence(it to 0, Direction.R::nextFunction)
                .takeWhile { (r, c) -> r in forest.indices && c in forest[0].indices }
                .fold(-1) { h, pos -> scanFunction(h, pos) }
            generateSequence(it to forest[0].size - 1, Direction.L::nextFunction)
                .takeWhile { (r, c) -> r in forest.indices && c in forest[0].indices }
                .fold(-1) { h, pos -> scanFunction(h, pos) }
        }
        return visible.sumOf { it.count { v -> v > 0 } }
    }

    fun part2(): Int {
        val input = readInput("Day08")
        val forest: List<List<Int>> = input.map { it.map { num -> num - '0' } }
        val visible: List<MutableList<MutableList<Int>>> =
            List(forest.size) { MutableList(forest[0].size) { mutableListOf() } }

        repeat(forest.size) { row ->
            repeat(forest[0].size) { col ->
                Direction.values().forEach {
                    var count = 0
                    fun compare(a: Pair<Int, Int>, b: Pair<Int, Int>): Boolean {
                        count++
                        return forest[a.first][a.second] < forest[b.first][b.second]
                    }
                    generateSequence(it.nextFunction(row to col), it::nextFunction)
                        .takeWhile { (r, c) ->
                            r in forest.indices
                                    && c in forest[0].indices
                                    && compare(r to c, row to col)
                        }
                        .count()
                    visible[row][col].add(count)
                }
            }
        }
        return visible.maxOf { it.maxOf { row -> row.reduce(Int::times) } }
    }

    println(part1())
    println(part2())
}
