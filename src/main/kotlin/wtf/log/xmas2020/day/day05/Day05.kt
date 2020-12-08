package wtf.log.xmas2020.day.day05

import wtf.log.xmas2020.Day
import java.io.BufferedReader

object Day05 : Day<List<Day05.Seat>, Int, Int> {

    override fun parseInput(reader: BufferedReader): List<Seat> = reader
        .lineSequence()
        .map((Seat)::parse)
        .sortedBy { it.id }
        .toList()

    override fun part1(input: List<Seat>): Int = input.last().id

    override fun part2(input: List<Seat>): Int {
        var previous: Seat? = null
        for (seat in input) {
            if (previous == null) {
                previous = seat
                continue
            }
            val expectedId = previous.id + 1
            previous = seat
            if (expectedId != seat.id) {
                return expectedId
            }
        }
        error("Couldn't find the seat")
    }

    data class Seat(val row: Int = 0, val column: Int = 0) {

        val id: Int
            get() = row * 8 + column

        companion object {

            fun parse(input: String): Seat {
                require(input.length == 10)
                val row = input
                    .substring(0, 7)
                    .map { char ->
                        when (char) {
                            'F' -> 0
                            'B' -> 1
                            else -> error("Unknown char: $input")
                        }
                    }
                    .joinToString("")
                    .toInt(2)
                val column = input
                    .substring(7)
                    .map { char ->
                        when (char) {
                            'L' -> 0
                            'R' -> 1
                            else -> error("Unknown char: $input")
                        }
                    }
                    .joinToString("")
                    .toInt(2)
                return Seat(row, column)
            }
        }
    }
}
