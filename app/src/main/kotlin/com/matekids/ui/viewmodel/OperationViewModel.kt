package com.matekids.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matekids.data.repository.OperationRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.Operation
import com.matekids.domain.model.OperationType
import com.matekids.domain.usecase.ResolveOperationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

                // Los operandos y la respuesta tienen que salir del mismo
                // calculo: generarlos por separado producia respuestas falsas.
                val (operand1, operand2, answer) = when (operationType) {
                    OperationType.SUM -> {
                        val a = (1..20).random()
                        val b = (1..20).random()
                        Triple(a, b, a + b)
                    }
                    OperationType.SUBTRACT -> {
                        // El minuendo manda para no dar resultados negativos.
                        val a = (1..20).random()
                        val b = (1..a).random()
                        Triple(a, b, a - b)
                    }
                    OperationType.MULTIPLY -> {
                        val a = (1..12).random()
                        val b = (1..12).random()
                        Triple(a, b, a * b)
                    }
                    OperationType.DIVIDE -> {
                        // Se construye desde el resultado para que sea exacta.
                        val divisor = (2..10).random()
                        val cociente = (1..10).random()
                        Triple(divisor * cociente, divisor, cociente)
                    }
                }

                val nextOp = Operation(
                    type = operationType,
                    operand1 = operand1,
                    operand2 = operand2,
                    correctAnswer = answer
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
        loadOperations(currentOperationType)
    }
}
