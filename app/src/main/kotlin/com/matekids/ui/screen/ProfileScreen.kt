package com.matekids.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.matekids.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var editingAlias by remember { mutableStateOf(false) }
    var newAlias by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color.White
                )
            }
            Text(
                text = "Mi Perfil",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            uiState.userProfile?.let { profile ->
                // Profile Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = profile.alias,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Text(
                            text = "Nivel ${profile.level}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = "${profile.totalXP} XP",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // XP Progress
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Progreso al siguiente nivel", fontSize = 14.sp, fontWeight = FontWeight.Bold)

                        LinearProgressIndicator(
                            progress = profile.getProgressToNextLevel(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )

                        Text(
                            text = "${profile.getXPToNextLevel()} XP faltantes",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                // Statistics
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Estadísticas", fontSize = 14.sp, fontWeight = FontWeight.Bold)

                        StatRow("Operaciones resueltas", "${profile.operationsResolved}")
                        StatRow("Problemas resueltos", "${profile.problemsResolved}")
                        StatRow("Racha actual", "${profile.currentStreak}")
                        StatRow("Precisión", "${(profile.accuracyRate * 100).toInt()}%")
                    }
                }

                // Edit Alias Section
                if (editingAlias) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Cambiar alias", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

                            OutlinedTextField(
                                value = newAlias,
                                onValueChange = { newAlias = it },
                                label = { Text("Nuevo alias") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (newAlias.isNotBlank()) {
                                            viewModel.updateAlias(newAlias)
                                            editingAlias = false
                                            newAlias = ""
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Guardar")
                                }

                                Button(
                                    onClick = {
                                        editingAlias = false
                                        newAlias = ""
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancelar")
                                }
                            }
                        }
                    }
                } else {
                    Button(
                        onClick = { editingAlias = true; newAlias = profile.alias },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Cambiar Alias")
                    }
                }

                // Achievements Progress
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Logros", fontSize = 14.sp, fontWeight = FontWeight.Bold)

                        LinearProgressIndicator(
                            progress = uiState.unlockedAchievementCount.toFloat() / uiState.achievements.size.coerceAtLeast(1),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )

                        Text(
                            text = "${uiState.unlockedAchievementCount}/${uiState.achievements.size} desbloqueados",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                // Reset Progress Button
                Button(
                    onClick = { viewModel.resetProgress() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text("Reiniciar Progreso")
                }
            }

            // Success Message
            if (uiState.successMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF10B981),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = uiState.successMessage!!,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            // Error Message
            if (uiState.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFEF4444),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = uiState.error!!,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * Una linea de estadistica: etiqueta a la izquierda, valor a la derecha.
 *
 * Vivia en StatsScreen, que se elimino con el cambio de rumbo; se recupera
 * aqui, que es donde se sigue usando.
 */
@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
