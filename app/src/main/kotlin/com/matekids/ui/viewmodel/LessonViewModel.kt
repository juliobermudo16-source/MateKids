package com.matekids.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matekids.data.repository.PathProgressRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.Exercise
import com.matekids.domain.model.Lesson
import com.matekids.domain.model.MathCurriculum
import com.matekids.domain.usecase.GenerateExerciseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Como quedo el ultimo intento del ejercicio en pantalla. */
enum class AnswerFeedback {
    NONE,
    CORRECT,
    WRONG
}

data class LessonUiState(
    val lesson: Lesson? = null,
    val exercise: Exercise? = null,
    val position: Int = 0,
    val total: Int = 0,
    val correctCount: Int = 0,
    val selectedPiece: Int? = null,
    val feedback: AnswerFeedback = AnswerFeedback.NONE,
    /** Explicacion de por que ese numero es el correcto. */
    val explanation: String = "",
    val isFinished: Boolean = false,
    val xpEarned: Int = 0,
    val isLoading: Boolean = true
) {
    val progress: Float
        get() = if (total == 0) 0f else (position.toFloat() / total).coerceIn(0f, 1f)

    /** Sin fallar ninguno a la primera. */
    val isPerfect: Boolean
        get() = total > 0 && correctCount == total
}

private const val XP_POR_ACIERTO = 10
private const val XP_LECCION_PERFECTA = 20

/**
 * Lleva una leccion de principio a fin.
 *
 * Solo cuenta como acierto el que se logra a la primera: si se falla, se
 * muestra la explicacion y se deja reintentar, pero ese ejercicio ya no suma.
 * Asi la estrella de leccion perfecta significa algo.
 */
@HiltViewModel
class LessonViewModel @Inject constructor(
    private val generateExercise: GenerateExerciseUseCase,
    private val progressRepository: PathProgressRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonUiState())
    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    private var exercises: List<Exercise> = emptyList()
    private var failedCurrent = false

    fun load(lessonId: String) {
        val lesson = MathCurriculum.path().findLesson(lessonId) ?: return
        exercises = generateExercise.forLesson(lesson)
        failedCurrent = false

        _uiState.value = LessonUiState(
            lesson = lesson,
            exercise = exercises.firstOrNull(),
            position = 0,
            total = exercises.size,
            isLoading = false
        )
    }

    /** Coloca una pieza en el hueco. */
    fun selectPiece(piece: Int) {
        val state = _uiState.value
        val exercise = state.exercise ?: return
        // Con el acierto ya cantado no se admiten mas toques.
        if (state.feedback == AnswerFeedback.CORRECT) return

        val acierta = exercise.accepts(piece)

        _uiState.value = state.copy(
            selectedPiece = piece,
            feedback = if (acierta) AnswerFeedback.CORRECT else AnswerFeedback.WRONG,
            explanation = if (acierta) exercise.explanation() else pistaDelFallo(exercise),
            correctCount = if (acierta && !failedCurrent) state.correctCount + 1 else state.correctCount
        )

        if (!acierta) failedCurrent = true
    }

    /** Pasa al siguiente ejercicio o cierra la leccion. */
    fun next() {
        val state = _uiState.value
        val siguiente = state.position + 1

        if (siguiente >= exercises.size) {
            finish()
            return
        }

        failedCurrent = false
        _uiState.value = state.copy(
            exercise = exercises[siguiente],
            position = siguiente,
            selectedPiece = null,
            feedback = AnswerFeedback.NONE,
            explanation = ""
        )
    }

    private fun finish() {
        val state = _uiState.value
        val lesson = state.lesson ?: return

        viewModelScope.launch {
            progressRepository.recordCompletion(
                lesson = lesson,
                correct = state.correctCount,
                total = state.total
            )
            otorgarXp(state)
        }

        _uiState.value = state.copy(
            isFinished = true,
            position = state.total,
            xpEarned = xpDe(state)
        )
    }

    /**
     * 10 XP por acierto a la primera, mas 20 de premio si la leccion sale
     * perfecta. Sin la parte fija no habria diferencia entre resolverla y
     * limitarse a reintentar hasta que salga.
     */
    private fun xpDe(state: LessonUiState): Int =
        state.correctCount * XP_POR_ACIERTO + if (state.isPerfect) XP_LECCION_PERFECTA else 0

    private suspend fun otorgarXp(state: LessonUiState) {
        val ganados = xpDe(state)
        val perfil = userRepository.getUserProfileSync() ?: return
        val totalXp = perfil.totalXP + ganados
        // 50 XP por nivel, como define UserProfile.getNextLevelXP().
        val nivel = (totalXp / 50L).toInt() + 1
        userRepository.updateXPAndLevel(totalXp, nivel)
    }

    /**
     * Ante un fallo se orienta sin dar la solucion, para que el nino pueda
     * volver a intentarlo pensando.
     */
    private fun pistaDelFallo(exercise: Exercise): String {
        val op = exercise.symbol()
        return "Esa no encaja. Fíjate bien en la operación: recuerda que el $op " +
            when (exercise.type) {
                com.matekids.domain.model.OperationType.SUM -> "junta las dos cantidades."
                com.matekids.domain.model.OperationType.SUBTRACT -> "quita una cantidad de la otra."
                com.matekids.domain.model.OperationType.MULTIPLY -> "repite una cantidad varias veces."
                com.matekids.domain.model.OperationType.DIVIDE -> "reparte en partes iguales."
            }
    }
}
