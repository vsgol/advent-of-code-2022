fun main() {
    var calm = true
    var mod = 1
    fun parseOperation(operationString: String): (Int) -> Int {
        val operands = operationString.substringAfter("Operation: new = ")
            .split(' ')
        val operation: (Long, Long) -> Long = when (operands[1]) {
            "+" -> Long::plus
            "-" -> Long::minus
            "*" -> Long::times
            "/" -> Long::div
            else -> error("No such operation")
        }

        return { x ->
            (operation.invoke(
                if (operands[0] == "old") x.toLong() else operands[0].toLong(),
                if (operands[2] == "old") x.toLong() else operands[2].toLong()
            ) % mod).toInt()
        }
    }

    class Monkey(
        val items: MutableList<Int>,
        val operation: (Int) -> Int,
        val divideBy: Int,
        val ifTrue: Int,
        val ifFalse: Int
    ) {
        constructor(list: List<String>) : this(
            items = list[0].substringAfter("Starting items: ")
                .split(", ")
                .map(String::toInt)
                .toMutableList(),
            operation = parseOperation(list[1]),
            divideBy = list[2].substringAfter("divisible by ").toInt(),
            ifTrue = list[3].substringAfter("throw to monkey ").toInt(),
            ifFalse = list[4].substringAfter("throw to monkey ").toInt(),
        )

        fun worryLevel(x: Int): Int {
            return if (calm) {
                operation.invoke(x) / 3
            } else {
                operation.invoke(x)
            }
        }

        fun passItems(): Map<Int, List<Int>> {
            inspectionsCounts += items.size
            return items.groupBy({ x ->
                if (worryLevel(x) % divideBy == 0) {
                    ifTrue
                } else {
                    ifFalse
                }
            }, { x ->
                worryLevel(x)
            }).also { items.clear() }
        }

        var inspectionsCounts: Long = 0
    }

    fun playRounds(monkeys: List<Monkey>, rounds: Int) {
        repeat(rounds) {
            for (monkey in monkeys) {
                monkey.passItems().forEach { (i, v) -> monkeys[i].items.addAll(v) }
            }
        }
    }

    fun part1(): Long {
        val input = readInput("Day11")
        val monkeys = input.chunked(7).map { Monkey(it.drop(1)) }
        calm = true
        mod = monkeys.map {it.divideBy}.reduce(Int::times)
        playRounds(monkeys, 20)
        return monkeys.map { it.inspectionsCounts }.sortedDescending().take(2).reduce(Long::times)
    }

    fun part2(): Long {
        val input = readInput("Day11")
        val monkeys = input.chunked(7).map { Monkey(it.drop(1)) }
        calm = false
        mod = monkeys.map {it.divideBy}.reduce(Int::times)
        playRounds(monkeys, 10000)
        return monkeys.map { it.inspectionsCounts }.sortedDescending().take(2).reduce(Long::times)
    }

    println(part1())
    println(part2())
}
