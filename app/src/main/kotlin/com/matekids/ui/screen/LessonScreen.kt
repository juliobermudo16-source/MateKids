package com.matekids.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matekids.domain.model.Exercise
import com.matekids.ui.theme.PathColors
import com.matekids.ui.viewmodel.AnswerFeedback
import com.matekids.ui.viewmodel.LessonUiState

private val Acierto = Color(0xFF22C55E)
private val Fallo = Color(0xFFEF4444)

/**
 * Una leccion: se completa la operacion eligiendo la pieza que falta.
 *
 * El hueco cambia de sitio entre ejercicios, asi que no vale con calcular
 * siempre de izquierda a derecha.
 */
@Composable
fun LessonScreen(
    uiState: LessonUiState,
    onSelectPiece: (Int) -> Unit,
    onNext: () -> Unit,
    onExit: () -> Unit
) {
    if (uiState.isFinished) {
        LessonSummary(uiState = uiState, onExit = onExit)
        return
    }

    val exercise = uiState.exercise
    if (uiState.isLoading || exercise == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Preparando la lección...", fontSize = 16.sp)
        }
        return
    }

    val color = PathColors.of(uiState.lesson?.skill ?: com.matekids.domain.model.Skill.SUMAR)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(20.dp)
    ) {
        LessonTopBar(uiState = uiState, onExit = onExit)

        Spacer(Modifier.height(28.dp))

        Text(
            text = "Completa la operación",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))

        OperationRow(exercise = exercise, selected = uiState.selectedPiece, accent = color)

        Spacer(Modifier.height(32.dp))

        PieceGrid(
            exercise = exercise,
            selected = uiState.selectedPiece,
            feedback = uiState.feedback,
            accent = color,
            enabled = uiState.feedback != AnswerFeedback.CORRECT,
            onSelect = onSelectPiece
        )

        Spacer(Modifier.weight(1f))

        AnimatedVisibility(visible = uiState.feedback != AnswerFeedback.NONE) {
            FeedbackPanel(
                feedback = uiState.feedback,
                explanation = uiState.explanation,
                onNext = onNext
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun LessonTopBar(uiState: LessonUiState, onExit: () -> Unit) {
    val animated by animateFloatAsState(targetValue = uiState.progress, label = "avance")

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "✕",
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.clickable(onClick = onExit)
        )
        Spacer(Modifier.width(16.dp))
        LinearProgressIndicator(
            progress = { animated },
            modifier = Modifier
                .weight(1f)
                .height(12.dp),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = "${uiState.position + 1}/${uiState.total}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

/** La operacion con su hueco, en fila. */
@Composable
private fun OperationRow(exercise: Exercise, selected: Int?, accent: Color) {
    val casillas = exercise.slots()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberBox(value = casillas[0], selected = selected, accent = accent)
        Symbol(exercise.symbol())
        NumberBox(value = casillas[1], selected = selected, accent = accent)
        Symbol("=")
        NumberBox(value = casillas[2], selected = selected, accent = accent)
    }
}

@Composable
private fun Symbol(text: String) {
    Text(
        text = text,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

/** Un numero de la operacion, o el hueco donde falta la pieza. */
@Composable
private fun NumberBox(value: Int?, selected: Int?, accent: Color) {
    val esHueco = value == null
    val mostrado = value ?: selected

    Box(
        modifier = Modifier
            .size(width = 72.dp, height = 72.dp)
            .background(
                color = when {
                    !esHueco -> MaterialTheme.colorScheme.surfaceVariant
                    mostrado != null -> accent
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(16.dp)
            )
            .then(
                if (esHueco && mostrado == null) {
                    Modifier.border(3.dp, accent, RoundedCornerShape(16.dp))
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = mostrado?.toString() ?: "?",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = when {
                esHueco && mostrado != null -> Color.White
                esHueco -> accent
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

/** Las piezas candidatas, en dos filas de dos. */
@Composable
private fun PieceGrid(
    exercise: Exercise,
    selected: Int?,
    feedback: AnswerFeedback,
    accent: Color,
    enabled: Boolean,
    onSelect: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        exercise.pieces.chunked(2).forEach { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                fila.forEach { piece ->
                    PieceButton(
                        value = piece,
                        isSelected = piece == selected,
                        feedback = feedback,
                        accent = accent,
                        enabled = enabled,
                        onClick = { onSelect(piece) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PieceButton(
    value: Int,
    isSelected: Boolean,
    feedback: AnswerFeedback,
    accent: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borde = when {
        isSelected && feedback == AnswerFeedback.CORRECT -> Acierto
        isSelected && feedback == AnswerFeedback.WRONG -> Fallo
        isSelected -> accent
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    Box(
        modifier = modifier
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(3.dp, borde, RoundedCornerShape(16.dp))
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/** Aviso con la explicacion: nunca un "incorrecto" a secas. */
@Composable
private fun FeedbackPanel(
    feedback: AnswerFeedback,
    explanation: String,
    onNext: () -> Unit
) {
    val acierto = feedback == AnswerFeedback.CORRECT
    val color = if (acierto) Acierto else Fallo

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = if (acierto) "¡Muy bien!" else "Casi",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = explanation,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (acierto) {
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/** Resumen al terminar la leccion. */
@Composable
private fun LessonSummary(uiState: LessonUiState, onExit: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (uiState.isPerfect) "★" else "✓",
            fontSize = 72.sp,
            color = if (uiState.isPerfect) PathColors.Problemas else Acierto
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = if (uiState.isPerfect) "¡Lección perfecta!" else "¡Lección completada!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "${uiState.correctCount} de ${uiState.total} a la primera",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(36.dp))

        Button(onClick = onExit, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al camino", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
