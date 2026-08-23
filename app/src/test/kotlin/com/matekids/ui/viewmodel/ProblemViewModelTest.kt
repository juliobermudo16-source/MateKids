package com.matekids.ui.viewmodel

import com.matekids.data.repository.ProblemRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.usecase.ResolveProblemUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProblemViewModelTest {

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
    fun testLoadProblemsSuccess() = runBlocking {
        whenever(problemRepository.getProblemCount()).thenReturn(10)

        viewModel.loadProblems(difficulty = 1)

        assertNotNull(viewModel.uiState.value.currentProblem)
    }

    @Test
    fun testSubmitInvalidAnswer() {
        viewModel.submitAnswer("invalid")
        assertEquals("Ingresa un número válido", viewModel.uiState.value.feedback)
    }

    @Test
    fun testNextProblem() {
        viewModel.nextProblem()
        assertEquals("", viewModel.uiState.value.feedback)
    }
}
