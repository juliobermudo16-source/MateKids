package com.matekids.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Avance guardado de una leccion del camino.
 *
 * Solo se escribe cuando la leccion se termina; mientras se esta jugando el
 * estado vive en el ViewModel. La clave es el id de la leccion, asi que
 * repetir una ya hecha actualiza la fila en vez de duplicarla.
 */
@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey
    val lessonId: String,
    val unitId: String,
    /** Aciertos a la primera en la mejor partida. */
    val bestCorrect: Int,
    val totalExercises: Int,
    /** Terminada sin fallar ni un ejercicio. */
    val isPerfect: Boolean,
    val timesPlayed: Int,
    val completedAt: Long
)
