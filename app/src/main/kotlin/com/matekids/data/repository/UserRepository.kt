package com.matekids.data.repository

import com.matekids.data.local.dao.UserDao
import com.matekids.data.local.entity.UserProfileEntity
import com.matekids.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val userDao: UserDao) {

    suspend fun saveUser(user: UserProfile) {
        userDao.insertUser(user.toEntity())
    }

    suspend fun updateUser(user: UserProfile) {
        userDao.updateUser(user.toEntity())
    }

    fun getUserProfile(): Flow<UserProfile> {
        return userDao.getUserProfile().map { it.toDomain() }
    }

    suspend fun getUserProfileSync(): UserProfile? {
        return userDao.getUserProfileSync()?.toDomain()
    }

    suspend fun updateXPAndLevel(xp: Long, level: Int) {
        userDao.updateXPAndLevel(xp, level)
    }

    suspend fun incrementOperationsResolved() {
        userDao.incrementOperationsResolved()
    }

    suspend fun incrementProblemsResolved() {
        userDao.incrementProblemsResolved()
    }

    suspend fun updateCurrentStreak(streak: Int) {
        userDao.updateCurrentStreak(streak)
    }

    private fun UserProfileEntity.toDomain(): UserProfile {
        return UserProfile(
            id = id,
            avatar = avatar,
            alias = alias,
            totalXP = totalXP,
            level = level,
            operationsResolved = operationsResolved,
            problemsResolved = problemsResolved,
            accuracyRate = accuracyRate,
            currentStreak = currentStreak,
            lastActivityDate = lastActivityDate
        )
    }

    private fun UserProfile.toEntity(): UserProfileEntity {
        return UserProfileEntity(
            id = id,
            avatar = avatar,
            alias = alias,
            totalXP = totalXP,
            level = level,
            operationsResolved = operationsResolved,
            problemsResolved = problemsResolved,
            accuracyRate = accuracyRate,
            currentStreak = currentStreak,
            lastActivityDate = lastActivityDate
        )
    }
}
