package com.matekids.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // MACHINE_SUMADORA, MACHINE_RESTADORA, etc.
    val unlockedAt: Long? = null,
    val isUnlocked: Boolean = false
)
