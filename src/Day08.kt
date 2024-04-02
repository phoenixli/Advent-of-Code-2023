data class Node(val left: String, val right: String)

fun parseNode(nodeString: String): Pair<String, Node> {
    val curr = nodeString.substring(0, nodeString.indexOf(' '))
    val left =
        nodeString.substring(
            nodeString.indexOf('(') + 1,
            nodeString.indexOf(','))
    val right =
        nodeString.substring(
            nodeString.lastIndexOf(" ") + 1,
            nodeString.indexOf(')'))
    return Pair(curr, Node(left, right))
}

fun buildMap(input: List<String>): Map<String, Node> {
    val map = mutableMapOf<String, Node>()

    for (inputStr in input) {
        val (curr, node) = parseNode(inputStr)
//        println("curr: $curr, node: $node")
        map[curr] = node
    }

    return map
}

fun main() {
    fun part1(input: List<String>): Int {
        val instructions = input[0].toCharArray()
        val map = buildMap(input.subList(2, input.size))

        var node = map["AAA"]
        var result = 0
        loop@ while (true) {
            for (instruction in instructions) {
                result++
                val next = if (instruction == 'L') node!!.left else node!!.right
                if (next == "ZZZ") {
                    break@loop
                }
                node = map[next]
            }
        }
        return result
    }

    fun findAllStartingNodes(nodes: List<String>): List<String> {
        return nodes.filter { it.endsWith('A') }.toList()
    }

    fun part2(input: List<String>): Int {
        val instructions = input[0].toCharArray()
        val map = buildMap(input.subList(2, input.size))

        val startingNodes = findAllStartingNodes(map.keys.toList())
        val nodes = mutableListOf<Node>()
        for (startingNode in startingNodes) {
            nodes.add(map[startingNode]!!)
        }
        var result = 0
        val results = mutableListOf<Int>()
        loop@ while (true) {
            for (instruction in instructions) {
                result++

                val next = mutableListOf<String>()
                for (node in nodes) {
                    next.add(if (instruction == 'L') node.left else node.right)
                }

                nodes.clear()
                for (n in next) {
                    if (n.endsWith('Z')) {
                        results.add(result)
                    } else {
                        nodes.add(map[n]!!)
                    }
                }

                if (nodes.isEmpty()) {
                    break@loop
                }
            }
        }
        println(results)
        // TODO: calculate the LCM
        return results.reduce { sum, element -> sum * element}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    println("=== test output ===")
    part2(testInput).println()

    val input = readInput("Day08")
//    println("=== test output: part1 ===")
//    part1(input).println()
    println("=== test output: part2 ===")
    part2(input).println()
}

