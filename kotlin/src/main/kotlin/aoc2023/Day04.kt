package aoc2023

import resourceAsListOfString
import java.util.regex.Pattern

data class Card(val id: Int, val winners: List<Int>, val values: List<Int>) {
    fun calculateWinPoints(): Int {
        val winCount =  winners.toSet().intersect(values.toSet()).count()
        if (winCount == 0) return 0

        var score = 1;
        for (i in 1..winCount - 1) {
             score = score * 2
        }

        return score
    }

    companion object {
        fun parse(input: String): Card {
            val id = getId(input)
            return Card(id, parseWinners(input), parseValues(input))
        }

        private fun parseWinners(input: String): List<Int> {
            return input.split(":").get(1)
                .split("|").get(0)
                .split(Pattern.compile("(\\s)+"))
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
        }

        private fun parseValues(input: String): List<Int> {
            return input.split(":").get(1)
                .split("|").get(1)
                .split(Pattern.compile("(\\s)+"))
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
        }

        fun getId(input: String) = "(\\d+):".toRegex().find(input)?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }
}

class Day04 {

    fun part1(input: List<String>): Any? {
        return input.map { Card.parse(it) }.map { it.calculateWinPoints()}.sum()
    }

    fun part2(input: List<String>): Int {
        return calculatePart2Score(input.map {Card.parse(it)})
    }

    private fun calculatePart2Score(cards: List<Card>): Int {

        val cardsbyId = cards.associateBy { it.id }

        val unprocessedScratchCards =  cards.toMutableList()
        val processedCards = mutableListOf<Card>()

        for (card in unprocessedScratchCards) {


        }

    }
}


fun main() {

    val input = resourceAsListOfString("day04.txt")

    // 1. challenge
    println("Part 1: " + Day04().part1(input))

    // 2. challenge
    //println("Part 2: " + Day04().part2(input))

}