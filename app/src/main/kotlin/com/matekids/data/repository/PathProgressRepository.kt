package com.matekids.data.repository

import com.matekids.data.local.dao.LessonProgressDao
import com.matekids.data.local.entity.LessonProgressEntity
import com.matekids.domain.model.Lesson
import com.matekids.domain.model.PathProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Guarda y recupera el avance por el camino.
 *
 * Traduce entre las filas de Room y el PathProgress que entiende el dominio,
 * de modo que las reglas del camino no sepan nada de la base de datos.
 */
@Singleton
class PathProgressRepository @Inject constructor(
    private val dao: LessonProgressDao
) {

    fun observeProgress(): Flow<PathProgress> = dao.observeAll().map { filas ->
        PathProgress(
            completed = filas.map { it.lessonId }.toSet(),
            perfect = filas.filter { it.isPerfect }.map { it.lessonId }.toSet()
        )
    }

    /**
     * Registra el final de una leccion.
     *
     * Conserva siempre el mejor resultado: si el nino la repite y le va peor,
     * no pierde la marca anterior ni deja de estar perfecta.
     */
    suspend fun recordCompletion(lesson: Lesson, correct: Int, total: Int) {
        val previo = dao.findById(lesson.id)
        val fuePerfecta = correct == total

        dao.save(
            LessonProgressEntity(
                lessonId = lesson.id,
                unitId = lesson.unitId,
                bestCorrect = maxOf(correct, previo?.bestCorrect ?: 0),
                totalExercises = total,
                isPerfect = fuePerfecta || (previo?.isPerfect ?: false),
                timesPlayed = (previo?.timesPlayed ?: 0) + 1,
                completedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun completedCount(): Int = dao.completedCount()

    suspend fun reset() = dao.clear()
}
