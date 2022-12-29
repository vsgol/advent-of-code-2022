fun main() {
    val valveRate: MutableMap<String, Int> = mutableMapOf()
    val children: MutableMap<String, Set<String>> = mutableMapOf()
    fun parseInput(input: List<String>) {
        input.map {
            val regex = Regex("Valve ([A-Z]+) has flow rate=(\\d+); tunnels? leads? to valves? ([A-Z, ]+)$")
            val (valve, rate, dest) = regex.matchEntire(it)!!.destructured
            valveRate[valve] = rate.toInt()
            children[valve] = dest.split(", ").toSet()
        }
    }

    val memory: MutableMap<Triple<String, Int, Set<String>>, Int> = mutableMapOf()

    fun maxRate(pos: String, timeLeft: Int, closedValves: Set<String>): Int {
        if (timeLeft == 2 && pos in closedValves) {
            return valveRate.getValue(pos)
        } else if (timeLeft <= 1 || closedValves.isEmpty()) {
            return 0
        }
        return memory.getOrPut(Triple(pos, timeLeft, closedValves)) {
            var res = 0
            if (pos in closedValves) {
                res = valveRate.getValue(pos) * (timeLeft - 1) +
                        children.getValue(pos).maxOf { maxRate(it, timeLeft - 2, closedValves - pos) }
            }
            res = maxOf(res, children.getValue(pos).maxOf { maxRate(it, timeLeft - 1, closedValves) })
            res
        }
    }

    fun part1(): Int {
        val input = readInput("Day16")
        parseInput(input)
        return maxRate("AA", 30, valveRate.keys.filter { valveRate[it] != 0 }.toSet())
    }

    fun part2(): Int {
        val input = readInput("Day16")
        parseInput(input)
        val valves = valveRate.keys.filter { valveRate[it] != 0 }.toSet()
        var res = 0

        fun Set<String>.subsets(size: Int): Sequence<Set<String>> {
            if (size == 1) return sequenceOf(setOf(valves.first()))

            return sequence {
                for (s in subsets(size - 1)) {
                    for (e in this@subsets - s) {
                        yield(s + e)
                    }
                }
            }
        }

        for (myValves in valves.subsets(valves.size / 2) + valves.subsets(valves.size / 2 - 1) + valves.subsets(valves.size / 2 + 1)) {
            val elValves = valves - myValves
            val newRes = maxOf(res, maxRate("AA", 26, myValves) + maxRate("AA", 26, elValves))
            if (newRes > res) {
                println(newRes)
                res = newRes
            }
        }
        return res
    }

    println(part1())
    println(part2())
}
