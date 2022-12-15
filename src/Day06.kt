fun findMarker(input: String, windowSize: Int): Int {
    val window = mutableMapOf<Char, Int>()

    repeat(windowSize) { window[input[it]] = window.getOrDefault(input[it], 0) + 1 }

    var i = 0
    while (window.size < windowSize) {
        window[input[i]] = window.getOrDefault(input[i], 0) - 1
        if (window[input[i]] == 0) {
            window.remove(input[i])
        }
        window[input[i + windowSize]] = window.getOrDefault(input[i + windowSize], 0) + 1
        i++
    }
    return i + windowSize
}

fun main() {
    fun part1(): Int {
        val input = readInput("Day06")
        return findMarker(input.first(), 4)
    }

    fun part2(): Int {
        val input = readInput("Day06")
        return findMarker(input.first(), 14)
    }


    println(part1())
    println(part2())
}
