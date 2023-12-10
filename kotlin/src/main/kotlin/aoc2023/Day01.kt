package aoc2023

import resourceAsListOfString


class Day01 {


    fun part1(lines: List<String>): Int {
        // 1. challenge
        return lines.map({ stringLineToNumbers(it) }).sum()
    }

    /**
     * The hard part here was understanding the actual rules of the second input ..
     * The order of which TEXTUAL numbers are converted, matters!
     *
     * I.e you cant replace all textual numbers, because SOME numbers interfere with others
     * example: eightwo  <- if you replace eight before two, then you get a different output
     *
     * For a given line, do the following:
     * - start from the beginning of the string, find the FIRST number OR the first textual number
     *    - if the number is textual, switch it out
     *    - stop as soon as you found the first number
     * - from the end of the string, find the FIRST number OR the first textual number
     *    - if the number is textual, switch it out
     *    - stop as soon as you found the last number
     *
     * So, you need to search for the first number from the beginning, and the last from the end.
     */
    fun part2(lines: List<String>): Int {
        return lines.map { line ->
            var curr = line
            var fromBeginning = letterToDigit.keys.filter({ curr.contains(it) }).sortedBy { curr.indexOf(it) }

            var replaceMe = fromBeginning.firstOrNull() ?: ""

            if (curr.indexOfFirst { it.isDigit() } > curr.indexOf(replaceMe)) {
                curr = curr.replaceFirst(replaceMe, letterToDigit[replaceMe] ?: "", true)
            }

            var fromEnd = letterToDigit.keys.filter({ curr.contains(it) }).sortedBy { curr.lastIndexOf(it) }

            replaceMe = fromEnd.lastOrNull() ?: ""

            if (curr.indexOfLast { it.isDigit() } < curr.lastIndexOf(replaceMe)) {
                curr = curr.replaceLast(replaceMe, letterToDigit[replaceMe] ?: "")
            }

            val output = stringLineToNumbers(curr)
            //println(line + " -> " + output)
            output
        }.sum()
    }

    private fun stringLineToNumbers(line: String): Int {
        val first = line.first { it.isDigit() }
        val last = line.last { it.isDigit() }
        return Integer.parseInt("$first$last")
    }

    fun String.replaceLast(oldValue: String, newValue: String): String {
        val lastIndex = lastIndexOf(oldValue)
        if (lastIndex == -1) {
            return this
        }
        val prefix = substring(0, lastIndex)
        val suffix = substring(lastIndex + oldValue.length)
        return "$prefix$newValue$suffix"
    }

    val letterToDigit = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )
}

fun main(args: Array<String>) {
    val input = resourceAsListOfString("day01.txt")

    // 1. challenge
    println("Part 1: " + Day01().part1(input))

    // 2. challenge
    println("Part 2: " + Day01().part2(input))
}
