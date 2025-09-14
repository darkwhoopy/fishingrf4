package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.models.Fish
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import com.rf4.fishingrf4.utils.getLocalizedBaitName
import com.rf4.fishingrf4.utils.getLocalizedName // ✅ IMPORT pour la traduction des lacs

@Composable
fun FishInfoScreen(
    fish: Fish,
    viewModel: FishingViewModel,
    onBack: () -> Unit
) {
    var showAddBaitDialog by remember { mutableStateOf(false) }
    var showCommunityBaitDialog by remember { mutableStateOf(false) }
    var votingMessage by remember { mutableStateOf("") }
    var isVotingSuccess by remember { mutableStateOf(true) }
    val customAddedBaits by viewModel.getCustomBaitsForFish(fish.id).collectAsState()

    // Données calculées localement
    val allLakesForThisFish = remember(fish.id) { viewModel.getLakesForFish(fish) }
    val captureStats = remember(fish.id) { viewModel.getCaptureStatsForFishByLake(fish.id) }
    val topSpots = remember(fish.id) { viewModel.getTopSpotsForFish(fish.id) }
    val timeOfDayStats = remember(fish.id) { viewModel.getCaptureStatsByTimeOfDay(fish.id) }

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

        // Présence & Captures - ✅ TRADUIT
        item {
            FishCard(icon = "📍", title = stringResource(R.string.fish_info_presence_captures)) {
                Row(modifier = Modifier.padding(bottom = 12.dp)) {
                    RarityBadge(text = fish.rarity.displayName, color = Color(fish.rarity.colorValue))
                }
                if (allLakesForThisFish.isNotEmpty()) {
                    allLakesForThisFish.forEach { lake ->
                        LakeInfoRow(
                            lake = lake,
                            captureCount = captureStats[lake.id] ?: 0
                        )
                    }
                } else {
                    Text(stringResource(R.string.fish_info_no_lakes), color = Color.Gray)
                }
            }
        }

        // ✅ SECTION APPÂTS FAVORIS - TRADUITE
        item {
            FishCard(icon = "🎣", title = stringResource(R.string.fish_info_baits)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // ✅ Utilisation directe des propriétés du Fish
                    val baitsList = if (fish.preferredBaits.isNotEmpty()) {
                        fish.preferredBaits // Nouvelle structure
                    } else {
                        fish.preferredBait // Ancienne structure (fallback)
                    }

                    // Appâts favoris du poisson
                    if (baitsList.isNotEmpty()) {
                        Text(
                            stringResource(R.string.fish_info_favorite_baits),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        baitsList.forEach { bait ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(bait.getLocalizedBaitName(), color = Color.White)
                            }
                        }
                        if (customAddedBaits.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    } else {
                        Text(stringResource(R.string.fish_info_no_predefined_baits), color = Color.Gray)
                    }

                    // Appâts personnalisés ajoutés
                    if (customAddedBaits.isNotEmpty()) {
                        Text(
                            stringResource(R.string.fish_info_my_baits),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        customAddedBaits.forEach { bait ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color(0xFF3B82F6),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(bait.getLocalizedBaitName(), color = Color.White, modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = { viewModel.removeCustomBaitFromFish(fish.id, bait) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = stringResource(R.string.action_delete),
                                        tint = Color.Red,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Boutons d'ajout d'appâts
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showAddBaitDialog = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF3B82F6)
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.fish_info_add_personal_bait), fontSize = 12.sp)
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showCommunityBaitDialog = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFEF4444)
                            )
                        ) {
                            Icon(Icons.Default.ThumbUp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.fish_info_vote_community_bait), fontSize = 12.sp)
                        }
                    }

                    // Message de vote (si présent)
                    if (votingMessage.isNotEmpty()) {
                        Text(
                            text = votingMessage,
                            color = if (isVotingSuccess) Color.Green else Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // ✅ SECTION HEURES OPTIMALES - TRADUITE
        item {
            FishCard(icon = "🕐", title = stringResource(R.string.fish_info_optimal_hours)) {
                when {
                    // Nouvelle structure : preferredTime (List<String>)
                    fish.preferredTime.isNotEmpty() -> {
                        val timesText = fish.preferredTime.joinToString(", ")
                        Text(timesText, color = Color.White)
                    }
                    // Ancienne structure : bestHours (List<Int>)
                    fish.bestHours.isNotEmpty() -> {
                        val hoursText = fish.bestHours.sorted().joinToString("h, ") + "h"
                        Text(hoursText, color = Color.White)
                    }
                    // Aucune donnée
                    else -> {
                        Text(stringResource(R.string.fish_info_all_hours), color = Color.Gray)
                    }
                }
            }
        }

        // ✅ SECTION MÉTÉO FAVORABLE - TRADUITE
        item {
            FishCard(icon = "🌤️", title = stringResource(R.string.fish_info_favorable_weather)) {
                val weatherList = if (fish.preferredWeather.isNotEmpty()) {
                    fish.preferredWeather // Nouvelle structure
                } else {
                    fish.bestWeather // Ancienne structure (fallback)
                }

                if (weatherList.isNotEmpty()) {
                    val weatherText = weatherList.joinToString(", ") { it.displayName }
                    Text(weatherText, color = Color.White)
                } else {
                    Text(stringResource(R.string.fish_info_all_conditions), color = Color.Gray)
                }
            }
        }

        // Mes meilleurs spots - ✅ TRADUIT
        if (topSpots.isNotEmpty()) {
            item {
                FishCard(icon = "🏆", title = stringResource(R.string.fish_info_best_spots)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        topSpots.forEachIndexed { index, spot ->
                            TopSpotRow(rank = index + 1, spotName = spot.first, count = spot.second)
                        }
                    }
                }
            }
        }

        // Captures par période - ✅ TRADUIT
        if (timeOfDayStats.isNotEmpty()) {
            item {
                FishCard(icon = "📊", title = stringResource(R.string.fish_info_captures_by_period)) {
                    val periods = listOf(
                        stringResource(R.string.time_morning),
                        stringResource(R.string.time_day),
                        stringResource(R.string.time_evening),
                        stringResource(R.string.time_night)
                    )
                    val periodKeys = listOf("Matinée", "Journée", "Soirée", "Nuit")

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        periods.forEachIndexed { index, translatedPeriod ->
                            val periodKey = periodKeys[index]
                            val count = timeOfDayStats[periodKey] ?: 0
                            if (count > 0) {
                                TimeOfDayStatsRow(period = translatedPeriod, count = count, periodKey = periodKey)
                            }
                        }
                    }
                }
            }
        }
    }

    // ✅ Dialog d'ajout d'appât personnel - TRADUIT
    if (showAddBaitDialog) {
        SimpleBaitDialog(
            allGameBaits = viewModel.getAllGameBaits(),
            currentBaits = (if (fish.preferredBaits.isNotEmpty()) fish.preferredBaits else fish.preferredBait) + customAddedBaits,
            onBaitSelected = { selectedBait ->
                viewModel.addCustomBaitToFish(fish.id, selectedBait)
                showAddBaitDialog = false
            },
            onDismiss = { showAddBaitDialog = false }
        )
    }

    // ✅ Dialog pour voter pour un appât communautaire - TRADUIT
    if (showCommunityBaitDialog) {
        CommunityBaitVotingDialog(
            fish = fish,
            viewModel = viewModel,
            onVoteSuccess = { baitName ->
                votingMessage = "✅ Vote enregistré pour '$baitName' !"
                isVotingSuccess = true
                showCommunityBaitDialog = false
            },
            onDismiss = { showCommunityBaitDialog = false }
        )
    }
}

// ✅ COMPOSABLE FISHCARD - Pour structurer les sections
@Composable
fun FishCard(
    icon: String,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = icon,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            content()
        }
    }
}

// ✅ COMPOSABLE POUR LES BADGES DE RARETÉ
@Composable
fun RarityBadge(text: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// ✅ COMPOSABLE POUR AFFICHER LES INFORMATIONS DE LAC - TRADUIT
@Composable
fun LakeInfoRow(lake: Lake, captureCount: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(
            Icons.Default.Place,
            contentDescription = null,
            tint = Color(0xFF3B82F6),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = lake.getLocalizedName(), // ✅ NOM TRADUIT
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        if (captureCount > 0) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF059669)),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "$captureCount",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}

// ✅ COMPOSABLE POUR AFFICHER LES TOPS SPOTS - TRADUIT
@Composable
fun TopSpotRow(rank: Int, spotName: String, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Médaille/rang
        val medalColor = when (rank) {
            1 -> Color(0xFFFFD700) // Or
            2 -> Color(0xFFC0C0C0) // Argent
            3 -> Color(0xFFCD7F32) // Bronze
            else -> Color.Gray
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = medalColor),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "$rank",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = spotName,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = stringResource(R.string.fish_info_captures_count, count),
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

// ✅ COMPOSABLE POUR LES STATISTIQUES PAR PÉRIODE - TRADUIT
@Composable
fun TimeOfDayStatsRow(period: String, count: Int, periodKey: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        val periodIcon = when (periodKey) {
            "Matinée" -> "🌅"
            "Journée" -> "☀️"
            "Soirée" -> "🌇"
            "Nuit" -> "🌙"
            else -> "⏰"
        }

        Text(text = periodIcon, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = period,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF059669)),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = "$count",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}

// ✅ DIALOG SIMPLE POUR AJOUTER UN APPÂT PERSONNEL - TRADUIT
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleBaitDialog(
    allGameBaits: List<String>,
    currentBaits: List<String>,
    onBaitSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    val availableBaits = allGameBaits.filter { bait ->
        bait !in currentBaits && bait.contains(searchText, ignoreCase = true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E3A5F),
        title = {
            Text(stringResource(R.string.fish_info_add_bait_dialog_title), color = Color.White)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text(stringResource(R.string.fish_info_search_bait)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color(0xFF3B82F6),
                        unfocusedLabelColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.height(200.dp)
                ) {
                    items(availableBaits.size) { index ->
                        val bait = availableBaits[index]
                        OutlinedButton(
                            onClick = { onBaitSelected(bait) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text(bait.getLocalizedBaitName())
                        }
                        if (index < availableBaits.size - 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel), color = Color.White)
            }
        }
    )
}

// ✅ Dialog pour voter pour un appât communautaire - TRADUIT
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityBaitVotingDialog(
    fish: Fish,
    viewModel: FishingViewModel,
    onVoteSuccess: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedBait by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Récupérer tous les appâts disponibles SAUF ceux prédéfinis
    val allGameBaits = viewModel.getAllGameBaits()
    val fishBaits = if (fish.preferredBaits.isNotEmpty()) fish.preferredBaits else fish.preferredBait
    val availableBaitsForVoting = allGameBaits.filter { bait ->
        bait !in fishBaits // On exclut les appâts prédéfinis
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E3A5F),
        title = {
            Text(stringResource(R.string.fish_info_community_vote_title), color = Color.White)
        },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.fish_info_community_vote_question, fish.name),
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.height(200.dp)
                ) {
                    items(availableBaitsForVoting.size) { index ->
                        val bait = availableBaitsForVoting[index]
                        OutlinedButton(
                            onClick = { selectedBait = bait },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (selectedBait == bait) Color(0xFF10B981) else Color.White,
                                containerColor = if (selectedBait == bait) Color(0xFF10B981).copy(alpha = 0.1f) else Color.Transparent
                            )
                        ) {
                            Text(bait.getLocalizedBaitName())
                        }
                        if (index < availableBaitsForVoting.size - 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedBait.isNotEmpty()) {
                        isLoading = true
                        // Simuler un vote (à remplacer par l'appel API réel)
                        onVoteSuccess(selectedBait)
                        isLoading = false
                    }
                },
                enabled = selectedBait.isNotEmpty() && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF059669)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White
                    )
                } else {
                    Text(stringResource(R.string.fish_info_vote_button))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel), color = Color.White)
            }
        }
    )
}