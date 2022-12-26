import java.util.LinkedList
import java.util.Queue

fun main() {
    fun List<List<Char>>.findPos(e: Char): Pair<Int, Int> {
        val r = indexOfFirst { it.contains(e) }
        val c = get(r).indexOf(e)
        return r to c
    }

    fun findPath(area: List<MutableList<Char>>, stopChar: (Char) -> Boolean = { false }): Int {
        val (sr, sc) = area.findPos('S')
        area[sr][sc] = 'a'
        val (er, ec) = area.findPos('E')
        area[er][ec] = 'z'

        val directions = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)

        val distance = mutableMapOf((er to ec) to 0)
        val queue: Queue<Pair<Int, Int>> = LinkedList(listOf(er to ec))

        while (queue.isNotEmpty()) {
            val (r, c) = queue.remove()
            val h = area[r][c]

            for ((dr, dc) in directions) {
                if (r + dr to c + dc in distance
                    || r + dr !in area.indices
                    || c + dc !in area.first().indices
                    || area[r + dr][c + dc] < h - 1
                ) {
                    continue
                }

                distance[r + dr to c + dc] = distance.getValue(r to c) + 1
                if (r + dr to c + dc == sr to sc || stopChar(area[r + dr][c + dc])) {
                    return distance.getValue(r + dr to c + dc)
                }
                queue.add(r + dr to c + dc)
            }
        }
        for (i in area.indices) {
            for (j in area.first().indices) {
                if (i to j !in distance)
                    print(area[i][j])
                else
                    print(area[i][j].uppercaseChar())
            }
            println()
        }
        error("no path found")
    }

    fun part1(): Int {
        val input = readInput("Day12")
        val area = input.map { it.toMutableList() }
        return findPath(area)
    }

    fun part2(): Int {
        val input = readInput("Day12")
        val area = input.map { it.toMutableList() }
        return findPath(area) { it == 'a' }
    }


    println(part1())
    println(part2())
}
