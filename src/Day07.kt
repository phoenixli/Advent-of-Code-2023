data class Card(val label: Char): Comparable<Card> {
    private val strengthMap =
        mapOf(
            'A' to 13,
            'K' to 12,
            'Q' to 11,
            'J' to 10,
            'T' to 9,
            '9' to 8,
            '8' to 7,
            '7' to 6,
            '6' to 5,
            '5' to 4,
            '4' to 3,
            '3' to 2,
            '2' to 1)

    private val strengthMapV2 =
        mapOf(
            'A' to 13,
            'K' to 12,
            'Q' to 11,
            'T' to 9,
            '9' to 8,
            '8' to 7,
            '7' to 6,
            '6' to 5,
            '5' to 4,
            '4' to 3,
            '3' to 2,
            '2' to 1,
            'J' to 0)

    override fun compareTo(other: Card): Int {
//        return strengthMap[this.label]!!.compareTo(strengthMap[other.label]!!)
        return strengthMapV2[this.label]!!.compareTo(strengthMapV2[other.label]!!)
    }
}

class Hand(val cards: List<Card>): Comparable<Hand> {
    enum class HandType(val strength: Int) {
        FIVE_OF_A_KIND(7),
        FOUR_OF_A_KIND(6),
        FULL_HOUSE(5),
        THREE_OF_A_KIND(4),
        TWO_PAIR(3),
        ONE_PAIR(2),
        HIGH_CARD(1)
    }

    fun getHandType(cards: List<Card>): HandType {
        val groupedCards = cards.groupingBy { it }.eachCount()
        val type = if (groupedCards.size == 1) {
            HandType.FIVE_OF_A_KIND
        } else if (groupedCards.size == 2 && groupedCards.containsValue(4)) {
            HandType.FOUR_OF_A_KIND
        } else if (groupedCards.size == 2 && groupedCards.containsValue(3)) {
            HandType.FULL_HOUSE
        } else if (groupedCards.size == 3 && groupedCards.containsValue(3)) {
            HandType.THREE_OF_A_KIND
        } else if (groupedCards.size == 3 && groupedCards.containsValue(2)) {
            HandType.TWO_PAIR
        } else if (groupedCards.size == 4) {
            HandType.ONE_PAIR
        } else {
            HandType.HIGH_CARD
        }
        return type
    }

    private val type = getHandType(cards)
    private val alternativeType: HandType
    init {
        val groupedCards: Map<Card, Int> = cards.groupingBy { it }.eachCount()
        if (groupedCards.containsKey(Card('J'))) {
            val maxEntry =
                groupedCards
                    .filterNot { (card, _) -> card.label == 'J' }
                    .maxByOrNull { it.value }
            alternativeType = if (maxEntry != null) {
                val newHand = mutableListOf<Card>()
                for (card in cards) {
                    if (card.label == 'J') {
                        newHand.add(Card(maxEntry.component1().label))
                    } else {
                        newHand.add(card)
                    }
                }
                getHandType(newHand)
        //            println("alternative hand: ${newHand}, type: $alternativeType")
            } else {
                type
            }
        } else {
            alternativeType = type
        }
    }

    override fun compareTo(other: Hand): Int {
        val type = if (this.type.strength >= this.alternativeType.strength) {
            this.type
        } else {
            this.alternativeType
        }

        val otherType = if (other.type.strength >= other.alternativeType.strength) {
            other.type
        } else {
            other.alternativeType
        }

        var result = type.strength.compareTo(otherType.strength)
        if (result == 0) {
            for (i in cards.indices) {
                result = cards[i].compareTo(other.cards[i])
                if (result == 0) {
                    continue
                } else {
                    return result
                }
            }
        }
        return result
    }
}

data class Play(val hand: Hand, val bid: Int): Comparable<Play> {
    override fun compareTo(other: Play): Int {
        return hand.compareTo(other.hand)
    }
}

fun main() {
    fun parseInput(input: String): Play {
        val (cardsStr, bidStr) = input.split(" ")
        val cards = cardsStr.toCharArray().map{ Card(it) }.toList()
        return Play(Hand(cards), bidStr.toInt())
    }

    fun part1(input: List<String>): Int {
        val plays = mutableListOf<Play>()
        input.forEach {
            plays.add(parseInput(it))
        }

        val sortedPlays = plays.sorted()
        var result = 0
        for (i in sortedPlays.indices) {
//            println("$i, hand: ${sortedPlays[i].hand.cards}, bid: ${sortedPlays[i].bid}")
            result += (i + 1) * sortedPlays[i].bid
        }

        return result
    }

    fun part2(input: List<String>): Int {
        val plays = mutableListOf<Play>()
        input.forEach {
            plays.add(parseInput(it))
        }

        val sortedPlays = plays.sorted()
        var result = 0
        for (i in sortedPlays.indices) {
            println("$i, hand: ${sortedPlays[i].hand.cards}, bid: ${sortedPlays[i].bid}")
            result += (i + 1) * sortedPlays[i].bid
        }

        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    println("=== test output ===")
    part2(testInput).println()

    val input = readInput("Day07")
//    println("=== test output: part1 ===")
//    part1(input).println()
    println("=== test output: part2 ===")
    part2(input).println()
}

