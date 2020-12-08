package wtf.log.xmas2020.day.day07

import com.google.common.collect.HashMultimap
import wtf.log.xmas2020.Day
import java.io.BufferedReader

object Day07 : Day<Map<String, Map<String, Int>>, Int, Int> {

    override fun parseInput(reader: BufferedReader): Map<String, Map<String, Int>> = reader
        .lineSequence()
        .filter { it.isNotBlank() }
        .associate { line ->
            val (color, rulesPart) = line.split(" bags contain ")
            val rules = when (rulesPart) {
                "no other bags." -> emptyMap()
                else -> rulesPart
                    .split(Regex(""" bags?(?:\.$|, )"""))
                    .filter { it.isNotBlank() }
                    .associate { rule ->
                        val split = rule.split(' ')
                        val quantity = split[0].toInt()
                        val ruleColor = split.drop(1).joinToString(" ")
                        ruleColor to quantity
                    }
            }
            color to rules
        }

    override fun part1(input: Map<String, Map<String, Int>>): Int {
        val parents = HashMultimap.create<String, String>()
        for ((color, rules) in input) {
            for ((ruleColor) in rules) {
                parents.put(ruleColor, color)
            }
        }
        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<String>(parents["shiny gold"])
        while (queue.isNotEmpty()) {
            val color = queue.removeFirst()
            if (color in visited) continue
            visited += color
            queue.addAll(parents[color])
        }
        return visited.size
    }

    override fun part2(input: Map<String, Map<String, Int>>): Int = countRecursively("shiny gold", input) - 1

    private fun countRecursively(
        color: String,
        input: Map<String, Map<String, Int>>,
        requirements: MutableMap<String, Int> = mutableMapOf()
    ): Int {
        if (color in requirements) return requirements.getValue(color)
        val result = 1 + input
            .getValue(color)
            .entries
            .sumBy { (childColor, childQuantity) ->
                childQuantity * countRecursively(childColor, input, requirements)
            }
        requirements[color] = result
        return result
    }
}
