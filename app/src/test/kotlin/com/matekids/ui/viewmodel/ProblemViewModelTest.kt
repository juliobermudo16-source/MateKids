package com.matekids.ui.viewmodel

import com.matekids.data.repository.ProblemRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.usecase.ResolveProblemUseCase
import com.matekids.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProblemViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var problemRepository: ProblemRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var resolveProblemUseCase: ResolveProblemUseCase

    private lateinit var viewModel: ProblemViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = ProblemViewModel(problemRepository, userRepository, resolveProblemUseCase)
    }

    @Test
    fun `al cargar deja un problema listo y sin girar`() = runTest {
        whenever(problemRepository.getProblemCount()).thenReturn(10)

        viewModel.loadProblems(difficulty = 2)

        val state = viewModel.uiState.value
        assertNotNull(state.currentProblem, "no se cargo ningun problema")
        assertEquals(false, state.isLoading)
        assertEquals(2, state.currentProblem?.difficulty)
    }

    @Test
    fun `arranca sin problema cargado`() {
        assertNull(viewModel.uiState.value.currentProblem)
    }

    @Test
    fun `avisa cuando la respuesta no es un numero`() = runTest {
        whenever(problemRepository.getProblemCount()).thenReturn(10)
        // Hace falta un problema en pantalla: sin el, submitAnswer no hace nada.
        viewModel.loadProblems(difficulty = 1)

        viewModel.submitAnswer("no soy un numero")

        assertEquals("Ingresa un número válido", viewModel.uiState.value.feedback)
    }

    @Test
    fun `sin problema cargado responder no cambia nada`() = runTest {
        viewModel.submitAnswer("42")

        assertEquals("", viewModel.uiState.value.feedback)
        assertNull(viewModel.uiState.value.currentProblem)
    }

    @Test
    fun `pasar al siguiente limpia el mensaje anterior`() = runTest {
        whenever(problemRepository.getProblemCount()).thenReturn(10)
        viewModel.loadProblems(difficulty = 1)
        viewModel.submitAnswer("otra vez no")
        assertEquals("Ingresa un número válido", viewModel.uiState.value.feedback)

        viewModel.nextProblem()

        assertEquals("", viewModel.uiState.value.feedback)
    }

    @Test
    fun `la dificultad pedida se conserva`() = runTest {
        whenever(problemRepository.getProblemCount()).thenReturn(5)

        viewModel.loadProblems(difficulty = 3)

        assertEquals(3, viewModel.difficulty)
    }
}
