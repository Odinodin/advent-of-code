package aoc2024

import resourceAsListOfString

fun solveDay01Part1(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  val (left, right) = lines.map {
    val (a, b) = it.split(Regex("\\s+")).map { it.toLong() }
    a to b // Pair
  }.unzip()

  left.sorted()
    .zip(right.sorted())
    .map { (first, second) -> Math.abs(first - second) }
    .sum()
    .also { println(it) }
}

fun solveDay01Part2(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  val (left, right) = lines.map {
    val (a, b) = it.split("""\s+""".toRegex()).map { it.toLong() }
    a to b // Pair
  }.unzip()

  // Calculate how many times each number appears in the left list
  // This version runs through the right list for each number, not very efficient
//  left.map { curr -> right.count { curr == it } * curr }
//    .sum()
//    .also { println(it) }

  // Note the use of groupingBy, which is not the same as groupBy.
  // A Grouping is a construct that supports certain operations per group, like fold or eachCount.s
  val freqs = right.groupingBy { it }.eachCount()
  left.map { freqs.getOrDefault(it, 0) * it }.sum().also { println(it) }
}


fun main() {
  solveDay01Part1("2024/day01_sample.txt")
  solveDay01Part1("2024/day01.txt")

  solveDay01Part2("2024/day01_sample.txt")
  solveDay01Part2("2024/day01.txt")
}