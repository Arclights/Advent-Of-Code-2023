package com.arclights.adventofcode

import com.arclights.adventofcode.Day4.part1

fun main() {
    part1()
}

object Day4 {
    fun part1() {
        val input = parse("Day4.txt")
        val result = calculateWinnings(input)
        println(result)
    }

    private fun calculateWinnings(tickets: List<Pair<Set<Int>, Set<Int>>>) =
        tickets.sumOf { ticket -> calculateWinning(ticket) }

    private fun calculateWinning(ticket: Pair<Set<Int>, Set<Int>>): Int =
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