package com.arclights.adventofcode

import com.arclights.adventofcode.Day12.part1

fun main() {
    part1()
}

object Day12 {
    fun part1() {
        val rows = parse("Day12.txt")
        val result = getPossibleArrangements(rows)
        println(result)
    }

    private fun getPossibleArrangements(rows: List<Row>) = rows.sumOf { getPossibleArrangements(it) }

    private fun getPossibleArrangements(row: Row): Int =
        getPossibleArrangements(generateRegEx(row.groups), row.springs, "")

    private fun getPossibleArrangements(regex: Regex, springs: String, arrangement: String): Int = when {
        springs.isEmpty() -> if (regex.matches(arrangement)) 1 else 0
        else -> when (springs.take(1)) {
            "?" -> getPossibleArrangements(regex, springs.drop(1), "$arrangement.") +
                    getPossibleArrangements(regex, springs.drop(1), "$arrangement#")

            else -> getPossibleArrangements(regex, springs.drop(1), "$arrangement${springs.take(1)}")
        }
    }

    private fun generateRegEx(groups: List<Int>) =
        """^\.*${groups.joinToString(separator = """\.+""") { "#{$it}" }}\.*$""".toRegex()

    private fun parse(file: String): List<Row> = read(file)
        .map { row ->
            row.split(" ")
                .let { (springs, groups) ->
                    Row(
                        springs,
                        groups.split(",").map(String::toInt)
                    )
                }
        }

    data class Row(val springs: String, val groups: List<Int>)
}