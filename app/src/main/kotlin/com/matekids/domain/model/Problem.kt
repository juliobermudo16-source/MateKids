package com.matekids.domain.model

data class Problem(
    val id: Long = 0,
    val description: String,
    val difficulty: Int, // 1-3
    val correctAnswer: Int,
    val userAnswer: Int? = null,
    val isCorrect: Boolean = false,
    val xpEarned: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun validate(): Boolean {
        return userAnswer == correctAnswer
    }

    fun getDifficultyLabel(): String {
        return when (difficulty) {
            1 -> "Básico"
            2 -> "Intermedio"
            3 -> "Avanzado"
            else -> "Desconocido"
        }
    }
}
