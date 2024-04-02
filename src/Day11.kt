import kotlin.math.abs

data class Point(val x: Long, val y: Long)

fun getManhattanDistance(point0: Point, point1: Point): Long {
    return abs(point1.x - point0.x) + abs(point1.y - point0.y)
}

fun calculateAdjustments(data: List<String>, expansion: Long): List<Long> {
    val adjustments = mutableListOf<Long>()
    var accumulator = 0L
    for (i in data.indices) {
        if (data[i].all { it == '.' }) {
            accumulator += (expansion - 1L)
        }
        adjustments.add(accumulator)
    }
    return adjustments
}

fun invertListString(input: List<String>): List<String> {
    val result = mutableListOf<String>()
    for (i in input.indices) {
        var str = ""
        for (inputStr in input) {
            str += inputStr[i]
        }
        result.add(str)
    }
    return result
}

fun findGalaxies(input: List<String>): List<Point> {
    val result = mutableListOf<Point>()
    for (i in input.indices) {
        for (j in input[i].indices) {
            if (input[i][j] == '#') {
                result.add(Point(i.toLong(), j.toLong()))
            }
        }
    }
    return result
}

fun getAllDistinctGalaxyPairs(galaxies: List<Point>): List<Pair<Point, Point>> {
    val result = mutableListOf<Pair<Point, Point>>()
    for (i in galaxies.indices) {
        for (j in i+1 until galaxies.size) {
            result.add(Pair(galaxies[i], galaxies[j]))
        }
    }
    return result
}

fun getAdjustedGalaxies(galaxies: List<Point>, rowAdjustments: List<Long>, colAdjustments: List<Long>): List<Point> {
    val result = mutableListOf<Point>()
    for (galaxy in galaxies) {
        result.add(Point(galaxy.x + rowAdjustments[galaxy.x.toInt()], galaxy.y + colAdjustments[galaxy.y.toInt()]))
    }
    return result
}

fun calculateSummedDistance(input: List<String>, expansion: Long): Long {
    val galaxies = findGalaxies(input)
    val rowAdjustment = calculateAdjustments(input, expansion)
    val colAdjustment = calculateAdjustments(invertListString(input), expansion)
    val adjustedGalaxies = getAdjustedGalaxies(galaxies, rowAdjustment, colAdjustment)
    val galaxyPairs = getAllDistinctGalaxyPairs(adjustedGalaxies)
    var result = 0L
    for (pair in galaxyPairs) {
        result += getManhattanDistance(pair.first, pair.second)
    }
    return result
}

fun main() {
    fun part1(input: List<String>): Long {
        return calculateSummedDistance(input, 2)
    }

    fun part2(input: List<String>): Long {
        return calculateSummedDistance(input, 1_000_000)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    println("=== test output ===")
    part2(testInput).println()

    val input = readInput("Day11")
    println("=== test output: part1 ===")
    part1(input).println()
    println("=== test output: part2 ===")
    part2(input).println()
}

