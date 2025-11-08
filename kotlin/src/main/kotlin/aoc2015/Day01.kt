package aoc2015

import resourceAsListOfString

fun solveDay01Part1(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)
  val input: String = resourceAsListOfString(inputPath).first()

  input.toList().fold(0) { acc, ch ->
    when (ch) {
      '(' -> acc + 1
      ')' -> acc - 1
      else -> acc
    }
  }.also { println("Solution part 1: " + it) }
}

fun solveDay01Part2Imperative(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)
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

fun solveDay01Part2(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)
  val input: String = resourceAsListOfString(inputPath).first()


  input
    .asSequence()
    // runningFold gir en LISTE med alle delsvarene underveis
    .runningFold(0) { acc, ch ->
      when (ch) {
        '(' -> acc + 1
        ')' -> acc - 1
        else -> acc
      }
    }.indexOfFirst { it == -1 }
    .also { println("Solution part 2: " + (it)) }
}

fun main() {
  solveDay01Part1("2024/day02.txt")
  //solveDay01Part2Imperative("2024/day02.txt")
  solveDay01Part2("2024/day02.txt")

  // Fold vs runningFold
  val input = listOf(1,2,3,4,5)

  input.fold(0) { acc, curr ->
    acc + curr
  }.also { println(it)}

  input.runningFold(0) { acc, curr ->
    acc + curr
  }.also { println(it)}
}


