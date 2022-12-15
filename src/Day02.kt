enum class GameResult(val score: Int) {
    WIN(6),
    LOSE(0),
    DRAW(3);

    companion object {
        fun fromChar(name: Char): GameResult {
            return when (name) {
                'X' -> LOSE
                'Y' -> DRAW
                'Z' -> WIN
                else -> error("Illegal char")
            }
        }
    }
}

enum class Moves(val score: Int) {
    ROCK(1) {
        override fun beats(): Moves = SCISSORS
        override fun loses(): Moves = PAPER
    },
    PAPER(2) {
        override fun beats(): Moves = ROCK
        override fun loses(): Moves = SCISSORS
    },
    SCISSORS(3) {
        override fun beats(): Moves = PAPER
        override fun loses(): Moves = ROCK
    };

    abstract fun beats(): Moves
    abstract fun loses(): Moves
    fun compare(other: Moves): GameResult =
        when (other) {
            this -> GameResult.DRAW
            this.beats() -> GameResult.LOSE
            this.loses() -> GameResult.WIN
            else -> error("All options have been checked ")
        }

    companion object {
        fun fromChar(name: Char): Moves {
            return when (name) {
                'A', 'X' -> ROCK
                'B', 'Y' -> PAPER
                'C', 'Z' -> SCISSORS
                else -> error("Illegal char")
            }
        }

        fun convert(opMove: Moves, result: GameResult): Moves {
            return when (result) {
                GameResult.LOSE -> opMove.beats()
                GameResult.DRAW -> opMove
                GameResult.WIN -> opMove.loses()
            }
        }
    }
}

fun main() {
    fun part1(): Int {
        fun evalRound(round: String): Int {
            val myMove = Moves.fromChar(round.last())
            val opMove = Moves.fromChar(round.first())
            return opMove.compare(myMove).score + myMove.score
        }

        val input = readInput("Day02")
        return input.sumOf { evalRound(it) }
    }

    fun part2(): Int {
        fun evalRound(round: String): Int {
            val gameResult = GameResult.fromChar(round.last())
            val opMove = Moves.fromChar(round.first())
            return gameResult.score + Moves.convert(opMove, gameResult).score
        }

        val input = readInput("Day02")
        return input.sumOf { evalRound(it) }
    }


    println(part1())
    println(part2())
}
