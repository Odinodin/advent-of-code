package aoc2025

import resourceAsListOfString

data class Coord(val x: Int, val y: Int)

fun solveDay04Part1(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  val worldMap = linesToWorldMap(lines)
  worldMap.also { println("Solution part 1: " + it) }
  worldMap
    .map { (coord, value) -> isRollOfPaper(value) && numberOfAdjacentPapers(coord, worldMap) < 4 }
    .also { println("Solution part 2: " + it.count { it }) }
}

fun linesToWorldMap(lines: List<String>): Map<Coord, String> {
  return lines.foldIndexed(mapOf<Coord, String>()) { rowIdx, acc, curr ->
    println("Row $rowIdx:" + curr)

    val currRow = curr.mapIndexed { colIdx, value ->
      Pair(Coord(colIdx, rowIdx), value + "")
    }.toMap()
    acc.plus(currRow)
  }
}

fun numberOfAdjacentPapers(coord: Coord, worldMap: Map<Coord, String>): Int {
  val (x, y) = coord

  return listOf(
    Coord(x + 1, y), // right
    Coord(x - 1, y), // left
    Coord(x, y + 1),  // up
    Coord(x, y - 1), // down
    Coord(x - 1, y - 1), // top-left
    Coord(x + 1, y - 1), // top-right
    Coord(x - 1, y + 1), // bottom-left
    Coord(x + 1, y + 1), // bottom-right
  ).count { worldMap.containsKey(it) && isRollOfPaper(worldMap.get(it).orEmpty()) }
}

fun coordsForMovablePaperRolls(worldMap: Map<Coord, String>): List<Coord> {
  return worldMap.filter { isRollOfPaper(it.value) && numberOfAdjacentPapers(it.key, worldMap) < 4 }.keys.toList()
}

fun clearPaperRolls(worldMap: Map<Coord, String>, coords: List<Coord>): Map<Coord, String> =
  coords.fold(worldMap) { acc, coord -> acc.minus(coord) }

fun isRollOfPaper(value: String): Boolean {
  return value == "@"
}

fun solveDay04Part2(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  var worldMap = linesToWorldMap(lines)

  var done = false
  var numberOfRolls = 0

  while (!done) {
    val coords = coordsForMovablePaperRolls(worldMap)

    if (coords.isEmpty()) {
      done = true
    } else {
      worldMap = clearPaperRolls(worldMap, coords)
      numberOfRolls += coords.size
    }

  }

  println("Solution part 2: $numberOfRolls")
}

fun main() {
  //solveDay04Part1("2025/day04_sample.txt")
//  solveDay04Part1("2025/day04.txt")
  //solveDay04Part1("2025/day04.txt")

  solveDay04Part2("2025/day04.txt")

}

