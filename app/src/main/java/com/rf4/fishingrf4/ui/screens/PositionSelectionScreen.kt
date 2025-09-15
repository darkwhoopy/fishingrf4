// ==========================================
// FICHIER: ui/screens/PositionSelectionScreen.kt
// Écran de sélection de position avec coordonnées
// ==========================================

package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.components.CoordinatePickerDialog
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel

@Composable
fun PositionSelectionScreen(
    lake: Lake,
    viewModel: FishingViewModel,
    onPositionSelected: (String) -> Unit,
    onBack: () -> Unit,
    onAddFavoriteSpot: () -> Unit = {} // Nouveau paramètre pour la navigation
) {
    var showCoordinateDialog by remember { mutableStateOf(false) }
    var showAddFavoriteDialog by remember { mutableStateOf(false) }
    var selectedPosition by remember { mutableStateOf("100:100") }

    // États pour le dialog d'ajout de spot favori
    var selectedFish by remember { mutableStateOf("") }
    var selectedBait by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var showFishSelector by remember { mutableStateOf(false) }
    var showBaitSelector by remember { mutableStateOf(false) }

    // Calculer le max de coordonnées selon le lac (exemple de logique)
    val maxCoordinate = remember(lake.name) {
        when {
            lake.name.contains("Mosquito", ignoreCase = true) -> 150
            lake.name.contains("Bear", ignoreCase = true) -> 120
            lake.name.contains("Amber", ignoreCase = true) -> 180
            else -> 200 // Valeur par défaut pour les grands lacs
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
            // Header
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
                // Section principale - Coordonnées communautaires
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
                                    text = "🎯 Position par coordonnées",
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

                            // Bouton principal pour ouvrir le sélecteur
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

                // Section - Spots favoris (placeholder pour future implémentation)
                // Section - Spots favoris avec bouton ajouter
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
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFBBF24),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "⭐ Spots favoris",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                // BOUTON AJOUTER - C'EST ÇA QUI MANQUE
                                IconButton(
                                    onClick = { onAddFavoriteSpot() },
                                    modifier = Modifier
                                        .background(
                                            Color(0xFF10B981),
                                            RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Ajouter un spot favori",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Text(
                                text = "Sauvegardez vos meilleures positions avec les détails de pêche",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            // Liste des spots favoris (placeholder)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                                shape = RoundedCornerShape(8.dp)
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
                }


                // Spacer pour éviter que le contenu touche le bas
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // ==========================================
    // DIALOGS
    // ==========================================

    // Dialog de sélection de coordonnées RF4
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

    // Dialog d'ajout de spot favori
    if (showAddFavoriteDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddFavoriteDialog = false
                selectedFish = ""
                selectedBait = ""
                distance = ""
            },
            title = {
                Text(
                    text = "Ajouter un spot favori",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Position actuelle
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Position",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = selectedPosition,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981)
                                )
                            }
                        }
                    }

                    // Sélection du poisson
                    OutlinedTextField(
                        value = selectedFish,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Poisson cible", color = Color.Gray) },
                        placeholder = { Text("Choisir un poisson", color = Color.Gray) },
                        trailingIcon = {
                            IconButton(onClick = { showFishSelector = true }) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    // Sélection de l'appât
                    OutlinedTextField(
                        value = selectedBait,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Appât recommandé", color = Color.Gray) },
                        placeholder = { Text("Choisir un appât", color = Color.Gray) },
                        trailingIcon = {
                            IconButton(onClick = { showBaitSelector = true }) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    // Distance en mètres
                    OutlinedTextField(
                        value = distance,
                        onValueChange = {
                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                distance = it
                            }
                        },
                        label = { Text("Distance (mètres)", color = Color.Gray) },
                        placeholder = { Text("30", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Gray
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                val isValid = selectedFish.isNotEmpty() &&
                        selectedBait.isNotEmpty() &&
                        distance.isNotEmpty()

                Button(
                    onClick = {
                        if (isValid) {
                            // TODO: Sauvegarder le spot favori
                            showAddFavoriteDialog = false
                            selectedFish = ""
                            selectedBait = ""
                            distance = ""
                        }
                    },
                    enabled = isValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF10B981),
                        disabledContainerColor = Color.Gray
                    )
                ) {
                    Text(
                        text = "Ajouter",
                        color = if (isValid) Color.White else Color.Gray
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddFavoriteDialog = false
                        selectedFish = ""
                        selectedBait = ""
                        distance = ""
                    }
                ) {
                    Text(
                        text = "Annuler",
                        color = Color.Gray
                    )
                }
            },
            containerColor = Color(0xFF1F2937)
        )
    }

    // Sélecteur de poissons
    if (showFishSelector) {
        AlertDialog(
            onDismissRequest = { showFishSelector = false },
            title = { Text("Choisir un poisson", color = Color.White) },
            text = {
                LazyColumn(
                    modifier = Modifier.height(300.dp)
                ) {
                    items(lake.availableFish) { fish ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedFish = fish.name
                                    showFishSelector = false
                                }
                                .padding(vertical = 2.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedFish == fish.name)
                                    Color(0xFF10B981) else Color(0xFF374151)
                            )
                        ) {
                            Text(
                                text = fish.name,
                                modifier = Modifier.padding(12.dp),
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFishSelector = false }) {
                    Text("Fermer", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF1F2937)
        )
    }

    // Sélecteur d'appâts
    if (showBaitSelector) {
        val commonBaits = listOf(
            "Ver de terre", "Ver de vase", "Asticot", "Maïs", "Pain",
            "Lombric", "Chrysalide", "Blé", "Orge perlé", "Pâte"
        )

        AlertDialog(
            onDismissRequest = { showBaitSelector = false },
            title = { Text("Choisir un appât", color = Color.White) },
            text = {
                LazyColumn(
                    modifier = Modifier.height(300.dp)
                ) {
                    items(commonBaits) { bait ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedBait = bait
                                    showBaitSelector = false
                                }
                                .padding(vertical = 2.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedBait == bait)
                                    Color(0xFF10B981) else Color(0xFF374151)
                            )
                        ) {
                            Text(
                                text = bait,
                                modifier = Modifier.padding(12.dp),
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showBaitSelector = false }) {
                    Text("Fermer", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF1F2937)
        )
    }
}