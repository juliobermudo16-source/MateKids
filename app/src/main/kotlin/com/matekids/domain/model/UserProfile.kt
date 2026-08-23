package com.matekids.domain.model

data class UserProfile(
    val id: Long = 1,
    val avatar: String = "avatar_1",
    val alias: String = "Ingeniero",
    val totalXP: Long = 0,
    val level: Int = 1,
    val operationsResolved: Int = 0,
    val problemsResolved: Int = 0,
    val accuracyRate: Float = 0f,
    val currentStreak: Int = 0,
    val lastActivityDate: Long = System.currentTimeMillis()
) {
    fun getNextLevelXP(): Long {
        return (level * 50L)
    }

    fun getXPToNextLevel(): Long {
        return (getNextLevelXP() - totalXP).coerceAtLeast(0)
    }

    fun getProgressToNextLevel(): Float {
        val nextLevelXP = getNextLevelXP()
        val prevLevelXP = (level - 1) * 50L
        val totalInLevel = nextLevelXP - prevLevelXP
        val currentProgress = (totalXP - prevLevelXP).coerceAtLeast(0)
        return if (totalInLevel > 0) (currentProgress.toFloat() / totalInLevel).coerceIn(0f, 1f) else 0f
    }
}
