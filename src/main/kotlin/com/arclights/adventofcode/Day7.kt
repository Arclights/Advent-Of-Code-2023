package com.arclights.adventofcode

import com.arclights.adventofcode.Day7.HandType.FIVE_OF_A_KIND
import com.arclights.adventofcode.Day7.HandType.FOUR_OF_A_KIND
import com.arclights.adventofcode.Day7.HandType.FULL_HOUSE
import com.arclights.adventofcode.Day7.HandType.ONE_PAIR
import com.arclights.adventofcode.Day7.HandType.THREE_OF_A_KIND
import com.arclights.adventofcode.Day7.HandType.TWO_PAIR
import com.arclights.adventofcode.Day7.part1
import com.arclights.adventofcode.Day7.part2

fun main() {
    part1()
    part2()
}

object Day7 {
    fun part1() {
        val input = parse("Day7.txt")
        val result = calculateTotalWinnings(input, regularComparator)
        println(result)
    }

    fun part2() {
        val input = parse("Day7.txt")
        val result = calculateTotalWinnings(input, jokerComparator)
        println(result)
    }

    private fun calculateTotalWinnings(
        hands: List<Pair<CharArray, Long>>,
        comparator: Comparator<Pair<CharArray, Long>>
    ) = hands
        .sortedWith(comparator)
        .reversed()
        .mapIndexed { i, hand -> hand.second * (i + 1) }
        .sum()

    private val regularComparator = Comparator<Pair<CharArray, Long>> { hand1, hand2 ->
        val hand1Type = hand1.first.getType()
        val hand2Type = hand2.first.getType()

        if (hand1Type == hand2Type) {
            return@Comparator hand1.first.zip(hand2.first)
                .first { it.first != it.second }
                .let { cardRankings.indexOf(it.first).compareTo(cardRankings.indexOf(it.second)) }
        }

        return@Comparator hand1Type.ordinal.compareTo(hand2Type.ordinal)
    }

    private val jokerComparator = Comparator<Pair<CharArray, Long>> { hand1, hand2 ->
        val hand1Type = hand1.first.getTypeWithJoker()
        val hand2Type = hand2.first.getTypeWithJoker()

        if (hand1Type == hand2Type) {
            return@Comparator hand1.first.zip(hand2.first)
                .first { it.first != it.second }
                .let { cardRankingsWithJoker.indexOf(it.first).compareTo(cardRankingsWithJoker.indexOf(it.second)) }
        }

        return@Comparator hand1Type.ordinal.compareTo(hand2Type.ordinal)
    }

    private fun CharArray.getType() = this
        .groupBy { it }
        .values
        .let { cardGroups ->
            when {
                cardGroups.any { it.size == 5 } -> FIVE_OF_A_KIND
                cardGroups.any { it.size == 4 } -> FOUR_OF_A_KIND
                cardGroups.any { it.size == 3 } && cardGroups.any { it.size == 2 } -> FULL_HOUSE
                cardGroups.any { it.size == 3 } -> THREE_OF_A_KIND
                cardGroups.filter { it.size == 2 }.size == 2 -> TWO_PAIR
                cardGroups.filter { it.size == 2 }.size == 1 -> ONE_PAIR
                else -> HandType.HIGH_CARD
            }
        }

    private fun CharArray.getTypeWithJoker() = this
        .groupBy { it }
        .let { cardGroups ->
            val nbrOfJokers = cardGroups['J']?.size ?: 0
            val restCardGroups = cardGroups.filter { group -> group.key != 'J' }.values
            when {
                restCardGroups.any { it.size == 5 } // AAAAA
                        || restCardGroups.any { it.size == 4 } && nbrOfJokers == 1 // AAAAJ
                        || restCardGroups.any { it.size == 3 } && nbrOfJokers == 2 // AAAJJ
                        || restCardGroups.any { it.size == 2 } && nbrOfJokers == 3 // AAJJJ
                        || restCardGroups.any { it.size == 1 } && nbrOfJokers == 4 // AJJJJ
                        || nbrOfJokers == 4 // JJJJK
                        || nbrOfJokers == 5 // JJJJJ
                -> FIVE_OF_A_KIND

                restCardGroups.any { it.size == 4 } // AAAAK
                        || restCardGroups.any { it.size == 3 } && nbrOfJokers == 1 // AAAJK
                        || restCardGroups.any { it.size == 2 } && nbrOfJokers == 2 // AAJJK
                        || restCardGroups.any { it.size == 1 } && nbrOfJokers == 3 // AJJJK
                -> FOUR_OF_A_KIND

                restCardGroups.any { it.size == 3 } && restCardGroups.any { it.size == 2 } // AAAKK
                        || restCardGroups.filter { it.size == 2 }.size == 2 && nbrOfJokers == 1 //AAKKJ
                        || restCardGroups.any { it.size == 3 } && restCardGroups.any { it.size == 1 } && nbrOfJokers == 1 //AAAKJ
                -> FULL_HOUSE

                restCardGroups.any { it.size == 3 } // AAAKQ
                        || restCardGroups.any { it.size == 2 } && nbrOfJokers == 1 // AAJKQ
                        || restCardGroups.any { it.size == 1 } && nbrOfJokers == 2 // AJJKQ
                -> THREE_OF_A_KIND

                restCardGroups.filter { it.size == 2 }.size == 2 // AAKKQ
                -> TWO_PAIR // JJAKQ Redundant?

                restCardGroups.filter { it.size == 2 }.size == 1 // AAKQT
                        || nbrOfJokers == 1 // AJKQT
                -> ONE_PAIR

                else -> HandType.HIGH_CARD
            }
        }

    private enum class HandType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

    private val cardRankings = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    private val cardRankingsWithJoker = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    private fun parse(file: String) = read(file)
        .map { it.split(" ") }
        .map { it[0].toCharArray() to it[1].toLong() }
}