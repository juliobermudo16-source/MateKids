package com.matekids.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.matekids.data.local.entity.OperationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OperationDao {
    @Insert
    suspend fun insertOperation(operation: OperationEntity): Long

    @Update
    suspend fun updateOperation(operation: OperationEntity)

    @Query("SELECT * FROM operations ORDER BY timestamp DESC")
    fun getAllOperations(): Flow<List<OperationEntity>>

    @Query("SELECT * FROM operations WHERE type = :type ORDER BY timestamp DESC")
    fun getOperationsByType(type: String): Flow<List<OperationEntity>>

    @Query("SELECT * FROM operations WHERE id = :id")
    suspend fun getOperationById(id: Long): OperationEntity?

    @Query("SELECT COUNT(*) FROM operations")
    suspend fun getOperationCount(): Int

    @Query("SELECT COUNT(*) FROM operations WHERE isCorrect = 1")
    suspend fun getCorrectOperationCount(): Int

    @Query("SELECT COUNT(*) FROM operations WHERE type = :type AND isCorrect = 1")
    suspend fun getCorrectOperationCountByType(type: String): Int

    @Query("DELETE FROM operations")
    suspend fun deleteAllOperations()
}
