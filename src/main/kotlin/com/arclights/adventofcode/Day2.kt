package com.arclights.adventofcode

import com.arclights.adventofcode.Day2.part1
import com.arclights.adventofcode.Day2.part2
import kotlin.math.max

fun main() {
    part1()
    part2()
}

object Day2 {
    fun part1() {
        val input = read("Day2.txt")
        val games = parse(input)
        val result = filterValidGames(Bag(12, 13, 14), games)
            .map(Game::id)
            .sum()
        println(result)
    }

    fun part2() {
        val input = read("Day2.txt")
        val games = parse(input)
        val result = games
            .map(::findPower)
            .sum()
        println(result)
    }

    private fun filterValidGames(bag: Bag, games: List<Game>) = games
        .filter { isValidGame(bag, it) }

    private fun isValidGame(bag: Bag, game: Game) = game.draws
        .map { isValidDraw(bag, it) }
        .reduceRight(Boolean::and)

    private fun isValidDraw(bag: Bag, draw: Draw) =
        draw.red <= bag.red && draw.green <= bag.green && draw.blue <= bag.blue

    private fun findPower(game: Game) = findMinimumBag(game.draws).let { it.red * it.green * it.blue }
    private fun findMinimumBag(draws: List<Draw>) = draws.fold(Bag(0, 0, 0)) { bag, draw ->
        Bag(
            max(bag.red, draw.red),
            max(bag.green, draw.green),
            max(bag.blue, draw.blue)
        )
    }

    private fun parse(input: List<String>): List<Game> = input
        .map { line ->
            val (rawTitle, rawDraws) = line.split(": ")
            val id = parseId(rawTitle)
            val draws = parseDraws(rawDraws)
            Game(id, draws)
        }

    private fun parseId(title: String) = title.split(" ")[1].toInt()
    private fun parseDraws(draws: String) = draws
        .split("; ")
        .map(::parseDraw)

    private fun parseDraw(draw: String) = draw
        .split(", ")
        .map { it.split(" ") }
        .let { parts ->
            val red = parts.find { it[1] == "red" }?.get(0)?.toInt() ?: 0
            val green = parts.find { it[1] == "green" }?.get(0)?.toInt() ?: 0
            val blue = parts.find { it[1] == "blue" }?.get(0)?.toInt() ?: 0
            Draw(red, green, blue)
        }

    data class Game(val id: Int, val draws: List<Draw>)
    data class Draw(val red: Int, val green: Int, val blue: Int)
    data class Bag(val red: Int, val green: Int, val blue: Int)
}