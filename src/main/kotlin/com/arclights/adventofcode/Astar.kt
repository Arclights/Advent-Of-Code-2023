package com.arclights.adventofcode

fun <P> aStar(
    start: P,
    isAtGoal: (P) -> Boolean,
    h: (P) -> Float,
    neighbours: (P, Map<P, P>) -> Set<P>,
    d: (P, P, Map<P, P>) -> Float
): List<P> {
    val openSet = mutableSetOf(start)
    val cameFrom = mutableMapOf<P, P>()
    val gScore = mutableMapOf(start to 0.0f)
    val fScore = mutableMapOf(start to h(start))

    while (openSet.isNotEmpty()) {
        val current = openSet.minBy { fScore.getOrDefault(it, Float.MAX_VALUE) }
        if (isAtGoal(current)) {
            return reconstructPath(cameFrom, current)
        }
        openSet.remove(current)
        for (neighbour in neighbours(current, cameFrom)) {
            val tentativeGScore = gScore.getOrDefault(current, Float.MAX_VALUE) + d(current, neighbour, cameFrom)
            if (tentativeGScore < gScore.getOrDefault(neighbour, Float.MAX_VALUE)) {
                cameFrom[neighbour] = current
                gScore[neighbour] = tentativeGScore
                fScore[neighbour] = tentativeGScore + h(neighbour)
                if (openSet.contains(neighbour).not()) {
                    openSet.add(neighbour)
                }
            }
        }
    }
    throw IllegalStateException("Could not reach the goal")
}

fun <P> reconstructPath(cameFrom: Map<P, P>, current: P): List<P> {
    val totalPath = mutableListOf(current)
    var tmpCurrent = current
    while (cameFrom.containsKey(tmpCurrent)) {
        tmpCurrent = cameFrom.getValue(tmpCurrent)
        totalPath.add(tmpCurrent)
    }
    return totalPath.reversed()
}