fun main() {
    fun part1(input: List<String>): Int {
        var calibrationSum = 0
        input.forEach {
            var calibrationValue = ""
            val chars = it.toCharArray()
            for (char in chars) {
                if (char.isDigit()) {
                    calibrationValue += char
                    break
                }
            }
            for (char in chars.reversed()) {
                if (char.isDigit()) {
                    calibrationValue += char
                    break
                }
            }
            calibrationSum += calibrationValue.toInt()
        }
        return calibrationSum
    }

    fun part2(input: List<String>): Int {
        val numberMap = HashMap<String, Char> ()
        numberMap["one"] = '1'
        numberMap["two"] = '2'
        numberMap["three"] = '3'
        numberMap["four"] = '4'
        numberMap["five"] = '5'
        numberMap["six"] = '6'
        numberMap["seven"] = '7'
        numberMap["eight"] = '8'
        numberMap["nine"] = '9'
        val numberKeys = numberMap.keys

        var calibrationSum = 0
        input.forEach {
            var calibrationValue = ""
            val chars = it.toCharArray()
            // check for start
            for (i in chars.indices) {
                if (chars[i].isDigit()) {
                    calibrationValue += chars[i]
                    break
                } else {
                    var isFound = false
                    for (word in numberKeys) {
                        if (String(chars).substring(i).startsWith(word)) {
                            calibrationValue += numberMap[word]
                            isFound = true
                            break
                        }
                    }
                    if (isFound) {
                        break
                    }
                }
            }

            // check for end
            for (i in chars.size-1 downTo 0) {
                if (chars[i].isDigit()) {
                    calibrationValue += chars[i]
                    break
                } else {
                    var isFound = false
                    for (word in numberKeys) {
                        if (String(chars).substring(i).startsWith(word)) {
                            calibrationValue += numberMap[word]
                            isFound = true
                            break
                        }
                    }
                    if (isFound) {
                        break
                    }
                }
            }

            calibrationSum += calibrationValue.toInt()
        }
        return calibrationSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    println("=== test output ===")
    part2(testInput).println()
//    check(part1(testInput) == 77)

    val input = readInput("Day01")
    println("=== test output: part1 ===")
    part1(input).println()
    println("=== test output: part2 ===")
    part2(input).println()
}

