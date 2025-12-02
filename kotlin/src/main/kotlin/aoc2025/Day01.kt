package aoc2025

import resourceAsListOfString

fun solveDay01Part1(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  val atZero = lines.runningFold(50L) { acc, curr ->
    val operation = curr.first()
    val amount = curr.drop(1).toLong()

    val result = when (operation) {
      'L' -> acc - amount
      'R' -> acc + amount
      else -> throw RuntimeException("Unhandle operation: $operation")
    }

    val actual = wrapAroundModulo(result, 100)
    return@runningFold actual
  }.filter { it == 0L }
    .count()
}

fun solveDay01Part2(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  var timesZero = 0L;
  var currentPosition = 50L;

  for (line in lines) {
    val operation = line.first()
    val amount = line.drop(1).toLong()

    val result = when (operation) {
      'L' -> amount * -1
      'R' -> amount
      else -> throw RuntimeException("Unhandle operation: $operation")
    }

    timesZero += timesPastZero(currentPosition, result)
    currentPosition = wrapAroundModulo(currentPosition + result, 100)
  }
}

fun timesPastZero(start: Long, delta: Long): Long {
  // Brute force
  if (delta == 0L) return 0L

  var count = 0L
  var pos = start

  val step = if (delta > 0) 1 else -1
  val steps = kotlin.math.abs(delta)

  repeat(steps.toInt()) {
    pos = wrapAroundModulo(pos + step, 100)
    if (pos == 0L) count++
  }

  return count
}

fun wrapAroundModulo(value: Long, modulus: Long): Long {
  val remainder = value % modulus
  return if (remainder < 0) remainder + modulus else remainder
}

fun checkIt() {

  val test = listOf(
    Triple(0L, 100L, 1L),
    Triple(0L, 1L, 0L),
    Triple(0L, -1L, 0L),
    Triple(50L, 100L, 1L),
    Triple(50L, 1000L, 10L),)

  test.forEach { (start, amount, expected) ->
    check(timesPastZero(start, amount) == expected, lazyMessage = { "Times past zero is wrong" })
  }
}

fun main() {
  checkIt()
//  solveDay01Part1("2025/day01_sample.txt")
//  solveDay01Part1("2025/day01.txt")

  //solveDay01Part2("2025/day01_sample.txt")
  solveDay01Part2("2025/day01.txt")
}