package wtf.log.xmas2020.day.day02

import wtf.log.xmas2020.Day
import java.io.BufferedReader

object Day02 : Day<List<Day02.Entry>, Int, Int> {

    override fun parseInput(reader: BufferedReader): List<Entry> = reader.useLines { lines ->
        lines
            .filter { it.isNotBlank() }
            .map((Entry)::parse)
            .toList()
    }

    override fun part1(input: List<Entry>): Int = input.count { it.isValidV1 }

    override fun part2(input: List<Entry>): Int = input.count { it.isValidV2 }

    data class Entry(
        val range: ClosedRange<Int>,
        val character: Char,
        val password: String
    ) {

        val isValidV1: Boolean
            get() = password.count { it == character } in range

        val isValidV2: Boolean
            get() {
                val firstChar = password[range.start - 1]
                val secondChar = password[range.endInclusive - 1]
                return (firstChar == character || secondChar == character) && firstChar != secondChar
            }

        companion object {

            private val REGEX = Regex("""^(\d+)-(\d+) (\w): (\w+)${'$'}""")

            fun parse(input: String): Entry {
                val (start, end, character, password) = REGEX.matchEntire(input)!!.destructured
                return Entry(
                    range = start.toInt()..end.toInt(),
                    character = character.single(),
                    password = password
                )
            }
        }
    }
}
