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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.models.Difficulty
import com.rf4.fishingrf4.data.models.Fish
import com.rf4.fishingrf4.data.models.FishRarity
import com.rf4.fishingrf4.data.models.FishingEntry
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.data.models.LakeType
import com.rf4.fishingrf4.ui.components.BackButton
import androidx.compose.ui.platform.LocalContext
import com.rf4.fishingrf4.data.models.getLocalizedName

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

// Classe de données pour les appâts
data class BaitInfo(
    val name: String,
    val englishName: String,
    val category: String,
    val description: String,
    val effectiveness: String,
    val targetFish: List<String>,
    val acquisition: String,
    val tips: String
)

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
                                label = { Text(stringResource(R.string.search_lake_level_format, lake.name, lake.unlockLevel), fontSize = 11.sp) },
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

    // Base de données d'appâts
    val baitsDatabase = remember {
        listOf(
            BaitInfo(
                name = "Ver de terre",
                englishName = "Worm",
                category = "Appâts naturels",
                description = "C'est l'appât de base par excellence. Il peut être acheté ou obtenu gratuitement en creusant avec une pelle.",
                effectiveness = "Particulièrement efficace la nuit (de 20:00 à 6:00) pour attraper une grande variété de poissons de petite et moyenne taille.",
                targetFish = listOf("Gardon", "Ablette", "Carassin", "Brème"),
                acquisition = "Achat ou creusage avec pelle",
                tips = "Appât universel parfait pour débuter"
            ),
            BaitInfo(
                name = "Ver de vase",
                englishName = "Bloodworm",
                category = "Appâts naturels",
                description = "Petite larve rouge, extrêmement efficace.",
                effectiveness = "L'appât le plus rentable pour gagner de l'expérience de jour (6h-20h).",
                targetFish = listOf("Gardon", "Ablette", "Carassin"),
                acquisition = "Achat",
                tips = "Idéal pour l'expérience et les petits poissons"
            ),
            BaitInfo(
                name = "Casticot",
                englishName = "Caster",
                category = "Appâts naturels",
                description = "Larve de mouche, très attractive.",
                effectiveness = "Une excellente alternative au ver de vase, particulièrement pour la brème.",
                targetFish = listOf("Brème", "Gardon", "Ide"),
                acquisition = "Achat",
                tips = "Très efficace sur les cyprinidés"
            ),
            BaitInfo(
                name = "Vif",
                englishName = "Baitfish",
                category = "Appâts vivants",
                description = "Petit poisson utilisé comme appât vivant.",
                effectiveness = "Nécessite un montage spécifique. Un vif légèrement avarié est parfois meilleur.",
                targetFish = listOf("Brochet", "Silure", "Lotte", "Sandre"),
                acquisition = "Pêche (Goujon, Ablette, etc.)",
                tips = "Incontournable pour les gros prédateurs"
            ),
            BaitInfo(
                name = "Boulette de pain",
                englishName = "Bread",
                category = "Appâts fabriqués",
                description = "Le premier appât que l'on apprend à fabriquer.",
                effectiveness = "Excellent moyen de faire progresser la compétence au tout début.",
                targetFish = listOf("Carpe", "Gardon", "Brème"),
                acquisition = "Fabrication (pain + eau)",
                tips = "Parfait pour débuter la fabrication d'appâts"
            ),
            BaitInfo(
                name = "Cube de pomme de terre",
                englishName = "Potato Cubes",
                category = "Appâts fabriqués",
                description = "Un des meilleurs appâts pour la carpe.",
                effectiveness = "Extrêmement efficace sur les carpes de toutes tailles.",
                targetFish = listOf("Carpe", "Carpe miroir", "Carpe cuir"),
                acquisition = "Fabrication (pommes de terre du marché fermier)",
                tips = "Voyage au Ruisselet qui Serpente nécessaire"
            ),
            BaitInfo(
                name = "Grenouille",
                englishName = "Frog",
                category = "Appâts vivants",
                description = "Appât spécifique pour certains prédateurs.",
                effectiveness = "Se pêche avec un petit hameçon (taille 20) et une mouche près des nénuphars.",
                targetFish = listOf("Brochet", "Silure"),
                acquisition = "Pêche près des nénuphars",
                tips = "Technique de pêche particulière requise"
            ),
            BaitInfo(
                name = "Ver de nuit",
                englishName = "Nightcrawler",
                category = "Appâts vivants",
                description = "Appât spécifique pour certains prédateurs.",
                effectiveness = "À utiliser pour filtrer les petites prises et cibler les beaux spécimens",
                targetFish = listOf("Brème", "Tanche", "Carpe", "Carassin"),
                acquisition = "Achat, Pelle",
                tips = "Parfait pour les gros spécimens"
            ),
            BaitInfo(
                name = "Orge perlé",
                englishName = "Pearl Barley",
                category = "Appâts fabriqués",
                description = "Très efficace sur la brème et d'autres cyprinidés.",
                effectiveness = "Ne pas confondre avec l'orge perlé vendu comme additif pour amorce.",
                targetFish = listOf("Brème", "Gardon", "Carassin"),
                acquisition = "Fabrication (orge perlé de l'épicerie)",
                tips = "Attention à ne pas acheter l'additif pour amorce"
            )
        )
    }

    val filteredBaits = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            baitsDatabase
        } else {
            baitsDatabase.filter { bait ->
                bait.name.contains(searchQuery, ignoreCase = true) ||
                        bait.englishName.contains(searchQuery, ignoreCase = true) ||
                        bait.category.contains(searchQuery, ignoreCase = true) ||
                        bait.targetFish.any { it.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barre de recherche appâts
        Column(
            modifier = Modifier
                .background(Color(0xFF0F172A))
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(stringResource(R.string.bait_search_placeholder), color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.desc_search), tint = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF10B981),
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Compteur
            Text(
                stringResource(R.string.bait_results_count, filteredBaits.size),
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Liste des appâts
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredBaits) { bait ->
                BaitCard(bait = bait)
            }
        }
    }
}

// Carte d'information pour un appât
@Composable
fun BaitCard(bait: BaitInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // En-tête
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bait.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${bait.englishName} • ${bait.category}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Badge d'acquisition
                Surface(
                    color = when {
                        bait.acquisition.contains("Achat") -> Color(0xFF10B981)
                        bait.acquisition.contains("Fabrication") -> Color(0xFF3B82F6)
                        bait.acquisition.contains("Pêche") -> Color(0xFFF59E0B)
                        else -> Color(0xFF6B7280)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = bait.acquisition.split(" ")[0],
                        color = Color.White,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = bait.description,
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 20.sp
            )

            if (bait.effectiveness.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "💡 ${bait.effectiveness}",
                    fontSize = 13.sp,
                    color = Color(0xFF10B981),
                    lineHeight = 18.sp
                )
            }

            // Poissons cibles
            if (bait.targetFish.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.bait_target_fish_label),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    items(bait.targetFish) { fish ->
                        Surface(
                            color = Color(0xFF3B82F6),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = fish,
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Conseil
            if (bait.tips.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "⭐ ${bait.tips}",
                    fontSize = 12.sp,
                    color = Color(0xFFF59E0B),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
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