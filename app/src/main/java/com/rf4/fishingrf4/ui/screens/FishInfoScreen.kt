package com.rf4.fishingrf4.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.FishingData
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

        // Présence & Captures
        item {
            FishCard(icon = "📍", title = "Présence & Captures") {
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
                    Text("Aucun lac disponible", color = Color.Gray)
                }
            }
        }

        // Appâts
        item {
            FishCard(icon = "🎣", title = "Appâts") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Appâts prédéfinis (non modifiables)
                    if (fish.preferredBait.isNotEmpty()) {
                        Text("Appâts prédéfinis :", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        fish.preferredBait.forEach { bait ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(bait, color = Color.White)
                            }
                        }
                        if (customAddedBaits.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // Appâts personnalisés ajoutés
                    if (customAddedBaits.isNotEmpty()) {
                        Text("Mes appâts :", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        customAddedBaits.forEach { bait ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF3B82F6), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(bait, color = Color.White, modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = { viewModel.removeCustomBaitFromFish(fish.id, bait) },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color.Red, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Boutons d'action
                    OutlinedButton(
                        onClick = { showAddBaitDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ajouter un appât personnel")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ✅ NOUVEAU : Bouton pour voter pour un appât communautaire
                    Button(
                        onClick = { showCommunityBaitDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48))
                    ) {
                        Icon(Icons.Default.Group, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("👥 Voter pour un appât (Communauté)")
                    }

                    // Message de feedback
                    if (votingMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = votingMessage,
                            color = if (isVotingSuccess) Color.Green else Color.Red,
                            fontSize = 12.sp
                        )
                    }
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
                        periods.forEach { period ->
                            val count = timeOfDayStats[period] ?: 0
                            if (count > 0) {
                                TimeOfDayStatsRow(period = period, count = count)
                            }
                        }
                    }
                }
            }
        }
    }

// ✅ Dialog d'ajout d'appât personnel (inchangé)
    if (showAddBaitDialog) {
        SimpleBaitDialog(
            allGameBaits = viewModel.getAllGameBaits(),
            currentBaits = fish.preferredBait + customAddedBaits,
            onBaitSelected = { selectedBait ->
                viewModel.addCustomBaitToFish(fish.id, selectedBait)
                showAddBaitDialog = false
            },
            onDismiss = { showAddBaitDialog = false }
        )
    }

// ✅ NOUVEAU : Dialog pour voter pour un appât communautaire
    if (showCommunityBaitDialog) {
        CommunityBaitVotingDialog(
            fish = fish,
            viewModel = viewModel,
            onVoteSuccess = { baitName ->
                votingMessage = "✅ Vote enregistré pour '$baitName' !"
                isVotingSuccess = true
                showCommunityBaitDialog = false
                // Effacer le message après 3 secondes

            },
            onVoteError = { error ->
                votingMessage = "❌ $error"
                isVotingSuccess = false
                showCommunityBaitDialog = false
                // Effacer le message après 5 secondes

            },
            onDismiss = { showCommunityBaitDialog = false }
        )
    }
}
// ✅ NOUVEAU : Dialog pour voter pour un appât communautaire
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityBaitVotingDialog(
    fish: Fish,
    viewModel: FishingViewModel,
    onVoteSuccess: (String) -> Unit,
    onVoteError: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedBait by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
// Récupérer tous les appâts disponibles SAUF ceux prédéfinis
    val allGameBaits = viewModel.getAllGameBaits()
    val availableBaitsForVoting = allGameBaits.filter { bait ->
        bait !in fish.preferredBait // On exclut les appâts prédéfinis
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("👥 Vote communautaire", color = Color.White)
        },
        text = {
            Column {
                Text(
                    text = "Quel appât recommandez-vous pour ${fish.name} ?",
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Liste déroulante des appâts
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedBait,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Choisir un appât") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color(0xFF1E3A5F))
                    ) {
                        availableBaitsForVoting.forEach { bait ->
                            DropdownMenuItem(
                                text = { Text(bait, color = Color.White) },
                                onClick = {
                                    selectedBait = bait
                                    expanded = false
                                },
                                modifier = Modifier.background(Color(0xFF1E3A5F))
                            )
                        }
                    }
                }

                if (selectedBait.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "⚠️ Note : Vous ne pouvez voter qu'une seule fois pour cet appât sur ce poisson.",
                        color = Color.Yellow,
                        fontSize = 11.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedBait.isNotEmpty()) {
                        isLoading = true

                        // Appel au ViewModel pour voter
                        viewModel.addCommunityBaitForFish(
                            fishId = fish.id,
                            baitName = selectedBait,
                            onSuccess = {
                                isLoading = false
                                onVoteSuccess(selectedBait)
                            },
                            onError = { error ->
                                isLoading = false
                                onVoteError(error)
                            }
                        )
                    }
                },
                enabled = selectedBait.isNotEmpty() && !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Vote en cours...")
                } else {
                    Text("👍 Voter")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler", color = Color.White)
            }
        },
        containerColor = Color(0xFF1E3A5F)
    )
}
// Reste du code inchangé (toutes vos fonctions existantes)
@Composable
fun FishCard(icon: String, title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "$icon $title",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}
@Composable
fun LakeInfoRow(lake: Lake, captureCount: Int) {
    val hasCaught = captureCount > 0
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (hasCaught) Icons.Default.CheckCircle else Icons.Default.HighlightOff,
            contentDescription = if (hasCaught) "Capturé" else "Non capturé",
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
fun RarityBadge(text: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}
@Composable
fun TopSpotRow(rank: Int, spotName: String, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "$rank.", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = spotName, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(text = "x$count", color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
@Composable
fun TimeOfDayStatsRow(period: String, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = period, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(text = "x$count", color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
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

    // ✅ CHANGEMENT : Utiliser la liste de FishingData au lieu du paramètre
    val availableBaits = remember(currentBaits, searchQuery) {
        FishingData.getAllBaitNames()
            .filter { !currentBaits.contains(it) }
            .filter { it.contains(searchQuery, ignoreCase = true) }
            .sorted()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter un appât", color = Color.White) },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Rechercher") },
                    placeholder = { Text("Tapez 'graine' pour trouver facilement") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (availableBaits.isEmpty()) {
                    Text(
                        "Aucun appât trouvé",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(modifier = Modifier.heightIn(max = 250.dp)) {
                        items(availableBaits) { bait ->
                            Text(
                                text = bait,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onBaitSelected(bait) }
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Fermer") } },
        containerColor = Color(0xFF1E3A5F)
    )
}