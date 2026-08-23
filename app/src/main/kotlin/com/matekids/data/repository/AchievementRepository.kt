package com.matekids.data.repository

import com.matekids.data.local.dao.AchievementDao
import com.matekids.data.local.entity.AchievementEntity
import com.matekids.domain.model.Achievement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AchievementRepository(private val achievementDao: AchievementDao) {

    suspend fun saveAchievement(achievement: Achievement): Long {
        return achievementDao.insertAchievement(achievement.toEntity())
    }

    suspend fun updateAchievement(achievement: Achievement) {
        achievementDao.updateAchievement(achievement.toEntity())
    }

    fun getAllAchievements(): Flow<List<Achievement>> {
        return achievementDao.getAllAchievements().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getAchievementByType(type: String): Achievement? {
        return achievementDao.getAchievementByType(type)?.toDomain()
    }

    suspend fun getUnlockedAchievementCount(): Int {
        return achievementDao.getUnlockedAchievementCount()
    }

    fun getUnlockedAchievements(): Flow<List<Achievement>> {
        return achievementDao.getUnlockedAchievements().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun unlockAchievement(type: String, timestamp: Long = System.currentTimeMillis()) {
        achievementDao.unlockAchievement(type, timestamp)
    }

    private fun AchievementEntity.toDomain(): Achievement {
        return Achievement(
            id = id,
            type = type,
            unlockedAt = unlockedAt,
            isUnlocked = isUnlocked
        )
    }

    private fun Achievement.toEntity(): AchievementEntity {
        return AchievementEntity(
            id = id,
            type = type,
            unlockedAt = unlockedAt,
            isUnlocked = isUnlocked
        )
    }
}
