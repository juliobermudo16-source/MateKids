package com.matekids.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.matekids.domain.model.OperationType
import com.matekids.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Bienvenida al Núcleo",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Repara las máquinas del laboratorio",
                    color = Color(0xFFE0E9FF),
                    fontSize = 14.sp
                )
            }

            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Machines Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(6) { index ->
                MachineCard(
                    title = getMachineTitle(index),
                    isRepaired = false,
                    onClick = {
                        when (index) {
                            0 -> navController.navigate("operations/${OperationType.SUM}")
                            1 -> navController.navigate("operations/${OperationType.SUBTRACT}")
                            2 -> navController.navigate("operations/${OperationType.MULTIPLY}")
                            3 -> navController.navigate("operations/${OperationType.DIVIDE}")
                            4 -> navController.navigate("problems/1")
                            5 -> navController.navigate("challenges")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun MachineCard(
    title: String,
    isRepaired: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .size(160.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRepaired) Color(0xFF10B981) else Color(0xFFE5E7EB)
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                // Machine Icon Placeholder
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = if (isRepaired) Color(0xFF059669) else Color(0xFF9CA3AF),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚙",
                        fontSize = 32.sp
                    )
                }

                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isRepaired) Color.White else Color.Black,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                if (isRepaired) {
                    Text(
                        text = "✓ Reparada",
                        fontSize = 10.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

private fun getMachineTitle(index: Int): String {
    return when (index) {
        0 -> "Sumadora\nCuántica"
        1 -> "Restadora\nEquilibrio"
        2 -> "Multiplicadora\nEnergía"
        3 -> "Divisora\nPrecisa"
        4 -> "Cálculo\nMental"
        5 -> "Fábrica de\nDesafíos"
        else -> "Máquina"
    }
}
