package com.matekids.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey
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
)
