package aoc2025

import resourceAsListOfString

fun solveDay03Part1(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  lines
    .map { toMaxJolt(it) }
    .map { it.toLong() }
    .sum()
    .also { println("SUM: " + it) }
}

fun toMaxJolt(it: String): Number {
  var allDigits = it.toList().map { it.digitToInt() }
  val (maxIndex, tenPlace) = allDigits.dropLast(1).mapIndexed { index, i -> Pair(index, i) }.maxBy { it.second }
  val onePlace = allDigits.drop(maxIndex + 1).max()
  val alternate = ((tenPlace * 10) + onePlace)
  return alternate
}

fun toMaxJoltPart2(it: String): Number {
  val allDigits = it.toList().map { it.digitToInt() }

  var removalCount = allDigits.count() - 12
  val stack = ArrayDeque<Int>()

  for (d in allDigits) {
    while(stack.count() > 0 && removalCount > 0 && stack.last() < d ) {
      stack.pop()
      removalCount--
    }
    stack.push(d)
  }

  if (removalCount > 0) {
    (1..removalCount).forEach { stack.pop() }
  }

  println("DONE! " + stack)
  return stack.map { it.toString() }.joinToString("").toLong()



}


inline fun <T> ArrayDeque<T>.push(element: T) = addLast(element) // returns Unit

inline fun <T> ArrayDeque<T>.pop() = removeLastOrNull()

fun solveDay03Part2(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  lines
    .map { toMaxJoltPart2(it) }
    .map { it.toLong() }
    .sum()
    .also { println("SUM: " + it) }
}

fun main() {
  //solveDay03Part1("2025/day03_sample.txt")
  //solveDay03Part1("2025/day03.txt")

  solveDay03Part2("2025/day03.txt")

  //solveDay03Part2("2025/day02_sample.txt")
  // solveDay03Part2("2025/day02.txt")
}

