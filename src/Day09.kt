fun main() {
    fun parseInput(input: String): List<Int> {
        return input
            .split(' ')
            .map { str -> str.toInt() }
            .toList()
    }

    fun getDifference(list: List<Int>): List<Int> {
        val diff = mutableListOf<Int>()
        for (i in 1 until list.size) {
            diff.add(list[i] - list[i-1])
        }
//        println(diff)
        return diff
    }

    fun isDiffAllZeroes(list: List<Int>): Boolean {
        return list.all { diff -> diff == 0 }
    }

    fun buildRecords(history: List<Int>): List<List<Int>> {
        val records = mutableListOf<List<Int>>()

        records.add(history)
        var diff = getDifference(history)
        while (!isDiffAllZeroes(diff)) {
            records.add(diff)
            diff = getDifference(diff)
        }

        return records
    }

    fun getNextValue(history: List<Int>): Int {
        val records = buildRecords(history)

        var nextValue = 0
        for (record in records.reversed()) {
            nextValue += record.last()
        }
        return nextValue
    }

    fun getPrevValue(history: List<Int>): Int {
        val records = buildRecords(history)

        var prevValue = 0
        for (record in records.reversed()) {
            prevValue = record.first() - prevValue
        }
        return prevValue

    }

    fun part1(input: List<String>): Int {
        var result = 0
        input.forEach {
            result += getNextValue(parseInput(it))
        }
        return result
    }

    fun part2(input: List<String>): Int {
        var result = 0
        input.forEach {
            result += getPrevValue(parseInput(it))
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    println("=== test output ===")
    part2(testInput).println()

    val input = readInput("Day09")
    println("=== test output: part1 ===")
    part1(input).println()
    println("=== test output: part2 ===")
    part2(input).println()
}

