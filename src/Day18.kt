import java.util.*

fun main() {
    val blocks: MutableSet<Triple<Int, Int, Int>> = mutableSetOf()
    val directions = listOf(
        listOf(1, 0, 0),
        listOf(-1, 0, 0),
        listOf(0, 1, 0),
        listOf(0, -1, 0),
        listOf(0, 0, 1),
        listOf(0, 0, -1),
    )
    fun parseInput(input: List<String>) {
        input.map {
            val regex = Regex("^(\\d+),(\\d+),(\\d+)$")
            val (x1, x2, x3) = regex.matchEntire(it)!!.destructured.toList().map(String::toInt)
            blocks.add(Triple(x1, x2, x3))
        }
    }

    fun part1(): Int {
        val input = readInput("Day18")
        parseInput(input)
        var res = 0
        for ((x1, x2, x3) in blocks) {
            for ((dx1, dx2, dx3) in directions) {
                Triple(x1 + dx1, x2 + dx2, x3 + dx3).let {
                    if (it !in blocks) {
                        res++
                    }
                }
            }
        }
        return res
    }

    fun part2(): Int {
        val input = readInput("Day18")
        parseInput(input)
        val air: MutableSet<Triple<Int, Int, Int>> = mutableSetOf()
        for ((x1, x2, x3) in blocks) {
            for ((dx1, dx2, dx3) in directions) {
                Triple(x1 + dx1, x2 + dx2, x3 + dx3).let {
                    if (it !in blocks) {
                        air.add(it)
                    }
                }
            }
        }
        val x1r = blocks.minOf { it.first } - 1..blocks.maxOf { it.first } + 1
        val x2r = blocks.minOf { it.second } - 1..blocks.maxOf { it.second } + 1
        val x3r = blocks.minOf { it.third } - 1..blocks.maxOf { it.third } + 1
        var res = 0
        val queue = ArrayDeque<Triple<Int, Int, Int>>()
        queue.addLast(Triple(x1r.first, x2r.first, x3r.first))
        val visited: MutableSet<Triple<Int, Int, Int>> = mutableSetOf(queue.first)

        while (queue.isNotEmpty()) {
            val (x1, x2, x3) = queue.removeFirst()
            for ((dx1, dx2, dx3) in directions) {
                Triple(x1 + dx1, x2 + dx2, x3 + dx3).let { child ->
                    if (child in visited) {
                        return@let
                    }
                    if (child in blocks) {
                        res++
                        return@let
                    }
                    if (x1 + dx1 in x1r &&
                        x2 + dx2 in x2r &&
                        x3 + dx3 in x3r) {
                        visited.add(child)
                        queue.addLast(child)
                    }
                }
            }
        }

        return res
    }

    println(part1())
    println(part2())
}
