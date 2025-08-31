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
import com.rf4.fishingrf4.data.models.Fish
import com.rf4.fishingrf4.data.online.SpeciesCount
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
@Composable
fun CommunityScreen(
    viewModel: FishingViewModel,
    onBack: () -> Unit
) {
    var currentTab by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(16.dp))
            Text("👥 Communauté", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        // Deux bulles principales
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Bulle Appâts
            CommunityFeatureCard(
                title = "🎣 Appâts",
                subtitle = "Top communautaire",
                color = Color(0xFFE11D48),
                isActive = currentTab == 0,
                modifier = Modifier.weight(1f)
            ) {
                currentTab = 0
            }

            // Bulle Lacs (pour plus tard)
            CommunityFeatureCard(
                title = "🏞️ Lacs",
                subtitle = "Spots préférés",
                color = Color(0xFF0EA5E9),
                isActive = currentTab == 1,
                modifier = Modifier.weight(1f)
            ) {
                currentTab = 1
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenu selon l'onglet sélectionné
        when (currentTab) {
            0 -> CommunityBaitsTab(viewModel = viewModel)
            1 -> CommunityLakesTab() // À compléter plus tard
        }
    }
}
@Composable
fun CommunityFeatureCard(
    title: String,
    subtitle: String,
    color: Color,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) color else color.copy(alpha = 0.3f)
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
// ✅ Onglet des appâts communautaires
@Composable
fun CommunityBaitsTab(viewModel: FishingViewModel) {
    var communityBaitsByFish by remember { mutableStateOf<Map<Fish, List<Pair<String, Long>>>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(true) }

    // Charger les données au démarrage
    LaunchedEffect(Unit) {
        val allFish = viewModel.getAllFish()
        val baitData = mutableMapOf<Fish, List<Pair<String, Long>>>()

        // Pour chaque poisson, récupérer le top 3 des appâts du mois
        allFish.forEach { fish ->
            viewModel.fetchTopCommunityBaitsThisMonth(fish.id) { topBaits ->
                baitData[fish] = topBaits.take(3)

                // Trier les poissons par nombre total de votes reçus
                val sortedBaitData = baitData.toList()
                    .sortedByDescending { (_, baits) ->
                        baits.sumOf { it.second } // Somme des votes pour ce poisson
                    }
                    .toMap()

                communityBaitsByFish = sortedBaitData
            }
        }
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFE11D48))
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text(
                    "Poissons classés par popularité (nombre total de votes)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Les poissons avec le plus de votes d'appâts apparaissent en premier",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(communityBaitsByFish.entries.toList()) { (fish, topBaits) ->
                FishCommunityBaitCard(fish = fish, topBaits = topBaits)
            }

            if (communityBaitsByFish.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Aucun vote communautaire pour le moment", color = Color.White)
                            Text("Soyez le premier à voter pour un appât !", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun FishCommunityBaitCard(fish: Fish, topBaits: List<Pair<String, Long>>) {
    val totalVotes = topBaits.sumOf { it.second }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // En-tête du poisson avec badge de popularité
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = fish.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                // Badge du total des votes avec couleurs
                if (totalVotes > 0) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                totalVotes >= 10 -> Color(0xFFFFD700) // Or
                                totalVotes >= 5 -> Color(0xFFC0C0C0)  // Argent
                                else -> Color(0xFFCD7F32)             // Bronze
                            }
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "$totalVotes votes",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                RarityBadge(text = fish.rarity.displayName, color = Color(fish.rarity.colorValue))
            }

            // Top 3 des appâts
            if (topBaits.isNotEmpty()) {
                topBaits.forEachIndexed { index, (baitName, voteCount) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val medal = when (index) {
                                0 -> "🥇"
                                1 -> "🥈"
                                2 -> "🥉"
                                else -> "${index + 1}."
                            }
                            Text(text = medal, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = baitName, color = Color.White, fontSize = 14.sp)
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE11D48)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "$voteCount votes",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "Aucun vote pour ce poisson",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// ✅ Onglet Lacs (à compléter plus tard)
@Composable
fun CommunityLakesTab() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Construction,
                contentDescription = null,
                tint = Color(0xFF0EA5E9),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "🚧 En construction",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Les spots préférés de la communauté arriveront bientôt !",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
