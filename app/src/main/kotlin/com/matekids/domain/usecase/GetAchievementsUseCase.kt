package com.matekids.domain.usecase

import com.matekids.data.repository.AchievementRepository
import com.matekids.domain.model.Achievement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAchievementsUseCase(
    private val achievementRepository: AchievementRepository
) {

    fun execute(): Flow<List<Achievement>> {
        return achievementRepository.getAllAchievements()
    }

    fun getUnlockedAchievements(): Flow<List<Achievement>> {
        return achievementRepository.getUnlockedAchievements()
    }

    suspend fun unlockAchievement(type: String) {
        achievementRepository.unlockAchievement(type)
    }

    suspend fun getUnlockedCount(): Int {
        return achievementRepository.getUnlockedAchievementCount()
    }
}
