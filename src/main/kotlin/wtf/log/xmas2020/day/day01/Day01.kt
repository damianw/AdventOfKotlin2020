package wtf.log.xmas2020.day.day01

import wtf.log.xmas2020.Day
import java.io.BufferedReader

object Day01 : Day<List<Int>, Int, Int> {

    override fun part1(input: List<Int>): Int? {
        for (i in 0..input.lastIndex) {
            for (j in (i + 1)..input.lastIndex) {
                val first = input[i]
                val second = input[j]
                if (first + second == 2020) {
                    return first * second
                }
            }
        }
        error("No solution")
    }

    override fun part2(input: List<Int>): Int? {
        for (i in 0..input.lastIndex) {
            for (j in (i + 1)..input.lastIndex) {
                for (k in (j + 1)..input.lastIndex) {
                    val first = input[i]
                    val second = input[j]
                    val third = input[k]
                    if (first + second + third == 2020) {
                        return first * second * third
                    }
                }
            }
        }
        error("No solution")
    }

    override fun parseInput(reader: BufferedReader): List<Int> = reader.useLines { lines ->
        lines
            .filter { it.isNotBlank() }
            .map { it.toInt() }
            .toList()
    }
}
