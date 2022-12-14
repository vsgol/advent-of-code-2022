fun main() {
    fun part1(): Int {
        val input = readInput("Day01")
        val (last, max) = input.fold(0 to 0) { acc, s ->
            if (s.isEmpty()) {
                0 to maxOf(acc.second, acc.first)
            } else {
                acc.first + s.toInt() to acc.second
            }
        }
        return maxOf(max, last)
    }

    fun part2(): Int {
        val input = readInput("Day01")
        val (last, maxList) = input.fold<String, Pair<Int, MutableList<Int>>>(0 to mutableListOf()) { acc, s ->
            if (s.isEmpty()) {
                acc.second.add(acc.first)
                0 to acc.second
            } else {
                acc.first + s.toInt() to acc.second
            }
        }
        maxList.add(last)
        println(maxList)
        return maxList.sorted().takeLast(3).sum()
    }


    println(part1())
    println(part2())
}
