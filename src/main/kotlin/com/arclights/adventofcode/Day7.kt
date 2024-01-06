package com.arclights.adventofcode

import com.arclights.adventofcode.Day7.HandType.FIVE_OF_A_KIND
import com.arclights.adventofcode.Day7.HandType.FOUR_OF_A_KIND
import com.arclights.adventofcode.Day7.HandType.FULL_HOUSE
import com.arclights.adventofcode.Day7.HandType.ONE_PAIR
import com.arclights.adventofcode.Day7.HandType.THREE_OF_A_KIND
import com.arclights.adventofcode.Day7.HandType.TWO_PAIR
import com.arclights.adventofcode.Day7.part1

fun main() {
    part1()
}

object Day7 {
    fun part1() {
        val input = parse("Day7.txt")
        val result = calculateTotalWinnings(input)
        println(result)
    }

    private fun calculateTotalWinnings(hands: List<Pair<CharArray, Long>>) =
        hands
            .sortedWith(Comparator { hand1, hand2 ->
                val hand1Type = hand1.first.getType()
                val hand2Type = hand2.first.getType()

                if (hand1Type == hand2Type) {
                    return@Comparator hand1.first.zip(hand2.first)
                        .first { it.first != it.second }
                        .let { cardRankings.indexOf(it.first).compareTo(cardRankings.indexOf(it.second)) }
                }

                return@Comparator hand1Type.ordinal.compareTo(hand2Type.ordinal)
            })
            .reversed()
            .mapIndexed { i, hand -> hand.second * (i + 1) }
            .sum()

    private fun CharArray.getType() = this.groupBy { it }
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

    private fun parse(file: String) = read(file)
        .map { it.split(" ") }
        .map { it[0].toCharArray() to it[1].toLong() }
}