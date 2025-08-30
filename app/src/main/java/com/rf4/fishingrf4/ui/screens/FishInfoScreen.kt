package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.models.Fish
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel

@Composable
fun FishInfoScreen(
    fish: Fish,
    viewModel: FishingViewModel,
    onBack: () -> Unit
) {
    var showAddBaitDialog by remember { mutableStateOf(false) }
    var showCommunityBaitDialog by remember { mutableStateOf(false) }

    val customAddedBaits by viewModel.getCustomBaitsForFish(fish.id).collectAsState()

    // Données calculées localement
    val allLakesForThisFish = remember(fish.id) { viewModel.getLakesForFish(fish) }
    val captureStats = remember(fish.id) { viewModel.getCaptureStatsForFishByLake(fish.id) }
    val topSpots = remember(fish.id) { viewModel.getTopSpotsForFish(fish.id) }
    val timeOfDayStats = remember(fish.id) { viewModel.getCaptureStatsByTimeOfDay(fish.id) }

    // Communauté
    var communityTop by remember { mutableStateOf<List<Pair<String, Long>>>(emptyList()) }
    LaunchedEffect(fish.id) {
        viewModel.fetchTopCommunityBaits(fish.id) { communityTop = it }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                BackButton(onClick = onBack)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = fish.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = fish.species,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }

        // Présence & captures
        item {
            FishCard(icon = "📍", title = "Présence & Captures") {
                Row(modifier = Modifier.padding(bottom = 12.dp)) {
                    RarityBadge(
                        text = fish.rarity.displayName,
                        color = Color(fish.rarity.colorValue)
                    )
                }
                if (allLakesForThisFish.isNotEmpty()) {
                    allLakesForThisFish.forEach { lake ->
                        LakeInfoRow(
                            lake = lake,
                            captureCount = captureStats[lake.id] ?: 0
                        )
                    }
                } else {
                    Text(
                        "Ce poisson n'est actuellement disponible dans aucun lac connu.",
                        color = Color.Gray
                    )
                }
            }
        }

        // Poids
        item {
            FishCard(icon = "⚖️", title = "Poids") {
                val weightText = fish.weight?.let { "${it.start} - ${it.endInclusive} kg" } ?: "Non spécifié"
                Text(
                    text = weightText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // 🎣 Appâts (jeu + perso + communauté)
        item {
            FishCard(icon = "🎣", title = "Appâts Recommandés + Communauté") {
                // --- Tes appâts persos / du jeu ---
                Button(
                    onClick = { showAddBaitDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ajouter un de vos appâts")
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (fish.preferredBait.isNotEmpty()) {
                    fish.preferredBait.forEach { bait ->
                        SimpleBaitCard(bait = bait, isOriginal = true)
                    }
                }
                if (customAddedBaits.isNotEmpty()) {
                    Text(
                        "Vos ajouts :",
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    customAddedBaits.forEach { bait ->
                        SimpleBaitCard(
                            bait = bait,
                            isOriginal = false,
                            onRemove = { viewModel.removeCustomBaitFromFish(fish.id, bait) }
                        )
                    }
                }
                if (fish.preferredBait.isEmpty() && customAddedBaits.isEmpty()) {
                    Text("Aucun appât spécifique", color = Color.Gray)
                }

                // --- Communauté ---
                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Color(0x334B5563))
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "🌍 Propositions des joueurs",
                    color = Color(0xFF60A5FA),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (communityTop.isEmpty()) {
                    Text("Aucun vote pour l’instant.", color = Color(0xFFE5E7EB))
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        communityTop.forEachIndexed { index, (bait, count) ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF2455AF)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${index + 1}. $bait", color = Color.White)
                                    AssistChip(
                                        onClick = {
                                            // voter pour ce bait
                                            viewModel.addCommunityBaitForFish(fish.id, bait) {
                                                viewModel.fetchTopCommunityBaits(fish.id) { list ->
                                                    communityTop = list
                                                }
                                            }
                                        },
                                        label = { Text("$count", color = Color.White) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = Color(0xFF3B82F6)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { showCommunityBaitDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Proposer / voter un appât (liste déroulante)")
                }
            }
        }

        // Heures optimales
        item {
            FishCard(icon = "🕐", title = "Heures Optimales") {
                if (fish.bestHours.isNotEmpty()) {
                    Text(fish.bestHours.joinToString("h, ") + "h", color = Color.White)
                } else {
                    Text("Toutes les heures", color = Color.Gray)
                }
            }
        }

        // Météo
        item {
            FishCard(icon = "🌤️", title = "Météo Favorable") {
                if (fish.bestWeather.isNotEmpty()) {
                    Text(fish.bestWeather.joinToString { it.displayName }, color = Color.White)
                } else {
                    Text("Toutes les conditions", color = Color.Gray)
                }
            }
        }

        // Mes meilleurs spots
        if (topSpots.isNotEmpty()) {
            item {
                FishCard(icon = "🏆", title = "Mes Meilleurs Spots") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        topSpots.forEachIndexed { index, spot ->
                            TopSpotRow(rank = index + 1, spotName = spot.first, count = spot.second)
                        }
                    }
                }
            }
        }

        // Captures par période
        if (timeOfDayStats.isNotEmpty()) {
            item {
                FishCard(icon = "📊", title = "Captures par Période") {
                    val periods = listOf("Matinée", "Journée", "Soirée", "Nuit")
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        periods.forEach { p ->
                            val c = timeOfDayStats[p] ?: 0
                            if (c > 0) TimeOfDayStatsRow(period = p, count = c)
                        }
                    }
                }
            }
        }
    }

    // Dialog — appât perso
    if (showAddBaitDialog) {
        SimpleBaitDialog(
            allGameBaits = viewModel.getAllGameBaits(),
            currentBaits = fish.preferredBait + customAddedBaits,
            onBaitSelected = { selected ->
                viewModel.addCustomBaitToFish(fish.id, selected)
                showAddBaitDialog = false
            },
            onDismiss = { showAddBaitDialog = false }
        )
    }

    // Dialog — appât communauté (même UI que perso)
    if (showCommunityBaitDialog) {
        val alreadyInCommunity = communityTop.map { it.first }
        SimpleBaitDialog(
            allGameBaits = viewModel.getAllGameBaits(),
            currentBaits = alreadyInCommunity,     // on masque ceux déjà votés (optionnel)
            onBaitSelected = { selected ->
                viewModel.addCommunityBaitForFish(fish.id, selected) {
                    viewModel.fetchTopCommunityBaits(fish.id) { list -> communityTop = list }
                }
                showCommunityBaitDialog = false
            },
            onDismiss = { showCommunityBaitDialog = false }
        )
    }
}

/* ---------- Composants réutilisés ---------- */

@Composable
fun LakeInfoRow(lake: Lake, captureCount: Int) {
    val hasCaught = captureCount > 0
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (hasCaught) Icons.Default.CheckCircle else Icons.Default.HighlightOff,
            contentDescription = null,
            tint = if (hasCaught) Color(0xFF4CAF50) else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "${lake.type.emoji} ${lake.name}", color = Color.White, fontSize = 16.sp)
            if (hasCaught) {
                Text(
                    text = " x$captureCount",
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        RarityBadge(text = "Niv. ${lake.unlockLevel}", color = Color(lake.difficulty.colorValue))
    }
}

@Composable
fun TopSpotRow(rank: Int, spotName: String, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(text = "$rank.", fontWeight = FontWeight.Bold, color = Color(0xFFFFD700), fontSize = 16.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = spotName, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(text = "x$count", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun TimeOfDayStatsRow(period: String, count: Int) {
    val (emoji, color) = when (period) {
        "Matinée" -> "☀️" to Color(0xFFFFC107)
        "Journée" -> "☀️" to Color(0xFF4CAF50)
        "Soirée" -> "🌙" to Color(0xFF9C27B0)
        else -> "🌃" to Color(0xFF3F51B5)
    }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(text = emoji, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = period, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(text = "x$count", color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FishCard(icon: String, title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                Text(text = icon, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            content()
        }
    }
}

@Composable
fun RarityBadge(text: String, color: Color) {
    Card(colors = CardDefaults.cardColors(containerColor = color), shape = RoundedCornerShape(6.dp)) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun SimpleBaitCard(bait: String, isOriginal: Boolean, onRemove: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(containerColor = if (isOriginal) Color(0xFF455A64) else Color(0xFF2E7D32))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = bait, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
            if (onRemove != null) {
                IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color(0xFFE57373), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleBaitDialog(
    allGameBaits: List<String>,
    currentBaits: List<String>,
    onBaitSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val availableBaits = remember(allGameBaits, currentBaits, searchQuery) {
        allGameBaits
            .filter { !currentBaits.contains(it) }
            .filter { it.contains(searchQuery, ignoreCase = true) }
            .sorted()
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choisir un appât", color = Color.White) },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Rechercher") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.heightIn(max = 260.dp)) {
                    items(availableBaits) { bait ->
                        Text(
                            text = bait,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onBaitSelected(bait) }
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Fermer") } },
        containerColor = Color(0xFF1E3A5F)
    )
}
