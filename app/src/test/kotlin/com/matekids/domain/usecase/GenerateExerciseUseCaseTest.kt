package com.matekids.domain.usecase

import com.matekids.domain.model.Skill
import com.matekids.domain.model.OperationType
import com.matekids.domain.model.SlotPosition
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GenerateExerciseUseCaseTest {

    private val generate = GenerateExerciseUseCase()

    /** Semilla fija: los retos deben ser reproducibles para poder testearlos. */
    private fun seeded() = Random(1234)

    @Test
    fun `la suma cuadra con sus operandos`() {
        repeat(200) {
            val exercise = generate(Skill.SUMAR, difficulty = 2, random = seeded())
            assertEquals(exercise.operand1 + exercise.operand2, exercise.result)
        }
    }

    @Test
    fun `la resta nunca da negativo`() {
        val random = Random(99)
        repeat(200) {
            val exercise = generate(Skill.RESTAR, difficulty = 3, random = random)
            assertTrue(exercise.result >= 0, "resultado negativo: ${exercise.result}")
            assertEquals(exercise.operand1 - exercise.operand2, exercise.result)
        }
    }

    @Test
    fun `la division siempre es exacta`() {
        val random = Random(7)
        repeat(200) {
            val exercise = generate(Skill.DIVIDIR, difficulty = 3, random = random)
            assertTrue(exercise.operand2 != 0, "divisor cero")
            assertEquals(0, exercise.operand1 % exercise.operand2, "division inexacta")
            assertEquals(exercise.operand1 / exercise.operand2, exercise.result)
        }
    }

    @Test
    fun `la multiplicacion cuadra con sus factores`() {
        val random = Random(55)
        repeat(200) {
            val exercise = generate(Skill.MULTIPLICAR, difficulty = 3, random = random)
            assertEquals(exercise.operand1 * exercise.operand2, exercise.result)
        }
    }

    @Test
    fun `entre las piezas esta siempre la correcta`() {
        val random = Random(2024)
        repeat(200) {
            val exercise = generate(Skill.SUMAR, difficulty = 2, random = random)
            assertTrue(
                exercise.missingValue in exercise.pieces,
                "falta la pieza correcta ${exercise.missingValue} en ${exercise.pieces}"
            )
        }
    }

    @Test
    fun `las piezas no se repiten y son positivas`() {
        val random = Random(31)
        repeat(200) {
            val exercise = generate(Skill.MULTIPLICAR, difficulty = 1, random = random)
            assertEquals(
                exercise.pieces.size,
                exercise.pieces.distinct().size,
                "piezas duplicadas: ${exercise.pieces}"
            )
            assertTrue(exercise.pieces.all { it > 0 }, "pieza no positiva: ${exercise.pieces}")
        }
    }

    @Test
    fun `se ofrecen siempre cuatro piezas`() {
        val random = Random(8)
        repeat(100) {
            val exercise = generate(Skill.DIVIDIR, difficulty = 2, random = random)
            assertEquals(GenerateExerciseUseCase.PIECE_COUNT, exercise.pieces.size)
        }
    }

    @Test
    fun `el hueco cae en las tres posiciones a lo largo del juego`() {
        val random = Random(5)
        val seen = (1..300)
            .map { generate(Skill.SUMAR, difficulty = 1, random = random).slot }
            .toSet()
        assertEquals(SlotPosition.entries.toSet(), seen, "alguna posicion nunca sale")
    }

    @Test
    fun `las unidades mixtas combinan varias operaciones`() {
        val random = Random(17)
        val seen = (1..300)
            .map { generate(Skill.CALCULO_MENTAL, difficulty = 2, random = random).type }
            .toSet()
        assertEquals(OperationType.entries.toSet(), seen, "no aparecen todas las operaciones")
    }

    @Test
    fun `la dificultad fuera de rango no rompe la generacion`() {
        val random = Random(3)
        val facil = generate(Skill.SUMAR, difficulty = -5, random = random)
        val dificil = generate(Skill.SUMAR, difficulty = 99, random = random)
        assertEquals(facil.operand1 + facil.operand2, facil.result)
        assertEquals(dificil.operand1 + dificil.operand2, dificil.result)
    }

    @Test
    fun `accepts solo acepta la pieza correcta`() {
        val exercise = generate(Skill.SUMAR, difficulty = 1, random = Random(42))
        assertTrue(exercise.accepts(exercise.missingValue))
        exercise.pieces.filter { it != exercise.missingValue }.forEach {
            assertTrue(!exercise.accepts(it), "acepto una pieza incorrecta: $it")
        }
    }

    @Test
    fun `el mecanismo muestra un unico hueco`() {
        val random = Random(64)
        repeat(100) {
            val exercise = generate(Skill.RESTAR, difficulty = 2, random = random)
            assertEquals(1, exercise.slots().count { it == null }, "deberia faltar solo una pieza")
            assertEquals(3, exercise.slots().size)
        }
    }
}
