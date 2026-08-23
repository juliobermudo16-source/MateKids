package com.matekids.ui.viewmodel

import com.matekids.data.repository.UserRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertNotNull

class DashboardViewModelTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = DashboardViewModel(userRepository)
    }

    @Test
    fun testLoadDashboardDataSuccess() = runBlocking {
        val userProfileFlow = flowOf()
        whenever(userRepository.getUserProfile()).thenReturn(userProfileFlow)

        assertNotNull(viewModel.uiState.value)
    }

    @Test
    fun testDashboardInitialization() {
        val state = viewModel.uiState.value
        assertNotNull(state)
    }
}
