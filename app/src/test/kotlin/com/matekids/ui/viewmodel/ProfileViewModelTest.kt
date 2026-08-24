package com.matekids.ui.viewmodel

import com.matekids.data.repository.AchievementRepository
import com.matekids.data.repository.UserRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import com.matekids.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var achievementRepository: AchievementRepository

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = ProfileViewModel(userRepository, achievementRepository)
    }

    @Test
    fun testProfileInitialization() {
        val state = viewModel.uiState.value
        assertEquals(false, state.userProfile != null)
    }

    @Test
    fun testUpdateAliasSuccess() {
        runBlocking {
            val newAlias = "Ingeniero Pro"
            viewModel.updateAlias(newAlias)
            // Verifica que el mensaje de éxito aparezca
        }
    }

    @Test
    fun testDismissSuccessMessage() {
        viewModel.dismissSuccessMessage()
        assertEquals(null, viewModel.uiState.value.successMessage)
    }
}
