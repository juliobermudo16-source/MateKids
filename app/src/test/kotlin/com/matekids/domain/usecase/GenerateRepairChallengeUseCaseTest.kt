package com.matekids.domain.usecase

import com.matekids.domain.model.MachineType
import com.matekids.domain.model.OperationType
import com.matekids.domain.model.SlotPosition
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GenerateRepairChallengeUseCaseTest {

    private val generate = GenerateRepairChallengeUseCase()

    /** Semilla fija: los retos deben ser reproducibles para poder testearlos. */
    private fun seeded() = Random(1234)

    @Test
    fun `la suma cuadra con sus operandos`() {
        repeat(200) {
            val challenge = generate(MachineType.SUMADORA, difficulty = 2, random = seeded())
            assertEquals(challenge.operand1 + challenge.operand2, challenge.result)
        }
    }

    @Test
    fun `la resta nunca da negativo`() {
        val random = Random(99)
        repeat(200) {
            val challenge = generate(MachineType.RESTADORA, difficulty = 3, random = random)
            assertTrue(challenge.result >= 0, "resultado negativo: ${challenge.result}")
            assertEquals(challenge.operand1 - challenge.operand2, challenge.result)
        }
    }

    @Test
    fun `la division siempre es exacta`() {
        val random = Random(7)
        repeat(200) {
            val challenge = generate(MachineType.DIVISORA, difficulty = 3, random = random)
            assertTrue(challenge.operand2 != 0, "divisor cero")
            assertEquals(0, challenge.operand1 % challenge.operand2, "division inexacta")
            assertEquals(challenge.operand1 / challenge.operand2, challenge.result)
        }
    }

    @Test
    fun `la multiplicacion cuadra con sus factores`() {
        val random = Random(55)
        repeat(200) {
            val challenge = generate(MachineType.MULTIPLICADORA, difficulty = 3, random = random)
            assertEquals(challenge.operand1 * challenge.operand2, challenge.result)
        }
    }

    @Test
    fun `entre las piezas esta siempre la correcta`() {
        val random = Random(2024)
        repeat(200) {
            val challenge = generate(MachineType.SUMADORA, difficulty = 2, random = random)
            assertTrue(
                challenge.missingValue in challenge.pieces,
                "falta la pieza correcta ${challenge.missingValue} en ${challenge.pieces}"
            )
        }
    }

    @Test
    fun `las piezas no se repiten y son positivas`() {
        val random = Random(31)
        repeat(200) {
            val challenge = generate(MachineType.MULTIPLICADORA, difficulty = 1, random = random)
            assertEquals(
                challenge.pieces.size,
                challenge.pieces.distinct().size,
                "piezas duplicadas: ${challenge.pieces}"
            )
            assertTrue(challenge.pieces.all { it > 0 }, "pieza no positiva: ${challenge.pieces}")
        }
    }

    @Test
    fun `se ofrecen siempre cuatro piezas`() {
        val random = Random(8)
        repeat(100) {
            val challenge = generate(MachineType.DIVISORA, difficulty = 2, random = random)
            assertEquals(GenerateRepairChallengeUseCase.PIECE_COUNT, challenge.pieces.size)
        }
    }

    @Test
    fun `el hueco cae en las tres posiciones a lo largo del juego`() {
        val random = Random(5)
        val seen = (1..300)
            .map { generate(MachineType.SUMADORA, difficulty = 1, random = random).slot }
            .toSet()
        assertEquals(SlotPosition.entries.toSet(), seen, "alguna posicion nunca sale")
    }

    @Test
    fun `las maquinas mixtas combinan varias operaciones`() {
        val random = Random(17)
        val seen = (1..300)
            .map { generate(MachineType.CALCULO_MENTAL, difficulty = 2, random = random).type }
            .toSet()
        assertEquals(OperationType.entries.toSet(), seen, "no aparecen todas las operaciones")
    }

    @Test
    fun `la dificultad fuera de rango no rompe la generacion`() {
        val random = Random(3)
        val facil = generate(MachineType.SUMADORA, difficulty = -5, random = random)
        val dificil = generate(MachineType.SUMADORA, difficulty = 99, random = random)
        assertEquals(facil.operand1 + facil.operand2, facil.result)
        assertEquals(dificil.operand1 + dificil.operand2, dificil.result)
    }

    @Test
    fun `accepts solo acepta la pieza correcta`() {
        val challenge = generate(MachineType.SUMADORA, difficulty = 1, random = Random(42))
        assertTrue(challenge.accepts(challenge.missingValue))
        challenge.pieces.filter { it != challenge.missingValue }.forEach {
            assertTrue(!challenge.accepts(it), "acepto una pieza incorrecta: $it")
        }
    }

    @Test
    fun `el mecanismo muestra un unico hueco`() {
        val random = Random(64)
        repeat(100) {
            val challenge = generate(MachineType.RESTADORA, difficulty = 2, random = random)
            assertEquals(1, challenge.slots().count { it == null }, "deberia faltar solo una pieza")
            assertEquals(3, challenge.slots().size)
        }
    }
}
