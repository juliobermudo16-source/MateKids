package com.matekids.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operations")
data class OperationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // SUM, SUBTRACT, MULTIPLY, DIVIDE
    val operand1: Int,
    val operand2: Int,
    val correctAnswer: Int,
    val userAnswer: Int? = null,
    val isCorrect: Boolean = false,
    val xpEarned: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
