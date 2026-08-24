package com.matekids.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Fondo = Color(0xFF0F172A)
private val FondoBajo = Color(0xFF1E293B)
private val Acento = Color(0xFF22C55E)

/** Los simbolos que orbitan alrededor del logo. */
private val simbolos = listOf("+", "−", "×", "÷", "=", "%")

/**
 * Pantalla de bienvenida.
 *
 * El logo entra con un rebote y los simbolos de las operaciones aparecen
 * alrededor, uno detras de otro, para que se vea de que trata la app antes de
 * leer una sola palabra.
 */
@Composable
fun SplashScreen(onNavigateToDashboard: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    var mostrarSimbolos by remember { mutableStateOf(false) }

    val escala by animateFloatAsState(
        targetValue = if (visible) 1f else 0.55f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escala"
    )
    val opacidad by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 450),
        label = "opacidad"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(350)
        mostrarSimbolos = true
        delay(1650)
        onNavigateToDashboard()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Fondo, FondoBajo))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                OrbitaSimbolos(visible = mostrarSimbolos)

                Box(
                    modifier = Modifier
                        .size(112.dp)
                        .scale(escala)
                        .background(
                            Brush.linearGradient(listOf(Acento, Color(0xFF0EA5E9))),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "M",
                        color = Color.White,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            Text(
                text = "MateKids",
                color = Color.White,
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.alpha(opacidad)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Aprende matemáticas jugando",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.alpha(opacidad)
            )

            Spacer(Modifier.height(28.dp))

            PuntosDeCarga(visible = mostrarSimbolos)
        }
    }
}

/** Los seis simbolos repartidos en circulo alrededor del logo. */
@Composable
private fun OrbitaSimbolos(visible: Boolean) {
    val radio by animateFloatAsState(
        targetValue = if (visible) 1f else 0.3f,
        animationSpec = tween(durationMillis = 700),
        label = "radio"
    )

    Box(modifier = Modifier.size(260.dp), contentAlignment = Alignment.Center) {
        simbolos.forEachIndexed { index, simbolo ->
            val angulo = (2.0 * Math.PI * index / simbolos.size) - Math.PI / 2
            val distancia = 104.dp.value * radio

            Text(
                text = simbolo,
                color = Acento.copy(alpha = 0.55f * radio),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(0.dp)
                    .offsetPolar(angulo, distancia)
            )
        }
    }
}

/** Coloca un elemento a cierto angulo y distancia del centro. */
private fun Modifier.offsetPolar(angulo: Double, distancia: Float): Modifier =
    this.then(
        Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(
                    x = (distancia * kotlin.math.cos(angulo)).toInt(),
                    y = (distancia * kotlin.math.sin(angulo)).toInt()
                )
            }
        }
    )

@Composable
private fun PuntosDeCarga(visible: Boolean) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(3) { index ->
            val alpha by animateFloatAsState(
                targetValue = if (visible) 1f else 0.2f,
                animationSpec = tween(durationMillis = 400, delayMillis = index * 140),
                label = "punto$index"
            )
            Canvas(modifier = Modifier.size(8.dp)) {
                drawCircle(color = Acento.copy(alpha = alpha), center = Offset(size.width / 2, size.height / 2))
            }
        }
    }
}
