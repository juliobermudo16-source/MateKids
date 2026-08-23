package com.matekids.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matekids.domain.usecase.CalculateProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class StatsUiState(
    val totalAttempts: Int = 0,
    val totalCorrect: Int = 0,
    val accuracyRate: Float = 0f,
    val operationCount: Int = 0,
    val correctOperationCount: Int = 0,
    val problemCount: Int = 0,
    val correctProblemCount: Int = 0,
    val userLevel: Int = 1,
    val userXP: Long = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class StatsViewModel(
    private val calculateProgressUseCase: CalculateProgressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val stats = calculateProgressUseCase.getUserProgressStats()

                if (stats != null) {
                    _uiState.value = StatsUiState(
                        totalAttempts = stats.totalAttempts,
                        totalCorrect = stats.totalCorrect,
                        accuracyRate = stats.accuracyRate,
                        operationCount = stats.operationCount,
                        correctOperationCount = stats.correctOperationCount,
                        problemCount = stats.problemCount,
                        correctProblemCount = stats.correctProblemCount,
                        userLevel = stats.userProfile.level,
                        userXP = stats.userProfile.totalXP,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun refreshStats() {
        loadStats()
    }
}
