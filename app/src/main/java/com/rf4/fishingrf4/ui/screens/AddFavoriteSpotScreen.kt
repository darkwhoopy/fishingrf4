// ==========================================
// FICHIER: ui/screens/AddFavoriteSpotScreen.kt
// Écran d'ajout de zone favori - VALIDATION SIMPLIFIÉE
// ==========================================

package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.FishingData
import com.rf4.fishingrf4.data.models.Fish
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.data.models.FavoriteSpot
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.components.CoordinatePickerDialog
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel

@Composable
fun AddFavoriteSpotScreen(
    lake: Lake,
    viewModel: FishingViewModel,
    onBack: () -> Unit,
    onSaveSpot: (() -> Unit)? = null
) {
    // États pour la position (OBLIGATOIRE)
    var selectedPosition by remember { mutableStateOf("") }
    var showCoordinateDialog by remember { mutableStateOf(false) }

    // États pour les sélections multiples (OPTIONNELS)
    var selectedFish by remember { mutableStateOf<List<Fish>>(emptyList()) }
    var selectedBaits by remember { mutableStateOf<List<String>>(emptyList()) }
    var distance by remember { mutableStateOf("") }
    var spotName by remember { mutableStateOf("") }

    // États pour les dialogs
    var showFishSelector by remember { mutableStateOf(false) }
    var showBaitSelector by remember { mutableStateOf(false) }

    // Récupérer toutes les données depuis FishingData
    val allFish = remember {
        FishingData.getAllFish().sortedBy { it.name }
    }
    val allBaits = remember {
        listOf(
            "Ablette", "Asticot", "Blé", "Bouillettes", "Calamar", "Chironome",
            "Chrysalide", "Crabe", "Crevette", "Cuiller", "Devon", "Épinoche",
            "Fromage", "Gammare", "Graine de maïs", "Hareng", "Jig",
            "Leurre souple", "Lombric", "Maïs", "Maquereau", "Mouche sèche",
            "Nymphe", "Pain", "Pâte", "Pâte à l'ail", "Pellets", "Poisson vif",
            "Popper", "Porte-bois", "Sardine", "Spinnerbait", "Sprat",
            "Streamer", "Ver de terre", "Ver de vase", "Ver marin", "Ver rouge",
            "Viande", "Wobbler"
        ).sorted()
    }

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
                .verticalScroll(rememberScrollState())
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
                        text = "Nouveau spot favori",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = lake.name,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            // ==========================================
            // POSITION (OBLIGATOIRE)
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "🎯 Position",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Indicateur obligatoire
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEF4444)),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "OBLIGATOIRE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Button(
                        onClick = { showCoordinateDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedPosition.isEmpty())
                                Color(0xFFEF4444) else Color(0xFF10B981)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (selectedPosition.isEmpty())
                                "🚨 Choisir les coordonnées" else "📍 $selectedPosition",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // NOM DU SPOT (OPTIONNEL)
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "📍 Nom du spot",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Indicateur optionnel
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF6B7280)),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "OPTIONNEL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = spotName,
                        onValueChange = { spotName = it },
                        label = { Text("Nom du spot", color = Color.Gray) },
                        placeholder = { Text("Ex: Zone aux brèmes", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Gray
                        ),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // POISSONS CIBLES (OPTIONNEL)
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🐟 Poissons cibles",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF6B7280)),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "OPTIONNEL",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        IconButton(
                            onClick = { showFishSelector = true },
                            modifier = Modifier
                                .background(
                                    Color(0xFF10B981),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Ajouter un poisson",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    if (selectedFish.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Aucun poisson sélectionné (optionnel)",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(selectedFish) { fish ->
                                AssistChip(
                                    onClick = {
                                        selectedFish = selectedFish - fish
                                    },
                                    label = {
                                        Text(
                                            fish.name,
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    },
                                    trailingIcon = {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Retirer",
                                            modifier = Modifier.size(16.dp),
                                            tint = Color.White
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Color(fish.rarity.colorValue)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // APPÂTS RECOMMANDÉS (OPTIONNEL)
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🎣 Appâts recommandés",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF6B7280)),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "OPTIONNEL",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        IconButton(
                            onClick = { showBaitSelector = true },
                            modifier = Modifier
                                .background(
                                    Color(0xFF10B981),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Ajouter un appât",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    if (selectedBaits.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Aucun appât sélectionné (optionnel)",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(selectedBaits) { bait ->
                                AssistChip(
                                    onClick = {
                                        selectedBaits = selectedBaits - bait
                                    },
                                    label = {
                                        Text(
                                            text = bait,
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    },
                                    trailingIcon = {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Retirer",
                                            modifier = Modifier.size(16.dp),
                                            tint = Color.White
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Color(0xFF3B82F6)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // DISTANCE DE PÊCHE (OPTIONNEL)
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "📏 Distance de pêche",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF6B7280)),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "OPTIONNEL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = distance,
                        onValueChange = {
                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                distance = it
                            }
                        },
                        label = { Text("Distance en mètres", color = Color.Gray) },
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
                        singleLine = true,
                        suffix = { Text("m", color = Color.Gray) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // BOUTON DE SAUVEGARDE - VALIDATION SIMPLIFIÉE
            // ==========================================

            // ✅ NOUVELLE VALIDATION : Seules les coordonnées sont obligatoires
            val isValid = selectedPosition.isNotEmpty()

            Button(
                onClick = {
                    if (isValid) {
                        viewModel.addFavoriteSpot(
                            name = spotName.ifEmpty { "Spot $selectedPosition" }, // Nom par défaut si vide
                            position = selectedPosition,
                            lake = lake,
                            fishNames = selectedFish.map { it.name }, // Peut être vide
                            baits = selectedBaits, // Peut être vide
                            distance = distance.toIntOrNull() ?: 0 // 0 si vide
                        )
                        onSaveSpot?.invoke() ?: onBack()
                    }
                },
                enabled = isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isValid) Color(0xFF10B981) else Color.Gray,
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isValid) "✅ Sauvegarder le spot" else "🚨 Position requise",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Message d'aide
            if (!isValid) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0x33EF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Les coordonnées sont obligatoires pour sauvegarder",
                            color = Color(0xFFEF4444),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // ==========================================
    // DIALOGS (INCHANGÉS)
    // ==========================================

    // Dialog de sélection de coordonnées
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

    // Dialog de sélection de poissons (multi-sélection)
    if (showFishSelector) {
        Dialog(onDismissRequest = { showFishSelector = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2937)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Sélectionner les poissons",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(allFish) { fish ->
                            val isSelected = selectedFish.contains(fish)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedFish = if (isSelected) {
                                            selectedFish - fish
                                        } else {
                                            selectedFish + fish
                                        }
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected)
                                        Color(fish.rarity.colorValue) else Color(0xFF374151)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = fish.name,
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = fish.scientificName,
                                            color = Color.Gray,
                                            fontSize = 12.sp,
                                            fontStyle = FontStyle.Italic
                                        )
                                    }
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = { showFishSelector = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Annuler", color = Color.Gray)
                        }
                        Button(
                            onClick = { showFishSelector = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF10B981)
                            )
                        ) {
                            Text("Valider (${selectedFish.size})")
                        }
                    }
                }
            }
        }
    }

    // Dialog de sélection d'appâts (multi-sélection)
    if (showBaitSelector) {
        Dialog(onDismissRequest = { showBaitSelector = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2937)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Sélectionner les appâts",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(allBaits) { bait ->
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
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected)
                                        Color(0xFF3B82F6) else Color(0xFF374151)
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = { showBaitSelector = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Annuler", color = Color.Gray)
                        }
                        Button(
                            onClick = { showBaitSelector = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF3B82F6)
                            )
                        ) {
                            Text("Valider (${selectedBaits.size})")
                        }
                    }
                }
            }
        }
    }
}