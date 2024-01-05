package com.arclights.adventofcode

import com.arclights.adventofcode.Day6.part1
import com.arclights.adventofcode.Day6.part2
import kotlin.math.ceil
import kotlin.math.sqrt

fun main() {
    part1()
    part2()
}

object Day6 {
    fun part1() {
        val input = parse("Day6.txt")
        val result = input
            .map { (time, record) -> getNbrOfWaysToBeatRecord(time, record) }
            .reduce(Long::times)
        println(result)
    }

    fun part2() {
        val (time, record) = parseWithoutKerning("Day6.txt")
        val result = getNbrOfWaysToBeatRecord(time, record)
        println(result)
    }

    private fun getNbrOfWaysToBeatRecord(time: Long, record: Long) =
        (upperBound(time, record) - lowerBound(time, record))

    private fun upperBound(time: Long, record: Long) =
        ceil(((time + sqrt((time * time - 4 * record).toDouble())) / 2)).toLong()

    private fun lowerBound(time: Long, record: Long) =
        ((time - sqrt((time * time - 4 * record).toDouble())) / 2).toLong() + 1

    private fun parse(file: String) = read(file)
        .let { lines ->
            val times = lines[0].split(":")[1].trim().split("\\s+".toRegex()).map { it.toLong() }
            val distances = lines[1].split(":")[1].trim().split("\\s+".toRegex()).map { it.toLong() }
            times.zip(distances)
        }

    private fun parseWithoutKerning(file: String) = read(file)
        .let { lines ->
            val time = lines[0].split(":")[1].filterNot { it == ' ' }.toLong()
            val distance = lines[1].split(":")[1].filterNot { it == ' ' }.toLong()
            time to distance
        }
}