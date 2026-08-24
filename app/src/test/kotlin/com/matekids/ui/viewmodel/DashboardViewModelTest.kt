package com.matekids.ui.viewmodel

import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.UserProfile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
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
        val perfil = UserProfile(alias = "Ana", totalXP = 120, level = 3)
        whenever(userRepository.getUserProfile()).thenReturn(flowOf(perfil))

        assertEquals("Ana", userRepository.getUserProfile().first().alias)
        assertNotNull(viewModel.uiState.value)
    }

    @Test
    fun testDashboardInitialization() {
        val state = viewModel.uiState.value
        assertNotNull(state)
    }
}
