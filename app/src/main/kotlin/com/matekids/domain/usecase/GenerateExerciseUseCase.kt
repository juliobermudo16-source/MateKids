package com.matekids.domain.usecase

import com.matekids.domain.model.Exercise
import com.matekids.domain.model.Lesson
import com.matekids.domain.model.OperationType
import com.matekids.domain.model.Skill
import com.matekids.domain.model.SlotPosition
import javax.inject.Inject
import kotlin.random.Random

/**
 * Fabrica los ejercicios de una leccion.
 *
 * Los operandos y el resultado salen siempre del mismo calculo, y las opciones
 * senuelo se construyen a partir de errores tipicos (equivocarse por uno,
 * fallar el acarreo) en vez de numeros al azar: si los senuelos fueran
 * absurdos, se acertaria por descarte sin hacer la cuenta.
 */
class GenerateExerciseUseCase @Inject constructor() {

    companion object {
        /** Opciones ofrecidas, contando la correcta. */
        const val PIECE_COUNT = 4
    }

    /** Tanda completa de ejercicios para una leccion del camino. */
    fun forLesson(lesson: Lesson, random: Random = Random.Default): List<Exercise> =
        (1..lesson.exerciseCount).map {
            invoke(lesson.skill, lesson.difficulty, random)
        }

    operator fun invoke(
        skill: Skill,
        difficulty: Int = 1,
        random: Random = Random.Default
    ): Exercise {
        val type = operationFor(skill, random)
        val level = difficulty.coerceIn(1, 3)

        val (operand1, operand2, result) = operands(type, level, random)
        val slot = SlotPosition.entries.toList().random(random)
        val correct = when (slot) {
            SlotPosition.FIRST -> operand1
            SlotPosition.SECOND -> operand2
            SlotPosition.RESULT -> result
        }

        return Exercise(
            type = type,
            operand1 = operand1,
            operand2 = operand2,
            result = result,
            slot = slot,
            pieces = (decoys(correct, random) + correct).shuffled(random)
        )
    }

    /** Operacion que toca segun la destreza de la unidad. */
    private fun operationFor(skill: Skill, random: Random): OperationType = when (skill) {
        Skill.SUMAR -> OperationType.SUM
        Skill.RESTAR -> OperationType.SUBTRACT
        Skill.MULTIPLICAR -> OperationType.MULTIPLY
        Skill.DIVIDIR -> OperationType.DIVIDE
        Skill.SUMAR_RESTAR ->
            listOf(OperationType.SUM, OperationType.SUBTRACT).random(random)
        Skill.CALCULO_MENTAL, Skill.PROBLEMAS ->
            OperationType.entries.toList().random(random)
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
