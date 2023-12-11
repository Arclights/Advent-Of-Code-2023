package com.arclights.adventofcode

import kotlin.math.abs

fun manhattanDistance(start: Point, goal: Point): Long = abs(start.x - goal.x) + abs(start.y - goal.y)