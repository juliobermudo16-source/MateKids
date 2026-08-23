package com.matekids.domain.usecase

import com.matekids.data.repository.OperationRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.Operation
import com.matekids.domain.model.OperationType

class ResolveOperationUseCase(
    private val operationRepository: OperationRepository,
    private val userRepository: UserRepository
) {

    suspend fun execute(operation: Operation, userAnswer: Int): ResolveOperationResult {
        val isCorrect = userAnswer == operation.correctAnswer
        val xpEarned = if (isCorrect) 10 else 0

        val updatedOperation = operation.copy(
            userAnswer = userAnswer,
            isCorrect = isCorrect,
            xpEarned = xpEarned
        )

        operationRepository.updateOperation(updatedOperation)

        if (isCorrect) {
            val userProfile = userRepository.getUserProfileSync() ?: return ResolveOperationResult.Error
            val newTotalXP = userProfile.totalXP + xpEarned
            val newLevel = calculateLevel(newTotalXP)

            userRepository.updateXPAndLevel(newTotalXP, newLevel)
            userRepository.incrementOperationsResolved()
        }

        return if (isCorrect) {
            ResolveOperationResult.Success(xpEarned)
        } else {
            ResolveOperationResult.Failure(operation.correctAnswer)
        }
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

    sealed class ResolveOperationResult {
        data class Success(val xpEarned: Int) : ResolveOperationResult()
        data class Failure(val correctAnswer: Int) : ResolveOperationResult()
        object Error : ResolveOperationResult()
    }
}
