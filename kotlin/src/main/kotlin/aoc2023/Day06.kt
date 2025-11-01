package aoc2023

import resourceAsListOfString

data class Race(val time: Int, val distance: Int)

class Day06 {
    fun part1(input: List<String>): Any? {
        val times = input.first().split(":").get(1).trim().split(" ").map { it.trim() }.filter { it.isNotEmpty() }
            .map { it.toInt() }
        val distances = input.get(1).split(":").get(1).trim().split(" ").map { it.trim() }.filter { it.isNotEmpty() }
            .map { it.toInt() }

        val races = times.zip(distances).map { (time, distance) -> Race(time, distance) }
        return races.map { numberOfWaysToWin(it) }.fold(1, { acc, count -> acc * count })
    }

    private fun numberOfWaysToWin(race: Race): Int {
        return (1..(race.time - 1))
            .filter { timePressed -> ((race.time - timePressed) * timePressed) > race.distance }
            .count()
    }

    fun part2(input: List<String>): Any? {
        TODO("Not yet implemented")
    }
}

fun main() {
    //val input = resourceAsListOfString("day06_sample1.txt")
    val input = resourceAsListOfString("2023/day06.txt")

    // 1. challenge
    println("Part 1: " + Day06().part1(input))

    // 2. challenge
    // println("Part 2: " + Day06().part2(input))
}