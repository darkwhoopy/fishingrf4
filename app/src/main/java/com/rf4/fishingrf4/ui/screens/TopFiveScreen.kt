package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.FishingData
import com.rf4.fishingrf4.data.models.getLocalizedName
import com.rf4.fishingrf4.data.online.SpeciesCount
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import com.rf4.fishingrf4.utils.getLocalizedName
import com.rf4.fishingrf4.data.models.BaitDatabase
import com.rf4.fishingrf4.data.models.BaitDatabase.getLocalizedName
import com.rf4.fishingrf4.utils.LanguageManager

// Énumération pour les périodes - MAINTENANT TRADUITE
enum class TopPeriod(val emoji: String, val daysBack: Int, val stringResId: Int) {
    DAY("📅", 1, R.string.period_day),
    WEEK("📊", 7, R.string.period_week),
    MONTH("📈", 30, R.string.period_month)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopFiveScreen(
    viewModel: FishingViewModel,
    onBack: () -> Unit
) {
    var selectedPeriod by remember { mutableStateOf(TopPeriod.DAY) }
    var selectedTab by remember { mutableStateOf(0) }

    // États pour les données
    var communityPlayers by remember { mutableStateOf<List<Pair<String, Long>>>(emptyList()) }
    var communityLakes by remember { mutableStateOf<List<Pair<String, Long>>>(emptyList()) }
    var communitySpecies by remember { mutableStateOf<List<SpeciesCount>>(emptyList()) }
    var communitySpeciesWithBaits by remember { mutableStateOf<Map<String, Pair<Long, List<Pair<String, Long>>>>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(false) }

    // Fonction pour charger les données selon la période
    fun loadDataForPeriod() {
        isLoading = true
        val timestampBack = System.currentTimeMillis() - (selectedPeriod.daysBack * 24 * 60 * 60 * 1000L)

        when (selectedTab) {
            0 -> { // Joueurs
                viewModel.fetchTop5PlayersOfDay(timestampBack) { players ->
                    communityPlayers = players
                    isLoading = false
                }
            }
            1 -> { // Lacs
                viewModel.fetchTop5LakesOfDay(timestampBack) { lakes ->
                    communityLakes = lakes
                    isLoading = false
                }
            }
            2 -> { // Espèces
                viewModel.fetchTop5SpeciesCountsToday(timestampBack) { species ->
                    communitySpecies = species
                    isLoading = false
                }
            }
            3 -> { // Appâts
                viewModel.fetchSpeciesWithBaitStats(timestampBack) { stats ->
                    communitySpeciesWithBaits = stats
                    isLoading = false
                }
            }
        }
    }

    // Charger au changement de période ou d'onglet
    LaunchedEffect(selectedPeriod, selectedTab) {
        loadDataForPeriod()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // En-tête avec bouton retour
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.top5_title),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.top5_subtitle),
                    fontSize = 14.sp,
                    color = Color(0xFFE2E8F0)
                )
            }
        }

        // Sélecteur de période plus compact
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "📊",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            TopPeriod.entries.forEach { period ->
                FilterChip(
                    onClick = { selectedPeriod = period },
                    label = {
                        Text(stringResource(period.stringResId))
                    },
                    selected = selectedPeriod == period,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF3B82F6),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFF374151),
                        labelColor = Color.Gray
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Onglets améliorés
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF1E3A5F),
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth(),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color(0xFF3B82F6)
                )
            }
        ) {
            listOf(
                "🏆" to stringResource(R.string.top5_players),
                "🏞️" to stringResource(R.string.top5_lakes),
                "🐟" to stringResource(R.string.top5_species),
                "🎣" to stringResource(R.string.top5_baits)
            ).forEachIndexed { index, (emoji, text) ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = "$emoji $text",
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenu selon l'onglet sélectionné
        Box(modifier = Modifier.weight(1f)) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF3B82F6))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.loading_data),
                            color = Color.Gray
                        )
                    }
                }
            } else {
                when (selectedTab) {
                    0 -> PlayersRankingContent(communityPlayers)
                    1 -> LakesRankingContent(communityLakes)
                    2 -> SpeciesRankingContent(communitySpecies)
                    3 -> BaitsRankingContent(communitySpeciesWithBaits)
                }
            }
        }
    }
}

@Composable
private fun PlayersRankingContent(players: List<Pair<String, Long>>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (players.isEmpty()) {
            item {
                EmptyStateCard(
                    title = stringResource(R.string.top5_no_players_title),
                    message = stringResource(R.string.top5_no_players_message),
                    icon = "🏆"
                )
            }
        } else {
            items(players.take(5)) { (playerName, catches) ->
                val rank = players.indexOf(playerName to catches) + 1
                RankingCard(
                    rank = rank,
                    title = playerName,
                    subtitle = stringResource(R.string.top5_catches_format, catches),
                    icon = "🎣"
                )
            }
        }
    }
}

@Composable
private fun LakesRankingContent(lakes: List<Pair<String, Long>>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (lakes.isEmpty()) {
            item {
                EmptyStateCard(
                    title = stringResource(R.string.top5_no_lakes_title),
                    message = stringResource(R.string.top5_no_lakes_message),
                    icon = "🏞️"
                )
            }
        } else {
            items(lakes.take(5)) { (lakeName, visits) ->
                val rank = lakes.indexOf(lakeName to visits) + 1

                // Chercher par ID ou par nom
                val lake = FishingData.lakes.find {
                    it.name == lakeName || it.id == lakeName
                }
                val localizedName = lake?.getLocalizedName() ?: lakeName

                RankingCard(
                    rank = rank,
                    title = localizedName,
                    subtitle = stringResource(R.string.top5_visits_format, visits),
                    icon = "🌊"
                )
            }
        }
    }
}

@Composable
private fun SpeciesRankingContent(species: List<SpeciesCount>) {
    val context = LocalContext.current

    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (species.isEmpty()) {
            item {
                EmptyStateCard(
                    title = stringResource(R.string.top5_no_species_title),
                    message = stringResource(R.string.top5_no_species_message),
                    icon = "🐟"
                )
            }
        } else {
            items(species.take(5)) { speciesCount ->
                val rank = species.indexOf(speciesCount) + 1

                // Recherche optimisée et logique
                val fish = FishingData.getAllFish().find { fishItem ->
                    // Comparaison directe d'abord (plus rapide)
                    fishItem.name == speciesCount.species ||
                            fishItem.nameEn == speciesCount.species ||
                            // Puis comparaison insensible à la casse
                            fishItem.name.equals(speciesCount.species, ignoreCase = true) ||
                            fishItem.nameEn.equals(speciesCount.species, ignoreCase = true)
                }

                // Gestion des cas null plus robuste
                val displayName = when {
                    fish != null -> {
                        // Utiliser la localisation si le poisson est trouvé
                        try {
                            fish.getLocalizedName(context)
                        } catch (e: Exception) {
                            // Fallback si getLocalizedName échoue
                            fish.name
                        }
                    }
                    else -> {
                        // Si le poisson n'est pas trouvé, utiliser le nom original
                        speciesCount.species
                    }
                }

                RankingCard(
                    rank = rank,
                    title = displayName,
                    subtitle = stringResource(R.string.top5_captures_format, speciesCount.count),
                    icon = "🐟"
                )
            }
        }
    }
}

@Composable
private fun BaitsRankingContent(baitsStats: Map<String, Pair<Long, List<Pair<String, Long>>>>) {
    val context = LocalContext.current

    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (baitsStats.isEmpty()) {
            item {
                EmptyStateCard(
                    title = stringResource(R.string.top5_no_baits_title),
                    message = stringResource(R.string.top5_no_baits_message),
                    icon = "🎣"
                )
            }
        } else {
            items(baitsStats.entries.toList()) { (fishName, baitData) ->
                CommunityBaitCard(
                    fishName = fishName,
                    totalCaptures = baitData.first,
                    baits = baitData.second,
                    context = context
                )
            }
        }
    }
}

@Composable
private fun EmptyStateCard(
    title: String,
    message: String,
    icon: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = message,
                fontSize = 13.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
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

            // Badge de rang
            Surface(
                color = when (rank) {
                    1 -> Color(0xFFFFD700)
                    2 -> Color(0xFFC0C0C0)
                    3 -> Color(0xFFCD7F32)
                    else -> Color(0xFF3B82F6)
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.top5_rank_format, rank),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun CommunityBaitCard(
    fishName: String,
    totalCaptures: Long,
    baits: List<Pair<String, Long>>,
    context: android.content.Context
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
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

                    // Recherche et traduction du nom du poisson
                    val fish = FishingData.getAllFish().find {
                        it.name == fishName || it.nameEn == fishName
                    }
                    val displayName = fish?.getLocalizedName(context) ?: fishName

                    Text(
                        text = displayName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    color = Color(0xFF10B981).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.top5_total_captures_format, totalCaptures),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Top des appâts pour ce poisson
            if (baits.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.top5_top_baits_label),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF10B981)
                )

                Spacer(modifier = Modifier.height(8.dp))

                baits.take(3).forEachIndexed { index, (bait, count) ->
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
                            Text(text = medal, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = getLocalizedBaitName(bait, context),
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }

                        Text(
                            text = stringResource(R.string.top5_bait_count_format, count),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                    }

                    if (index < baits.size - 1 && index < 2) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.top5_no_baits_for_species),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    style = androidx.compose.ui.text.TextStyle(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )
            }
        }
    }/**
     * Fonction pour obtenir le nom localisé d'un appât en utilisant BaitDatabase
     */

    }
private fun getLocalizedBaitName(baitName: String, context: android.content.Context): String {
    val baitInfo = BaitDatabase.allBaits.find {
        it.name == baitName || it.englishName == baitName
    }

    return if (baitInfo != null) {
        // Logique de traduction directement ici
        when (LanguageManager.getCurrentLanguage(context)) {
            LanguageManager.Language.ENGLISH -> baitInfo.englishName
            LanguageManager.Language.FRENCH -> baitInfo.name
        }
    } else {
        baitName
    }
}