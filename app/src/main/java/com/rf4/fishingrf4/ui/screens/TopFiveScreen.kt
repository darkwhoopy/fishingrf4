package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import com.rf4.fishingrf4.data.online.SpeciesCount
import com.rf4.fishingrf4.ui.components.BackButton

private val BlueCard = Color(0xFF1F4690)      // fond carte
private val BlueCardAlt = Color(0xFF2455AF)   // variante légère
private val BlueChip = Color(0xFF3B82F6)      // chip compteur

@Composable
fun TopFiveScreen(
    speciesTop5: List<SpeciesCount>,
    playersTop5: List<Pair<String, Long>>,
    lakesTop5: List<Pair<String, Long>>,
    onBack: () -> Unit
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Espèces", "Joueurs", "Lacs")

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackButton(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("🏆 Top 5 du jour", style = MaterialTheme.typography.titleLarge, color = Color.White)
        }
        Spacer(Modifier.height(8.dp))

        TabRow(selectedTabIndex = tabIndex, containerColor = Color(0xFF3B82F6)) {
            tabs.forEachIndexed { i, title ->
                Tab(
                    selected = tabIndex == i,
                    onClick = { tabIndex = i },
                    text = { Text(title, color = Color.White) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        when (tabIndex) {
            0 -> Top5List(speciesTop5.map { it.species to it.count })
            1 -> Top5List(playersTop5)
            2 -> Top5List(lakesTop5)
        }
    }
}

@Composable
private fun Top5List(data: List<Pair<String, Long>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2563EB)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            data.forEachIndexed { index, (label, value) ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2455AF)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${index + 1}. $label", color = Color.White)
                        AssistChip(
                            onClick = { },
                            label = { Text("$value", color = Color.White) },
                            colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFF3B82F6))
                        )
                    }
                }
            }
            if (data.isEmpty()) {
                Text("Aucune donnée pour aujourd’hui.", color = Color(0xFFE5E7EB))
            }
        }
    }
}



