fun intersectionSize(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
    return maxOf(minOf(a.second, b.second) - maxOf(a.first, b.first) + 1, 0)
}

fun fullIntersection(a: Pair<Int, Int>, b: Pair<Int, Int>): Boolean {
    return (a.second >= b.second && a.first <= b.first) ||
            (b.second >= a.second && b.first <= a.first)
}

fun main() {
    fun part1(): Int {
        val input = readInput("Day04")
        return input.count { line ->
            val (first, second) = line.split(',').map { it.split('-').map(String::toInt).zipWithNext().first() }
            fullIntersection(first, second)
        }
    }

    fun part2(): Int {
        val input = readInput("Day04")
        return input.count { line ->
            val (first, second) = line.split(',').map { it.split('-').map(String::toInt).zipWithNext().first() }
            intersectionSize(first, second) > 0
        }
    }

    println(part1())
    println(part2())
}
