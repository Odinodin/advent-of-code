package aoc2025

import resourceAsListOfString

fun isOdd(n: Int): Boolean = n % 2 != 0

fun invalidId(id: String): Boolean {
  if (isOdd(id.length)) return false
  val middle = id.length / 2
  return id.substring(0, middle) == id.substring(middle)
}

// Could use this for part 1 as well
fun invalidIdPart2(id: String): Boolean {
  val middle = Math.ceil((id.length / 2).toDouble()).toInt()

  return (1..middle)
    .map { numOfChars ->
      val candidate = id.substring(0, numOfChars)
      val rest = id.substring(numOfChars)

      val allMatching = rest.chunked(numOfChars).all { it == candidate }
      return@map allMatching
    }.any { it == true }
}

fun solveDay02Part1(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  lines.joinToString(",")
    .split(",")
    .mapNotNull { it.split("-") }
    .filter { it.count() == 2 }
    .flatMap { (a, b) ->
      (a.toLong()..b.toLong()).filter { invalidId(it.toString()) }
    }
    .sum()
    .also { println(it) }
}


fun solveDay02Part2(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  lines.joinToString(",")
    .split(",")
    .mapNotNull { it.split("-") }
    .filter { it.count() == 2 }
    .flatMap { (a, b) ->
      (a.toLong()..b.toLong()).filter { invalidIdPart2(it.toString()) }
    }
    .sum()
    .also { println(it) }
}

fun main() {

  //solveDay02Part1("2025/day02_sample.txt")
  //solveDay02Part1("2025/day02.txt")

  //solveDay02Part2("2025/day02_sample.txt")
  solveDay02Part2("2025/day02.txt")

}