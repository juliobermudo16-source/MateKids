package com.matekids.domain.usecase

import com.matekids.data.repository.ProblemRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.Problem

class ResolveProblemUseCase(
    private val problemRepository: ProblemRepository,
    private val userRepository: UserRepository
) {

    suspend fun execute(problem: Problem, userAnswer: Int): ResolveProblemResult {
        val isCorrect = userAnswer == problem.correctAnswer
        val xpEarned = if (isCorrect) 15 else 0

        val updatedProblem = problem.copy(
            userAnswer = userAnswer,
            isCorrect = isCorrect,
            xpEarned = xpEarned
        )

        problemRepository.updateProblem(updatedProblem)

        if (isCorrect) {
            val userProfile = userRepository.getUserProfileSync() ?: return ResolveProblemResult.Error
            val newTotalXP = userProfile.totalXP + xpEarned
            val newLevel = calculateLevel(newTotalXP)

            userRepository.updateXPAndLevel(newTotalXP, newLevel)
            userRepository.incrementProblemsResolved()
        }

        return if (isCorrect) {
            ResolveProblemResult.Success(xpEarned)
        } else {
            ResolveProblemResult.Failure(problem.correctAnswer)
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

    sealed class ResolveProblemResult {
        data class Success(val xpEarned: Int) : ResolveProblemResult()
        data class Failure(val correctAnswer: Int) : ResolveProblemResult()
        object Error : ResolveProblemResult()
    }
}
