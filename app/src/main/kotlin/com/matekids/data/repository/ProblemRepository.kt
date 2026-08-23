package com.matekids.data.repository

import com.matekids.data.local.dao.ProblemDao
import com.matekids.data.local.entity.ProblemEntity
import com.matekids.domain.model.Problem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProblemRepository(private val problemDao: ProblemDao) {

    suspend fun saveProblem(problem: Problem): Long {
        return problemDao.insertProblem(problem.toEntity())
    }

    suspend fun updateProblem(problem: Problem) {
        problemDao.updateProblem(problem.toEntity())
    }

    fun getAllProblems(): Flow<List<Problem>> {
        return problemDao.getAllProblems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getProblemsByDifficulty(difficulty: Int): Flow<List<Problem>> {
        return problemDao.getProblemsByDifficulty(difficulty).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getProblemById(id: Long): Problem? {
        return problemDao.getProblemById(id)?.toDomain()
    }

    suspend fun getProblemCount(): Int {
        return problemDao.getProblemCount()
    }

    suspend fun getCorrectProblemCount(): Int {
        return problemDao.getCorrectProblemCount()
    }

    suspend fun deleteAllProblems() {
        problemDao.deleteAllProblems()
    }

    private fun ProblemEntity.toDomain(): Problem {
        return Problem(
            id = id,
            description = description,
            difficulty = difficulty,
            correctAnswer = correctAnswer,
            userAnswer = userAnswer,
            isCorrect = isCorrect,
            xpEarned = xpEarned,
            timestamp = timestamp
        )
    }

    private fun Problem.toEntity(): ProblemEntity {
        return ProblemEntity(
            id = id,
            description = description,
            difficulty = difficulty,
            correctAnswer = correctAnswer,
            userAnswer = userAnswer,
            isCorrect = isCorrect,
            xpEarned = xpEarned,
            timestamp = timestamp
        )
    }
}
