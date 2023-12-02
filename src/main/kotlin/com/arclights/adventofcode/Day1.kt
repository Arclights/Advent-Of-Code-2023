package com.arclights.adventofcode

import com.arclights.adventofcode.Day1.part1
import com.arclights.adventofcode.Day1.part2

fun main() {
    part1()
    part2()
}

object Day1 {
    fun part1() {
        fun findFirstNumber(line: String) = line.dropWhile { it.isDigit().not() }.take(1).toInt()
        fun findLastNumber(line: String) = line.dropLastWhile { it.isDigit().not() }.takeLast(1).toInt()

        fun extractCalibrationValue(line: String): Int {
            val firstNumber = findFirstNumber(line)
            val lastNumber = findLastNumber(line)
            return combineNumbers(firstNumber, lastNumber)
        }

        val input = read("Day1.txt")
        val sum = input
            .map(::extractCalibrationValue)
            .sum()
        println(sum)
    }

    fun part2() {
        fun findNumber(
            line: String,
            wordComparer: (String, String) -> Boolean,
            charPicker: (String) -> Char,
            lineMutator: (String) -> String
        ): Int = when {
            wordComparer(line, "one") -> 1
            wordComparer(line, "one") -> 1
            wordComparer(line, "two") -> 2
            wordComparer(line, "three") -> 3
            wordComparer(line, "four") -> 4
            wordComparer(line, "five") -> 5
            wordComparer(line, "six") -> 6
            wordComparer(line, "seven") -> 7
            wordComparer(line, "eight") -> 8
            wordComparer(line, "nine") -> 9
            charPicker(line).isDigit() -> charPicker(line).digitToInt()
            else -> findNumber(lineMutator(line), wordComparer, charPicker, lineMutator)
        }

        fun findFirstNumber(line: String): Int = findNumber(line, String::startsWith, String::first) { it.drop(1) }

        fun findLastNumber(line: String): Int = findNumber(line, String::endsWith, String::last) { it.dropLast(1) }

        fun extractCalibrationValue(line: String): Int {
            val firstNumber = findFirstNumber(line)
            val lastNumber = findLastNumber(line)
            return combineNumbers(firstNumber, lastNumber)
        }

        val input = read("Day1.txt")
        val sum = input
            .map(::extractCalibrationValue)
            .sum()
        println(sum)
    }

    fun combineNumbers(number1: Int, number2: Int) = number1 * 10 + number2
}