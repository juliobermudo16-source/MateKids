package com.matekids.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matekids.data.repository.OperationRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.Operation
import com.matekids.domain.model.OperationType
import com.matekids.domain.usecase.ResolveOperationUseCase
import dagger.hilt.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OperationUiState(
    val currentOperation: Operation? = null,
    val isCorrect: Boolean = false,
    val userAnswer: String = "",
    val feedback: String = "",
    val xpEarned: Int = 0,
    val operationsCompleted: Int = 0,
    val totalOperations: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OperationViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    private val userRepository: UserRepository,
    private val resolveOperationUseCase: ResolveOperationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OperationUiState())
    val uiState: StateFlow<OperationUiState> = _uiState.asStateFlow()
    private var currentOperationType: OperationType = OperationType.SUM

    fun loadOperations(operationType: OperationType) {
        currentOperationType = operationType
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val allOps = operationRepository.getOperationCount()
                val nextOp = Operation(
                    type = operationType,
                    operand1 = (1..20).random(),
                    operand2 = (1..20).random(),
                    correctAnswer = when (operationType) {
                        OperationType.SUM -> (1..20).random() + (1..20).random()
                        OperationType.SUBTRACT -> kotlin.math.abs((1..20).random() - (1..20).random())
                        OperationType.MULTIPLY -> (1..12).random() * (1..12).random()
                        OperationType.DIVIDE -> {
                            val divisor = (2..10).random()
                            ((1..10).random() * divisor)
                        }
                    }
                )

                _uiState.value = _uiState.value.copy(
                    currentOperation = nextOp,
                    isLoading = false,
                    totalOperations = allOps
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
            val operation = _uiState.value.currentOperation ?: return@launch

            val userAnswerInt = answer.toIntOrNull() ?: run {
                _uiState.value = _uiState.value.copy(feedback = "Ingresa un número válido")
                return@launch
            }

            val result = resolveOperationUseCase.execute(operation, userAnswerInt)

            when (result) {
                is ResolveOperationUseCase.ResolveOperationResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isCorrect = true,
                        feedback = "¡Correcto! +${result.xpEarned} XP",
                        xpEarned = result.xpEarned,
                        operationsCompleted = _uiState.value.operationsCompleted + 1,
                        userAnswer = answer
                    )
                }
                is ResolveOperationUseCase.ResolveOperationResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isCorrect = false,
                        feedback = "Incorrecto. La respuesta es ${result.correctAnswer}",
                        userAnswer = answer
                    )
                }
                ResolveOperationUseCase.ResolveOperationResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = "Error al guardar respuesta"
                    )
                }
            }
        }
    }

    fun nextOperation() {
        _uiState.value = _uiState.value.copy(userAnswer = "", feedback = "")
        loadOperations()
    }
}
