package aoc2024

import resourceAsListOfString

fun solveDay02Part1(inputPath: String) {val lines = resourceAsListOfString(inputPath)
  val input: String = resourceAsListOfString(inputPath).first()

  input.toList().fold(0) { acc, ch ->
    when (ch) {
      '(' -> acc + 1
      ')' -> acc - 1
      else -> acc
    }
  }.also { println("Solution part 1: " + it) }
}

fun solveDay02Part2(inputPath: String) {val lines = resourceAsListOfString(inputPath)
  val input: String = resourceAsListOfString(inputPath).first()

  var acc = 0
  var currIdx = 0
  for ((index, ch) in input.withIndex()) {
    val curr = when (ch) {
      '(' -> 1
      ')' -> -1
      else -> 0
    }
    acc += curr
    currIdx = index
    if (index > 0 && acc == -1) break;
  }

  println("Solution part 2: " + (currIdx + 1))
}

// 1770 too low

fun main() {
  solveDay02Part1("2024/day02.txt")
  solveDay02Part2("2024/day02.txt")
}