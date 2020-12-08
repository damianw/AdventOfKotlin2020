package wtf.log.xmas2020.day.day08

import wtf.log.xmas2020.Day
import java.io.BufferedReader

object Day08 : Day<List<Instruction>, Int, Int> {

    override fun parseInput(reader: BufferedReader): List<Instruction> = reader
        .lineSequence()
        .filter { it.isNotBlank() }
        .map((Instruction)::parse).toList()

    override fun part1(input: List<Instruction>): Int {
        val machine = Machine(input)
        machine.run()
        return machine.accumulator
    }

    override fun part2(input: List<Instruction>): Int {
        val machine = Machine()
        for (index in input.indices) {
            val instruction = input[index]
            val replacement = when (instruction.kind) {
                Instruction.Kind.ACC -> continue
                Instruction.Kind.JMP -> instruction.copy(kind = Instruction.Kind.NOP)
                Instruction.Kind.NOP -> instruction.copy(kind = Instruction.Kind.JMP)
            }
            machine.reset()
            machine.program = input.toMutableList().apply { set(index, replacement) }
            if (machine.run()) {
                return machine.accumulator
            }
        }
        error("Couldn't find an instruction to replace")
    }
}

class Machine(var program: List<Instruction> = listOf(Instruction(Instruction.Kind.NOP, 0))) {

    private val visitedInstructions = mutableSetOf<Int>()

    var accumulator: Int = 0
        private set

    var pc: Int = 0
        private set

    fun reset() {
        visitedInstructions.clear()
        accumulator = 0
        pc = 0
    }

    fun run(): Boolean {
        while (pc in program.indices) {
            if (pc in visitedInstructions) {
                return false
            }
            visitedInstructions += pc
            execute(program[pc])
        }
        return true
    }

    private fun execute(instruction: Instruction): Unit = when (instruction.kind) {
        Instruction.Kind.ACC -> {
            accumulator += instruction.value
            pc += 1
        }
        Instruction.Kind.JMP -> pc += instruction.value
        Instruction.Kind.NOP -> pc += 1
    }
}

data class Instruction(
    val kind: Kind,
    val value: Int
) {

    enum class Kind {
        ACC,
        JMP,
        NOP,
    }

    companion object {

        fun parse(input: String) = Instruction(
            kind = when (input.substring(0, 3)) {
                "acc" -> Kind.ACC
                "jmp" -> Kind.JMP
                "nop" -> Kind.NOP
                else -> error("Unknown instruction: $input")
            },
            value = input.substring(4).toInt()
        )
    }
}
