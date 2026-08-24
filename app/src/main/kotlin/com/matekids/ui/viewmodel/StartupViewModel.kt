package com.matekids.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matekids.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** A donde ir al terminar el splash. */
enum class StartDestination {
    UNKNOWN,
    ONBOARDING,
    PATH
}

/**
 * Decide la primera pantalla: si aun no hay perfil se pasa por el alta, y si
 * ya existe se va directo al camino. El onboarding no se repite.
 */
@HiltViewModel
class StartupViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _destination = MutableStateFlow(StartDestination.UNKNOWN)
    val destination: StateFlow<StartDestination> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            _destination.value = if (userRepository.hasProfile()) {
                StartDestination.PATH
            } else {
                StartDestination.ONBOARDING
            }
        }
    }
}
