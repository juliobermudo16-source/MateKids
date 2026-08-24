package com.matekids.domain.usecase

import com.matekids.domain.model.MachineType
import com.matekids.domain.model.OperationType
import com.matekids.domain.model.RepairChallenge
import com.matekids.domain.model.SlotPosition
import javax.inject.Inject
import kotlin.random.Random

/**
 * Fabrica los mecanismos rotos de cada maquina.
 *
 * Los operandos y el resultado salen siempre del mismo calculo, y las piezas
 * senuelo se construyen a partir de errores tipicos (equivocarse por uno,
 * fallar el acarreo) en vez de numeros al azar: si los senuelos fueran
 * absurdos, se acertaria por descarte sin hacer la cuenta.
 */
class GenerateRepairChallengeUseCase @Inject constructor() {

    companion object {
        /** Piezas ofrecidas para arrastrar, contando la correcta. */
        const val PIECE_COUNT = 4
    }

    operator fun invoke(
        machine: MachineType,
        difficulty: Int = 1,
        random: Random = Random.Default
    ): RepairChallenge {
        // Las maquinas mixtas sortean el tipo en cada pieza.
        val type = machine.operationType() ?: OperationType.entries.toList().random(random)
        val level = difficulty.coerceIn(1, 3)

        val (operand1, operand2, result) = operands(type, level, random)
        val slot = SlotPosition.entries.toList().random(random)
        val correct = when (slot) {
            SlotPosition.FIRST -> operand1
            SlotPosition.SECOND -> operand2
            SlotPosition.RESULT -> result
        }

        return RepairChallenge(
            type = type,
            operand1 = operand1,
            operand2 = operand2,
            result = result,
            slot = slot,
            pieces = (decoys(correct, random) + correct).shuffled(random)
        )
    }

    /** Genera operandos validos: sin restas negativas ni divisiones inexactas. */
    private fun operands(type: OperationType, level: Int, random: Random): Triple<Int, Int, Int> =
        when (type) {
            OperationType.SUM -> {
                val max = when (level) { 1 -> 10; 2 -> 25; else -> 50 }
                val a = random.nextInt(1, max + 1)
                val b = random.nextInt(1, max + 1)
                Triple(a, b, a + b)
            }
            OperationType.SUBTRACT -> {
                val max = when (level) { 1 -> 10; 2 -> 25; else -> 50 }
                val a = random.nextInt(2, max + 1)
                val b = random.nextInt(1, a)
                Triple(a, b, a - b)
            }
            OperationType.MULTIPLY -> {
                val max = when (level) { 1 -> 5; 2 -> 9; else -> 12 }
                val a = random.nextInt(2, max + 1)
                val b = random.nextInt(2, max + 1)
                Triple(a, b, a * b)
            }
            OperationType.DIVIDE -> {
                val maxDivisor = when (level) { 1 -> 5; 2 -> 8; else -> 12 }
                val divisor = random.nextInt(2, maxDivisor + 1)
                val quotient = random.nextInt(2, maxDivisor + 1)
                Triple(divisor * quotient, divisor, quotient)
            }
        }

    /** Senuelos cercanos al valor correcto, siempre positivos y distintos. */
    private fun decoys(correct: Int, random: Random): List<Int> {
        val candidates = listOf(1, -1, 2, -2, 3, -3, 10, -10)
            .map { correct + it }
            .filter { it > 0 && it != correct }
            .distinct()
            .shuffled(random)

        val chosen = candidates.take(PIECE_COUNT - 1).toMutableList()
        // Con valores muy bajos puede no haber suficientes vecinos validos.
        var extra = correct + 4
        while (chosen.size < PIECE_COUNT - 1) {
            if (extra != correct && extra !in chosen) chosen += extra
            extra++
        }
        return chosen
    }
}
