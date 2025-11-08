package aoc2015

import resourceAsListOfString


fun solveDay02Part1(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  lines
    .map { line -> line.split("x").map { it.toLong() } }
    .sumOf { (l, w, h) ->
       (
          (2 * l * w) +                       // Two short sides
          (2 * w * h) +                       // Two other sides
          (2 * h * l) +                       // Two remaining sides
          listOf(l * w, w * h, h * l).min())  // Extra wrapping paper
    }
    .also { println(it) }
}

fun main() {
  solveDay02Part1("2015/day02.txt")
}