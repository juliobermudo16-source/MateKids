package com.matekids.domain.model

enum class OperationType {
    SUM, SUBTRACT, MULTIPLY, DIVIDE
}

data class Operation(
    val id: Long = 0,
    val type: OperationType,
    val operand1: Int,
    val operand2: Int,
    val correctAnswer: Int,
    val userAnswer: Int? = null,
    val isCorrect: Boolean = false,
    val xpEarned: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun validate(): Boolean {
        return userAnswer == correctAnswer
    }

    fun getDisplayText(): String {
        return when (type) {
            OperationType.SUM -> "$operand1 + $operand2 = ?"
            OperationType.SUBTRACT -> "$operand1 - $operand2 = ?"
            OperationType.MULTIPLY -> "$operand1 × $operand2 = ?"
            OperationType.DIVIDE -> "$operand1 ÷ $operand2 = ?"
        }
    }
}
