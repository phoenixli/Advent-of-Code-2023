fun main() {
    fun part1(input: List<String>): Int {
        val max = HashMap<String, Int>()
        max["red"] = 12
        max["green"] = 13
        max["blue"] = 14

        var result = 0
        input.forEach {
            var impossible = false
            val gameStr = it.substring(0, it.indexOf(':'))
            val gameId = gameStr.substring(it.indexOf(' ')).trim().toInt()

            val cubeStr = it.substring(it.indexOf(':') + 1)
            val cubeSet = cubeStr.split(';').toTypedArray()
            for (set in cubeSet) {
                val cubes = set.split(',').map { cube -> cube.trim() }.toTypedArray()
                for (cube in cubes) {
                    val (countStr, color) = cube.split(' ')
                    if (countStr.toInt() > max[color]!!) {
                        impossible = true
                        break
                    }
                }
                if (impossible) {
                    break
                }
            }

            if (!impossible) {
                result += gameId
            }
        }

        return result
    }

    fun part2(input: List<String>): Int {
        var result = 0

        input.forEach {
            val max = HashMap<String, Int>()
            max["red"] = 0
            max["green"] = 0
            max["blue"] = 0

            val cubeStr = it.substring(it.indexOf(':') + 1)
            val cubeSet = cubeStr.split(';').toTypedArray()
            for (set in cubeSet) {
                val cubes = set.split(',').map { cube -> cube.trim() }.toTypedArray()
                for (cube in cubes) {
                    val (countStr, color) = cube.split(' ')
                    if (countStr.toInt() > max[color]!!) {
                        max[color] = countStr.toInt()
                    }
                }
            }


            result += max["red"]!! * max["green"]!! * max["blue"]!!
        }

        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    println("=== test output ===")
    part2(testInput).println()
//    check(part1(testInput) == 1)

    val input = readInput("Day02")
    println("=== test output: part1 ===")
    part1(input).println()
    println("=== test output: part2 ===")
    part2(input).println()
}

