package com.matekids.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matekids.data.repository.ProblemRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.Problem
import com.matekids.domain.usecase.ResolveProblemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProblemUiState(
    val currentProblem: Problem? = null,
    val isCorrect: Boolean = false,
    val userAnswer: String = "",
    val feedback: String = "",
    val xpEarned: Int = 0,
    val problemsCompleted: Int = 0,
    val totalProblems: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProblemViewModel @Inject constructor(
    private val problemRepository: ProblemRepository,
    private val userRepository: UserRepository,
    private val resolveProblemUseCase: ResolveProblemUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProblemUiState())
    val uiState: StateFlow<ProblemUiState> = _uiState.asStateFlow()
    var difficulty: Int = 1

    fun loadProblems(difficulty: Int = 1) {
        this.difficulty = difficulty
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val allProblems = problemRepository.getProblemCount()
                val nextProblem = Problem(
                    description = "Resuelve este problema matemático",
                    difficulty = difficulty,
                    correctAnswer = (10..100).random()
                )

                _uiState.value = _uiState.value.copy(
                    currentProblem = nextProblem,
                    isLoading = false,
                    totalProblems = allProblems
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun submitAnswer(answer: String) {
        viewModelScope.launch {
            val problem = _uiState.value.currentProblem ?: return@launch

            val userAnswerInt = answer.toIntOrNull() ?: run {
                _uiState.value = _uiState.value.copy(feedback = "Ingresa un número válido")
                return@launch
            }

            val result = resolveProblemUseCase.execute(problem, userAnswerInt)

            when (result) {
                is ResolveProblemUseCase.ResolveProblemResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isCorrect = true,
                        feedback = "¡Correcto! +${result.xpEarned} XP",
                        xpEarned = result.xpEarned,
                        problemsCompleted = _uiState.value.problemsCompleted + 1,
                        userAnswer = answer
                    )
                }
                is ResolveProblemUseCase.ResolveProblemResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isCorrect = false,
                        feedback = "Incorrecto. La respuesta es ${result.correctAnswer}",
                        userAnswer = answer
                    )
                }
                ResolveProblemUseCase.ResolveProblemResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = "Error al guardar respuesta"
                    )
                }
            }
        }
    }

    fun nextProblem() {
        _uiState.value = _uiState.value.copy(userAnswer = "", feedback = "")
        loadProblems()
    }
}
