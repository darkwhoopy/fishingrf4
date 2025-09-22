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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.models.*
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.utils.getLocalizedName

// Classe locale pour les statistiques de capture dans la recherche
data class FishStats(
    val totalCaught: Int
)

enum class SortOption(val displayNameRes: Int) {
    NAME(R.string.search_sort_name),
    RARITY(R.string.search_sort_rarity),
    LEVEL(R.string.search_sort_level),
    CAPTURES(R.string.search_sort_captures)
}

enum class SearchTab(val displayNameRes: Int, val emoji: String) {
    FISH(R.string.search_tab_fish, "🐟"),
    BAITS(R.string.search_tab_baits, "🎣")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishSearchScreen(
    allLakes: List<Lake>,
    fishingEntries: List<FishingEntry>,
    onBack: () -> Unit,
    onFishDetail: (Fish, Lake) -> Unit
) {
    var selectedTab by remember { mutableStateOf(SearchTab.FISH) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // En-tête avec onglets
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
                    stringResource(R.string.search_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Onglets Poissons / Appâts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SearchTab.values().forEach { tab ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = tab },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedTab == tab)
                                Color(0xFF10B981) else Color(0xFF374151)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${tab.emoji} ${stringResource(tab.displayNameRes)}",
                                color = Color.White,
                                fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Contenu selon l'onglet sélectionné
        when (selectedTab) {
            SearchTab.FISH -> {
                FishSearchContent(
                    allLakes = allLakes,
                    fishingEntries = fishingEntries,
                    onFishDetail = onFishDetail
                )
            }
            SearchTab.BAITS -> {
                BaitSearchContent()
            }
        }
    }
}

@Composable
fun FishSearchContent(
    allLakes: List<Lake>,
    fishingEntries: List<FishingEntry>,
    onFishDetail: (Fish, Lake) -> Unit
) {
    val context = LocalContext.current
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
    val filteredAndSortedFish = remember(searchQuery, selectedLake, selectedRarity, sortOption, onlyCaught, allFish, fishCaptureStats, context) {
        var filtered = allFish

        // Filtre par recherche textuelle
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter { fish ->
                fish.name.contains(searchQuery, ignoreCase = true) ||
                        fish.scientificName.contains(searchQuery, ignoreCase = true)
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
            SortOption.NAME -> filtered.sortedBy { it.getLocalizedName(context) }
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

    Column(modifier = Modifier.fillMaxSize()) {
        // Contenu des filtres et recherche
        Column(
            modifier = Modifier
                .background(Color(0xFF0F172A))
                .padding(horizontal = 16.dp)
        ) {
            // Barre de recherche compacte
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(stringResource(R.string.fish_search_placeholder), color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.desc_search), tint = Color.Gray) },
                trailingIcon = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            if (showFilters) Icons.Default.Close else Icons.Default.Tune,
                            contentDescription = if (showFilters) stringResource(R.string.search_close_filters) else stringResource(R.string.search_filters),
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
                                trailingIcon = { Icon(Icons.Default.Close, contentDescription = stringResource(R.string.delete), modifier = Modifier.size(14.dp)) }
                            )
                        }
                    }
                    if (selectedRarity != null) {
                        item {
                            FilterChip(
                                onClick = { selectedRarity = null },
                                label = { Text(selectedRarity!!.name, fontSize = 11.sp) },
                                selected = true,
                                trailingIcon = { Icon(Icons.Default.Close, contentDescription = stringResource(R.string.delete), modifier = Modifier.size(14.dp)) }
                            )
                        }
                    }
                    if (onlyCaught) {
                        item {
                            FilterChip(
                                onClick = { onlyCaught = false },
                                label = { Text(stringResource(R.string.search_my_catches), fontSize = 11.sp) },
                                selected = true,
                                trailingIcon = { Icon(Icons.Default.Close, contentDescription = stringResource(R.string.delete), modifier = Modifier.size(14.dp)) }
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
                    stringResource(R.string.fish_results_count, filteredAndSortedFish.size),
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
                            label = { Text(stringResource(option.displayNameRes), fontSize = 10.sp) },
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
                    Text(stringResource(R.string.search_advanced_filters), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sélection du lac (simplifié)
                    Text(stringResource(R.string.search_lake_filter), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                onClick = { selectedLake = null },
                                label = { Text(stringResource(R.string.search_all), fontSize = 12.sp) },
                                selected = selectedLake == null
                            )
                        }
                        items(allLakes) { lake ->
                            FilterChip(
                                onClick = { selectedLake = if (selectedLake == lake) null else lake },
                                label = { Text(stringResource(R.string.search_lake_level_format, lake.getLocalizedName(), lake.unlockLevel), fontSize = 11.sp) },
                                selected = selectedLake == lake
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sélection de la rareté
                    Text(stringResource(R.string.search_rarity_filter), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                onClick = { selectedRarity = null },
                                label = { Text(stringResource(R.string.search_all_rarities), fontSize = 12.sp) },
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
                        Text(stringResource(R.string.search_only_my_catches), color = Color.White, fontSize = 14.sp)
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
                            Text(stringResource(R.string.search_clear_all), fontSize = 12.sp)
                        }

                        // Bouton "Appliquer"
                        Button(
                            onClick = { showFilters = false },
                            modifier = Modifier.weight(2f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Text(stringResource(R.string.search_apply_filters), fontSize = 12.sp)
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

@Composable
fun BaitSearchContent() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSearchType by remember { mutableStateOf("Nom") }
    var selectedCategory by remember { mutableStateOf("Toutes") }
    var selectedTimeOfDay by remember { mutableStateOf("Tous") }
    var selectedWaterType by remember { mutableStateOf("Tous") }
    var showAdvancedFilters by remember { mutableStateOf(false) }

    // Types de recherche disponibles
    val searchTypes = listOf("Nom", "Poisson", "Catégorie")
    val categories = listOf("Toutes") + BaitDatabase.getAllCategories()
    val timeOptions = listOf("Tous", "Jour", "Nuit", "Crépuscule")
    val waterTypes = listOf("Tous", "Eau douce", "Mer", "Saumâtre")

    // Logique de filtrage avec tri alphabétique
    val filteredBaits = remember(searchQuery, selectedSearchType, selectedCategory, selectedTimeOfDay, selectedWaterType) {
        val results = when {
            searchQuery.isEmpty() && selectedCategory == "Toutes" &&
                    selectedTimeOfDay == "Tous" && selectedWaterType == "Tous" -> BaitDatabase.allBaits

            else -> {
                val searchResults = when (selectedSearchType) {
                    "Nom" -> BaitDatabase.searchByName(searchQuery)
                    "Poisson" -> BaitDatabase.searchByFish(searchQuery)
                    "Catégorie" -> BaitDatabase.searchByCategory(searchQuery)
                    else -> BaitDatabase.allBaits
                }

                // Appliquer les filtres additionnels
                searchResults.filter { bait ->
                    (selectedCategory == "Toutes" || bait.category.contains(selectedCategory, ignoreCase = true)) &&
                            (selectedTimeOfDay == "Tous" || bait.timeOfDay.contains(selectedTimeOfDay, ignoreCase = true)) &&
                            (selectedWaterType == "Tous" || bait.waterType.contains(selectedWaterType, ignoreCase = true))
                }
            }
        }

        // Tri alphabétique par nom d'appât
        results.sortedBy { it.name }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Section de recherche compacte
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Type de recherche - Plus compact
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(searchTypes) { type ->
                        FilterChip(
                            onClick = { selectedSearchType = type },
                            label = { Text(type, fontSize = 12.sp) },
                            selected = selectedSearchType == type,
                            modifier = Modifier.height(32.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = when (type) {
                                        "Nom" -> Icons.Default.Search
                                        "Poisson" -> Icons.Default.SetMeal
                                        "Catégorie" -> Icons.Default.Category
                                        else -> Icons.Default.Search
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Barre de recherche compacte
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = when (selectedSearchType) {
                                "Nom" -> "Nom d'appât..."
                                "Poisson" -> "Nom de poisson..."
                                "Catégorie" -> "Catégorie..."
                                else -> "Rechercher..."
                            },
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Rechercher",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        Row {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Effacer",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            IconButton(onClick = { showAdvancedFilters = !showAdvancedFilters }) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Filtres",
                                    tint = if (selectedCategory != "Toutes" || selectedTimeOfDay != "Tous" || selectedWaterType != "Tous")
                                        Color(0xFF10B981) else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF10B981),
                        unfocusedBorderColor = Color.Gray
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                )

                // Filtres avancés compacts
                AnimatedVisibility(visible = showAdvancedFilters) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Filtres en lignes compactes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Catégorie
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Catégorie",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                                FilterChip(
                                    onClick = {
                                        selectedCategory = if (selectedCategory == "Toutes") categories[1] else "Toutes"
                                    },
                                    label = {
                                        Text(
                                            text = selectedCategory.split(" - ").lastOrNull() ?: selectedCategory,
                                            fontSize = 10.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    selected = selectedCategory != "Toutes",
                                    modifier = Modifier.fillMaxWidth().height(28.dp)
                                )
                            }

                            // Moment
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Moment",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                                FilterChip(
                                    onClick = {
                                        val currentIndex = timeOptions.indexOf(selectedTimeOfDay)
                                        selectedTimeOfDay = timeOptions[(currentIndex + 1) % timeOptions.size]
                                    },
                                    label = { Text(selectedTimeOfDay, fontSize = 10.sp) },
                                    selected = selectedTimeOfDay != "Tous",
                                    modifier = Modifier.fillMaxWidth().height(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Compteur de résultats compact
        if (searchQuery.isNotEmpty() || selectedCategory != "Toutes" || selectedTimeOfDay != "Tous") {
            Text(
                text = "${filteredBaits.size} appât(s) trouvé(s)",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Résultats - Liste compacte avec bulles expansibles
        if (filteredBaits.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF7F1D1D))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Aucun appât trouvé",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredBaits) { bait ->
                    CompactBaitCard(bait = bait)
                }
            }
        }
    }
}

@Composable
fun CompactBaitCard(bait: BaitInfo) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // En-tête compact - toujours visible
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bait.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )

                    // Catégorie en plus petit
                    Text(
                        text = bait.category.split(" - ").lastOrNull() ?: bait.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF10B981),
                        fontSize = 12.sp
                    )

                    // Poissons principaux (max 2)
                    if (bait.targetFish.isNotEmpty()) {
                        Text(
                            text = bait.targetFish.take(2).joinToString(", ") +
                                    if (bait.targetFish.size > 2) "..." else "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }

                // Indicateur d'expansion
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Réduire" else "Développer",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Contenu étendu - bulle qui se déplie
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    HorizontalDivider(
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Description
                    Text(
                        text = bait.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Poissons cibles complets
                    Text(
                        text = "Poissons cibles :",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF10B981),
                        fontSize = 12.sp
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        items(bait.targetFish) { fish ->
                            Surface(
                                color = Color(0xFF064E3B),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = fish,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF10B981),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Efficacité et acquisition compacts
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        CompactDetailRow(
                            title = "Efficacité",
                            content = bait.effectiveness,
                            icon = Icons.Default.TrendingUp
                        )

                        CompactDetailRow(
                            title = "Acquisition",
                            content = bait.acquisition,
                            icon = Icons.Default.ShoppingCart
                        )

                        CompactDetailRow(
                            title = "Conseils",
                            content = bait.tips,
                            icon = Icons.Default.Lightbulb
                        )
                    }

                    // Infos techniques si disponibles - très compact
                    if (bait.price.isNotEmpty() || bait.timeOfDay.isNotEmpty() ||
                        bait.waterType.isNotEmpty() || bait.depth.isNotEmpty()) {

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (bait.price.isNotEmpty()) {
                                item {
                                    CompactTechChip("Prix", bait.price)
                                }
                            }
                            if (bait.timeOfDay.isNotEmpty()) {
                                item {
                                    CompactTechChip("Moment", bait.timeOfDay)
                                }
                            }
                            if (bait.waterType.isNotEmpty()) {
                                item {
                                    CompactTechChip("Eau", bait.waterType)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun CompactDetailRow(
    title: String,
    content: String,
    icon: ImageVector
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color(0xFF10B981)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF10B981),
                fontSize = 11.sp
            )
        }

        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(start = 20.dp, top = 2.dp)
        )
    }
}

@Composable
fun CompactTechChip(label: String, value: String) {
    Surface(
        color = Color(0xFF374151),
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontSize = 9.sp
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun BaitCard(bait: BaitInfo) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // En-tête de la carte
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bait.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                    Text(
                        text = bait.englishName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF3B82F6),
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Catégorie avec badge
                    Surface(
                        color = Color(0xFF374151),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = bait.category.split(" - ").lastOrNull() ?: bait.category,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
                }

                // Icône d'expansion
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Réduire" else "Développer",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description courte
            Text(
                text = bait.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Poissons cibles (toujours visibles)
            Text(
                text = "Poissons cibles :",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF10B981)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                items(bait.targetFish.take(if (expanded) bait.targetFish.size else 3)) { fish ->
                    Surface(
                        color = Color(0xFF064E3B),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = fish,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF10B981)
                        )
                    }
                }

                if (!expanded && bait.targetFish.size > 3) {
                    item {
                        Surface(
                            color = Color(0xFF374151),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "+${bait.targetFish.size - 3}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Détails étendus
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Efficacité
                    DetailSection(
                        title = "Efficacité",
                        content = bait.effectiveness,
                        icon = Icons.Default.TrendingUp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Acquisition
                    DetailSection(
                        title = "Acquisition",
                        content = bait.acquisition,
                        icon = Icons.Default.ShoppingCart
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Conseils
                    DetailSection(
                        title = "Conseils",
                        content = bait.tips,
                        icon = Icons.Default.Lightbulb
                    )

                    // Informations techniques si disponibles
                    if (bait.price.isNotEmpty() || bait.timeOfDay.isNotEmpty() ||
                        bait.waterType.isNotEmpty() || bait.depth.isNotEmpty()) {

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(color = Color(0xFF374151))

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Informations techniques",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF10B981)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)
                        ) {
                            if (bait.price.isNotEmpty()) {
                                item {
                                    TechnicalInfoChip(
                                        label = "Prix",
                                        value = bait.price,
                                        icon = Icons.Default.AttachMoney
                                    )
                                }
                            }

                            if (bait.timeOfDay.isNotEmpty()) {
                                item {
                                    TechnicalInfoChip(
                                        label = "Moment",
                                        value = bait.timeOfDay,
                                        icon = Icons.Default.Schedule
                                    )
                                }
                            }

                            if (bait.waterType.isNotEmpty()) {
                                item {
                                    TechnicalInfoChip(
                                        label = "Eau",
                                        value = bait.waterType,
                                        icon = Icons.Default.Water
                                    )
                                }
                            }

                            if (bait.depth.isNotEmpty()) {
                                item {
                                    TechnicalInfoChip(
                                        label = "Profondeur",
                                        value = bait.depth,
                                        icon = Icons.Default.Straighten
                                    )
                                }
                            }

                            if (bait.hookSize.isNotEmpty()) {
                                item {
                                    TechnicalInfoChip(
                                        label = "Hameçon",
                                        value = bait.hookSize,
                                        icon = Icons.Default.Anchor
                                    )
                                }
                            }

                            if (bait.level.isNotEmpty()) {
                                item {
                                    TechnicalInfoChip(
                                        label = "Niveau",
                                        value = bait.level,
                                        icon = Icons.Default.Star
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailSection(
    title: String,
    content: String,
    icon: ImageVector
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF10B981)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF10B981)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun TechnicalInfoChip(
    label: String,
    value: String,
    icon: ImageVector
) {
    Surface(
        color = Color(0xFF374151),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Gray
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
    }
}

// Composable simplifié pour afficher une carte de poisson dans la recherche
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleFishCard(fish: Fish, stats: FishStats?, onClick: () -> Unit) {
    val context = LocalContext.current
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
                    text = fish.getLocalizedName(context),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = fish.scientificName,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (stats != null) {
                    Text(
                        text = stringResource(R.string.search_fish_captures_count, stats.totalCaught),
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