package com.arclights.adventofcode

fun main() {
    part1()
}

fun part1() {
    val input = read("Day1.txt")
    val sum = input
        .map(::extractCalibrationValue)
        .sum()
    println(sum)
}

fun extractCalibrationValue(line: String): Int {
    val firstNumber = findFirstNumber(line)
    val lastNumber = findLastNumber(line)
    return combineNumbers(firstNumber, lastNumber)
}

fun combineNumbers(number1: Int, number2: Int) = number1 * 10 + number2
fun findFirstNumber(line: String) = line.dropWhile { it.isDigit().not() }.take(1).toInt()
fun findLastNumber(line: String) = line.dropLastWhile { it.isDigit().not() }.takeLast(1).toInt()