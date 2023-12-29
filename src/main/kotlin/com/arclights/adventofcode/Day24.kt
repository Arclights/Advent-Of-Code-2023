package com.arclights.adventofcode

import com.arclights.adventofcode.Day24.part1
import kotlin.math.abs

fun main() {
    part1()
}

object Day24 {
    fun part1() {
        val input = parse("Day24.txt")
        println(input)
        val result = countIntersects(
            input,
            Point(200000000000000, 200000000000000) to Point(400000000000000, 400000000000000)
        )
        println(result)
    }

    private fun countIntersects(
        hails: List<Pair<List<Long>, List<Long>>>,
        bounds: Pair<Point, Point>
    ): Int =
        when (hails) {
            emptyList<Pair<List<Long>, List<Long>>>() -> 0
            else -> hails.drop(1).map { it to doIntersectXY(hails.first(), it, bounds) }
                .let { intersects ->
                    val sum = intersects.map { it.second }.map { if (it) 1 else 0 }.sum()
                    sum + countIntersects(hails.drop(1), bounds)
                }
        }

    private fun doIntersectXY(
        hail1: Pair<List<Long>, List<Long>>,
        hail2: Pair<List<Long>, List<Long>>,
        bounds: Pair<Point, Point>
    ): Boolean {
        // (x - h_x)*h_yv - (y-h_y)*h_xv = 0 => h_yv*x + h_xv*-1*y = h_x*h_yv - h_y*h_xv
        val hail1Terms = listOf(
            hail1.second[1],
            hail1.second[0] * -1,
            hail1.first[0] * hail1.second[1] - hail1.first[1] * hail1.second[0]
        ).map { it.toFloat() }
        val hail2Terms = listOf(
            hail2.second[1],
            hail2.second[0] * -1,
            hail2.first[0] * hail2.second[1] - hail2.first[1] * hail2.second[0]
        ).map { it.toFloat() }
        val intersection = solveSystem(hail1Terms, hail2Terms)
        return (intersection
            ?.let {
                it.first in bounds.first.x.toFloat()..bounds.second.x.toFloat()
                        && it.second in bounds.first.y.toFloat()..bounds.second.y.toFloat()
                        && isInTheFuture(it.first, hail1.first[0], hail1.second[0])
                        && isInTheFuture(it.second, hail1.first[1], hail1.second[1])
                        && isInTheFuture(it.first, hail2.first[0], hail2.second[0])
                        && isInTheFuture(it.second, hail2.first[1], hail2.second[1])
            }
            ?: false)
    }

    private fun isInTheFuture(coord: Float, startingCoord: Long, velocity: Long): Boolean {
        val diff = coord - startingCoord
        return diff / abs(diff) == velocity.toFloat() / abs(velocity)
    }

    private fun solveSystem(equation1: List<Float>, equation2: List<Float>): Pair<Float, Float>? {
        var l1 = equation1
        var l2 = equation2

        // Eliminate x from L2
        l2 = (l2[0] / l1[0] * -1).let { multiplier -> l1.map { it * multiplier } }.zip(l2) { t1, t2 -> t1 + t2 }

        if (l2[0] == 0f && l2[1] == 0f) {
            // No solution found
            return null
        }

        // Reduce L2
        l2 = l2[1].let { divider -> l2.map { it / divider } }

        // Eliminating y from L1
        l1 = (l1[1] / l2[1] * -1).let { multiplier -> l2.map { it * multiplier } }.zip(l1) { t1, t2 -> t1 + t2 }

        // Reduce L1
        l1 = l1[0].let { divider -> l1.map { it / divider } }

        return l1.last() to l2.last()
    }

    private fun parse(file: String) = read(file)
        .map { line -> line.split(" @ ") }
        .map { (position, velocity) ->
            position.split(", ").map(String::toLong) to velocity.split(", ").map(String::toLong)
        }
}