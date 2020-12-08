package wtf.log.xmas2020.day.day04

import wtf.log.xmas2020.Day
import java.io.BufferedReader

object Day04 : Day<List<Day04.Passport>, Int, Int> {

    override fun parseInput(reader: BufferedReader): List<Passport> = Passport.parse(reader.readText())

    override fun part1(input: List<Passport>): Int = input.count { it.isValidV1 }

    override fun part2(input: List<Passport>): Int = input.count { it.isValidV2 }

    data class Passport(
        val birthYear: String?,
        val issueYear: String?,
        val expirationYear: String?,
        val height: String?,
        val hairColor: String?,
        val eyeColor: String?,
        val passportId: String?,
        val countryId: String?,
    ) {

        val isValidV1: Boolean
            get() = birthYear != null &&
                issueYear != null &&
                expirationYear != null &&
                height != null &&
                hairColor != null &&
                eyeColor != null &&
                passportId != null

        val isValidV2: Boolean
            get() = birthYear?.toIntOrNull()?.let { it in 1920..2002 } == true &&
                issueYear?.toIntOrNull()?.let { it in 2010..2020 } == true &&
                expirationYear?.toIntOrNull()?.let { it in 2020..2030 } == true &&
                isHeightValid &&
                hairColor?.let(REGEX_HAIR_COLOR::matches) == true &&
                eyeColor in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") &&
                passportId?.let(REGEX_PASSPORT_NUMBER::matches) == true

        private val isHeightValid: Boolean
            get() = height?.let(REGEX_HEIGHT::matchEntire)?.destructured?.let { (amount, unit) ->
                when (unit) {
                    "cm" -> amount.toIntOrNull()?.let { it in 150..193 }
                    "in" -> amount.toIntOrNull()?.let { it in 56..76 }
                    else -> false
                }
            } == true

        companion object {

            private val REGEX_HEIGHT = Regex("""(\d+)(cm|in)""")
            private val REGEX_HAIR_COLOR = Regex("""#[\da-f]{6}""")
            private val REGEX_PASSPORT_NUMBER = Regex("""\d{9}""")

            fun parse(text: String): List<Passport> = text
                .split("\n\n")
                .filter { it.isNotBlank() }
                .map { item ->
                    val properties = item
                        .split(Regex("\\s"))
                        .filter { it.isNotBlank() }
                        .associate { entry ->
                            val (key, value) = entry.split(":")
                            key to value
                        }
                    println("PROPS: $properties")
                    Passport(
                        birthYear = properties["byr"],
                        issueYear = properties["iyr"],
                        expirationYear = properties["eyr"],
                        height = properties["hgt"],
                        hairColor = properties["hcl"],
                        eyeColor = properties["ecl"],
                        passportId = properties["pid"],
                        countryId = properties["cid"],
                    )
                }
        }
    }
}
