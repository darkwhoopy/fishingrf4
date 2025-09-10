// ============================================================================
// FICHIER: ui/screens/Top5Screen.kt (VERSION AMÉLIORÉE)
// Remplacer votre Top5Screen existant par cette version
// ============================================================================

package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.background
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
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import com.rf4.fishingrf4.data.online.SpeciesCount
import androidx.compose.ui.platform.LocalContext
import com.rf4.fishingrf4.data.models.getLocalizedName
import com.rf4.fishingrf4.data.FishingData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopFiveScreen(
    viewModel: FishingViewModel,
    onBack: () -> Unit
) {
    val startOfDayTimestamp by viewModel.startOfCurrentGameDayTimestamp.collectAsState()

    // États pour les différents tops
    var selectedTab by remember { mutableStateOf(0) }
    var communityPlayers by remember { mutableStateOf<List<Pair<String, Long>>>(emptyList()) }
    var communityLakes by remember { mutableStateOf<List<Pair<String, Long>>>(emptyList()) }
    var communitySpecies by remember { mutableStateOf<List<SpeciesCount>>(emptyList()) }

    // ✅ CORRECTION : Type correct
    var communitySpeciesWithBaits by remember { mutableStateOf<Map<String, Pair<Long, List<Pair<String, Long>>>>>(emptyMap()) }

    // Charger les données au démarrage
    LaunchedEffect(startOfDayTimestamp) {

        val weekAgoTimestamp = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)

        viewModel.fetchTop5PlayersOfDay(weekAgoTimestamp) { players ->
            communityPlayers = players
        }

        viewModel.fetchTop5LakesOfDay(weekAgoTimestamp) { lakes ->
            communityLakes = lakes
        }

        viewModel.fetchTop5SpeciesCountsToday { species ->
            communitySpecies = species

            // ✅ CORRECTION : Chargement des appâts avec le bon format
            if (species.isNotEmpty()) {
                val baitStatsMap = mutableMapOf<String, Pair<Long, List<Pair<String, Long>>>>()
                species.take(5).forEach { speciesCount ->
                    val fishId = "${speciesCount.species}_species"
                    viewModel.fetchSpeciesWithBaitStats { stats ->
                        communitySpeciesWithBaits = stats
                    }
                }
            }
        }
    }

    val tabs = listOf(
        "🏆 Joueurs",
        "🌊 Lacs",
        "🐟 Top Espèces",
        "🎣 Appâts Communauté"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp)
    ) {
        // En-tête
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "🏆 Top 5 Communauté",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Statistiques communautaires en temps réel",  // ✅ CHANGÉ
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        // Onglets
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF1E293B),
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenu selon l'onglet sélectionné
        when (selectedTab) {
            0 -> CommunityPlayersTab(communityPlayers)
            1 -> CommunityLakesTab(communityLakes)
            2 -> CommunitySpeciesTab(communitySpecies)  // ✅ NOUVEAU
            3 -> CommunityBaitsRealTab(communitySpeciesWithBaits)  // ✅ NOUVEAU
        }
    }
}

@Composable
private fun CommunityPlayersTab(players: List<Pair<String, Long>>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (players.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = "🎣",
                    title = "Aucun joueur aujourd'hui",
                    description = "Soyez le premier à capturer des poissons !"
                )
            }
        } else {
            items(players.take(5)) { (playerName, count) ->
                RankingCard(
                    rank = players.indexOf(playerName to count) + 1,
                    title = playerName,
                    subtitle = "$count capture${if (count > 1) "s" else ""}",
                    icon = "👤"
                )
            }
        }
    }
}

@Composable
private fun CommunityLakesTab(lakes: List<Pair<String, Long>>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (lakes.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = "🌊",
                    title = "Aucun lac actif aujourd'hui",
                    description = "Commencez à pêcher pour voir les stats !"
                )
            }
        } else {
            items(lakes.take(5)) { (lakeName, count) ->
                RankingCard(
                    rank = lakes.indexOf(lakeName to count) + 1,
                    title = lakeName,
                    subtitle = "$count capture${if (count > 1) "s" else ""}",
                    icon = "🌊"
                )
            }
        }
    }
}
// ✅ NOUVEAU COMPOSANT : Top des espèces communautaires
@Composable
private fun CommunitySpeciesTab(species: List<SpeciesCount>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🐟 Top 5 Espèces Communauté",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Les poissons les plus capturés cette semaine    ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        if (species.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = "🎣",
                    title = "Aucune capture communautaire",
                    description = "Soyez le premier à capturer des poissons !"
                )
            }
        } else {
            items(species.take(5)) { speciesCount ->
                CommunitySpeciesCard(
                    rank = species.indexOf(speciesCount) + 1,
                    fishName = speciesCount.species,
                    count = speciesCount.count.toInt()
                )
            }
        }
    }
}
@Composable
private fun CommunityBaitsRealTab(speciesStats: Map<String, Pair<Long, List<Pair<String, Long>>>>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🎣 Appâts Réellement Utilisés",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Vrais appâts utilisés par la communauté",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        if (speciesStats.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = "🎣",
                    title = "Aucune capture avec appât",
                    description = "Soyez le premier à capturer avec des appâts !"
                )
            }
        } else {
            items(speciesStats.toList().sortedByDescending { it.second.first }.take(5)) { (species, stats) ->
                val (totalCount, baitStats) = stats
                CommunitySpeciesBaitCard(
                    fishName = species,
                    totalCaptures = totalCount.toInt(),
                    topBaits = baitStats
                )
            }
        }
    }
}

@Composable
private fun CommunitySpeciesBaitCard(
    fishName: String,
    totalCaptures: Int,
    topBaits: List<Pair<String, Long>>
) {
    val context = LocalContext.current
    val fish = FishingData.getAllFish().find { it.name == fishName }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // En-tête avec le total de captures
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🐟", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = fish?.getLocalizedName(context) ?: fishName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "$totalCaptures captures",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
            }

            // Top des appâts réellement utilisés
            if (topBaits.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "🎯 Top appâts utilisés :",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF10B981)
                )

                Spacer(modifier = Modifier.height(8.dp))

                topBaits.take(5).forEachIndexed { index, (bait, count) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val medal = when (index) {
                                0 -> "🥇"
                                1 -> "🥈"
                                2 -> "🥉"
                                3 -> "4️⃣"
                                4 -> "5️⃣"
                                else -> "🏅"
                            }
                            Text(text = medal, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = bait,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }

                        Text(
                            text = "×$count",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                    }

                    if (index < topBaits.size - 1 && index < 4) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}
// ✅ NOUVEAU COMPOSANT : Carte d'espèce communautaire
@Composable
private fun CommunitySpeciesCard(rank: Int, fishName: String, count: Int) {

    val context = LocalContext.current
    val fish = FishingData.getAllFish().find { it.name == fishName }
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Médaille selon le rang
            val medal = when (rank) {
                1 -> "🥇"
                2 -> "🥈"
                3 -> "🥉"
                4 -> "4️⃣"
                5 -> "5️⃣"
                else -> "🏅"
            }

            Text(text = medal, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "🐟", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fish?.getLocalizedName(context) ?: fishName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Espèce #$rank aujourd'hui (communauté)",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$count",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                Text(
                    text = "capture${if (count > 1) "s" else ""}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// ✅ NOUVEAU COMPOSANT : Top des appâts communautaires
@Composable
private fun CommunityBaitsTab(baitsStats: Map<String, List<Pair<String, Long>>>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🎣 Appâts Communauté",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Les appâts les plus votés par la communauté",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        if (baitsStats.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = "🎣",
                    title = "Aucun vote d'appât",
                    description = "Soyez le premier à voter pour des appâts !"
                )
            }
        } else {
            items(baitsStats.entries.toList()) { (fishName, baits) ->
                CommunityBaitCard(
                    fishName = fishName,
                    baits = baits
                )
            }
        }
    }
}

// ✅ NOUVEAU COMPOSANT : Carte des appâts communautaires par poisson
@Composable
private fun CommunityBaitCard(
    fishName: String,
    baits: List<Pair<String, Long>>
) {
    val context = LocalContext.current
    val fish = FishingData.getAllFish().find { it.name == fishName }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // En-tête du poisson
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🐟", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = fish?.getLocalizedName(context) ?: fishName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "Votes communauté",
                    fontSize = 12.sp,
                    color = Color(0xFF10B981),
                    fontWeight = FontWeight.Bold
                )
            }

            // Top des appâts pour ce poisson
            if (baits.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "🎯 Top appâts votés :",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF10B981)
                )

                Spacer(modifier = Modifier.height(8.dp))

                baits.take(3).forEachIndexed { index, (bait, votes) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val medal = when (index) {
                                0 -> "🥇"
                                1 -> "🥈"
                                2 -> "🥉"
                                else -> "🏅"
                            }
                            Text(text = medal, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = bait,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }

                        Text(
                            text = "$votes votes",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                    }

                    if (index < baits.size - 1 && index < 2) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}
@Composable
private fun TopFishTab(fishCounts: Map<String, Int>) {
    val sortedFish = fishCounts.toList().sortedByDescending { it.second }.take(5)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🐟 Top 5 Poissons du Jour",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Vos espèces les plus capturées",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        if (sortedFish.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = "🎣",
                    title = "Aucune capture aujourd'hui",
                    description = "Commencez à pêcher pour voir vos stats !"
                )
            }
        } else {
            items(sortedFish) { (fishName, count) ->
                TopFishCard(
                    rank = sortedFish.indexOf(fishName to count) + 1,
                    fishName = fishName,
                    count = count
                )
            }
        }
    }
}

// ✅ CHANGEMENT 4: Ajouter ce composant TopFishCard
@Composable
private fun TopFishCard(rank: Int, fishName: String, count: Int) {

    val context = LocalContext.current
    val fish = FishingData.getAllFish().find { it.name == fishName }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Médaille selon le rang
            val medal = when (rank) {
                1 -> "🥇"
                2 -> "🥈"
                3 -> "🥉"
                4 -> "4️⃣"
                5 -> "5️⃣"
                else -> "🏅"
            }

            Text(text = medal, fontSize = 24.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Text(text = "🐟", fontSize = 20.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fish?.getLocalizedName(context) ?: fishName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Espèce #$rank aujourd'hui",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$count",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                Text(
                    text = "capture${if (count > 1) "s" else ""}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
@Composable
private fun MyBaitStatsTab(
    baitStats: List<Triple<String, String, Int>>,
    fishCounts: Map<String, Int>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🎯 Mes Appâts les Plus Efficaces",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Vos meilleurs appâts aujourd'hui",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        if (baitStats.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = "🎣",
                    title = "Aucune capture aujourd'hui",
                    description = "Commencez à pêcher avec des appâts !"
                )
            }
        } else {
            items(baitStats.take(10)) { (fishName, bait, count) ->
                BaitStatsCard(
                    fishName = fishName,
                    bait = bait,
                    count = count,
                    totalFishCount = fishCounts[fishName] ?: 0
                )
            }
        }
    }
}

@Composable
private fun MyFishCountsTab(
    fishCounts: Map<String, Int>,
    viewModel: FishingViewModel,
    startOfDayTimestamp: Long
) {
    val sortedFishCounts = fishCounts.toList().sortedByDescending { it.second }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📊 Mes Captures par Espèce",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Détail par poisson avec appâts",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        if (sortedFishCounts.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = "🐟",
                    title = "Aucune capture aujourd'hui",
                    description = "Sortez vos cannes à pêche !"
                )
            }
        } else {
            items(sortedFishCounts) { (fishName, totalCount) ->
                FishCountCard(
                    fishName = fishName,
                    totalCount = totalCount,
                    baitStats = viewModel.repository.getTopBaitsForFishToday(fishName, startOfDayTimestamp)
                )
            }
        }
    }
}

@Composable
private fun BaitStatsCard(
    fishName: String,
    bait: String,
    count: Int,
    totalFishCount: Int
) {

    val context = LocalContext.current
    val fish = FishingData.getAllFish().find { it.name == fishName }


    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🎣", fontSize = 20.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fish?.getLocalizedName(context) ?: fishName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Appât : $bait",
                    fontSize = 14.sp,
                    color = Color(0xFF10B981)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "×$count",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                if (totalFishCount > count) {
                    Text(
                        text = "/$totalFishCount total",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun FishCountCard(
    fishName: String,
    totalCount: Int,
    baitStats: List<Pair<String, Int>>
) {

    val context = LocalContext.current
    val fish = FishingData.getAllFish().find { it.name == fishName }


    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // En-tête du poisson
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🐟", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = fish?.getLocalizedName(context) ?: fishName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "$totalCount capture${if (totalCount > 1) "s" else ""}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
            }

            // Top des appâts pour ce poisson
            if (baitStats.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "🎯 Top appâts :",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF10B981)
                )

                Spacer(modifier = Modifier.height(8.dp))

                baitStats.take(3).forEachIndexed { index, (bait, count) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val medal = when (index) {
                                0 -> "🥇"
                                1 -> "🥈"
                                2 -> "🥉"
                                else -> "🏅"
                            }
                            Text(text = medal, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = bait,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }

                        Text(
                            text = "×$count",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                    }

                    if (index < baitStats.size - 1 && index < 2) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun RankingCard(
    rank: Int,
    title: String,
    subtitle: String,
    icon: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Médaille selon le rang
            val medal = when (rank) {
                1 -> "🥇"
                2 -> "🥈"
                3 -> "🥉"
                else -> "🏅"
            }

            Text(text = medal, fontSize = 24.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Text(text = icon, fontSize = 20.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "#$rank",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
        }
    }
}

@Composable
private fun EmptyStateCard(
    icon: String,
    title: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}