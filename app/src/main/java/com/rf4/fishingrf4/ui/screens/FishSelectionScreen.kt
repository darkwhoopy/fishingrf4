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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.models.Fish
import com.rf4.fishingrf4.data.models.FishingEntry
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.data.models.FishRarity
import com.rf4.fishingrf4.data.models.getLocalizedName
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.components.BaitSelectionDialog
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import com.rf4.fishingrf4.utils.getLocalizedName
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// Data class pour les statistiques de capture
data class FishCaptureStats(
    val totalCaught: Int
)

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
    val context = LocalContext.current
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
            .groupBy { it.fish.name }
            .mapValues { (_, entries) ->
                FishCaptureStats(totalCaught = entries.size)
            }
    }

    // Tri par nombre de captures PUIS par rareté
    val sortedFish = remember(lake.availableFish, fishCaptureStats) {
        lake.availableFish.sortedWith(
            compareByDescending<Fish> { fishCaptureStats[it.name]?.totalCaught ?: 0 }
                .thenBy { it.rarity.ordinal }
                .thenBy { it.name }
        )
    }

    // Dialog pour sélection d'appât
    if (showBaitDialog && selectedFishForBait != null) {
        BaitSelectionDialog(
            fish = selectedFishForBait!!,
            recentBaits = recentBaits,
            fishingEntries = fishingEntries,
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
        // En-tête avec bouton retour, titre et bouton journal
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.fish_position_format, lake.getLocalizedName(), position),
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

        // Grille des poissons (2 colonnes)
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
                    context = context,
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

/**
 * Carte d'affichage des statistiques de pêche
 */
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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Statistique des espèces
            StatItem(
                icon = "🐟",
                value = "$caughtSpecies/$totalSpecies",
                label = stringResource(R.string.stat_species)
            )

            // Statistique des captures totales
            StatItem(
                icon = "🎯",
                value = "$totalCaught",
                label = stringResource(R.string.stat_captures)
            )

            // Temps de jeu
            StatItem(
                icon = "🕒",
                value = gameTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                label = stringResource(R.string.game_time_label)
            )
        }
    }
}

/**
 * Composant pour afficher une statistique individuelle
 */
@Composable
private fun StatItem(
    icon: String,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally  // ✅ CORRIGÉ: CenterHorizontally au lieu de CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 20.sp
        )
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

/**
 * Carte d'affichage d'un poisson avec traductions
 */
/**
 * Carte d'affichage d'un poisson avec traductions
 */
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
            .height(140.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .border(
                width = 4.dp,
                color = Color(fish.rarity.colorValue),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(fish.rarity.colorValue).copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            // Contenu principal centré
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Nom du poisson traduit
                Text(
                    text = fish.getLocalizedName(context),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // Multiplicateur de captures si > 1
                if (captureCount > 1) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "x$captureCount",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }

                // Affichage des étoiles pour les poissons capturés (centré)
                if (captureCount > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(minOf(captureCount, 3)) {
                            Text(
                                text = "⭐",
                                fontSize = 12.sp
                            )
                        }
                        if (captureCount > 3) {
                            Text(
                                text = "+",
                                fontSize = 14.sp,
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Badge NEW en bas à gauche
            if (captureCount == 0) {
                Surface(
                    color = Color(0xFFFFB74D),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Text(
                        text = stringResource(R.string.status_new),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // ✅ NOUVEAU: Symbole poisson en haut à droite
            Text(
                text = if (captureCount > 0) "🎣" else "🐟",  // 🎣 si pêché, 🐟 si pas pêché
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}