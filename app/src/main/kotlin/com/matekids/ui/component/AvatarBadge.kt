package com.matekids.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matekids.ui.theme.Avatar
import com.matekids.ui.theme.AvatarShape
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Dibuja un avatar: su figura de fondo y el simbolo encima.
 *
 * Todo se pinta con Canvas, sin imagenes externas, para que funcione sin
 * conexion y se vea nitido a cualquier tamano.
 */
@Composable
fun AvatarBadge(
    avatar: Avatar,
    size: Dp = 64.dp,
    selected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val borde = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            drawAvatarShape(avatar.shape, avatar.color)
            if (selected) {
                drawAvatarOutline(avatar.shape, borde)
            }
        }
        Text(
            text = avatar.symbol,
            fontSize = (size.value * 0.36f).sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

private fun DrawScope.drawAvatarShape(shape: AvatarShape, color: Color) {
    when (shape) {
        AvatarShape.CIRCLE -> drawCircle(color)
        AvatarShape.ROUNDED -> drawRoundRect(
            color = color,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.minDimension * 0.28f)
        )
        else -> drawPath(pathFor(shape), color)
    }
}

private fun DrawScope.drawAvatarOutline(shape: AvatarShape, color: Color) {
    val grosor = size.minDimension * 0.06f
    when (shape) {
        AvatarShape.CIRCLE -> drawCircle(
            color = color,
            style = androidx.compose.ui.graphics.drawscope.Stroke(grosor)
        )
        AvatarShape.ROUNDED -> drawRoundRect(
            color = color,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.minDimension * 0.28f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(grosor)
        )
        else -> drawPath(
            pathFor(shape),
            color,
            style = androidx.compose.ui.graphics.drawscope.Stroke(grosor)
        )
    }
}

/** Construye la figura dentro del lienzo disponible. */
private fun DrawScope.pathFor(shape: AvatarShape): Path {
    val ancho = size.width
    val alto = size.height
    val cx = ancho / 2f
    val cy = alto / 2f
    val radio = size.minDimension / 2f

    return when (shape) {
        AvatarShape.HEXAGON -> regularPolygon(cx, cy, radio, lados = 6, giro = -PI / 2)
        AvatarShape.DIAMOND -> regularPolygon(cx, cy, radio, lados = 4, giro = 0.0)
        AvatarShape.STAR -> starPath(cx, cy, radio, radio * 0.46f, puntas = 5)
        AvatarShape.GEAR -> starPath(cx, cy, radio, radio * 0.78f, puntas = 8)
        AvatarShape.SHIELD -> shieldPath(size)
        AvatarShape.DROP -> dropPath(size)
        else -> Path()
    }
}

private fun regularPolygon(cx: Float, cy: Float, radio: Float, lados: Int, giro: Double): Path {
    val path = Path()
    repeat(lados) { i ->
        val angulo = giro + 2.0 * PI * i / lados
        val x = cx + radio * cos(angulo).toFloat()
        val y = cy + radio * sin(angulo).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

/** Alterna radio exterior e interior para formar picos. */
private fun starPath(cx: Float, cy: Float, exterior: Float, interior: Float, puntas: Int): Path {
    val path = Path()
    val total = puntas * 2
    repeat(total) { i ->
        val radio = if (i % 2 == 0) exterior else interior
        val angulo = -PI / 2 + PI * i / puntas
        val x = cx + radio * cos(angulo).toFloat()
        val y = cy + radio * sin(angulo).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

private fun shieldPath(size: Size): Path = Path().apply {
    val w = size.width
    val h = size.height
    moveTo(w * 0.5f, h * 0.04f)
    lineTo(w * 0.94f, h * 0.24f)
    lineTo(w * 0.94f, h * 0.56f)
    quadraticBezierTo(w * 0.9f, h * 0.88f, w * 0.5f, h * 0.98f)
    quadraticBezierTo(w * 0.1f, h * 0.88f, w * 0.06f, h * 0.56f)
    lineTo(w * 0.06f, h * 0.24f)
    close()
}

private fun dropPath(size: Size): Path = Path().apply {
    val w = size.width
    val h = size.height
    moveTo(w * 0.5f, h * 0.03f)
    cubicTo(w * 0.86f, h * 0.38f, w * 0.97f, h * 0.6f, w * 0.5f, h * 0.97f)
    cubicTo(w * 0.03f, h * 0.6f, w * 0.14f, h * 0.38f, w * 0.5f, h * 0.03f)
    close()
}

/** Punto de anclaje por si se necesita centrar algo sobre el avatar. */
internal fun DrawScope.avatarCenter(): Offset = Offset(size.width / 2f, size.height / 2f)
