import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("inputs", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
@Suppress("unused")
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
@Suppress("unused")
fun Any?.println() = println(this)

operator fun <T> List<List<T>>.get(pos: Pair<Int, Int>) = this[pos.first][pos.second]
operator fun <T> List<MutableList<T>>.set(pos: Pair<Int, Int>, value: T) {
    this[pos.first][pos.second] = value
}
