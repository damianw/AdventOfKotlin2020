package wtf.log.xmas2020.day.day06

import wtf.log.xmas2020.Day
import java.io.BufferedReader

object Day06 : Day<List<Day06.Group>, Int, Int> {

    override fun parseInput(reader: BufferedReader): List<Group> = reader
        .readText()
        .splitToSequence("\n\n")
        .filter { it.isNotBlank() }
        .map { group ->
            val respondents = group
                .lines()
                .filter { it.isNotBlank() }
                .map { it.toCharArray().toSet() }
            Group(respondents)
        }
        .toList()

    override fun part1(input: List<Group>): Int = input.sumBy { it.affirmativeAnswersByAnyRespondent.size }

    override fun part2(input: List<Group>): Int? = input.sumBy { it.affirmativeAnswersByAllRespondents.size }

    data class Group(
        val respondents: List<Set<Char>>
    ) {

        val affirmativeAnswersByAnyRespondent: Set<Char>
            get() = respondents.flatMapTo(mutableSetOf()) { it }

        val affirmativeAnswersByAllRespondents: Set<Char>
            get() = respondents.reduce { left, right -> left.toMutableSet().apply { retainAll(right) } }
    }
}
