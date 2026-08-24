package com.matekids.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matekids.domain.model.LessonState
import com.matekids.ui.component.AvatarBadge
import com.matekids.ui.theme.Avatars
import com.matekids.ui.theme.PathColors
import com.matekids.ui.viewmodel.LessonNode
import com.matekids.ui.viewmodel.PathUiState
import com.matekids.ui.viewmodel.UnitSection

/**
 * El camino de aprendizaje: unidades encadenadas con sus lecciones.
 *
 * Los nodos se colocan en zigzag para que se lea como un sendero y no como
 * una lista, que es justo lo que la especificacion pide evitar.
 */
@Composable
fun PathScreen(
    uiState: PathUiState,
    onLessonClick: (String) -> Unit,
    onProfileClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PathHeader(
            alias = uiState.alias,
            avatarId = uiState.avatarId,
            completed = uiState.completedCount,
            total = uiState.totalLessons,
            progress = uiState.overallProgress,
            onProfileClick = onProfileClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp
            )
        ) {
            uiState.sections.forEach { section ->
                item(key = "header-${section.unit.id}") {
                    UnitHeader(section)
                }
                items(section.nodes, key = { it.lesson.id }) { node ->
                    LessonRow(node = node, onClick = { onLessonClick(node.lesson.id) })
                }
                item(key = "gap-${section.unit.id}") {
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

/** Cabecera fija con el avance global. */
@Composable
private fun PathHeader(
    alias: String,
    avatarId: String,
    completed: Int,
    total: Int,
    progress: Float,
    onProfileClick: () -> Unit
) {
    val animated by animateFloatAsState(targetValue = progress, label = "progreso")

    Surface(color = MaterialTheme.colorScheme.primaryContainer, tonalElevation = 3.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // Evita que el titulo quede debajo del reloj y la bateria.
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (alias.isBlank()) "Tu camino" else "Hola, $alias",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "$completed de $total lecciones",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(onClick = onProfileClick),
                    contentAlignment = Alignment.Center
                ) {
                    AvatarBadge(avatar = Avatars.byId(avatarId), size = 44.dp)
                }
            }

            Spacer(Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { animated },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
    }
}

@Composable
private fun UnitHeader(section: UnitSection) {
    val color = PathColors.of(section.unit.skill)

    Column(modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = PathColors.symbolOf(section.unit.skill),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = section.unit.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = section.unit.subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
            if (section.isFinished) {
                Text(text = "✓", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
            }
        }
    }
}

/**
 * Una leccion del sendero. Se desplaza segun su posicion para dibujar el
 * zigzag caracteristico del camino.
 */
@Composable
private fun LessonRow(node: LessonNode, onClick: () -> Unit) {
    val color = PathColors.of(node.lesson.skill)
    val locked = node.state == LessonState.LOCKED

    // Zigzag: centro, derecha, centro, izquierda...
    val offset = when (node.lesson.index % 4) {
        0 -> 0.dp
        1 -> 56.dp
        2 -> 0.dp
        else -> (-56).dp
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(start = if (offset > 0.dp) offset else 0.dp,
                         end = if (offset < 0.dp) -offset else 0.dp)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LessonNodeCircle(node = node, color = color, locked = locked, onClick = onClick)

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.alpha(if (locked) 0.45f else 1f)) {
                Text(
                    text = node.lesson.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stateLabel(node.state),
                    fontSize = 12.sp,
                    color = if (locked) PathColors.Locked else color
                )
            }
        }
    }
}

@Composable
private fun LessonNodeCircle(
    node: LessonNode,
    color: Color,
    locked: Boolean,
    onClick: () -> Unit
) {
    val fill = if (locked) PathColors.LockedTrack else color

    Box(
        modifier = Modifier
            .size(64.dp)
            .background(fill, CircleShape)
            .then(if (locked) Modifier else Modifier.clickable(onClick = onClick)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = nodeSymbol(node),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = if (locked) PathColors.Locked else Color.White
        )
    }
}

/** El icono distingue el estado sin depender solo del color. */
private fun nodeSymbol(node: LessonNode): String = when (node.state) {
    LessonState.LOCKED -> "🔒"
    LessonState.AVAILABLE -> PathColors.symbolOf(node.lesson.skill)
    LessonState.COMPLETED -> "✓"
    LessonState.PERFECT -> "★"
}

private fun stateLabel(state: LessonState): String = when (state) {
    LessonState.LOCKED -> "Bloqueada"
    LessonState.AVAILABLE -> "Empezar"
    LessonState.COMPLETED -> "Completada"
    LessonState.PERFECT -> "¡Perfecta!"
}
