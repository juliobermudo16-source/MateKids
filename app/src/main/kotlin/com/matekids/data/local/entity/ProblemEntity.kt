package com.matekids.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "problems")
data class ProblemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val description: String,
    val difficulty: Int, // 1-3
    val correctAnswer: Int,
    val userAnswer: Int? = null,
    val isCorrect: Boolean = false,
    val xpEarned: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
