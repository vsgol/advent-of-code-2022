fun main() {
    fun parseInput(input: List<String>, floor: Boolean): List<MutableList<Char>> {
        val rock = List(500) { MutableList(1000) { '.' } }
        input.map {
            it.split(" -> ")
                .map { pair -> val (a, b) = pair.split(','); b.toInt() to a.toInt()}
        }
            .forEach {
                it.reduce { (fr, fc), (tr, tc) ->
                    for (i in minOf(fr, tr)..maxOf(fr, tr)) {
                        rock[i][fc] = '#'
                    }
                    for (i in minOf(fc, tc)..maxOf(fc, tc)) {
                        rock[fr][i] = '#'
                    }
                    tr to tc
                }
            }

        val y = rock.indexOfLast { it.contains('#') } + 2
        if (floor) {
            rock[y].fill('#')
        }
        return rock
    }

    fun dropSand(rock: List<MutableList<Char>>): Boolean {
        var (r, c) = 0 to 500
        if (rock[r][c] != '.') {
            return true
        }
        val directions = listOf(1 to 0, 1 to -1, 1 to 1)
        whileLoop@while (true) {
            for ((dr, dc) in directions) {
                if (r + dr in rock.indices && c + dc in rock.first().indices && rock[r + dr][c + dc] == '.') {
                    r += dr
                    c += dc
                    continue@whileLoop
                } else {
                    if (r + dr > rock.lastIndex) {
                        return true
                    }
                }
            }
            rock[r][c] = 'o'
            return false
        }
    }

    fun part1(): Int {
        val input = readInput("Day14")
        val rock = parseInput(input, false)
        var i = 0
        while (!dropSand(rock)) {
            i++
        }
        return i
    }

    fun part2(): Int {
        val input = readInput("Day14")
        val rock = parseInput(input, true)
        var i = 0
        while (!dropSand(rock)) {
            i++
        }
        return i
    }


    println(part1())
    println(part2())
}
