package com.matekids.ui.theme

import androidx.compose.ui.graphics.Color
import com.matekids.domain.model.Skill

/**
 * Color propio de cada destreza.
 *
 * Cada unidad del camino se reconoce por su color, asi el nino sabe de un
 * vistazo en que parte esta. Tonos vivos pero no chillones: el publico son
 * ninos de 9 a 12, no preescolares.
 */
object PathColors {

    val Sumar = Color(0xFF00D26A)
    val Restar = Color(0xFF2E7DFF)
    val SumarRestar = Color(0xFF00D3E0)
    val Multiplicar = Color(0xFFFF7A00)
    val Dividir = Color(0xFFB14BFF)
    val CalculoMental = Color(0xFFFF3D8B)
    val Problemas = Color(0xFFFFC300)

    /** Gris apagado de las lecciones aun cerradas. */
    val Locked = Color(0xFF9CA3AF)
    val LockedTrack = Color(0xFFE5E7EB)

    fun of(skill: Skill): Color = when (skill) {
        Skill.SUMAR -> Sumar
        Skill.RESTAR -> Restar
        Skill.SUMAR_RESTAR -> SumarRestar
        Skill.MULTIPLICAR -> Multiplicar
        Skill.DIVIDIR -> Dividir
        Skill.CALCULO_MENTAL -> CalculoMental
        Skill.PROBLEMAS -> Problemas
    }

    /** Simbolo que identifica la destreza en el nodo del camino. */
    fun symbolOf(skill: Skill): String = when (skill) {
        Skill.SUMAR -> "+"
        Skill.RESTAR -> "−"
        Skill.SUMAR_RESTAR -> "±"
        Skill.MULTIPLICAR -> "×"
        Skill.DIVIDIR -> "÷"
        Skill.CALCULO_MENTAL -> "≡"
        Skill.PROBLEMAS -> "?"
    }
}
