package aoc2015

import resourceAsListOfString

fun solveDay03Part1(inputPath: String) {
  val instructions = resourceAsListOfString(inputPath).first().toList()

  // Start at [0,0] and make a list of all movements.
  // At the end, count number of unique coordinates, which is the number of houses visited.
  instructions.toList().fold(listOf(Pair(0, 0))) { acc, curr ->
    val (preX, preY) = acc.last()

    val pos = when (curr) {
      '>' -> Pair(preX + 1, preY)
      '<' -> Pair((preX - 1), preY)
      '^' -> Pair(preX, preY + 1)
      'v' -> Pair(preX, (preY - 1))
      else -> throw RuntimeException("Fail")
    }
    acc + listOf(pos)
  }
    .toSet()
    .count()
    .also { println("Solution part 1: " + it) }
}

fun toCoordinates(input: List<Char>): List<Pair<Int, Int>> {
  return input.fold(listOf(Pair(0, 0))) { acc, curr ->
    val (preX, preY) = acc.last()

    val pos = when (curr) {
      '>' -> Pair(preX + 1, preY)
      '<' -> Pair((preX - 1), preY)
      '^' -> Pair(preX, preY + 1)
      'v' -> Pair(preX, (preY - 1))
      else -> throw RuntimeException("Fail")
    }
    acc + listOf(pos)
  }
}

fun solveDay03Part2(inputPath: String) {
  val instructionString = resourceAsListOfString(inputPath).first()
  val (santaInstructions, roboInstructions) = instructionString.toList().chunked(2)
    .map { it[0] to it[1] }.unzip()

  // Start at [0,0] and make a list of all movements.
  // At the end, count number of unique coordinates, which is the number of houses visited.
  (toCoordinates(santaInstructions) + toCoordinates(roboInstructions))
    .toSet()
    .count()
    .also { println("Solution part 2: " + it) }
}


fun main() {
  solveDay03Part1("2015/day03.txt")
  solveDay03Part2("2015/day03.txt")
}
