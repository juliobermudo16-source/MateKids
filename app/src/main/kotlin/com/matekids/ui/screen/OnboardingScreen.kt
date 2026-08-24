package com.matekids.ui.screen

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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matekids.ui.component.AvatarBadge
import com.matekids.ui.theme.Avatars
import com.matekids.ui.viewmodel.OnboardingUiState

/**
 * Alta del perfil: elegir avatar y apodo.
 *
 * Una sola pantalla, sin registro ni datos personales. El apodo puede
 * dejarse en blanco y se rellena solo.
 */
@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    onAliasChange: (String) -> Unit,
    onAvatarSelected: (String) -> Unit,
    onContinue: () -> Unit
) {
    val elegido = Avatars.byId(uiState.avatarId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        Text(
            text = "¡Hola!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Elige tu avatar y cómo quieres que te llamemos",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(Modifier.height(28.dp))

        // El avatar elegido, en grande.
        AvatarBadge(avatar = elegido, size = 108.dp)

        Spacer(Modifier.height(8.dp))

        Text(
            text = elegido.name,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = elegido.color
        )

        Spacer(Modifier.height(24.dp))

        AvatarPicker(selectedId = uiState.avatarId, onSelect = onAvatarSelected)

        Spacer(Modifier.height(28.dp))

        OutlinedTextField(
            value = uiState.alias,
            onValueChange = onAliasChange,
            label = { Text("Tu apodo") },
            placeholder = { Text("Explorador") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Solo un apodo. No pedimos tu nombre real.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onContinue,
            enabled = uiState.canContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text("Empezar", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))
    }
}

/** Los ocho avatares en dos filas de cuatro. */
@Composable
private fun AvatarPicker(selectedId: String, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Avatars.all.chunked(4).forEach { fila ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                fila.forEach { avatar ->
                    val seleccionado = avatar.id == selectedId
                    Box(
                        modifier = Modifier
                            .size(66.dp)
                            .background(
                                color = if (seleccionado) {
                                    avatar.color.copy(alpha = 0.16f)
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                },
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable { onSelect(avatar.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        AvatarBadge(avatar = avatar, size = 48.dp, selected = seleccionado)
                    }
                }
            }
        }
    }
}
