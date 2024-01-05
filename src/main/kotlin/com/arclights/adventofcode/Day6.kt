package com.arclights.adventofcode

import com.arclights.adventofcode.Day6.part1
import kotlin.math.ceil
import kotlin.math.sqrt

fun main() {
    part1()
}

object Day6 {
    fun part1() {
        val input = parse("Day6.txt")
        val result = input
            .map { (time, record) -> getNbrOfWaysToBeatRecord(time, record) }
            .reduce(Int::times)
        println(result)
    }

    private fun getNbrOfWaysToBeatRecord(time: Int, record: Int) = (upperBound(time, record) - lowerBound(time, record))
        .toInt()

    private fun upperBound(time: Int, record: Int) = ceil(((time + sqrt((time * time - 4 * record).toDouble())) / 2))

    private fun lowerBound(time: Int, record: Int) =
        ((time - sqrt((time * time - 4 * record).toDouble())) / 2).toInt() + 1

    private fun parse(file: String) = read(file)
        .let { lines ->
            val times = lines[0].split(":")[1].trim().split("\\s+".toRegex()).map { it.toInt() }
            val distances = lines[1].split(":")[1].trim().split("\\s+".toRegex()).map { it.toInt() }
            times.zip(distances)
        }
}