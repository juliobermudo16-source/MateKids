package com.matekids.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.matekids.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserProfileEntity)

    @Update
    suspend fun updateUser(user: UserProfileEntity)

    @Query("SELECT * FROM user_profiles WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles WHERE id = 1")
    suspend fun getUserProfileSync(): UserProfileEntity?

    @Query("UPDATE user_profiles SET totalXP = totalXP + :xp, level = :level WHERE id = 1")
    suspend fun updateXPAndLevel(xp: Long, level: Int)

    @Query("UPDATE user_profiles SET operationsResolved = operationsResolved + 1 WHERE id = 1")
    suspend fun incrementOperationsResolved()

    @Query("UPDATE user_profiles SET problemsResolved = problemsResolved + 1 WHERE id = 1")
    suspend fun incrementProblemsResolved()

    @Query("UPDATE user_profiles SET currentStreak = :streak WHERE id = 1")
    suspend fun updateCurrentStreak(streak: Int)
}
