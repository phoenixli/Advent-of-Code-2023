fun main() {
    fun parseWinningNumbers(input: String): Set<Int> {
        return input.substring(input.indexOf(':') + 1, input.indexOf('|'))
            .trim()
            .split(' ')
            .map { str -> str.trim() }
            .filterNot { str -> str.isEmpty() }
            .map { str -> str.toInt() }
            .toSet()
    }

    fun parseMyNumbers(input: String): List<Int> {
        return input.substring(input.indexOf('|') + 1)
            .trim()
            .split(' ')
            .map { str -> str.trim() }
            .filterNot { str -> str.isEmpty() }
            .map { str -> str.toInt() }
            .toList()
    }

    fun getScore(winningNumbers: Set<Int>, myNumbers: List<Int>): Int {
        var score = 0
        for (num in myNumbers) {
            if (winningNumbers.contains(num)) {
                if (score == 0) {
                    score = 1
                } else {
                    score *= 2
                }
            }
        }
        return score
    }

    fun part1(input: List<String>): Int {
        var result = 0
        input.forEach {
            val winningNumbers = parseWinningNumbers(it)
//            println("winningNumbers: $winningNumbers")
            val myNumbers = parseMyNumbers(it)
//            println("myNumbers: $myNumbers")
            result += getScore(winningNumbers, myNumbers)
        }
        return result
    }

    fun getMyWinningNumbersCount(winningNumbers: Set<Int>, myNumbers: List<Int>): Int {
        val myWinningNumbers = mutableListOf<Int>()
        for (num in myNumbers) {
            if (winningNumbers.contains(num)) {
                myWinningNumbers.add(num)
            }
        }
        return myWinningNumbers.size
    }

    fun part2(input: List<String>): Int {
        val totalCards = mutableMapOf<Int, Int>()
        input.forEach {
            val cardStr = it.substring(0, it.indexOf(':'))
            val cardId = cardStr.substring(it.indexOf(' ')).trim().toInt()
            if (totalCards[cardId] == null) {
                totalCards[cardId] = 1
            } else {
                totalCards[cardId] = totalCards[cardId]!!.plus(1)
            }

            val winningNumbers = parseWinningNumbers(it)
//            println("winningNumbers: $winningNumbers")
            val myNumbers = parseMyNumbers(it)
//            println("myNumbers: $myNumbers")
            val count = getMyWinningNumbersCount(winningNumbers, myNumbers)
            println("count: $count")
            repeat(totalCards[cardId]!!) {
                for (i in 1..count) {
                    val newCardId = cardId + i
                    val newCardCount =
                        if (totalCards[newCardId] != null) totalCards[newCardId]?.plus(1) else 1
                    totalCards[newCardId] = newCardCount!!
                }
            }
            println("$totalCards")
        }

        var result = 0
        for (cardId in totalCards.keys) {
            result += totalCards[cardId]!!
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day04_test")
//    println("=== test output ===")
//    part2(testInput).println()

    val input = readInput("Day04")
//    println("=== test output: part1 ===")
//    part1(input).println()
    println("=== test output: part2 ===")
    part2(input).println()
}

