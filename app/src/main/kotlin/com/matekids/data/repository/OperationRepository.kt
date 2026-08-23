package com.matekids.data.repository

import com.matekids.data.local.dao.OperationDao
import com.matekids.data.local.entity.OperationEntity
import com.matekids.domain.model.Operation
import com.matekids.domain.model.OperationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OperationRepository(private val operationDao: OperationDao) {

    suspend fun saveOperation(operation: Operation): Long {
        return operationDao.insertOperation(operation.toEntity())
    }

    suspend fun updateOperation(operation: Operation) {
        operationDao.updateOperation(operation.toEntity())
    }

    fun getAllOperations(): Flow<List<Operation>> {
        return operationDao.getAllOperations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getOperationsByType(type: OperationType): Flow<List<Operation>> {
        return operationDao.getOperationsByType(type.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getOperationById(id: Long): Operation? {
        return operationDao.getOperationById(id)?.toDomain()
    }

    suspend fun getOperationCount(): Int {
        return operationDao.getOperationCount()
    }

    suspend fun getCorrectOperationCount(): Int {
        return operationDao.getCorrectOperationCount()
    }

    suspend fun getCorrectOperationCountByType(type: OperationType): Int {
        return operationDao.getCorrectOperationCountByType(type.name)
    }

    suspend fun deleteAllOperations() {
        operationDao.deleteAllOperations()
    }

    private fun OperationEntity.toDomain(): Operation {
        return Operation(
            id = id,
            type = OperationType.valueOf(type),
            operand1 = operand1,
            operand2 = operand2,
            correctAnswer = correctAnswer,
            userAnswer = userAnswer,
            isCorrect = isCorrect,
            xpEarned = xpEarned,
            timestamp = timestamp
        )
    }

    private fun Operation.toEntity(): OperationEntity {
        return OperationEntity(
            id = id,
            type = type.name,
            operand1 = operand1,
            operand2 = operand2,
            correctAnswer = correctAnswer,
            userAnswer = userAnswer,
            isCorrect = isCorrect,
            xpEarned = xpEarned,
            timestamp = timestamp
        )
    }
}
