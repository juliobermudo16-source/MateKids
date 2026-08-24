package com.matekids.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matekids.data.repository.UserRepository
import com.matekids.ui.theme.Avatars
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val alias: String = "",
    val avatarId: String = Avatars.default.id,
    val isSaved: Boolean = false
) {
    /** El alias es opcional: si se deja vacio se pone uno por defecto. */
    val canContinue: Boolean get() = alias.length <= MAX_ALIAS

    companion object {
        const val MAX_ALIAS = 14
    }
}

/**
 * Alta del perfil la primera vez que se abre la app.
 *
 * Solo se pide un apodo y un avatar: nada de nombre real, correo ni edad,
 * como exige la parte de privacidad de la especificacion.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onAliasChange(value: String) {
        // Se corta al escribir para que no quepa mas de lo que cabe en pantalla.
        _uiState.value = _uiState.value.copy(alias = value.take(OnboardingUiState.MAX_ALIAS))
    }

    fun onAvatarSelected(avatarId: String) {
        _uiState.value = _uiState.value.copy(avatarId = avatarId)
    }

    fun save(onDone: () -> Unit) {
        val state = _uiState.value
        viewModelScope.launch {
            userRepository.createProfile(alias = state.alias.trim(), avatar = state.avatarId)
            _uiState.value = state.copy(isSaved = true)
            onDone()
        }
    }
}
