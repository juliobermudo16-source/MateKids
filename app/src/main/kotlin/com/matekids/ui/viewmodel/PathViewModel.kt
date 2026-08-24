package com.matekids.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matekids.data.repository.PathProgressRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.LearningPath
import com.matekids.domain.model.Lesson
import com.matekids.domain.model.LessonState
import com.matekids.domain.model.MathCurriculum
import com.matekids.domain.model.MathUnit
import com.matekids.domain.model.PathProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Una leccion ya resuelta para pintar: la pantalla no calcula estados. */
data class LessonNode(
    val lesson: Lesson,
    val state: LessonState
)

data class UnitSection(
    val unit: MathUnit,
    val nodes: List<LessonNode>,
    val progress: Float
) {
    val isFinished: Boolean get() = progress >= 1f
}

data class PathUiState(
    val sections: List<UnitSection> = emptyList(),
    val overallProgress: Float = 0f,
    val completedCount: Int = 0,
    val totalLessons: Int = 0,
    val nextLesson: Lesson? = null,
    val alias: String = "",
    val avatarId: String = "",
    val isLoading: Boolean = true
)

/**
 * Estado del camino de aprendizaje.
 *
 * Escucha el avance guardado y recalcula que lecciones estan abiertas, para
 * que al terminar una se vea al momento como se abre la siguiente.
 */
@HiltViewModel
class PathViewModel @Inject constructor(
    private val progressRepository: PathProgressRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val path: LearningPath = MathCurriculum.path()

    private val _uiState = MutableStateFlow(PathUiState())
    val uiState: StateFlow<PathUiState> = _uiState.asStateFlow()

    init {
        observeProgress()
        observeProfile()
    }

    /** El avatar y el apodo elegidos se ven en la cabecera del camino. */
    private fun observeProfile() {
        viewModelScope.launch {
            userRepository.getUserProfile().collect { perfil ->
                _uiState.value = _uiState.value.copy(
                    alias = perfil.alias,
                    avatarId = perfil.avatar
                )
            }
        }
    }

    private fun observeProgress() {
        viewModelScope.launch {
            progressRepository.observeProgress().collect { progress ->
                _uiState.value = buildState(progress)
            }
        }
    }

    private fun buildState(progress: PathProgress): PathUiState {
        val sections = path.units.sortedBy { it.order }.map { unit ->
            UnitSection(
                unit = unit,
                nodes = unit.lessons.sortedBy { it.index }.map { lesson ->
                    LessonNode(lesson = lesson, state = path.stateOf(lesson, progress))
                },
                progress = path.unitProgress(unit, progress)
            )
        }

        // copy sobre el estado actual: si se construyera uno nuevo se perderia
        // el perfil, que llega por otro flujo.
        return _uiState.value.copy(
            sections = sections,
            overallProgress = path.overallProgress(progress),
            completedCount = progress.completed.size,
            totalLessons = path.lessonsInOrder().size,
            nextLesson = path.nextLesson(progress),
            isLoading = false
        )
    }
}
