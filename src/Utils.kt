import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun findCommonItems(first: String, second: String): List<Char> {
    val common = mutableListOf<Char>()

    val itemMap = mutableMapOf<Char, Int>()
    for (item in first.iterator()) {
        val currQuantity = itemMap[item] ?: 0
        itemMap[item] = currQuantity + 1
    }

    for (item in second.iterator()) {
        if (itemMap.containsKey(item)) {
            common.add(item)
        }
    }

    return common
}

