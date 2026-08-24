package com.matekids.data.repository

import com.matekids.data.local.dao.OperationDao
import com.matekids.data.local.entity.OperationEntity
import com.matekids.domain.model.Operation
import com.matekids.domain.model.OperationType
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class OperationRepositoryTest {

    @Mock
    private lateinit var dao: OperationDao

    private lateinit var repository: OperationRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = OperationRepository(dao)
    }

    @Test
    fun testSaveOperation() {
        runBlocking {
            val operation = Operation(
                type = OperationType.SUM,
                operand1 = 5,
                operand2 = 3,
                correctAnswer = 8
            )

            whenever(dao.insertOperation(operation.toEntity())).thenReturn(1L)

            val result = repository.saveOperation(operation)

            assertEquals(1L, result)
        }
    }

    @Test
    fun testGetOperationsByType() {
        runBlocking {
            val entities = listOf(
                OperationEntity(type = "SUM", operand1 = 2, operand2 = 3, correctAnswer = 5)
            )

            whenever(dao.getOperationsByType("SUM")).thenReturn(flowOf(entities))

            repository.getOperationsByType(OperationType.SUM).collect { ops ->
                assertEquals(1, ops.size)
                assertEquals(OperationType.SUM, ops[0].type)
            }
        }
    }

    @Test
    fun testGetOperationCount() {
        runBlocking {
            whenever(dao.getOperationCount()).thenReturn(10)

            val count = repository.getOperationCount()

            assertEquals(10, count)
            verify(dao).getOperationCount()
        }
    }

    private fun Operation.toEntity() = OperationEntity(
        type = type.name,
        operand1 = operand1,
        operand2 = operand2,
        correctAnswer = correctAnswer
    )
}
