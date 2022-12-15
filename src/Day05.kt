fun handleInput(input: List<String>): Pair<List<MutableList<Char>>, List<List<Int>>> {
    val cratesInfo = input.takeWhile { it.isNotEmpty() }.reversed()
    val commandsInfo = input.takeLastWhile { it.isNotEmpty() }

    val length = cratesInfo.first().length
    val crates = List((length + 1) / 4) { mutableListOf<Char>() }
    for (line in cratesInfo) {
        for (i in 1 until length step 4) {
            val char = line[i]
            if (char != ' ' && char !in '0'..'9') {
                crates[i / 4].add(char)
            }
        }
    }

    // Just take all numbers from commands
    val commands = commandsInfo.map { it.split(" ").mapNotNull(String::toIntOrNull) }

    return crates to commands
}

fun main() {
    fun part1(): String {
        val input = readInput("Day05")
        val (crates, commands) = handleInput(input)
        for ((i, target, dest) in commands) {
            repeat(i) {crates[dest - 1].add(crates[target - 1].removeLast())}
        }
        return crates.filter { it.isNotEmpty() }.joinToString("") { it.last().toString() }
    }

    fun part2(): String {
        val input = readInput("Day05")
        val (crates, commands) = handleInput(input)
        for ((i, target, dest) in commands) {
            crates[dest - 1].addAll(crates[target - 1].takeLast(i))
            repeat(i) { crates[target - 1].removeLast() }
        }
        return crates.filter { it.isNotEmpty() }.joinToString("") { it.last().toString() }
    }

    println(part1())
    println(part2())
}
