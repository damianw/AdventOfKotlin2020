package wtf.log.xmas2020.day.day09

import wtf.log.xmas2020.Day
import java.io.BufferedReader

object Day09 : Day<List<Long>, Long, Long> {

    private const val WINDOW_SIZE = 25

    override fun parseInput(reader: BufferedReader): List<Long> = reader
        .lineSequence()
        .map { it.toLong() }
        .toList()

    override fun part1(input: List<Long>): Long = input
        .asSequence()
        .windowed(WINDOW_SIZE + 1) { window ->
            val subject = window.last()
            for (i in 0 until window.lastIndex) {
                for (j in i until window.lastIndex) {
                    if (window[i] + window[j] == subject) {
                        return@windowed null
                    }
                }
            }
            subject
        }
        .filterNotNull()
        .first()

    override fun part2(input: List<Long>): Long {
        val target = part1(input)
        var start = 0
        var end = 0
        while (start <= input.lastIndex && end <= input.size) {
            val subList = input.subList(start, end)
            val sum = subList.sum()
            when {
                sum < target -> end++
                sum == target -> return subList.minOrNull()!! + subList.maxOrNull()!!
                else -> start++
            }
        }
        error("Couldn't find a solution")
    }
}
