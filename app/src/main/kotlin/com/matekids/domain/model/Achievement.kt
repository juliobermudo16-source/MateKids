package com.matekids.domain.model

data class Achievement(
    val id: Long = 0,
    val type: String,
    val unlockedAt: Long? = null,
    val isUnlocked: Boolean = false
) {
    fun getTitle(): String {
        return when (type) {
            "MACHINE_SUMADORA" -> "Sumadora Reparada"
            "MACHINE_RESTADORA" -> "Restadora Reparada"
            "MACHINE_MULTIPLICADORA" -> "Multiplicadora Reparada"
            "MACHINE_DIVISORA" -> "Divisora Reparada"
            "MENTAL_MATH" -> "Mente Rápida"
            "PROBLEM_SOLVER" -> "Resolvedor de Problemas"
            "CHALLENGE_MASTER" -> "Maestro de Desafíos"
            "GENIUS" -> "Genio Matemático"
            else -> "Logro Desconocido"
        }
    }

    fun getDescription(): String {
        return when (type) {
            "MACHINE_SUMADORA" -> "Repara la Sumadora Cuántica resolviendo operaciones de suma"
            "MACHINE_RESTADORA" -> "Repara la Restadora de Equilibrio resolviendo operaciones de resta"
            "MACHINE_MULTIPLICADORA" -> "Repara la Multiplicadora de Energía resolviendo operaciones de multiplicación"
            "MACHINE_DIVISORA" -> "Repara la Divisora Precisa resolviendo operaciones de división"
            "MENTAL_MATH" -> "Completa 10 desafíos de cálculo mental"
            "PROBLEM_SOLVER" -> "Resuelve 20 problemas matemáticos"
            "CHALLENGE_MASTER" -> "Supera todos los desafíos especiales"
            "GENIUS" -> "Alcanza el nivel máximo (20)"
            else -> "Descripción desconocida"
        }
    }
}
