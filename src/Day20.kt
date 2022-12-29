fun main() {
    fun MutableList<IndexedValue<Long>>.encrypt() {
        indices.forEach { i ->
            val newIndex = indexOfFirst { it.index == i }
            val indexedValue = removeAt(newIndex)
            add((newIndex + indexedValue.value).mod(size), indexedValue)
        }
    }

    fun List<IndexedValue<Long>>.getResult(): Long {
        val zero = indexOfFirst { it.value == 0L }
        return listOf(1000, 2000, 3000).sumOf { get((zero + it) % size).value }
    }

    fun part1(): Long {
        val input = readInput("Day20")
        val data = input.mapIndexed { index, value -> IndexedValue(index, value.toLong())}.toMutableList()
        data.encrypt()
        return data.getResult()
    }

    fun part2(): Long {
        val input = readInput("Day20")
        val data = input.mapIndexed { index, value -> IndexedValue(index, 811_589_153L * value.toLong())}.toMutableList()
        repeat(10) { data.encrypt() }
        return data.getResult()
    }

    println(part1())
    println(part2())
}
