package com.matekids.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matekids.data.repository.PathProgressRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.LearningPath
import com.matekids.domain.model.MathCurriculum
import com.matekids.domain.model.PathProgress
import com.matekids.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Avance de una unidad, para la lista del perfil. */
data class UnitProgress(
    val title: String,
    val completed: Int,
    val total: Int
) {
    val fraction: Float get() = if (total == 0) 0f else completed.toFloat() / total
    val isFinished: Boolean get() = total > 0 && completed == total
}

data class ProfileUiState(
    val alias: String = "",
    val avatarId: String = "",
    val level: Int = 1,
    val totalXP: Long = 0,
    val xpToNextLevel: Long = 0,
    val levelProgress: Float = 0f,
    val lessonsCompleted: Int = 0,
    val lessonsTotal: Int = 0,
    val perfectLessons: Int = 0,
    val unitsFinished: Int = 0,
    val units: List<UnitProgress> = emptyList(),
    val isLoading: Boolean = true,
    val successMessage: String? = null
) {
    val overallProgress: Float
        get() = if (lessonsTotal == 0) 0f else lessonsCompleted.toFloat() / lessonsTotal
}

/**
 * Perfil y progreso.
 *
 * Las cifras salen del avance guardado en el camino, no de contadores que
 * hubiera que ir actualizando a mano: si una leccion consta como terminada,
 * aqui se ve.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val progressRepository: PathProgressRepository
) : ViewModel() {

    private val path: LearningPath = MathCurriculum.path()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observe()
    }

    private fun observe() {
        viewModelScope.launch {
            // combine y no collect anidados: asi ambos flujos siguen vivos y
            // la pantalla se refresca venga el cambio de donde venga.
            combine(
                userRepository.getUserProfile(),
                progressRepository.observeProgress()
            ) { perfil, progreso -> perfil to progreso }
                .collect { (perfil, progreso) ->
                    _uiState.value = buildState(perfil, progreso)
                }
        }
    }

    private fun buildState(profile: UserProfile, progress: PathProgress): ProfileUiState {
        val unidades = path.units.sortedBy { it.order }.map { unit ->
            UnitProgress(
                title = unit.title,
                completed = unit.lessons.count { progress.isCompleted(it.id) },
                total = unit.lessons.size
            )
        }

        return _uiState.value.copy(
            alias = profile.alias,
            avatarId = profile.avatar,
            level = profile.level,
            totalXP = profile.totalXP,
            xpToNextLevel = profile.getXPToNextLevel(),
            levelProgress = profile.getProgressToNextLevel(),
            lessonsCompleted = progress.completed.size,
            lessonsTotal = path.lessonsInOrder().size,
            perfectLessons = progress.perfect.size,
            unitsFinished = unidades.count { it.isFinished },
            units = unidades,
            isLoading = false
        )
    }

    fun updateAlias(nuevo: String) {
        viewModelScope.launch {
            val actual = _uiState.value
            userRepository.updateUser(
                UserProfile(
                    alias = nuevo.trim().ifBlank { "Explorador" },
                    avatar = actual.avatarId,
                    totalXP = actual.totalXP,
                    level = actual.level
                )
            )
            _uiState.value = _uiState.value.copy(successMessage = "Nombre actualizado")
        }
    }

    fun updateAvatar(avatarId: String) {
        viewModelScope.launch {
            val actual = _uiState.value
            userRepository.updateUser(
                UserProfile(
                    alias = actual.alias,
                    avatar = avatarId,
                    totalXP = actual.totalXP,
                    level = actual.level
                )
            )
        }
    }

    /**
     * Vuelve a empezar: borra el avance del camino y pone el nivel a cero,
     * conservando el apodo y el avatar elegidos.
     */
    fun resetProgress() {
        viewModelScope.launch {
            val actual = _uiState.value
            progressRepository.reset()
            userRepository.updateUser(
                UserProfile(alias = actual.alias, avatar = actual.avatarId)
            )
            _uiState.value = _uiState.value.copy(successMessage = "Progreso restablecido")
        }
    }

    fun dismissMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}
