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
import androidx.compose.ui.platform.LocalContext           // 🆕 AJOUTÉ
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.models.Fish
import com.rf4.fishingrf4.data.models.FishingEntry
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.data.models.getLocalizedName  // 🆕 AJOUTÉ
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.components.BaitSelectionDialog
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun FishSelectionScreen(
    lake: Lake,
    position: String,
    fishingEntries: List<FishingEntry>,
    viewModel: FishingViewModel,
    onBack: () -> Unit,
    onViewJournal: () -> Unit,
    onFishDetail: (Fish) -> Unit = {}
) {
    val context = LocalContext.current                    // 🆕 AJOUTÉ
    val startOfGameDayTimestamp by viewModel.startOfCurrentGameDayTimestamp.collectAsState()
    val recentBaits by viewModel.recentBaits.collectAsState()
    val gameTime by viewModel.gameTime.collectAsState()

    // States pour gérer le dialog d'appât
    var showBaitDialog by remember { mutableStateOf(false) }
    var selectedFishForBait by remember { mutableStateOf<Fish?>(null) }

    // Calcul des statistiques de capture
    val fishCaptureStats = remember(fishingEntries, lake.id, position, startOfGameDayTimestamp) {
        fishingEntries
            .filter { it.lake.id == lake.id && it.position == position && it.timestamp >= startOfGameDayTimestamp }
            .groupBy { it.fish.name }  // ✅ GARDÉ tel quel pour la logique interne
            .mapValues { (_, entries) ->
                FishCaptureStats(totalCaught = entries.size)
            }
    }

    // Tri par nombre de captures PUIS par rareté
    val sortedFish = remember(lake.availableFish, fishCaptureStats) {
        lake.availableFish.sortedWith(
            compareByDescending<Fish> { fishCaptureStats[it.name]?.totalCaught ?: 0 }
                .thenBy { it.rarity.ordinal }
                .thenBy { it.name }  // ✅ GARDÉ tel quel pour le tri
        )
    }

    // Dialog pour sélection d'appât
    if (showBaitDialog && selectedFishForBait != null) {
        BaitSelectionDialog(
            fish = selectedFishForBait!!,
            recentBaits = recentBaits,
            onBaitSelected = { bait ->
                viewModel.catchFishWithBait(
                    selectedFishForBait!!,
                    lake,
                    position,
                    bait
                )
                showBaitDialog = false
                selectedFishForBait = null
            },
            onDismiss = {
                showBaitDialog = false
                selectedFishForBait = null
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // En-tête
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.fish_position_format, lake.name, position),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.fish_selection_subtitle),
                    fontSize = 14.sp,
                    color = Color(0xFFE2E8F0)
                )
            }
            IconButton(onClick = onViewJournal) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = stringResource(R.string.nav_journal),
                    tint = Color(0xFFFFB74D)
                )
            }
        }

        // Carte de statistiques
        FishingStatsCard(
            totalSpecies = lake.availableFish.size,
            caughtSpecies = fishCaptureStats.size,
            totalCaught = fishCaptureStats.values.sumOf { it.totalCaught },
            gameTime = gameTime
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Grille des poissons
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(sortedFish) { fish ->
                FishCard(
                    fish = fish,
                    captureCount = fishCaptureStats[fish.name]?.totalCaught ?: 0,
                    context = context,  // 🆕 AJOUTÉ
                    onClick = {
                        selectedFishForBait = fish
                        showBaitDialog = true
                    },
                    onLongClick = { onFishDetail(fish) }
                )
            }
        }
    }
}

@Composable
private fun FishingStatsCard(
    totalSpecies: Int,
    caughtSpecies: Int,
    totalCaught: Int,
    gameTime: LocalTime
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E3A5F)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.stats_species_caught, caughtSpecies, totalSpecies),
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = stringResource(R.string.stats_total_caught, totalCaught),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.game_time_label),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = gameTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fontSize = 14.sp,
                    color = Color(0xFFFFB74D),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StatItem(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 20.sp)
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFFB0BEC5)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FishCard(
    fish: Fish,
    captureCount: Int,
    context: android.content.Context,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .let { modifier ->
                if (captureCount > 0) {
                    modifier.border(
                        4.dp,
                        Color.Yellow, // Vert pour capturé
                        RoundedCornerShape(8.dp)
                    )
                } else {
                    modifier.border(
                        2.dp,
                        Color(fish.rarity.colorValue), // 🎨 COULEUR DE RARETÉ
                        RoundedCornerShape(8.dp)
                    )
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(fish.rarity.colorValue)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nom du poisson traduit
            Text(
                text = fish.getLocalizedName(context),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Rareté avec couleur
            Text(
                text = fish.rarity.displayName,
                fontSize = 12.sp,
                color = Color(fish.rarity.colorValue), // 🎨 COULEUR DE RARETÉ
                fontWeight = FontWeight.Medium
            )


            Spacer(modifier = Modifier.height(8.dp))

            // Statistiques
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {


                if (captureCount > 0) {
                    Text(
                        text = "✓ $captureCount",
                        fontSize = 28.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "NEW",
                        fontSize = 10.sp,
                        color = Color(0xFFFFB74D),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private data class FishCaptureStats(
    val totalCaught: Int
)