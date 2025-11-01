package aoc2024

import resourceAsListOfString

fun solveDay01(inputPath: String) {
  val lines = resourceAsListOfString(inputPath)

  val output = lines.map {
    it.split("\\s+".toRegex()).map { it.toInt() }
  }



  println(output)

}

fun main() {

  solveDay01("2024/day01.txt")


}