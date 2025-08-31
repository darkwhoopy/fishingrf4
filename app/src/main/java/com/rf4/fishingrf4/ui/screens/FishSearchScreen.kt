package com.rf4.fishingrf4.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.models.Difficulty
import com.rf4.fishingrf4.data.models.Fish
import com.rf4.fishingrf4.data.models.FishRarity
import com.rf4.fishingrf4.data.models.FishingEntry
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.data.models.LakeType
import com.rf4.fishingrf4.ui.components.BackButton

// Classe locale pour les statistiques de capture dans la recherche
data class FishStats(
    val totalCaught: Int
)

enum class SortOption(val displayName: String) {
    NAME("Nom A-Z"),
    RARITY("Rareté"),
    LEVEL("Niveau lac"),
    CAPTURES("Mes captures")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishSearchScreen(
    allLakes: List<Lake>,
    fishingEntries: List<FishingEntry>,
    onBack: () -> Unit,
    onFishDetail: (Fish, Lake) -> Unit
) {
    // États pour les filtres et la recherche
    var searchQuery by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var selectedLake by remember { mutableStateOf<Lake?>(null) }
    var selectedRarity by remember { mutableStateOf<FishRarity?>(null) }
    var sortOption by remember { mutableStateOf(SortOption.NAME) }
    var onlyCaught by remember { mutableStateOf(false) }

    // Récupération des poissons de tous les lacs
    val allFish = allLakes.flatMap { lake ->
        lake.availableFish.map { fish ->
            fish to lake
        }
    }.distinctBy { it.first.name }.map { it.first }

    // Statistiques des captures par poisson
    val fishCaptureStats = fishingEntries.groupBy { it.fish.name }
        .mapValues { (_, entries) -> entries.size }

    // Logique de filtrage
    val filteredAndSortedFish = remember(searchQuery, selectedLake, selectedRarity, sortOption, onlyCaught, allFish, fishCaptureStats) {
        var filtered = allFish

        // Filtre par recherche textuelle
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter { fish ->
                fish.name.contains(searchQuery, ignoreCase = true) ||
                        fish.species.contains(searchQuery, ignoreCase = true)
            }
        }

        // Filtre par lac
        if (selectedLake != null) {
            filtered = filtered.filter { fish ->
                selectedLake!!.availableFish.any { it.name == fish.name }
            }
        }

        // Filtre par rareté
        if (selectedRarity != null) {
            filtered = filtered.filter { it.rarity == selectedRarity }
        }

        // Filtre "seulement capturés"
        if (onlyCaught) {
            filtered = filtered.filter { fishCaptureStats[it.name] ?: 0 > 0 }
        }

        // Tri
        when (sortOption) {
            SortOption.NAME -> filtered.sortedBy { it.name }
            SortOption.RARITY -> filtered.sortedBy { it.rarity.ordinal }
            SortOption.LEVEL -> filtered.sortedBy { fish ->
                allLakes.filter { lake -> lake.availableFish.any { it.name == fish.name } }
                    .minOfOrNull { it.unlockLevel } ?: 0
            }
            SortOption.CAPTURES -> filtered.sortedByDescending { fishCaptureStats[it.name] ?: 0 }
        }
    }

    // Fonction pour vérifier si des filtres sont actifs
    val hasActiveFilters = selectedLake != null || selectedRarity != null || onlyCaught

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // En-tête fixe
        Column(
            modifier = Modifier
                .background(Color(0xFF0F172A))
                .padding(16.dp)
        ) {
            // Bouton retour et titre
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButton(onClick = onBack)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "🔍 Recherche avancée",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Barre de recherche compacte
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Rechercher un poisson", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Rechercher", tint = Color.Gray) },
                trailingIcon = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            if (showFilters) Icons.Default.Close else Icons.Default.Tune,
                            contentDescription = if (showFilters) "Fermer filtres" else "Filtres",
                            tint = if (hasActiveFilters) Color(0xFF10B981) else Color.Gray
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF10B981),
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true
            )

            // Résumé des filtres actifs (compact)
            if (hasActiveFilters && !showFilters) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (selectedLake != null) {
                        item {
                            FilterChip(
                                onClick = { selectedLake = null },
                                label = { Text(selectedLake!!.name, fontSize = 11.sp) },
                                selected = true,
                                trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Supprimer", modifier = Modifier.size(14.dp)) }
                            )
                        }
                    }
                    if (selectedRarity != null) {
                        item {
                            FilterChip(
                                onClick = { selectedRarity = null },
                                label = { Text(selectedRarity!!.name, fontSize = 11.sp) },
                                selected = true,
                                trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Supprimer", modifier = Modifier.size(14.dp)) }
                            )
                        }
                    }
                    if (onlyCaught) {
                        item {
                            FilterChip(
                                onClick = { onlyCaught = false },
                                label = { Text("Mes captures", fontSize = 11.sp) },
                                selected = true,
                                trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Supprimer", modifier = Modifier.size(14.dp)) }
                            )
                        }
                    }
                }
            }

            // Compteur de résultats
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${filteredAndSortedFish.size} poisson(s) trouvé(s)",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                // Tri rapide (toujours visible)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(SortOption.values()) { option ->
                        FilterChip(
                            onClick = { sortOption = option },
                            label = { Text(option.displayName, fontSize = 10.sp) },
                            selected = sortOption == option,
                            modifier = Modifier.height(28.dp)
                        )
                    }
                }
            }
        }

        // Panneau des filtres (overlay slide depuis le haut)
        AnimatedVisibility(
            visible = showFilters,
            enter = slideInVertically(initialOffsetY = { -it }),
            exit = slideOutVertically(targetOffsetY = { -it })
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Filtres avancés", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sélection du lac (simplifié)
                    Text("Lac :", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                onClick = { selectedLake = null },
                                label = { Text("Tous", fontSize = 12.sp) },
                                selected = selectedLake == null
                            )
                        }
                        items(allLakes) { lake ->
                            FilterChip(
                                onClick = { selectedLake = if (selectedLake == lake) null else lake },
                                label = { Text("${lake.name} (Niv.${lake.unlockLevel})", fontSize = 11.sp) },
                                selected = selectedLake == lake
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sélection de la rareté
                    Text("Rareté :", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                onClick = { selectedRarity = null },
                                label = { Text("Toutes", fontSize = 12.sp) },
                                selected = selectedRarity == null
                            )
                        }
                        items(FishRarity.values()) { rarity ->
                            FilterChip(
                                onClick = { selectedRarity = if (selectedRarity == rarity) null else rarity },
                                label = { Text(rarity.name, fontSize = 12.sp) },
                                selected = selectedRarity == rarity
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Switch compact "Mes captures"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Seulement mes captures :", color = Color.White, fontSize = 14.sp)
                        Switch(
                            checked = onlyCaught,
                            onCheckedChange = { onlyCaught = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF10B981))
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Boutons d'action
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Bouton "Effacer tout"
                        OutlinedButton(
                            onClick = {
                                selectedLake = null
                                selectedRarity = null
                                onlyCaught = false
                                sortOption = SortOption.NAME
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Text("Effacer", fontSize = 12.sp)
                        }

                        // Bouton "Appliquer"
                        Button(
                            onClick = { showFilters = false },
                            modifier = Modifier.weight(2f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Text("Appliquer les filtres", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Liste des poissons (prend tout l'espace restant)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredAndSortedFish) { fish ->
                val lakesForFish = allLakes.filter { lake ->
                    lake.availableFish.any { it.name == fish.name }
                }
                val captureCount = fishCaptureStats[fish.name] ?: 0

                // Créer les statistiques de capture pour ce poisson
                val fishStats = if (captureCount > 0) {
                    FishStats(totalCaught = captureCount)
                } else null

                SimpleFishCard(
                    fish = fish,
                    stats = fishStats,
                    onClick = {
                        onFishDetail(fish, lakesForFish.firstOrNull() ?: Lake("", "", LakeType.LAKE, Difficulty.BEGINNER, emptyList()))
                    }
                )
            }
        }
    }
}

// Composable simplifié pour afficher une carte de poisson dans la recherche
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleFishCard(fish: Fish, stats: FishStats?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick() },
            )
            .border(width = 2.dp, color = Color(fish.rarity.colorValue), shape = RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(fish.rarity.colorValue).copy(alpha = 0.3f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fish.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = fish.species,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (stats != null) {
                    Text(
                        text = "${stats.totalCaught} capture(s)",
                        fontSize = 11.sp,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (stats == null) "✨" else "🎣",
                    fontSize = 20.sp
                )
                if (stats != null && stats.totalCaught > 0) {
                    Row {
                        repeat(minOf(stats.totalCaught, 3)) {
                            Text("⭐", fontSize = 12.sp)
                        }
                        if (stats.totalCaught > 3) {
                            Text("+", fontSize = 14.sp, color = Color(0xFFFFD700))
                        }
                    }
                }
            }
        }
    }
}