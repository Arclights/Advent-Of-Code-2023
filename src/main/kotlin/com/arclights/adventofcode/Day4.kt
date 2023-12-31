package com.arclights.adventofcode

import com.arclights.adventofcode.Day4.part1
import com.arclights.adventofcode.Day4.part2

fun main() {
    part1()
    part2()
}

object Day4 {
    fun part1() {
        val input = parse("Day4.txt")
        val result = calculateWinnings(input)
        println(result)
    }

    fun part2() {
        val input = parse("Day4.txt")
        val result = calculateWonCards(input)
        println(result.size)
    }

    private fun calculateWonCards(tickets: List<Ticket>) =
        calculateWonCards(tickets.indices, mapOf(), tickets).first

    private fun calculateWonCards(
        iRange: IntRange,
        winningLookup: Map<Int, List<Ticket>>,
        allTickets: List<Ticket>
    ): Pair<List<Ticket>, Map<Int, List<Ticket>>> = when {
        iRange.isEmpty() -> listOf<Ticket>() to winningLookup
        else -> iRange.fold(listOf<Ticket>() to winningLookup) { (accTickets, updatedWinningLookup), i ->
            calculateWonCards2(i, updatedWinningLookup, allTickets)
                .let { (wonTickets, newlyUpdatedWinningLookup) ->
                    accTickets.plus(wonTickets) to newlyUpdatedWinningLookup.plus(i to wonTickets)
                }
        }
    }

    private fun calculateWonCards2(
        i: Int,
        winningLookup: Map<Int, List<Ticket>>,
        allTickets: List<Ticket>
    ): Pair<List<Ticket>, Map<Int, List<Ticket>>> = winningLookup[i]
        ?.let { it to winningLookup }
        ?: run {
            val ticket = allTickets[i]
            val nbrOfWonCards = calculateWonCards(ticket)
            val wonCards = if (nbrOfWonCards == 0) IntRange.EMPTY else i + 1..i + nbrOfWonCards
            calculateWonCards(wonCards, winningLookup, allTickets)
                .let { (wonTickets, newlyUpdatedWinningLookup) ->
                    wonTickets.plus(ticket) to newlyUpdatedWinningLookup.plus(
                        i to wonTickets
                    )
                }
        }

    private fun calculateWonCards(ticket: Ticket) = ticket.first
        .intersect(ticket.second)
        .size

    private fun calculateWinnings(tickets: List<Pair<Set<Int>, Set<Int>>>) =
        tickets.sumOf { ticket -> calculateWinning(ticket) }

    private fun calculateWinning(ticket: Ticket): Int =
        ticket.first
            .intersect(ticket.second)
            .foldIndexed(0) { i, acc, _ -> if (i == 0) 1 else acc * 2 }

    private fun parse(file: String) = read(file)
        .map { line -> line.split(": ")[1] }
        .map { line -> line.split(" | ") }
        .map { line -> parseNumbers(line[0]) to parseNumbers(line[1]) }

    private fun parseNumbers(numberString: String) =
        numberString.trim().split("\\s+".toPattern()).map { it.toInt() }.toSet()
}

typealias Ticket = Pair<Set<Int>, Set<Int>>