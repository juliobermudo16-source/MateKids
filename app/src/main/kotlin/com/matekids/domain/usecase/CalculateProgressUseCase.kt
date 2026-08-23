package com.matekids.domain.usecase

import com.matekids.data.repository.OperationRepository
import com.matekids.data.repository.ProblemRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.UserProfile

class CalculateProgressUseCase(
    private val operationRepository: OperationRepository,
    private val problemRepository: ProblemRepository,
    private val userRepository: UserRepository
) {

    suspend fun getUserProgressStats(): UserProgressStats? {
        val userProfile = userRepository.getUserProfileSync() ?: return null
        val operationCount = operationRepository.getOperationCount()
        val correctOperationCount = operationRepository.getCorrectOperationCount()
        val problemCount = problemRepository.getProblemCount()
        val correctProblemCount = problemRepository.getCorrectProblemCount()

        val totalAttempts = operationCount + problemCount
        val totalCorrect = correctOperationCount + correctProblemCount
        val accuracyRate = if (totalAttempts > 0) {
            (totalCorrect.toFloat() / totalAttempts) * 100
        } else {
            0f
        }

        return UserProgressStats(
            userProfile = userProfile,
            totalAttempts = totalAttempts,
            totalCorrect = totalCorrect,
            accuracyRate = accuracyRate,
            operationCount = operationCount,
            correctOperationCount = correctOperationCount,
            problemCount = problemCount,
            correctProblemCount = correctProblemCount
        )
    }

    data class UserProgressStats(
        val userProfile: UserProfile,
        val totalAttempts: Int,
        val totalCorrect: Int,
        val accuracyRate: Float,
        val operationCount: Int,
        val correctOperationCount: Int,
        val problemCount: Int,
        val correctProblemCount: Int
    )
}
