sealed interface Packet : Comparable<Packet> {
    class PacketInt(private val value: Int) : Packet {
        override fun compareTo(other: Packet): Int = when (other) {
            is PacketMin -> 1
            is PacketInt -> value compareTo other.value
            is PacketList -> PacketList(this) compareTo other
        }
    }

    class PacketList(list: List<Packet>) : Packet {
        val values: List<Packet> = list + listOf(PacketMin)

        constructor(value: Packet): this(listOf(value, PacketMin))

        override fun compareTo(other: Packet): Int {
            return when (other) {
                is PacketMin -> 1
                is PacketInt -> this compareTo PacketList(other)
                is PacketList -> {
                    for (i in values.indices) {
                        val res = values[i] compareTo other.values[i]
                        if (res != 0) {
                            return res
                        }
                    }
                    return 0
                }
            }
        }
    }

    object PacketMin : Packet {
        override fun compareTo(other: Packet): Int = when (other) {
            is PacketMin -> 0
            else -> -1
        }

    }
}

fun main() {
    fun parseInput(input: String): Packet {
        val symbols = Regex("[\\[\\],]|(\\d+)").findAll(input).filter { it.value != "," }
        val stack = ArrayDeque<Packet?>()
        for (symbol in symbols) {
            when (symbol.value) {
                "[" -> stack.add(null)
                "]" -> {
                    val elements = ArrayDeque<Packet>()
                    while (stack.last() != null) {
                        elements.addFirst(stack.removeLast()!!)
                    }
                    stack.removeLast()
                    stack.add(Packet.PacketList(elements))
                }
                else -> stack.add(Packet.PacketInt(symbol.value.toInt()))
            }
        }
        return stack.last()!!
    }

    fun part1(): Int {
        val input = readInput("Day13")
        var i = 0
        var res = 0

        input.filter { it.isNotEmpty() }.chunked(2) { (left, right) ->
            i++
            if (parseInput(left) <= parseInput(right)) {
                res += i
            }
        }
        return res
    }

    fun part2(): Int {
        val input = readInput("Day13")

        val divider2 = parseInput("[[2]]")
        val divider6 = parseInput("[[6]]")

        val packets = input.filter { it.isNotEmpty() }.map { parseInput(it) }.toList()
        return (packets.count { it < divider2 } + 1) * (packets.count { it < divider6 } + 2)
    }


    println(part1())
    println(part2())
}
