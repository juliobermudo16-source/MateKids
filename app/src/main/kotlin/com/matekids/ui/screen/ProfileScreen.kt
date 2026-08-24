package com.matekids.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.matekids.domain.model.Skill
import com.matekids.ui.component.AvatarBadge
import com.matekids.ui.theme.Avatars
import com.matekids.ui.theme.PathColors
import com.matekids.ui.viewmodel.ProfileUiState
import com.matekids.ui.viewmodel.ProfileViewModel
import com.matekids.ui.viewmodel.UnitProgress

/**
 * Perfil y progreso real.
 *
 * Las cifras se calculan sobre las lecciones guardadas, asi que lo que se ve
 * aqui coincide siempre con lo que hay en el camino.
 */
@Composable
fun ProfileScreen(navController: NavHostController, viewModel: ProfileViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var editando by remember { mutableStateOf(false) }
    var confirmandoReset by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "←",
                fontSize = 26.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Mi progreso",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(Modifier.height(20.dp))

        CabeceraPerfil(uiState = uiState, onEditar = { editando = true })

        Spacer(Modifier.height(20.dp))

        ResumenCifras(uiState)

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Avance por unidad",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(10.dp))

        uiState.units.forEachIndexed { index, unidad ->
            FilaUnidad(unidad = unidad, color = PathColors.of(colorDeUnidad(index)))
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { confirmandoReset = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Empezar de nuevo", color = Color(0xFFEF4444), fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(16.dp))
    }

    if (editando) {
        DialogoAlias(
            actual = uiState.alias,
            onGuardar = {
                viewModel.updateAlias(it)
                editando = false
            },
            onCerrar = { editando = false }
        )
    }

    if (confirmandoReset) {
        AlertDialog(
            onDismissRequest = { confirmandoReset = false },
            title = { Text("¿Empezar de nuevo?") },
            text = {
                Text(
                    "Se borrarán las ${uiState.lessonsCompleted} lecciones que has " +
                        "completado y volverás al principio del camino. Tu avatar y tu " +
                        "nombre se quedan como están."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetProgress()
                    confirmandoReset = false
                }) {
                    Text("Sí, empezar de nuevo", color = Color(0xFFEF4444))
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmandoReset = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun CabeceraPerfil(uiState: ProfileUiState, onEditar: () -> Unit) {
    val avatar = Avatars.byId(uiState.avatarId)
    val progresoNivel by animateFloatAsState(
        targetValue = uiState.levelProgress,
        animationSpec = tween(700),
        label = "nivel"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(avatar.color, avatar.color.copy(alpha = 0.72f))),
                RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AvatarBadge(avatar = avatar, size = 68.dp)
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = uiState.alias.ifBlank { "Explorador" },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Nivel ${uiState.level}  ·  ${uiState.totalXP} XP",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
            Text(
                text = "✎",
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.clickable(onClick = onEditar)
            )
        }

        Spacer(Modifier.height(14.dp))

        LinearProgressIndicator(
            progress = { progresoNivel },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.3f),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Te faltan ${uiState.xpToNextLevel} XP para el nivel ${uiState.level + 1}",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}

@Composable
private fun ResumenCifras(uiState: ProfileUiState) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Cifra(
            valor = "${uiState.lessonsCompleted}/${uiState.lessonsTotal}",
            etiqueta = "Lecciones",
            color = PathColors.Sumar,
            modifier = Modifier.weight(1f)
        )
        Cifra(
            valor = "${uiState.perfectLessons}",
            etiqueta = "Perfectas ★",
            color = PathColors.Problemas,
            modifier = Modifier.weight(1f)
        )
        Cifra(
            valor = "${uiState.unitsFinished}",
            etiqueta = "Unidades",
            color = PathColors.Dividir,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun Cifra(valor: String, etiqueta: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(18.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = valor, fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
        Spacer(Modifier.height(2.dp))
        Text(
            text = etiqueta,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
        )
    }
}

@Composable
private fun FilaUnidad(unidad: UnitProgress, color: Color) {
    val avance by animateFloatAsState(
        targetValue = unidad.fraction,
        animationSpec = tween(600),
        label = "unidad"
    )
    val colorBarra by animateColorAsState(
        targetValue = if (unidad.isFinished) color else color.copy(alpha = 0.75f),
        label = "colorBarra"
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (unidad.isFinished) "${unidad.title}  ✓" else unidad.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${unidad.completed}/${unidad.total}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { avance },
            modifier = Modifier
                .fillMaxWidth()
                .height(9.dp),
            color = colorBarra,
            trackColor = PathColors.LockedTrack,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}

@Composable
private fun DialogoAlias(actual: String, onGuardar: (String) -> Unit, onCerrar: () -> Unit) {
    var texto by remember { mutableStateOf(actual) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("¿Cómo te llamamos?") },
        text = {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it.take(14) },
                singleLine = true,
                label = { Text("Tu apodo") }
            )
        },
        confirmButton = {
            Button(onClick = { onGuardar(texto) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) { Text("Cancelar") }
        }
    )
}

/** Reparte los colores del camino entre las unidades del resumen. */
private fun colorDeUnidad(index: Int): Skill =
    Skill.entries[index % Skill.entries.size]
