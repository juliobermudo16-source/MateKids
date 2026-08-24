package com.matekids.domain.usecase

import com.matekids.data.repository.OperationRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.Operation
import com.matekids.domain.model.OperationType
import com.matekids.domain.model.UserProfile
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertTrue

class ResolveOperationUseCaseTest {

    @Mock
    private lateinit var operationRepository: OperationRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var useCase: ResolveOperationUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = ResolveOperationUseCase(operationRepository, userRepository)
    }

    @Test
    fun testCorrectOperationResolution() {
        runBlocking {
            val operation = Operation(
                type = OperationType.SUM,
                operand1 = 5,
                operand2 = 3,
                correctAnswer = 8
            )

            whenever(userRepository.getUserProfileSync()).thenReturn(
                UserProfile()
            )

            val result = useCase.execute(operation, 8)

            assertTrue(result is ResolveOperationUseCase.ResolveOperationResult.Success)
        }
    }

    @Test
    fun testIncorrectOperationResolution() {
        runBlocking {
            val operation = Operation(
                type = OperationType.SUM,
                operand1 = 5,
                operand2 = 3,
                correctAnswer = 8
            )

            val result = useCase.execute(operation, 7)

            assertTrue(result is ResolveOperationUseCase.ResolveOperationResult.Failure)
        }
    }

    @Test
    fun testLevelCalculation() {
        // Level 1: 0-50 XP
        var level = calculateLevel(25)
        assertTrue(level == 1)

        // Level 2: 50-100 XP
        level = calculateLevel(75)
        assertTrue(level == 2)

        // Level 3: 100-150 XP
        level = calculateLevel(125)
        assertTrue(level == 3)
    }

    private fun calculateLevel(totalXP: Long): Int {
        var level = 1
        var xpRequired = 50L

        while (totalXP >= xpRequired) {
            level++
            xpRequired = level * 50L
            if (level >= 20) break
        }

        return level.coerceIn(1, 20)
    }
}
