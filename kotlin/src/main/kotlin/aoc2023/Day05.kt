package aoc2023

import resourceAsListOfString

data class Mapping(val from: Long, val to: Long, val count: Long)

data class AToB(val from: String, val to: String, val mapping: List<Mapping>) {

    companion object {
        fun parse(input: List<String>): AToB {

            val fromTo: MatchResult? = "(.*)-to-(.*)\\s".toRegex().find(input.first())
                ?: throw RuntimeException("Failed to parse: " + input.first())

            val from = fromTo?.groupValues?.get(1) ?: "";
            val to = fromTo?.groupValues?.get(2) ?: "";

            val mapping = input.drop(1).map {
                val row = it.trim().split(" ").map { it.toLong() }
                Mapping(row[1], row[0], row[2])
            }

            return AToB(from, to, mapping)
        }
    }
}

class Day05 {
    fun part1(input: List<String>): Long {
        val seeds = input.first()
            .split(":").get(1)
            .trim()
            .split(" ")
            .map { it.toLong() }

        val mappings: List<AToB> = parseToMappings(input)

        val seedToLocation = mapSeedToLocation(seeds, mappings)
        return seedToLocation.min()
    }

    private fun mapSeedToLocation(seeds: List<Long>, mappings: List<AToB>): List<Long> {
        return seeds.map {
            seedToLocation("seed", it, mappings)
        }
    }

    private fun seedToLocation(what: String, value: Long, mappings: List<AToB>): Long {
        if (what == "location") return value // Termination condition

        val atob = mappings.find { it.from == what }
        if (atob == null) throw RuntimeException("Failed to find " + what + " in " + mappings)

        val result = getValueFromMapping(value, atob)
        //println("$what $value -> $value")

        return seedToLocation(atob.to, result, mappings);
    }

    private fun getValueFromMapping(value: Long, atob: AToB): Long {
        val mapping = atob.mapping.find { value in it.from..(it.from + it.count) }

        if (mapping != null) {
            return mapping.to + (value - mapping.from)
        } else {
            return value;
        }
    }


    private fun parseToMappings(input: List<String>): List<AToB> {

        var linesOfMappings = input.drop(1)

        var groups = linesOfMappings
            .partitionBy { it == "" }
            .filter { it.count() > 1 }
            .map { AToB.parse(it) }

        return groups
    }

    fun part2(input: List<String>): Any? {
        val seedPairs = input.first()
            .split(":").get(1)
            .trim()
            .split(" ")
            .map { it.toLong() }.chunked(2).map { Pair(it.get(0), it.get(1)) }

        val mappings: List<AToB> = parseToMappings(input)

        var min = Long.MAX_VALUE;
        seedPairs.forEach {
            for (i in it.first..(it.first + it.second)) {
                val curr = seedToLocation("seed", i, mappings)
                if (curr < min) min = curr;
            }
        }

        return min
    }
}

fun main() {
    //val input = resourceAsListOfString("day05_sample1.txt")
    val input = resourceAsListOfString("day05.txt")

    // 1. challenge
    //println("Part 1: " + Day05().part1(input))

    // 2. challenge
    println("Part 2: " + Day05().part2(input))
}