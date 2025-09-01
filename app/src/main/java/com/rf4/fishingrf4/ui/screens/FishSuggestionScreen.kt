// ==========================================
// FICHIER: ui/screens/FishSuggestionScreen.kt - VERSION CORRIGÉE
// ==========================================

package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.rf4.fishingrf4.data.models.*
import com.rf4.fishingrf4.data.repository.CommunityRepository
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import kotlinx.coroutines.launch

/**
 * Écran de suggestion de nouveaux poissons - VERSION CORRIGÉE
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishSuggestionScreen(
    viewModel: FishingViewModel,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val communityRepo = remember { CommunityRepository() }

    // États du formulaire - Informations de base
    var fishName by remember { mutableStateOf("") }
    var scientificName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var justification by remember { mutableStateOf("") }
    var sourceUrl by remember { mutableStateOf("") }

    // États du formulaire - Caractéristiques
    var selectedRarity by remember { mutableStateOf(FishRarity.COMMON) }
    var minWeight by remember { mutableStateOf("") }
    var maxWeight by remember { mutableStateOf("") }

    // ✅ CORRECTION : États des listes multiples avec types explicites
    var selectedLakes by remember { mutableStateOf<Set<String>>(emptySet()) }
    var selectedBaits by remember { mutableStateOf<Set<String>>(emptySet()) }
    var selectedHours by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var selectedWeather by remember { mutableStateOf<Set<WeatherType>>(emptySet()) }

    // États de l'interface
    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showRarityDropdown by remember { mutableStateOf(false) }

    // États des dialogs de sélection multiple
    var showLakesDialog by remember { mutableStateOf(false) }
    var showBaitsDialog by remember { mutableStateOf(false) }
    var showHoursDialog by remember { mutableStateOf(false) }
    var showWeatherDialog by remember { mutableStateOf(false) }

    // ✅ CORRECTION : Récupération des données avec remember
    val allLakes = remember { viewModel.getAllLakes() }
    val allBaits = remember { viewModel.getAllGameBaits() }
    val allHours = remember { (0..23).toList() }
    val allWeatherTypes = remember { WeatherType.values().toList() }

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
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                BackButton(onClick = onBack)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "🐟 Suggérer un poisson",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Enrichissez le jeu avec vos connaissances",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Message d'information
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Conseils pour une bonne suggestion",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Vérifiez que le poisson n'existe pas déjà\n" +
                                    "• Utilisez des noms français et scientifiques corrects\n" +
                                    "• Basez-vous sur des sources fiables\n" +
                                    "• Plus d'informations = plus de chances d'être accepté !",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // ==========================================
            // SECTION 1: INFORMATIONS GÉNÉRALES
            // ==========================================

            Text(
                text = "📝 Informations générales",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Nom français
            OutlinedTextField(
                value = fishName,
                onValueChange = { fishName = it },
                label = { Text("Nom français *", color = Color.Gray) },
                placeholder = { Text("Ex: Truite fario", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nom scientifique
            OutlinedTextField(
                value = scientificName,
                onValueChange = { scientificName = it },
                label = { Text("Nom scientifique", color = Color.Gray) },
                placeholder = { Text("Ex: Salmo trutta", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description du poisson *", color = Color.Gray) },
                placeholder = {
                    Text(
                        "Décrivez les caractéristiques du poisson, son habitat, ses particularités...",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // SECTION 2: CARACTÉRISTIQUES DE JEU
            // ==========================================

            Text(
                text = "🎮 Caractéristiques de jeu",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Rareté
            ExposedDropdownMenuBox(
                expanded = showRarityDropdown,
                onExpandedChange = { showRarityDropdown = it }
            ) {
                OutlinedTextField(
                    value = selectedRarity.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rareté suggérée *", color = Color.Gray) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showRarityDropdown) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.Gray
                    )
                )

                ExposedDropdownMenu(
                    expanded = showRarityDropdown,
                    onDismissRequest = { showRarityDropdown = false },
                    modifier = Modifier.background(Color(0xFF1E3A5F))
                ) {
                    FishRarity.values().forEach { rarity ->
                        DropdownMenuItem(
                            text = { Text(rarity.displayName, color = Color.White) },
                            onClick = {
                                selectedRarity = rarity
                                showRarityDropdown = false
                            },
                            modifier = Modifier.background(Color(0xFF1E3A5F))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Poids min/max
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = minWeight,
                    onValueChange = { minWeight = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Poids min (kg)", color = Color.Gray) },
                    placeholder = { Text("0.5", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.Gray
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(16.dp))

                OutlinedTextField(
                    value = maxWeight,
                    onValueChange = { maxWeight = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Poids max (kg)", color = Color.Gray) },
                    placeholder = { Text("3.0", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.Gray
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // SECTION 3: PRÉFÉRENCES D'HABITAT
            // ==========================================

            Text(
                text = "🏞️ Préférences d'habitat",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lacs suggérés
            OutlinedTextField(
                value = if (selectedLakes.isEmpty()) "" else "${selectedLakes.size} lac(s) sélectionné(s)",
                onValueChange = {},
                readOnly = true,
                label = { Text("Lacs où le trouver", color = Color.Gray) },
                placeholder = { Text("Cliquer pour sélectionner", color = Color.Gray) },
                trailingIcon = {
                    IconButton(onClick = { showLakesDialog = true }) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Appâts préférés
            OutlinedTextField(
                value = if (selectedBaits.isEmpty()) "" else "${selectedBaits.size} appât(s) sélectionné(s)",
                onValueChange = {},
                readOnly = true,
                label = { Text("Appâts recommandés", color = Color.Gray) },
                placeholder = { Text("Cliquer pour sélectionner", color = Color.Gray) },
                trailingIcon = {
                    IconButton(onClick = { showBaitsDialog = true }) {
                        Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color.Gray)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Heures de pêche
            OutlinedTextField(
                value = if (selectedHours.isEmpty()) "" else "${selectedHours.size} heure(s) sélectionnée(s)",
                onValueChange = {},
                readOnly = true,
                label = { Text("Meilleures heures", color = Color.Gray) },
                placeholder = { Text("Cliquer pour sélectionner", color = Color.Gray) },
                trailingIcon = {
                    IconButton(onClick = { showHoursDialog = true }) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Météo préférée
            OutlinedTextField(
                value = if (selectedWeather.isEmpty()) "" else "${selectedWeather.size} condition(s) sélectionnée(s)",
                onValueChange = {},
                readOnly = true,
                label = { Text("Conditions météo", color = Color.Gray) },
                placeholder = { Text("Cliquer pour sélectionner", color = Color.Gray) },
                trailingIcon = {
                    IconButton(onClick = { showWeatherDialog = true }) {
                        Icon(Icons.Default.Cloud, contentDescription = null, tint = Color.Gray)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // SECTION 4: JUSTIFICATION
            // ==========================================

            Text(
                text = "💡 Justification de votre suggestion",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Justification
            OutlinedTextField(
                value = justification,
                onValueChange = { justification = it },
                label = { Text("Pourquoi ce poisson devrait être ajouté ? *", color = Color.Gray) },
                placeholder = {
                    Text(
                        "Ex: Ce poisson est présent dans les eaux françaises et très apprécié des pêcheurs...",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Source d'information
            OutlinedTextField(
                value = sourceUrl,
                onValueChange = { sourceUrl = it },
                label = { Text("Source d'information (optionnel)", color = Color.Gray) },
                placeholder = { Text("https://...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Message d'erreur
            if (errorMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x33EF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = errorMessage,
                            color = Color(0xFFEF4444),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Bouton d'envoi
            Button(
                onClick = {
                    // Validation des champs obligatoires
                    when {
                        fishName.isBlank() -> {
                            errorMessage = "Le nom français est obligatoire"
                            return@Button
                        }
                        description.isBlank() -> {
                            errorMessage = "La description est obligatoire"
                            return@Button
                        }
                        justification.isBlank() -> {
                            errorMessage = "La justification est obligatoire"
                            return@Button
                        }
                        minWeight.isNotBlank() && maxWeight.isNotBlank() -> {
                            val min = minWeight.toDoubleOrNull()
                            val max = maxWeight.toDoubleOrNull()
                            if (min == null || max == null) {
                                errorMessage = "Les poids doivent être des nombres valides"
                                return@Button
                            }
                            if (min >= max) {
                                errorMessage = "Le poids minimum doit être inférieur au poids maximum"
                                return@Button
                            }
                        }
                    }

                    isSubmitting = true
                    errorMessage = ""

                    coroutineScope.launch {
                        try {
                            communityRepo.submitFishSuggestion(
                                fishName = fishName,
                                scientificName = scientificName,
                                description = description,
                                suggestedLakes = selectedLakes.toList(),
                                preferredBaits = selectedBaits.toList(),
                                rarity = selectedRarity,
                                minWeight = minWeight.toDoubleOrNull() ?: 0.0,
                                maxWeight = maxWeight.toDoubleOrNull() ?: 0.0,
                                bestHours = selectedHours.toList(),
                                bestWeather = selectedWeather.toList(),
                                justification = justification,
                                sourceUrl = sourceUrl.ifBlank { null }
                            )
                            showSuccessDialog = true
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Erreur lors de l'envoi de la suggestion"
                        } finally {
                            isSubmitting = false
                        }
                    }
                },
                enabled = !isSubmitting && fishName.isNotBlank() && description.isNotBlank() && justification.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981),
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Envoyer la suggestion",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // ==========================================
    // ✅ CORRECTION : DIALOGS DE SÉLECTION MULTIPLE
    // ==========================================

    // Dialog de sélection des lacs
    if (showLakesDialog) {
        MultiSelectDialog(
            title = "Sélectionner les lacs",
            items = allLakes.map { it.name },
            selectedItems = selectedLakes,
            onConfirm = { newSelection ->
                selectedLakes = newSelection
                showLakesDialog = false
            },
            onDismiss = { showLakesDialog = false }
        )
    }

    // Dialog de sélection des appâts
    if (showBaitsDialog) {
        MultiSelectDialog(
            title = "Sélectionner les appâts",
            items = allBaits,
            selectedItems = selectedBaits,
            onConfirm = { newSelection ->
                selectedBaits = newSelection
                showBaitsDialog = false
            },
            onDismiss = { showBaitsDialog = false }
        )
    }

    // ✅ CORRECTION : Dialog de sélection des heures
    if (showHoursDialog) {
        MultiSelectDialog(
            title = "Sélectionner les heures",
            items = allHours.map { "${it}h" },
            selectedItems = selectedHours.map { "${it}h" }.toSet(),
            onConfirm = { newSelection ->
                selectedHours = newSelection.mapNotNull { hourString ->
                    hourString.replace("h", "").toIntOrNull()
                }.toSet()
                showHoursDialog = false
            },
            onDismiss = { showHoursDialog = false }
        )
    }

    // ✅ CORRECTION : Dialog de sélection de la météo
    if (showWeatherDialog) {
        MultiSelectDialog(
            title = "Sélectionner les conditions météo",
            items = allWeatherTypes.map { it.displayName },
            selectedItems = selectedWeather.map { it.displayName }.toSet(),
            onConfirm = { newSelection ->
                selectedWeather = newSelection.mapNotNull { displayName ->
                    allWeatherTypes.find { it.displayName == displayName }
                }.toSet()
                showWeatherDialog = false
            },
            onDismiss = { showWeatherDialog = false }
        )
    }

    // Dialog de succès
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            containerColor = Color(0xFF1E3A5F),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Suggestion envoyée !", color = Color.White)
                }
            },
            text = {
                Text(
                    "Merci pour votre suggestion ! La communauté pourra maintenant voter " +
                            "pour votre poisson. Si elle obtient suffisamment de votes positifs, " +
                            "elle sera étudiée par notre équipe pour être potentiellement ajoutée au jeu.",
                    color = Color.Gray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("OK")
                }
            }
        )
    }
}

// ✅ CORRECTION : Dialog réutilisable pour les sélections multiples
@Composable
fun MultiSelectDialog(
    title: String,
    items: List<String>,
    selectedItems: Set<String>,
    onConfirm: (Set<String>) -> Unit,
    onDismiss: () -> Unit
) {
    var tempSelection by remember { mutableStateOf(selectedItems) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E3A5F),
        title = {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold)
        },
        text = {
            LazyColumn(
                modifier = Modifier.height(300.dp)
            ) {
                items(items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item in tempSelection,
                            onCheckedChange = { isChecked ->
                                tempSelection = if (isChecked) {
                                    tempSelection + item
                                } else {
                                    tempSelection - item
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF10B981),
                                uncheckedColor = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Row {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Annuler", color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onConfirm(tempSelection) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Confirmer (${tempSelection.size})")
                }
            }
        }
    )
}