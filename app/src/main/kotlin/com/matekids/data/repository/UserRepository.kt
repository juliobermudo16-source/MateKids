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

    /**
     * Perfil del nino. Mientras no haya terminado el onboarding la tabla esta
     * vacia y Room emite null, asi que se devuelve un perfil por defecto en
     * lugar de reventar.
     */
    fun getUserProfile(): Flow<UserProfile> {
        return userDao.getUserProfile().map { it?.toDomain() ?: UserProfile() }
    }

    /** True cuando todavia no se ha creado ningun perfil. */
    suspend fun hasProfile(): Boolean = userDao.getUserProfileSync() != null

    /** Crea el perfil con lo elegido en el onboarding. */
    suspend fun createProfile(alias: String, avatar: String) {
        saveUser(UserProfile(alias = alias.ifBlank { "Explorador" }, avatar = avatar))
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
