interface Monkey {
    val value: Long
    val reverseValue: Long
    fun contains(monkey: String): Boolean
}

fun main() {
    val monkeys = mutableMapOf<String, Monkey>()
    val reverseMonkeys = mutableMapOf<String, Monkey>()
    class SimpleMonkey(override val value : Long): Monkey {
        override val reverseValue: Long = value
        override fun contains(monkey: String): Boolean {
            return false
        }
    }

    class HumanMonkey(override val value: Long): Monkey {
        override val reverseValue: Long = value
        override fun contains(monkey: String): Boolean {
            return true
        }
    }

    class ComplexMonkey(val first: String, val second: String, val operation: (Long, Long) -> Long): Monkey {
        override val value: Long
            get() = operation.invoke(monkeys.getValue(first).value, monkeys.getValue(second).value)
        override val reverseValue: Long
            get() = operation.invoke(reverseMonkeys.getValue(first).value, reverseMonkeys.getValue(second).value)

        override fun contains(monkey: String): Boolean {
            return monkeys.getValue(first).contains(monkey) || monkeys.getValue(second).contains(monkey)
        }
    }

    fun parseInput(input: List<String>) {
        for (line in input) {
            if (line.length > 10) {
                val regex = Regex("(\\w+): (\\w+) (.) (\\w+)")
                val (monkey, first, op, second) = regex.matchEntire(line)?.groupValues?.drop(1) ?: error("wrong regex")
                monkeys[monkey] = ComplexMonkey(first, second, when(op) {
                    "+" -> Long::plus
                    "-" -> Long::minus
                    "*" -> Long::times
                    "/" -> Long::div
                    else -> error("no such operation")
                })
                reverseMonkeys[first] = ComplexMonkey(monkey, second, when(op) {
                    "+" -> Long::minus  // x = f + s => f = x - s
                    "-" -> {x, s -> -s - x}  // x = f - s => f = -s - x
                    "*" -> Long::div  // x = f * s => f = x/s
                    "/" -> Long::times  // x = f / s => f = x * s
                    else -> error("no such operation")
                })
                reverseMonkeys[second] = ComplexMonkey(monkey, first, when(op) {
                    "+" -> Long::minus  // x = f + s => s = x - f
                    "-" -> {x, f -> f - x}  // x = f - s => s = f - x
                    "*" -> Long::div  // x = f * s => s = x/f
                    "/" -> {x, f -> f / x}  // x = f / s => s = f / x
                    else -> error("no such operation")
                })
            } else {
                val regex = Regex("(\\w+): (\\d+)")
                val (monkey, value) = regex.matchEntire(line)?.groupValues?.drop(1) ?: error("wrong regex")
                if (monkey == "humn") {
                    monkeys[monkey] = HumanMonkey(value.toLong())
                } else {
                    monkeys[monkey] = SimpleMonkey(value.toLong())
                    reverseMonkeys[monkey] = SimpleMonkey(value.toLong())
                }
            }
        }
    }

    fun part1(): Long {
        val input = readInput("Day21")
        parseInput(input)
        return monkeys.getValue("root").value
    }

    fun part2(): Long {
        val input = readInput("Day21")
        parseInput(input)
        val middleMonkey = monkeys.getValue("root") as ComplexMonkey
        if (monkeys.getValue(middleMonkey.first).contains("humn")) {
            reverseMonkeys[middleMonkey.first] = SimpleMonkey(monkeys.getValue(middleMonkey.second).value)
        } else {
            reverseMonkeys[middleMonkey.second] = SimpleMonkey(monkeys.getValue(middleMonkey.first).value)
        }
        return reverseMonkeys.getValue("humn").reverseValue
    }

    println(part1())
    println(part2())
}
