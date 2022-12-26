fun main() {
    val mainCycles = listOf(20, 60, 100, 140, 180, 220)
    var cycle = 0
    var x = 1
    var res = 0

    fun tick(argument: Int = 0) {
        if (cycle + 1 in mainCycles) {
            res += (cycle + 1) * x
        }

        if (cycle % 40 in x - 1..x + 1) {
            print("##")
        } else {
            print("  ")
        }
        cycle++
        if (cycle % 40 == 0) println()

        x += argument
    }

    fun part1(): Int {
        val input = readInput("Day10")
        for (line in input) {
            when(line) {
                "noop" -> tick()
                else -> {
                    tick()
                    tick(line.split(' ')[1].toInt())
                }
            }
        }
        return res
    }

    println(part1())
}
