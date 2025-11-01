package aoc2024

import resourceAsListOfString

fun solveDay01Part1(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  val (left, right) = lines.map {
    val (a, b) = it.split("\\s+".toRegex()).map { it.toInt() }
    Pair(a, b)
  }.unzip()

  left.sorted()
    .zip(right.sorted())
    .map { Math.abs(it.first - it.second) }
    .sum()
    .also { println(it) }
}

fun solveDay01Part2(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  val (left, right) = lines.map {
    val (a, b) = it.split("\\s+".toRegex()).map { it.toInt() }
    Pair(a, b)
  }.unzip()

  left.map { curr -> right.count { curr == it } * curr }
    .sum()
    .also { println(it) }
}


fun main() {

  solveDay01Part1("2024/day01_sample.txt")
  solveDay01Part1("2024/day01.txt")

  solveDay01Part2("2024/day01_sample.txt")
  solveDay01Part2("2024/day01.txt")

}