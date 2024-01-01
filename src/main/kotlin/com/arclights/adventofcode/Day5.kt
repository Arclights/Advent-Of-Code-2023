package com.arclights.adventofcode

import com.arclights.adventofcode.Day5.part1
import com.arclights.adventofcode.Day5.part2

fun main() {
    part1()
    part2()
}

object Day5 {
    fun part1() {
        val (seeds, maps) = parse("Day5.txt")
        val result = seeds.minOfOrNull { translateSeedToLocation(it, maps) }
        println(result)
    }

    fun part2(){
        val (seeds, maps) = parse("Day5.txt")
        val rangedSeeds = seeds.windowed(2, step = 2).map { (rangeStart,rangeLength)->rangeStart ..< rangeStart+rangeLength }
        val result = rangedSeeds.mapNotNull { range -> range.minOfOrNull { translateSeedToLocation(it, maps) } }.min()
        println(result)
    }

    private fun translateSeedToLocation(seed: Long, maps: Maps) = seed
        .let { maps.seed2Soil.translate(it) }
        .let { maps.soil2Fertilizer.translate(it) }
        .let { maps.fertilizer2Water.translate(it) }
        .let { maps.water2Light.translate(it) }
        .let { maps.light2Temperature.translate(it) }
        .let { maps.temperature2Humidity.translate(it) }
        .let { maps.humidity2Location.translate(it) }

    private fun parse(file: String) = read(file)
        .split("")
        .let { groups ->
            groups.fold(Input()) { input, group ->
                when {
                    group[0].startsWith("seeds") -> group[0]
                        .split(" ")
                        .drop(1)
                        .map { it.toLong() }
                        .let { input.copy(seeds = it) }

                    else -> parseTranslationMap(group.drop(1))
                        .let {
                            when (group[0]) {
                                "seed-to-soil map:" -> input.maps.copy(seed2Soil = it)
                                "soil-to-fertilizer map:" -> input.maps.copy(soil2Fertilizer = it)
                                "fertilizer-to-water map:" -> input.maps.copy(fertilizer2Water = it)
                                "water-to-light map:" -> input.maps.copy(water2Light = it)
                                "light-to-temperature map:" -> input.maps.copy(light2Temperature = it)
                                "temperature-to-humidity map:" -> input.maps.copy(temperature2Humidity = it)
                                "humidity-to-location map:" -> input.maps.copy(humidity2Location = it)
                                else -> throw IllegalArgumentException("Unknown group ${group[0]}")
                            }
                        }
                        .let { input.copy(maps = it) }

                }
            }

        }

    private fun parseTranslationMap(lines: List<String>) = lines
        .map { line -> line.split(" ").map { it.toLong() }.let { TranslationMapping(it[0], it[1], it[2]) } }
        .let { TranslationMap(it) }


    private fun <T> List<T>.split(splitOn: T): List<List<T>> = when {
        this.isEmpty() -> listOf()
        else -> {
            val restSplit: List<List<T>> = this.dropWhile { it != splitOn }.drop(1).split(splitOn)
            val split: List<T> = this.takeWhile { it != splitOn }
            restSplit.plusElement(split)
        }
    }

    private data class Input(val seeds: List<Long> = listOf(), val maps: Maps = Maps())

    private data class Maps(
        val seed2Soil: TranslationMap = TranslationMap(),
        val soil2Fertilizer: TranslationMap = TranslationMap(),
        val fertilizer2Water: TranslationMap = TranslationMap(),
        val water2Light: TranslationMap = TranslationMap(),
        val light2Temperature: TranslationMap = TranslationMap(),
        val temperature2Humidity: TranslationMap = TranslationMap(),
        val humidity2Location: TranslationMap = TranslationMap()
    )

    private data class TranslationMap(
        val mappings: List<TranslationMapping> = listOf()
    ) {
        fun translate(value: Long) = mappings.firstOrNull { it.canTranslate(value) }?.translate(value) ?: value
    }

    private data class TranslationMapping(
        val destinationRangeStart: Long,
        val sourceRangeStart: Long,
        val rangeLength: Long
    ) {
        fun canTranslate(value: Long) = value >= sourceRangeStart && value < sourceRangeStart + rangeLength

        fun translate(value: Long) = destinationRangeStart + (value - sourceRangeStart)
    }
}