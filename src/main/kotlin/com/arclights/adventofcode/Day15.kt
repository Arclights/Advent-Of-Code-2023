package com.arclights.adventofcode

import com.arclights.adventofcode.Day15.part1
import com.arclights.adventofcode.Day15.part2

fun main() {
    part1()
    part2()
}

object Day15 {
    fun part1() {
        val input = parse("Day15.txt")
        val result = input.sumOf { hash(it) }
        println(result)
    }

    fun part2() {
        val input = parseSequence("Day15.txt")
        val boxes = input.fold(Boxes()) { boxes, operation ->
            when (operation) {
                is AddOperation -> boxes.add(operation.lens)
                is RemoveOperation -> boxes.remove(operation.label)
                else -> throw IllegalArgumentException("Unknown operation: $operation")
            }
        }
        println(boxes.focusingPower())
    }

    private fun hash(input: String) = input.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }

    private class Boxes {
        var boxes = mutableMapOf<Int, List<Lens>>()

        fun add(lens: Lens): Boxes =
            boxes.getOrDefault(hash(lens.label), listOf())
                .let { lenses ->
                    boxes[hash(lens.label)] = if (lenses.any { it.label == lens.label }) {
                        lenses.map { if (it.label == lens.label) lens else it }
                    } else {
                        lenses.plus(lens)
                    }
                }
                .let { this }

        fun remove(label: String): Boxes =
            boxes.getOrDefault(hash(label), listOf())
                .let { lenses -> boxes[hash(label)] = lenses.filter { it.label != label } }
                .let { this }

        fun focusingPower() = boxes.entries
            .map { (boxNbr, lenses) -> lenses.mapIndexed { slot, lens -> (boxNbr + 1) * (slot + 1) * lens.focalLength } }
            .flatten()
            .sum()

        override fun toString(): String {
            return boxes.toString()
        }
    }

    private fun parseSequence(file: String) = parse(file).map { step ->
        when {
            step.contains("=") -> step
                .split("=")
                .let { (label, valueString) -> AddOperation(Lens(label, valueString.toInt())) }

            step.endsWith("-") -> RemoveOperation(step.dropLast(1))

            else -> throw IllegalStateException("Unknown step: $step")
        }
    }

    private fun parse(file: String) = read(file)[0].split(",")

    private data class Lens(val label: String, val focalLength: Int)

    private data class AddOperation(val lens: Lens) : Operation
    private data class RemoveOperation(val label: String) : Operation
    private interface Operation
}