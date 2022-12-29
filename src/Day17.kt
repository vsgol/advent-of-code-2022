import java.util.Objects

fun main() {
    val width = 7
    val rocks = listOf(
        listOf(0..3),
        listOf(1..1, 0..2, 1..1),
        listOf(0..2, 2..2, 2..2),
        listOf(0..0, 0..0, 0..0, 0..0),
        listOf(0..1, 0..1),
    )

    val chamber: MutableList<MutableList<Char>> = MutableList(10) { MutableList(width) { '#' } }
    val chamberData: MutableList<Int> = mutableListOf()
    val chamberHeights: MutableList<Int> = mutableListOf()
    val input = readInput("Day17").first()
    var counter = 0
    var rockNum = 0


    fun nextCommand(): Char {
        return input[counter].also { counter = (counter + 1) % input.length }
    }

    fun checkPos(dr: Int, dc: Int, rock: List<IntRange>): Boolean =
        rock.indices.all {
            chamber.getOrElse(dr + it) { MutableList(width) { '.' } }.mapIndexed { i, c ->
                dc >= 0 && rock[it].last() + dc < width && (c == '.' || i - dc !in rock[it])
            }.all { e -> e }
        }

    fun throwRock(chamber: MutableList<MutableList<Char>>) {
        val rock = rocks[rockNum % rocks.size]
        var (r, c) = chamber.size + 3 to 2
        while (true) {
            val d: Char = nextCommand()
            when (d) {
                '<' -> {
                    if (checkPos(r, c - 1, rock)) {
                        c--
                    }
                }

                '>' -> {
                    if (checkPos(r, c + 1, rock)) {
                        c++
                    }
                }
            }
            if (checkPos(r - 1, c, rock)) {
                r--
            } else {
                rock.indices.forEach {
                    if (r + it !in chamber.indices) {
                        chamber.add(MutableList(width) { '.' })
                    }
                    rock[it].forEach { dc -> chamber[r + it][c + dc] = '0' + rockNum % rocks.size }
                }
                chamberData.add(Objects.hash(d, rockNum++ % rocks.size, chamber.takeLast(10).joinToString { it.joinToString("") }))
                chamberHeights.add(chamber.size - 10)
                return
            }
        }
    }

    fun getData(i: Int): Int {
        while (i >= chamberData.size) {
            throwRock(chamber)
        }
        return chamberData[i]
    }

    fun part1(): Int {
        counter = 0
        repeat(2022) { throwRock(chamber) }
        return chamber.size - 10
    }

    fun part2(): Long {
        var i = 1
        var tortoise = getData(i)
        var hare = getData(2 * i)
        while (tortoise != hare) {
            tortoise = getData(++i)
            hare = getData(2 * i)
        }
        var mu = 0
        var j = 0
        tortoise = getData(j)
        while (tortoise != hare) {
            tortoise = getData(++j)
            hare = getData(++i)
            mu++
        }
        var lam = 1
        hare = getData(++j)
        while (tortoise != hare) {
            hare = getData(++j)
            lam++
        }

        println("$lam, $mu")

        val cycle = (1_000_000_000_000L - mu) / lam
        var res = cycle * (chamberHeights.last() - chamberHeights[chamberHeights.lastIndex - lam])
        res += chamberHeights[(1_000_000_000_000L - cycle * lam).toInt()]
        return res - 1
    }

    println(part1())
    println(part2())
}
