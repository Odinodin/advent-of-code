package aoc2023

import resourceAsListOfString

data class Part(val id: Int, val coords: List<Pair<Int, Int>>) {
    companion object {
        fun create(values: List<Char>, coordinates: List<Pair<Int, Int>>): Part {
            return Part(values.joinToString("").toInt(), coordinates)
        }
    }
}


fun isSymbol(a: Char): Boolean = a != '.' && !a.isDigit()

data class Schematic(val parts: List<Part>, val symbols: List<Pair<Int, Int>>) {

    companion object {

        fun parse(input: List<String>): Schematic {
            val parts = findParts(input)
            return Schematic(parts, findSymbols(input))
        }

        private fun findParts(input: List<String>): List<Part> {
            return input.mapIndexed { row: Int, line: String ->
                line
                    .mapIndexed { col, c -> Triple(c, col, row) }
                    .partitionBy { it.first.isDigit() }
                    .filter { it.first().first.isDigit() }
                    .map {
                        val partId = it.map { it.first }.joinToString("").toInt()
                        val coords = it.map { Pair(it.second, it.third) }
                        Part(partId, coords)
                    }
            }.flatten()
        }

        private fun findSymbols(input: List<String>): List<Pair<Int, Int>> {
            val result = input.mapIndexed { row, line ->
                line.mapIndexed { col, c ->
                    if (isSymbol(c)) {
                        Pair(col, row)
                    } else {
                        null
                    }
                }.filterNotNull()
            }.filter { it.isNotEmpty() }.flatten()

            return result
        }
    }

    fun onlyPartsTouchedBySymbol(): List<Part> {
        return parts.filter { part ->
            symbols.any { symbol ->
                touches(symbol, part)
            }
        }
    }

    fun onlyGearParts(): List<Pair<Part, Part>> {
        val gearParts = symbols
            .map { symbol -> Pair(symbol, parts.filter { touches(symbol, it) }) }
            .filter { it.second.count() == 2 }
            .map { Pair(it.second.get(0), it.second.get(1)) }

        return gearParts
    }

    private fun touches(symbol: Pair<Int, Int>, part: Part): Boolean {
        val areaOfAffect = setOf(
            symbol,
            symbol.copy(first = symbol.first + 1), // right
            symbol.copy(first = symbol.first - 1), // left
            symbol.copy(second = symbol.second - 1), // top
            symbol.copy(second = symbol.second + 1), // bottom
            symbol.copy(first = symbol.first - 1, second = symbol.second - 1), // top-left
            symbol.copy(first = symbol.first + 1, second = symbol.second - 1), // top-right
            symbol.copy(first = symbol.first - 1, second = symbol.second + 1), // bottom-left
            symbol.copy(first = symbol.first + 1, second = symbol.second + 1), // bottom-right
        )

        return areaOfAffect.intersect(part.coords).isNotEmpty()
    }
}

class Day03 {
    fun part1(input: List<String>): Int {
        val schematic = Schematic.parse(input)
        return schematic.onlyPartsTouchedBySymbol().sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        val schematic = Schematic.parse(input)

        return schematic.onlyGearParts().map { twoGearParts ->
            twoGearParts.first.id * twoGearParts.second.id
        }.sum()

    }
}


fun main() {
    //val input = resourceAsListOfString("day03_sample1.txt")
    val input = resourceAsListOfString("day03.txt")

    // 1. challenge
    println("Part 1: " + Day03().part1(input))

    // 2. challenge
    println("Part 2: " + Day03().part2(input))

}


inline fun <T, K> Iterable<T>.partitionBy(selector: (T) -> K): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var currentKey: K? = null
    var currentPartition = mutableListOf<T>()

    for (element in this) {
        val key = selector(element)
        if (key != currentKey && currentPartition.isNotEmpty()) {
            result.add(currentPartition)
            currentPartition = mutableListOf()
        }
        currentKey = key
        currentPartition.add(element)
    }

    if (currentPartition.isNotEmpty()) {
        result.add(currentPartition)
    }

    return result
}