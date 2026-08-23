package com.matekids.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.matekids.data.local.entity.ProblemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemDao {
    @Insert
    suspend fun insertProblem(problem: ProblemEntity): Long

    @Update
    suspend fun updateProblem(problem: ProblemEntity)

    @Query("SELECT * FROM problems ORDER BY timestamp DESC")
    fun getAllProblems(): Flow<List<ProblemEntity>>

    @Query("SELECT * FROM problems WHERE difficulty = :difficulty ORDER BY timestamp DESC")
    fun getProblemsByDifficulty(difficulty: Int): Flow<List<ProblemEntity>>

    @Query("SELECT * FROM problems WHERE id = :id")
    suspend fun getProblemById(id: Long): ProblemEntity?

    @Query("SELECT COUNT(*) FROM problems")
    suspend fun getProblemCount(): Int

    @Query("SELECT COUNT(*) FROM problems WHERE isCorrect = 1")
    suspend fun getCorrectProblemCount(): Int

    @Query("DELETE FROM problems")
    suspend fun deleteAllProblems()
}
