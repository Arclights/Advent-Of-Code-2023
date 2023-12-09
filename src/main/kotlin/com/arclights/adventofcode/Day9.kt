package com.arclights.adventofcode

import com.arclights.adventofcode.Day9.part1
import com.arclights.adventofcode.Day9.part2

fun main() {
    part1()
    part2()
}

object Day9 {
    fun part1() {
        val input = parse("Day9.txt")
        val result = input.sumOf(::predictNextNumber)
        println(result)
    }

    fun part2(){
        val input = parse("Day9.txt")
        val result = input.sumOf(::predictPreviousNumber)
        println(result)
    }

    private fun predictNextNumber(valueHistory: List<Long>): Long {
        val historyDiffs = valueHistory.windowed(2).map { (first, second) -> second - first }
        return when {
            historyDiffs.last() == 0L -> valueHistory.last()
            else -> predictNextNumber(historyDiffs) + valueHistory.last()
        }
    }

    private fun predictPreviousNumber(valueHistory: List<Long>): Long {
        val historyDiffs = valueHistory.windowed(2).map { (first, second) -> second - first }
        return when {
            historyDiffs.last() == 0L -> valueHistory.first()
            else -> valueHistory.first() - predictPreviousNumber(historyDiffs)
        }
    }

    private fun parse(file: String): List<List<Long>> = read(file).map { it.split(" ").map(String::toLong) }
}