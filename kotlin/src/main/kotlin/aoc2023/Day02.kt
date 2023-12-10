package aoc2023

import resourceAsListOfString

data class Hand(val colorCounts: Map<String, Int>) {
    fun isPossible(bagContents: Map<String, Int>): Boolean {

        return colorCounts.entries.all { entry ->
            val maxPossibleCount = bagContents.get(entry.key)

            println("Entry: " + entry + " Max: " + maxPossibleCount)

            maxPossibleCount != null && (entry.value <= maxPossibleCount)
        }
    }
}

data class Game(val id: Int, val hands: List<Hand>) {
    fun isPossible(bagContents: Map<String, Int>): Boolean {
        return hands.all {

            val possible = it.isPossible(bagContents)
            println("$it -> $possible")
            println("")
            possible
        }
    }

    fun minimumRequiredColorCounts(): Map<String, Int> {
        return hands.fold(mapOf<String, Int>()) { acc, hand ->
            val more = hand.colorCounts.filter { (key, value) ->
                value > acc.get(key) ?: 0
            }
            acc + more
        }
    }

    companion object {
        fun parse(input: String): Game {
            val id = getId(input)

            val handStrings = input
                .split(":").get(1)
                .split(";")
                .map { handStr ->
                    handStr.split(",").map {
                        val result = "(\\d+)\\s([a-z]+)".toRegex().find(it)?.groupValues?.orEmpty()
                        Pair(result?.get(2) ?: "na", result?.get(1)?.toIntOrNull() ?: 0)
                    }
                }.map { Hand(it.toMap()) }

            return Game(id, handStrings)
        }

        fun getId(input: String) = "(\\d+):".toRegex().find(input)?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }
}

class Day02 {

    fun part1(input: List<String>, bagContents: Map<String, Int>): Int {
        val games = input.map { Game.parse(it) }
        return games.filter { it.isPossible(bagContents) }.map { it.id }.sum()
    }

    fun part2(input: List<String>): Int {
        val games = input.map { Game.parse(it) }
        return games.map { it.minimumRequiredColorCounts().values.fold(1){ acc: Int, i: Int -> acc * i } }.sum()
    }
}


fun main() {
    val input = resourceAsListOfString("day02.txt")

    // 1. challenge
    val bagContents = mapOf("red" to 12, "green" to 13, "blue" to 14)
    println("Part 1: " + Day02().part1(input, bagContents))

    // 2. challenge
    println("Part 1: " + Day02().part2(input))

}