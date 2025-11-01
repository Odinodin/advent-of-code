package aoc2023

import resourceAsListOfString
import util.lcmOfList

class Day08 {
    fun part1(input: List<String>): Any? {
        val directions = generateSequence { input.first().toList() }.flatten().iterator()

        val nodes = parseNodes(input.drop(2))

        return navigateGraph(directions, nodes)
    }

    private fun navigateGraph(
        directions: Iterator<Char>,
        nodes: Map<String, Pair<String, String>>,
    ): Long {
        var currNodeName = "AAA"
        var stepCount = 0L
        while (currNodeName != "ZZZ") {
            stepCount++
            val curr = directions.next()

            val currNode = nodes.get(currNodeName)

            val key = if (curr == 'L') currNode?.first else currNode?.second
            if (key == null) throw RuntimeException("Bad node: " + key)
            currNodeName = key
        }

        return stepCount
    }

    private fun parseNodes(input: List<String>): Map<String, Pair<String, String>> {
        return input.map { line ->
            val nodeName = line.split("=").first().trim()

            val paths = line.split("=").get(1).trim().replaceFirst("(", "").replaceFirst(")", "").split(",")
            val left = paths.get(0).trim()
            val right = paths.get(1).trim()

            nodeName to Pair(left, right)
        }.toMap()

    }

    fun part2(input: List<String>): Any? {


        val nodes = parseNodes(input.drop(2))

        //return bruteForceNavigateGraphAsGhost(directions, nodes)
        return cycleAssumptionNavigateGraphAsGhost(input.first().toList(), nodes)
    }

    private fun cycleAssumptionNavigateGraphAsGhost(
        directionsAsChars: List<Char>,
        nodes: Map<String, Pair<String, String>>,
    ): Any? {
        var startingNodesNames = nodes.keys.filter { it.endsWith("A") }

        var cycles = listOf<Long>()
        for (startNodeName in startingNodesNames) {
            var directions = generateSequence { directionsAsChars }.flatten().iterator()
            var currNodeName = startNodeName
            var step = 0L

            while (!currNodeName.endsWith("Z")) {
                step++;
                val currDirection = directions.next()
                var currNode = nodes.get(currNodeName)
                val key = if (currDirection == 'L') currNode?.first else currNode?.second
                if (key == null) throw RuntimeException("Bad node: " + key)
                currNodeName = key
            }
            cycles += step
        }

        // find lcm of numbers in cycles
        return lcmOfList(cycles)

    }

    // This took too long ... need a smarter solution
    private fun bruteForceNavigateGraphAsGhost(directions: Iterator<Char>, nodes: Map<String, Pair<String, String>>): Any? {
        var currNodeNames = nodes.keys.filter { it.endsWith("A") }

        var stepCount = 0L
        while (!currNodeNames.all { it.endsWith("Z") }) {
            stepCount++
            val curr = directions.next()
            val currNodes = currNodeNames.map { nodes.get(it) }

            // print stepCount for every 100 iterations
            if (stepCount % 10000000 == 0L) {
                println("Step count: $stepCount " + currNodeNames)
            }

            currNodeNames = currNodes.map {
                val key = if (curr == 'L') it?.first else it?.second
                if (key == null) throw RuntimeException("Bad node: " + key)
                key
            }
        }

        return stepCount
    }
}

fun main() {
    val input = resourceAsListOfString("2023/day08.txt")
    //val input = resourceAsListOfString("day08.txt")

    // 1. challenge
    //println("Part 1: " + Day08().part1(input))

    // 2. challenge
    println("Part 2: " + Day08().part2(input))
}