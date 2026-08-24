package com.matekids.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matekids.data.repository.AchievementRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.Achievement
import com.matekids.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val userProfile: UserProfile? = null,
    val achievements: List<Achievement> = emptyList(),
    val unlockedAchievementCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                userRepository.getUserProfile().collect { userProfile ->
                    achievementRepository.getAllAchievements().collect { achievements ->
                        val unlockedCount = achievements.count { it.isUnlocked }
                        _uiState.value = _uiState.value.copy(
                            userProfile = userProfile,
                            achievements = achievements,
                            unlockedAchievementCount = unlockedCount,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun updateAlias(newAlias: String) {
        viewModelScope.launch {
            try {
                val currentProfile = _uiState.value.userProfile ?: return@launch
                val updatedProfile = currentProfile.copy(alias = newAlias)
                userRepository.updateUser(updatedProfile)

                _uiState.value = _uiState.value.copy(
                    userProfile = updatedProfile,
                    successMessage = "Alias actualizado a: $newAlias"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al actualizar alias: ${e.message}"
                )
            }
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            try {
                val newProfile = UserProfile()
                userRepository.updateUser(newProfile)
                achievementRepository.getAllAchievements().collect { achievements ->
                    achievements.forEach { achievement ->
                        val resetAchievement = achievement.copy(isUnlocked = false, unlockedAt = null)
                        achievementRepository.updateAchievement(resetAchievement)
                    }
                }

                _uiState.value = _uiState.value.copy(
                    userProfile = newProfile,
                    achievements = emptyList(),
                    unlockedAchievementCount = 0,
                    successMessage = "Progreso reiniciado"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al reiniciar progreso: ${e.message}"
                )
            }
        }
    }

    fun dismissSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}
