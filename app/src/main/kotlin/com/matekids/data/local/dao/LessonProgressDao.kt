package com.matekids.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.matekids.data.local.entity.LessonProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonProgressDao {

    @Query("SELECT * FROM lesson_progress")
    fun observeAll(): Flow<List<LessonProgressEntity>>

    @Query("SELECT * FROM lesson_progress WHERE lessonId = :lessonId")
    suspend fun findById(lessonId: String): LessonProgressEntity?

    /** Al repetir una leccion se actualiza su fila, no se crea otra. */
    @Upsert
    suspend fun save(progress: LessonProgressEntity)

    @Query("SELECT COUNT(*) FROM lesson_progress")
    suspend fun completedCount(): Int

    @Query("DELETE FROM lesson_progress")
    suspend fun clear()
}
