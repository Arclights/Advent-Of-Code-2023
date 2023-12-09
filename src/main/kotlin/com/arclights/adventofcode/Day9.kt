package com.arclights.adventofcode

import com.arclights.adventofcode.Day9.part1

fun main() {
    part1()
}

object Day9 {
    fun part1() {
        val input = parse("Day9.txt")
        val result = input.sumOf(::predictNextNumber)
        println(result)
    }

    private fun predictNextNumber(valueHistory: List<Long>): Long {
        val historyDiffs = valueHistory.windowed(2).map { (first, second) -> second - first }
        return when {
            historyDiffs.last() == 0L -> valueHistory.last()
            else -> predictNextNumber(historyDiffs) + valueHistory.last()
        }
    }

    private fun parse(file: String): List<List<Long>> = read(file).map { it.split(" ").map(String::toLong) }
}