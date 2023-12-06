import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    fun parseInput(input: String): List<Int> {
        return input.substring(input.indexOf(':') + 1)
            .trim()
            .split(' ')
            .map { str -> str.trim() }
            .filter { str -> str.isNotEmpty() }
            .map { str -> str.toInt() }
            .toList()
    }

    fun parseInputV2(input: String): Long {
        val list = input.substring(input.indexOf(':') + 1)
            .trim()
            .split(' ')
            .map { str -> str.trim() }
            .filter { str -> str.isNotEmpty() }
            .toList()
        return list.joinToString("").toLong()
    }

    fun getTravelDistance(totalTimeMs: Long, holdMs: Long): Long {
        return holdMs * (totalTimeMs - holdMs)
    }

    fun part1(input: List<String>): Int {
        val times = parseInput(input[0])
        val maxDistances = parseInput(input[1])
        var result = 1
        for (timeIdx in times.indices) {
            var numWaysToWin = 0
            for (i in 1 until times[timeIdx]) {
                if (getTravelDistance(times[timeIdx].toLong(), i.toLong())
                    > maxDistances[timeIdx]) {
                    numWaysToWin++
                }
            }
            result *= numWaysToWin
        }

        return result
    }

    fun part2(input: List<String>): Long {
        val time = parseInputV2(input[0])
        val maxDistance = parseInputV2(input[1])

//        var numWaysToWin = 0
//        for (i in 1 until time) {
//            if (getTravelDistance(time, i) > maxDistance) {
//                numWaysToWin++
//            }
//        }


        // An optimized way using quadratic formula.
        // If we are holding down the button for x milliseconds,
        // and the total time is t milliseconds,
        // the total distance traveled is x * (t - x), or tx - x^2
        // We want to solve for when tx - x^2 > maxDist, or x^2 - tx - maxDist > 0
        // ax^2 + bx + c = 0 means x = (-b +- sqrt(b^2 - 4ac) / 2a
        val highRoot = time + sqrt((time * time - 4 * maxDistance).toDouble()) / 2
        val lowRoot = time - sqrt((time * time - 4 * maxDistance).toDouble()) / 2

        return (ceil(highRoot - 1) - floor(lowRoot + 1)).toLong() + 1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    println("=== test output ===")
    part2(testInput).println()

    val input = readInput("Day06")
    println("=== test output: part1 ===")
    part1(input).println()
    println("=== test output: part2 ===")
    part2(input).println()
}

