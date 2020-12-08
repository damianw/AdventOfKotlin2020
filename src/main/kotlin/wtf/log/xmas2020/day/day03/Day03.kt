package wtf.log.xmas2020.day.day03

import org.apache.commons.math3.fraction.Fraction
import wtf.log.xmas2020.Day
import java.io.BufferedReader

object Day03 : Day<Day03.Grid, Int, Int> {

    override fun parseInput(reader: BufferedReader): Grid {
        val lines = reader.useLines { lines ->
            lines
                .filter { it.isNotBlank() }
                .toList()
        }
        return Grid.parse(lines)
    }

    override fun part1(input: Grid): Int = input.countTrees(Point(0, 0), Fraction(1, 3))

    override fun part2(input: Grid): Int {
        val start = Point(0, 0)
        val slopes = listOf(
            Fraction(1, 1),
            Fraction(1, 3),
            Fraction(1, 5),
            Fraction(1, 7),
            Fraction(2, 1),
        )
        return slopes
            .map { input.countTrees(start, it) }
            .reduce(Int::times)
    }

    data class Point(val x: Int = 0, val y: Int = 0) {

        fun castRay(slope: Fraction): Sequence<Point> = generateSequence(this) { point ->
            point.copy(x = point.x + slope.denominator, y = point.y + slope.numerator)
        }
    }

    class Grid(private val trees: BooleanArray, private val width: Int) {

        val height: Int
            get() = trees.size / width

        fun countTrees(start: Point, slope: Fraction): Int = start
            .castRay(slope)
            .takeWhile(::contains)
            .count(::get)

        operator fun contains(point: Point): Boolean = point.y in (0 until height)

        operator fun get(x: Int, y: Int): Boolean {
            val wrappedX = x % width
            return trees[y * width + wrappedX]
        }

        operator fun get(point: Point) = get(point.x, point.y)

        override fun toString(): String = buildString {
            append('┌')
            repeat(width) {
                append('─')
            }
            append("┐\n")
            repeat(height) { y ->
                append('│')
                repeat(width) { x ->
                    append(if (get(x, y)) TREE else EMPTY)
                }
                append("│\n")
            }
            append('└')
            repeat(width) {
                append('─')
            }
            append('┘')
        }

        companion object {

            private const val TREE = '#'
            private const val EMPTY = '.'

            fun parse(lines: List<String>): Grid {
                require(lines.isNotEmpty())
                val width = lines.first().length
                require(lines.all { it.length == width })
                val asteroids = BooleanArray(width * lines.size) { index ->
                    when (val character = lines[index / width][index % width]) {
                        TREE -> true
                        EMPTY -> false
                        else -> throw IllegalArgumentException("Unknown symbol: $character")
                    }
                }
                return Grid(asteroids, width)
            }
        }
    }
}
