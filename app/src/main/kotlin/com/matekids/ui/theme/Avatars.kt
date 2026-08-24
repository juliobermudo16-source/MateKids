package com.matekids.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Los ocho avatares que puede elegir el nino.
 *
 * Son figuras geometricas con un simbolo matematico dentro, dibujadas en la
 * propia app: nada que descargar y nada que dependa de internet. Cada una
 * tiene forma y color distintos para que se diferencien tambien sin color.
 */
enum class AvatarShape {
    CIRCLE,
    ROUNDED,
    HEXAGON,
    DIAMOND,
    STAR,
    SHIELD,
    DROP,
    GEAR
}

data class Avatar(
    val id: String,
    val shape: AvatarShape,
    val color: Color,
    val symbol: String,
    val name: String
)

object Avatars {

    val all: List<Avatar> = listOf(
        Avatar("avatar_1", AvatarShape.CIRCLE, Color(0xFF22C55E), "+", "Suma"),
        Avatar("avatar_2", AvatarShape.ROUNDED, Color(0xFF3B82F6), "−", "Resta"),
        Avatar("avatar_3", AvatarShape.HEXAGON, Color(0xFFF97316), "×", "Producto"),
        Avatar("avatar_4", AvatarShape.DIAMOND, Color(0xFFA855F7), "÷", "Reparto"),
        Avatar("avatar_5", AvatarShape.STAR, Color(0xFFEAB308), "★", "Estrella"),
        Avatar("avatar_6", AvatarShape.SHIELD, Color(0xFF06B6D4), "π", "Pi"),
        Avatar("avatar_7", AvatarShape.DROP, Color(0xFFEC4899), "%", "Porciento"),
        Avatar("avatar_8", AvatarShape.GEAR, Color(0xFF14B8A6), "√", "Raíz")
    )

    val default: Avatar get() = all.first()

    fun byId(id: String): Avatar = all.firstOrNull { it.id == id } ?: default
}
