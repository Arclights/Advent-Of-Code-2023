package com.arclights.adventofcode

import com.arclights.adventofcode.Day11.part1
import com.arclights.adventofcode.Day11.part2

fun main() {
    part1()
    part2()
}

object Day11 {
    fun part1() {
        val map = parse("Day11.txt")
        val expandedMap = expandSpace(map) { x -> x + 1 }
        val result = findAllShortestPathLengths(expandedMap).sum()
        println(result)
    }

    fun part2() {
        val map = parse("Day11.txt")
        val expandedMap = expandSpace(map) { x -> x + 1_000_000 - 1 }
        val result = findAllShortestPathLengths(expandedMap).sum()
        println(result)
    }

    private fun findAllShortestPathLengths(map: List<Galaxy>) = map
        .flatMapIndexed { i, startGalaxy ->
            map.subList(i + 1, map.size).map { goalGalaxy -> findShortestPathLength(startGalaxy, goalGalaxy) }
        }

    private fun findShortestPathLength(g1: Galaxy, g2: Galaxy): Long {
        val start = g1.let { Point(it.x, it.y) }
        val goal = g2.let { Point(it.x, it.y) }
        return manhattanDistance(start, goal)
    }

    private fun expandSpace(map: List<Galaxy>, expander: (Long) -> Long): List<Galaxy> {
        val maxX = map.maxOf(Galaxy::x)
        val maxY = map.maxOf(Galaxy::y)

        val emptyXs = (0..maxX).minus(map.map(Galaxy::x).toSet())
        val emptyYs = (0..maxY).minus(map.map(Galaxy::y).toSet())
        val expandedXMap = emptyXs.reversed().fold(map) { expandedMap, emptyX ->
            expandedMap.map { galaxy -> if (galaxy.x > emptyX) galaxy.copy(x = expander(galaxy.x)) else galaxy }
        }
        return emptyYs.reversed().fold(expandedXMap) { expandedMap, emptyY ->
            expandedMap.map { galaxy -> if (galaxy.y > emptyY) galaxy.copy(y = expander(galaxy.y)) else galaxy }
        }
    }

    private fun parse(file: String) = read(file).flatMapIndexed { y, line ->
        line.mapIndexed { x, dot ->
            if (dot == '#') Galaxy(x.toLong(), y.toLong()) else null
        }
    }
        .filterNotNull()

    private data class Galaxy(val x: Long, val y: Long)
}