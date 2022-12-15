import java.util.*

fun typePriority(c: Char) =
    if (c in 'a'..'z') {
        c - 'a' + 1
    } else {
        c - 'A' + 27
    }

fun getBitSet(rucksack: String): BitSet {
    val res = BitSet(53)
    for (c in rucksack) {
        res.set(typePriority(c), true)
    }
    return res
}

fun main() {
    fun part1(): Int {
        val input = readInput("Day03")
        return input.sumOf {
            val firstHalf = getBitSet(it.substring(0 until it.length/2))
            val secondHalf = getBitSet(it.substring(it.length/2 until it.length))
            firstHalf.and(secondHalf)
            firstHalf.nextSetBit(0)
        }
    }

    fun part2(): Int {
        val input = readInput("Day03")
        val groups = input.withIndex()
            .groupBy { it.index/3 }
            .map { entry -> entry.value.map { it.value } }
        return groups.sumOf { groupMembers ->
            groupMembers.map { getBitSet(it) }
                .reduce { f, s -> f.and(s); f }
                .nextSetBit(0)
        }
    }

    println(part1())
    println(part2())
}
