fun main() {
    data class KnotPos(val r: Int = 0, val c: Int = 0) {
        fun up() = KnotPos(r - 1, c)
        fun down() = KnotPos(r + 1, c)
        fun left() = KnotPos(r, c - 1)
        fun right() = KnotPos(r, c + 1)

        fun makeMove(c: String) = when(c) {
            "U" -> up()
            "D" -> down()
            "L" -> left()
            "R" -> right()
            else -> error("bad command")
        }

        fun touchingPos() = listOf(this, up(), down(), left(), right())
        fun touchingDiag() = listOf(up().left(), up().right(), down().left(), down().right())

        fun isNeighbour(other: KnotPos): Boolean = (touchingPos() + touchingDiag()).contains(other)

        fun follow(other: KnotPos): KnotPos {
            if (isNeighbour(other)) {
                return this
            }
            return touchingPos().firstOrNull { it.touchingPos().contains(other) }
                ?: touchingDiag().first { it.isNeighbour(other) }
        }
    }

    fun part1(): Int {
        val input = readInput("Day09")
        var head = KnotPos()
        var tail = KnotPos()
        val positions: MutableList<KnotPos> = mutableListOf()
        for (command in input) {
            val (c, n) = command.split(' ')
            repeat(n.toInt()) {
                head = head.makeMove(c)
                tail = tail.follow(head)
                positions.add(tail)
            }
        }
        return positions.toSet().size
    }

    fun part2(): Int {
        val input = readInput("Day09")
        var head = KnotPos()
        var prevKnot: KnotPos
        val knots = Array(9) { KnotPos() }
        val positions: MutableList<KnotPos> = mutableListOf()
        for (command in input) {
            val (c, n) = command.split(' ')
            repeat(n.toInt()) {
                head = head.makeMove(c)
                prevKnot = head
                for (i in knots.indices) {
                    knots[i] = knots[i].follow(prevKnot)
                    prevKnot = knots[i]
                }
                positions.add(knots[8])
            }
        }
        return positions.toSet().size
    }

    println(part1())
    println(part2())
}
