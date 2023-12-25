package com.arclights.adventofcode

import com.arclights.adventofcode.Day15.part1

fun main() {
    part1()
}

object Day15 {
    fun part1() {
        val input = parse("Day15.txt")
        val result = input.sumOf { hash(it) }
        println(result)
    }

    private fun hash(input:String) = input.fold(0){acc, c -> ((acc + c.code) * 17) % 256 }

    private fun parse(file:String) = read(file)[0].split(",")
}