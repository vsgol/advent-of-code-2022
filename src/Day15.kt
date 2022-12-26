import kotlin.math.abs

fun main() {
    fun Pair<Int, Int>.distance(other: Pair<Int, Int>): Int = abs(first-other.first) + abs(second - other.second)

    fun parseInput(input: List<String>): List<List<Pair<Int, Int>>> {
        val positions = input.map {
            it.substringAfter("Sensor at x=")
                .split(": closest beacon is at x=")
                .map { pos -> val (x, y) = pos.split(", y=").map(String::toInt); y to x}
        }
        return positions
    }

    fun part1(): Int {
        val input = readInput("Day15")
        val positions = parseInput(input)
        val covered = mutableListOf<IntRange>()
        val beacons = mutableListOf<Int>()

        for ((s, b) in positions) {
            val distance = s.distance(b)
            val targetDistance = abs(s.first - 2_000_000)
            if (b.first == 2_000_000) {
                beacons.add(b.second)
            }
            if (targetDistance < distance) {
                val diff = distance - targetDistance
                covered.add((s.second - diff)..(s.second + diff))
            }
        }

        return covered.flatten().distinct().filterNot { beacons.contains(it) }.count()
    }

    fun part2(): Long {
        val input = readInput("Day15")
        val positions = parseInput(input)
        fun checkPos(pos: Pair<Int, Int>): Boolean {
            val (r, c) = pos
            if (r !in 0..4_000_000 || c !in 0..4_000_000) {
                return false
            }
            return positions.none { (s, b) -> val distance = s.distance(b); distance >= s.distance(pos) }
        }

        for ((s, b) in positions) {
            val (r, c) = s
            val distance = s.distance(b) + 1
            for (dr in 0..distance) {
                if (checkPos(r + dr to c + dr - distance)) return r + dr + (c + dr - distance) * 4_000_000L
                if (checkPos(r - dr to c + dr - distance)) return r - dr + (c + dr - distance) * 4_000_000L
                if (checkPos(r + dr to c - dr + distance)) return r + dr + (c - dr + distance) * 4_000_000L
                if (checkPos(r - dr to c - dr + distance)) return r - dr + (c - dr + distance) * 4_000_000L
            }
        }
        error("no points found")
    }


    println(part1())
    println(part2())
}
