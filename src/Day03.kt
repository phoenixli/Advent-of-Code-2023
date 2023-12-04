fun main() {
    fun isSymbol(char: Char): Boolean {
        return !char.isDigit() && char != '.'
    }

    fun isSymbolInRange(charArray: CharArray, startIdx: Int, endIdx: Int): Boolean {
        for (i in startIdx..endIdx) {
            if (isSymbol(charArray[i])) {
                return true
            }
        }
        return false
    }

    fun isSymbolAround(beforeRow: CharArray, currRow: CharArray, afterRow: CharArray, numStartIdx: Int, numEndIdx: Int): Boolean {
        val startIdx = if (numStartIdx-1 >= 0) numStartIdx-1 else numStartIdx
        val endIdx = if (numEndIdx+1 < currRow.size) numEndIdx+1 else numEndIdx

        if (beforeRow.isNotEmpty() && isSymbolInRange(beforeRow, startIdx, endIdx)) {
            return true
        }

        if (afterRow.isNotEmpty() && isSymbolInRange(afterRow, startIdx, endIdx)) {
            return true
        }

        if (isSymbolInRange(currRow, startIdx, endIdx)) {
            return true
        }

        return false
    }

    fun part1(input: List<String>): Int {
        val rows = input.map { it.toCharArray() }.toList()
        var result = 0

        for (rowId in rows.indices) {
            val beforeRow = if (rowId-1 >= 0) rows[rowId-1] else charArrayOf()
            val currRow = rows[rowId]
            val afterRow = if (rowId+1 < rows.size) rows[rowId+1] else charArrayOf()

            var partNum = ""
            var startIdx = -1
            var endIdx = -1
            for (idx in currRow.indices) {
                if (currRow[idx].isDigit()) {
                    partNum += currRow[idx]
                    if (startIdx < 0) {
                        startIdx = idx
                    }
                } else {
                    if (startIdx >= 0 && idx-1 > 0) {
                        endIdx = idx - 1
                    }

                    if (endIdx > 0 && isSymbolAround(beforeRow, currRow, afterRow, startIdx, endIdx)) {
                        println("$partNum")
                        result += partNum.toInt()
                    }

                    partNum = ""
                    startIdx = -1
                    endIdx = -1
                }
            }

            if (partNum.isNotEmpty() && isSymbolAround(beforeRow, currRow, afterRow, startIdx, currRow.size-1)) {
                println("$partNum")
                result += partNum.toInt()
            }
        }
        return result
    }

    fun getNumBefore(row: CharArray, idx: Int): String? {
        val numBefore = mutableListOf<Char>()
        for (i in idx-1 downTo 0) {
            if (!row[i].isDigit()) {
                break
            }

            numBefore.add(row[i])
        }
        if (numBefore.size > 0) {
            return numBefore.reversed().toCharArray().concatToString()
        }
        return null
    }

    fun getNumAfter(row: CharArray, idx: Int): String? {
        val numAfter = mutableListOf<Char>()
        for (i in idx+1 until row.size) {
            if (!row[i].isDigit()) {
                break
            }

            numAfter.add(row[i])
        }
        if (numAfter.size > 0) {
            return numAfter.toCharArray().concatToString()
        }
        return null
    }

    fun getNumAtIdx(row: CharArray, idx: Int): String? {
        if (!row[idx].isDigit()) {
            return null
        }

        var num = mutableListOf<Char>()

        // Before.
        for (i in idx downTo 0) {
            if (!row[i].isDigit()) {
                break
            }

            num.add(row[i])
        }
        num = num.asReversed()

        // After.
        for (i in idx+1 until row.size) {
            if (!row[i].isDigit()) {
                break
            }

            num.add(row[i])
        }

        if (num.size > 0) {
            return num.toCharArray().concatToString()
        }
        return null
    }

    fun getGearRatio(beforeRow: CharArray, currRow: CharArray, afterRow: CharArray, gearIdx: Int): Int {
        val parts = mutableListOf<String>()

        // Check curr row.
        var numBefore = getNumBefore(currRow, gearIdx)
        if (numBefore != null) {
            parts.add(numBefore)
        }

        var numAfter = getNumAfter(currRow, gearIdx)
        if (numAfter != null) {
            parts.add(numAfter.toString())
        }

        if (beforeRow.isNotEmpty()) {
            val numAbove = getNumAtIdx(beforeRow, gearIdx)
            if (numAbove != null) {
                parts.add(numAbove)
            } else {
                numBefore = getNumBefore(beforeRow, gearIdx)
                if (numBefore != null) {
                    parts.add(numBefore)
                }

                numAfter = getNumAfter(beforeRow, gearIdx)
                if (numAfter != null) {
                    parts.add(numAfter.toString())
                }
            }
        }

        if (afterRow.isNotEmpty()) {
            val numAbove = getNumAtIdx(afterRow, gearIdx)
            if (numAbove != null) {
                parts.add(numAbove)
            } else {
                numBefore = getNumBefore(afterRow, gearIdx)
                if (numBefore != null) {
                    parts.add(numBefore)
                }

                numAfter = getNumAfter(afterRow, gearIdx)
                if (numAfter != null) {
                    parts.add(numAfter.toString())
                }
            }
        }

        if (parts.size == 2) {
//            println("parts[0]: ${parts[0]}")
//            println("parts[1]: ${parts[1]}")
            return parts[0].toInt() * parts[1].toInt()
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        val rows = input.map { it.toCharArray() }.toList()
        var result = 0

        for (rowId in rows.indices) {
            val beforeRow = if (rowId - 1 >= 0) rows[rowId - 1] else charArrayOf()
            val currRow = rows[rowId]
            val afterRow = if (rowId + 1 < rows.size) rows[rowId + 1] else charArrayOf()

            for (idx in currRow.indices) {
                if (currRow[idx] == '*') {
                    result += getGearRatio(beforeRow, currRow, afterRow, idx)
                }
            }
        }

        return result
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day03_test")
//    println("=== test output ===")
//    part2(testInput).println()

    val input = readInput("Day03")
//    println("=== test output: part1 ===")
//    part1(input).println()
    println("=== test output: part2 ===")
    part2(input).println()
}

