package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.models.Fish
import com.rf4.fishingrf4.data.models.FishingEntry
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun FishSelectionScreen(
    lake: Lake,
    position: String,
    fishingEntries: List<FishingEntry>,
    viewModel: FishingViewModel, // ✅ On ajoute le ViewModel pour accéder à l'heure
    onFishSelected: (Fish) -> Unit,
    onBack: () -> Unit,
    onViewJournal: () -> Unit,
    onFishDetail: (Fish) -> Unit = {}
) {
    val startOfGameDayTimestamp by viewModel.startOfCurrentGameDayTimestamp.collectAsState()

    val fishCaptureStats = remember(fishingEntries, lake.id, position, startOfGameDayTimestamp) {
        fishingEntries
            // ✅ On ajoute le filtre de temps ici : on ne garde que les captures faites "aujourd'hui"
            .filter { it.lake.id == lake.id && it.position == position && it.timestamp >= startOfGameDayTimestamp }
            .groupBy { it.fish.name }
            .mapValues { (_, entries) ->
                FishCaptureStats(totalCaught = entries.size)
            }
    }


    val sortedFish = remember(lake.availableFish, fishCaptureStats) {
        lake.availableFish.sortedWith(
            compareByDescending<Fish> { fishCaptureStats[it.name]?.totalCaught ?: 0 }
                .thenBy { it.rarity.ordinal }
        )
    }

    // ✅ On récupère l'heure du jeu en temps réel
    val gameTime by viewModel.gameTime.collectAsState()

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${lake.name} - Position $position", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "🎣 Sélectionnez votre poisson", fontSize = 14.sp, color = Color(0xFFE2E8F0))
            }
            IconButton(onClick = onViewJournal) {
                Icon(Icons.Default.Info, contentDescription = "Journal", tint = Color(0xFFFFB74D))
            }
        }

        // ✅ On passe l'heure du jeu à la carte de statistiques
        FishingStatsCard(
            totalSpecies = lake.availableFish.size,
            caughtSpecies = fishCaptureStats.size,
            totalCaught = fishCaptureStats.values.sumOf { it.totalCaught },
            gameTime = gameTime
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sortedFish, key = { it.id }) { fish ->
                val stats = fishCaptureStats[fish.name]
                FishCard(
                    fish = fish,
                    stats = stats,
                    onClick = { onFishSelected(fish) },
                    onLongClick = { onFishDetail(fish) }
                )
            }
        }
    }
}


@Composable
fun FishingStatsCard(
    totalSpecies: Int,
    caughtSpecies: Int,
    totalCaught: Int,
    gameTime: LocalTime // ✅ On reçoit l'heure du jeu
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(icon = "🐟", value = "$caughtSpecies/$totalSpecies", label = "Espèces")
            StatItem(icon = "🎯", value = "$totalCaught", label = "Captures")
            // ✅ On remplace "Découverte" par l'heure du jeu
            StatItem(
                icon = "🕒",
                value = gameTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                label = "Heure en jeu"
            )
        }
    }
}
// ... Le reste du fichier (FishingStatsCard, FishCard, etc.) est inchangé ...
@Composable
fun FishingStatsCard(totalSpecies: Int, caughtSpecies: Int, totalCaught: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(icon = "🐟", value = "$caughtSpecies/$totalSpecies", label = "Espèces")
            StatItem(icon = "🎯", value = "$totalCaught", label = "Captures")
            val discoveryPercent = if (totalSpecies > 0) (caughtSpecies.toFloat() / totalSpecies * 100).toInt() else 0
            StatItem(icon = "📊", value = "$discoveryPercent%", label = "Découverte")
        }
    }
}
@Composable
fun StatItem(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 20.sp)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = label, fontSize = 12.sp, color = Color(0xFFB0BEC5))
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FishCard(fish: Fish, stats: FishCaptureStats?, onClick: () -> Unit, onLongClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
            .border(width = 2.dp, color = Color(fish.rarity.colorValue), shape = RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(fish.rarity.colorValue).copy(alpha = 0.3f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = if (stats == null) "✨" else "🎣", fontSize = 16.sp)
                if (stats != null && stats.totalCaught > 0) {
                    Row {
                        repeat(minOf(stats.totalCaught, 3)) { Text("⭐", fontSize = 10.sp) }
                        if (stats.totalCaught > 3) { Text("+", fontSize = 12.sp, color = Color(0xFFFFD700)) }
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = fish.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(fish.rarity.colorValue)),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = fish.rarity.displayName.take(1),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
                if (stats != null) {
                    Text(text = "×${stats.totalCaught}", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                } else {
                    Text(text = "NEW", fontSize = 10.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
data class FishCaptureStats(val totalCaught: Int)