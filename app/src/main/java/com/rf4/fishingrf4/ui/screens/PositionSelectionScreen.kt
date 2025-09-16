// ==========================================
// FICHIER: ui/screens/PositionSelectionScreen.kt
// Écran de sélection de position avec navigation vers AddFavoriteSpotScreen
// ==========================================

package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.data.models.UserSpot
import com.rf4.fishingrf4.data.repository.CommunityRepository
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.components.CoordinatePickerDialog
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import kotlinx.coroutines.launch

@Composable
fun PositionSelectionScreen(
    lake: Lake,
    viewModel: FishingViewModel,
    onPositionSelected: (String) -> Unit,
    onBack: () -> Unit,
    onAddFavoriteSpot: () -> Unit // ← Paramètre pour navigation vers AddFavoriteSpotScreen
) {
    // États pour le sélecteur manuel de coordonnées
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    var selectedNumber by remember { mutableStateOf<String?>(null) }
    var showShareDialog by remember { mutableStateOf(false) }
    var spotToShare by remember { mutableStateOf<UserSpot?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val communityRepo = remember { CommunityRepository() }
    var shareMessage by remember { mutableStateOf("") }
    var showShareMessage by remember { mutableStateOf(false) }


    // États pour le dialog de coordonnées RF4
    var showCoordinateDialog by remember { mutableStateOf(false) }
    var selectedPosition by remember { mutableStateOf("100:100") }

    // Récupération des spots utilisateur pour ce lac
    val userSpots by viewModel.userSpots.collectAsState()
    val userSpotsForThisLake = remember(userSpots, lake.id) {
        userSpots.filter { it.lakeId == lake.id }
    }

    val completePosition = if (selectedLetter != null && selectedNumber != null) {
        "${selectedLetter}${selectedNumber}"
    } else null

    // Top zones du jour (en ligne) pour ce lac
    var topZones by remember { mutableStateOf<List<Pair<String, Long>>>(emptyList()) }
    LaunchedEffect(lake.id) {
        viewModel.fetchTop5PositionsForLakeToday(lake.id) { zones ->
            topZones = zones
        }
    }

    // Calculer le max de coordonnées selon le lac
    val maxCoordinate = remember(lake.name) {
        when {
            lake.name.contains("Mosquito", ignoreCase = true) -> 150
            lake.name.contains("Bear", ignoreCase = true) -> 120
            lake.name.contains("Amber", ignoreCase = true) -> 180
            else -> 200
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // ==========================================
            // HEADER
            // ==========================================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                BackButton(onClick = onBack)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = lake.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "📍 Choisissez votre position",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ==========================================
                // SECTION 1 - COORDONNÉES MANUELLES (GRILLE)
                // ==========================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "🎯 Coordonnées manuelles",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // Lettres
                            Text("🔤 Sélectionnez une lettre", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val letters = ('A'..'J').map { it.toString() }
                                items(letters) { letter ->
                                    GridButton(
                                        text = letter,
                                        isSelected = selectedLetter == letter,
                                        onClick = {
                                            selectedLetter = if (selectedLetter == letter) null else letter
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Chiffres
                            Text("🔢 Sélectionnez un chiffre", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val numbers = (1..10).map { it.toString() }
                                items(numbers) { number ->
                                    GridButton(
                                        text = number,
                                        isSelected = selectedNumber == number,
                                        onClick = {
                                            selectedNumber = if (selectedNumber == number) null else number
                                        }
                                    )
                                }
                            }

                            // Position générée et bouton d'utilisation
                            if (completePosition != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "Position générée",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                            Text(
                                                text = completePosition,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF10B981)
                                            )
                                        }
                                        Button(
                                            onClick = { onPositionSelected(completePosition) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF3B82F6)
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Utiliser",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // SECTION 2 - COORDONNÉES RF4 (DIALOG)
                // ==========================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "🎯 Position par coordonnées RF4",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Text(
                                text = "Utilisez le format standard de la communauté RF4 (ex: 80:95)",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Button(
                                onClick = { showCoordinateDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF10B981)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.MyLocation,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Choisir les coordonnées",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Affichage de la position actuelle
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Position actuelle",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = selectedPosition,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF10B981)
                                        )
                                    }
                                    Button(
                                        onClick = { onPositionSelected(selectedPosition) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF3B82F6)
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "Utiliser",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // SECTION 3 - TOP ZONES DU JOUR
                // ==========================================
                if (topZones.isNotEmpty()) {
                    item {
                        Text(
                            "🔥 Top zones du jour (communauté)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    items(topZones) { (pos, count) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPositionSelected(pos) },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2455AF)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(pos, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                AssistChip(
                                    onClick = { onPositionSelected(pos) },
                                    label = { Text("$count captures", color = Color.White, fontSize = 12.sp) },
                                    colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFF3B82F6))
                                )
                            }
                        }
                    }
                }

                // ==========================================
                // SECTION 4 - SPOTS FAVORIS ET POSITIONS RECOMMANDÉES
                // ==========================================
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "⭐ Mes spots favoris",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        // BOUTON + POUR NAVIGUER VERS AddFavoriteSpotScreen
                        IconButton(
                            onClick = onAddFavoriteSpot, // ← Navigation vers l'écran complet
                            modifier = Modifier
                                .background(
                                    Color(0xFF10B981),
                                    RoundedCornerShape(8.dp)
                                )
                                .size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Ajouter un spot favori",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Spots "built-in" du lac (positions recommandées)
                items(lake.coordinates.toList()) { (position, description) ->
                    RecommendedPositionCard(
                        position = position,
                        description = description,
                        onClick = { onPositionSelected(position) }
                    )
                }

                // ==========================================
                // SPOTS PERSONNELS DE L'UTILISATEUR
                // ==========================================
                items(userSpotsForThisLake) { spot ->
                    UserPositionCard(
                        spot = spot,
                        onClick = { onPositionSelected(spot.position) },
                        onDelete = { viewModel.deleteUserSpot(spot.id) },
                        onShare = { userSpot ->
                            spotToShare = userSpot
                            showShareDialog = true
                        }
                    )
                }

                // ==========================================
                // MESSAGE SI AUCUN SPOT FAVORI
                // ==========================================
                if (userSpotsForThisLake.isEmpty() && lake.coordinates.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "📌",
                                        fontSize = 32.sp
                                    )
                                    Text(
                                        text = "Aucun spot favori",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Cliquez sur + pour en ajouter un",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Spacer final
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // ==========================================
    // DIALOG DE COORDONNÉES RF4
    // ==========================================
    if (showCoordinateDialog) {
        CoordinatePickerDialog(
            lakeName = lake.name,
            maxCoordinate = maxCoordinate,
            onCoordinateSelected = { x, y ->
                selectedPosition = "$x:$y"
            },
            onDismiss = { showCoordinateDialog = false }
        )
    }
    // À la fin de PositionSelectionScreen, avant la fermeture
    if (showShareDialog && spotToShare != null) {
        // États pour les détails supplémentaires
        var selectedFish by remember { mutableStateOf<List<String>>(emptyList()) }
        var selectedBaits by remember { mutableStateOf<List<String>>(emptyList()) }
        var distance by remember { mutableStateOf("") }
        var showFishSelector by remember { mutableStateOf(false) }
        var showBaitSelector by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = {
                showShareDialog = false
                spotToShare = null
            },
            title = {
                Text("Partager ce spot", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Informations du spot
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                "Position ${spotToShare!!.position}",
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                spotToShare!!.comment,
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Text(
                        "Ajoutez des détails pour enrichir votre partage :",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Poissons cibles
                    OutlinedTextField(
                        value = if (selectedFish.isEmpty()) "" else selectedFish.joinToString(", "),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Poissons cibles (optionnel)", color = Color.Gray) },
                        placeholder = { Text("Aucun poisson sélectionné", color = Color.Gray) },
                        trailingIcon = {
                            IconButton(onClick = { showFishSelector = true }) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Gray)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Gray
                        ),
                        maxLines = 2
                    )

                    // Appâts recommandés
                    OutlinedTextField(
                        value = if (selectedBaits.isEmpty()) "" else selectedBaits.joinToString(", "),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Appâts recommandés (optionnel)", color = Color.Gray) },
                        placeholder = { Text("Aucun appât sélectionné", color = Color.Gray) },
                        trailingIcon = {
                            IconButton(onClick = { showBaitSelector = true }) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Gray)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Gray
                        ),
                        maxLines = 2
                    )

                    // Distance de pêche
                    OutlinedTextField(
                        value = distance,
                        onValueChange = {
                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                distance = it
                            }
                        },
                        label = { Text("Distance de pêche en mètres (optionnel)", color = Color.Gray) },
                        placeholder = { Text("30", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Gray
                        ),
                        singleLine = true,
                        suffix = { Text("m", color = Color.Gray) }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                communityRepo.shareSpotToCommunity(
                                    userSpot = spotToShare!!,
                                    lakeName = lake.name,
                                    description = spotToShare!!.comment.ifEmpty { "Spot partagé depuis mes favoris" },
                                    fishNames = selectedFish, // ✅ Maintenant avec les vraies données
                                    baits = selectedBaits,    // ✅ Maintenant avec les vraies données
                                    distance = distance.toIntOrNull() ?: 0 // ✅ Maintenant avec la vraie distance
                                )
                                shareMessage = "Spot partagé avec succès !"
                                showShareMessage = true
                                showShareDialog = false
                                spotToShare = null
                            } catch (e: Exception) {
                                shareMessage = when {
                                    e.message?.contains("déjà partagé", ignoreCase = true) == true ->
                                        "Vous avez déjà partagé ce spot !"
                                    e.message?.contains("connecté", ignoreCase = true) == true ->
                                        "Vous devez être connecté pour partager"
                                    e.message?.contains("permission", ignoreCase = true) == true ->
                                        "Permissions insuffisantes"
                                    else -> "Erreur lors du partage : ${e.message}"
                                }
                                showShareMessage = true
                                showShareDialog = false
                                spotToShare = null
                                android.util.Log.e("ShareSpot", "Erreur partage: ${e.message}", e)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Partager")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showShareDialog = false
                    spotToShare = null
                }) {
                    Text("Annuler", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF1F2937)
        )

        // ==========================================
        // SÉLECTEURS DE POISSONS ET APPÂTS
        // ==========================================

        // Sélecteur de poissons
        if (showFishSelector) {
            AlertDialog(
                onDismissRequest = { showFishSelector = false },
                title = { Text("Sélectionner les poissons", color = Color.White) },
                text = {
                    LazyColumn(modifier = Modifier.height(300.dp)) {
                        items(lake.availableFish) { fish ->
                            val isSelected = selectedFish.contains(fish.name)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedFish = if (isSelected) {
                                            selectedFish - fish.name
                                        } else {
                                            selectedFish + fish.name
                                        }
                                    }
                                    .padding(vertical = 2.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFF10B981) else Color(0xFF374151)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = fish.name,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Sélectionné",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showFishSelector = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                    ) {
                        Text("Valider (${selectedFish.size})")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showFishSelector = false }) {
                        Text("Annuler", color = Color.Gray)
                    }
                },
                containerColor = Color(0xFF1F2937)
            )
        }

        // Sélecteur d'appâts
        if (showBaitSelector) {
            val commonBaits = listOf(
                "Ver de terre", "Ver de vase", "Asticot", "Maïs", "Pain",
                "Lombric", "Chrysalide", "Blé", "Orge perlé", "Pâte",
                "Bouillettes", "Pellets", "Fromage", "Viande", "Crevette"
            )

            AlertDialog(
                onDismissRequest = { showBaitSelector = false },
                title = { Text("Sélectionner les appâts", color = Color.White) },
                text = {
                    LazyColumn(modifier = Modifier.height(300.dp)) {
                        items(commonBaits) { bait ->
                            val isSelected = selectedBaits.contains(bait)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedBaits = if (isSelected) {
                                            selectedBaits - bait
                                        } else {
                                            selectedBaits + bait
                                        }
                                    }
                                    .padding(vertical = 2.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFF3B82F6) else Color(0xFF374151)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = bait,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Sélectionné",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showBaitSelector = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                    ) {
                        Text("Valider (${selectedBaits.size})")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showBaitSelector = false }) {
                        Text("Annuler", color = Color.Gray)
                    }
                },
                containerColor = Color(0xFF1F2937)
            )
        }
    }

// ==========================================
// ✅ NOUVEAU DIALOG pour afficher les messages
// ==========================================

    if (showShareMessage) {
        AlertDialog(
            onDismissRequest = { showShareMessage = false },
            title = {
                Text(
                    text = if (shareMessage.contains("succès")) "Succès" else "Information",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = shareMessage,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showShareMessage = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (shareMessage.contains("succès"))
                            Color(0xFF10B981) else Color(0xFF3B82F6)
                    )
                ) {
                    Text("OK")
                }
            },
            containerColor = Color(0xFF1F2937)
        )
    }
}

// ==========================================
// COMPOSANTS - BOUTON GRILLE
// ==========================================
@Composable
private fun GridButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF10B981) else Color(0xFF374151),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.size(48.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ==========================================
// COMPOSANTS - CARTE POSITION RECOMMANDÉE
// ==========================================
@Composable
private fun RecommendedPositionCard(
    position: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF10B981))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = position,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

// ==========================================
// COMPOSANTS - CARTE SPOT UTILISATEUR
// ==========================================
@Composable
private fun UserPositionCard(
    spot: UserSpot,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onShare: (UserSpot) -> Unit = {} // Nouveau paramètre pour partager
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF3B82F6))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = spot.position,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = spot.comment,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Boutons d'actions
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Bouton Partager
                IconButton(
                    onClick = { onShare(spot) },
                    modifier = Modifier
                        .background(
                            Color(0xFF10B981),
                            RoundedCornerShape(6.dp)
                        )
                        .size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Partager ce spot",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Bouton Supprimer
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .background(
                            Color(0xFFEF4444),
                            RoundedCornerShape(6.dp)
                        )
                        .size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Supprimer",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}