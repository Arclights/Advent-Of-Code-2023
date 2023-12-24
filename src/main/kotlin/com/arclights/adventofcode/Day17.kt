package com.arclights.adventofcode

import com.arclights.adventofcode.Day17.part1
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    part1()
}

object Day17 {
    fun part1() {
        val heatMap = parse("Day17ex.txt")
        val optimalPath = findOptimalPath(heatMap)
        heatMap.draw(optimalPath)
        val totalHeatLoss = totalHeatLoss(optimalPath, heatMap)
        println(totalHeatLoss)
    }

    private fun totalHeatLoss(path: List<Node>, heatMap: HeatMap) =
        path.drop(1).map { heatMap.getHeatLoss(it.point) }.sum()

    private data class Node(val point: Point, val direction: Point, val stepsInSameDirection: Int)

    private fun findOptimalPath(heatMap: HeatMap): List<Node> {
        return aStar(
            start = Node(heatMap.start, Point(0, 0), 0),
            isAtGoal = { p -> p.point == heatMap.goal },
            h = { p -> (heatMap.goal.x - p.point.x + heatMap.goal.y - p.point.y).toFloat() },
            neighbours = { p, cameFrom ->
                val neighbours = heatMap.getNeighbours(p)
                cameFrom[p]
                    ?.let { previousNode -> neighbours.filter { it.point != previousNode.point }.toSet() }
                    ?: neighbours
            },
            d = { current, neighbour, cameFrom ->
                val previous = cameFrom[current]
                val secondPrevious = cameFrom[previous]
                if (isOnALine(listOfNotNull(neighbour, current, previous, secondPrevious))) {
                    Float.MAX_VALUE
                } else {
                    heatMap.getHeatLoss(neighbour.point)
                }
            }
        )
    }

    private fun isOnALine(nodes: List<Node>) =
        if (nodes.size < 4) {
            false
        } else {
            (nodes.map { it.point.x }.toSet().size == 1 || nodes.map { it.point.y }
                .toSet().size == 1) && nodes.map { it.direction }.toSet().size == 1
        }

    private fun parse(file: String): HeatMap = read(file)
        .mapIndexed { y, line ->
            line.mapIndexed { x, charVal ->
                Point(
                    x.toLong(),
                    y.toLong()
                ) to charVal.digitToInt().toFloat()
            }
        }
        .flatten()
        .also { println(it) }
        .let { HeatMap(it) }

    private val UP = Point(0, -1)
    private val LEFT = Point(-1, 0)
    private val RIGHT = Point(1, 0)
    private val DOWN = Point(0, 1)

    private class HeatMap(heatCoords: List<Pair<Point, Float>>) {
        private val heatLossMap = heatCoords.toMap()

        private val maxX = heatCoords.maxOf { it.first.x }
        private val maxY = heatCoords.maxOf { it.first.y }

        val start = Point(0, 0)
        val goal = heatCoords.map { it.first }.maxWith { p1, p2 -> p1.vectorLength().compareTo(p2.vectorLength()) }

        fun getNeighbours(node: Node): Set<Node> = setOf(
            UP,
            LEFT,
            RIGHT,
            DOWN,
        )
            .map { direction ->
                Node(
                    Point(node.point.x + direction.x, node.point.y + direction.y),
                    direction,
                    if (node.direction == direction) node.stepsInSameDirection + 1 else 0
                )
            }
            .filter { it.point.x in 0..maxX && it.point.y in 0..maxY }
            .toSet()

        fun getHeatLoss(point: Point): Float = heatLossMap.getValue(point)

        fun draw(path: List<Node>) {
            val visualisedPath = path.associate { it.point to getChar(it.direction) }
            (0..maxY).joinToString(separator = "\n") { y ->
                (0..maxX)
                    .map { x -> Point(x, y) }
                    .map { point -> visualisedPath.getOrElse(point) { getHeatLoss(point).toInt().toString() } }
                    .joinToString(separator = "")
            }
                .let { println(it) }
        }
    }

    private fun getChar(direction: Point) = when (direction) {
        Point(0, 0) -> '.'
        UP -> '^'
        LEFT -> '<'
        RIGHT -> '>'
        DOWN -> 'v'
        else -> throw IllegalArgumentException("Unknown direction: $direction")
    }

    private fun Point.vectorLength() = sqrt(x.toFloat().pow(2) + y.toFloat().pow(2))
}