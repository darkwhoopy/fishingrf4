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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.models.*
import com.rf4.fishingrf4.data.repository.CommunityRepository
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ✅ MISE À JOUR : Écran principal de la communauté avec traduction complète
 * Conserve votre système existant d'appâts et ajoute les signalements/suggestions
 */
@Composable
fun CommunityScreen(
    viewModel: FishingViewModel,
    onBack: () -> Unit
) {
    // ✅ MODIFICATION : Ajout d'onglets pour les nouvelles fonctionnalités
    var currentTab by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val communityRepo = remember { CommunityRepository() }

    // ✅ NOUVEAU : États pour les nouvelles données
    var myBugReports by remember { mutableStateOf<List<BugReport>>(emptyList()) }
    var fishSuggestions by remember { mutableStateOf<List<FishSuggestion>>(emptyList()) }
    var isLoadingNewFeatures by remember { mutableStateOf(true) }
    var userVotes by remember { mutableStateOf<Set<String>>(emptySet()) }

    // ✅ NOUVEAU : Charger les nouvelles données
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                myBugReports = communityRepo.getMyBugReports()
                fishSuggestions = communityRepo.getAllFishSuggestions()

                // ✅ CORRECTION : Vérification de l'utilisateur connecté
                val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    val votes = communityRepo.getUserVotes(currentUser.uid)
                    userVotes = votes.map { it.suggestionId }.toSet()
                }
            } catch (e: Exception) {
                // ✅ AJOUT : Log de l'erreur pour déboguer
                android.util.Log.e("CommunityScreen", "Erreur chargement données: ${e.message}", e)
            } finally {
                isLoadingNewFeatures = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A)) // Même couleur que votre interface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // ✅ HEADER TRADUIT
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                BackButton(onClick = onBack)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = stringResource(R.string.community_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.community_subtitle),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // ✅ BOUTONS D'ACTIONS RAPIDES TRADUITS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Bouton signaler un bug
                Button(
                    onClick = {
                        viewModel.navigateTo(com.rf4.fishingrf4.ui.navigation.Screen.BUG_REPORT)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.BugReport,
                        contentDescription = stringResource(R.string.community_report_bug),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.bug_short_label), fontSize = 11.sp)
                }

                // Bouton suggérer un poisson
                Button(
                    onClick = {
                        viewModel.navigateTo(com.rf4.fishingrf4.ui.navigation.Screen.FISH_SUGGESTION)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.community_suggest_fish),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.fish_short_label), fontSize = 11.sp)
                }
            }

            // ✅ ONGLETS TRADUITS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // ✅ Onglet Appâts traduit
                CommunityFeatureCard(
                    title = stringResource(R.string.community_tab_baits_title),
                    subtitle = stringResource(R.string.community_tab_baits_subtitle),
                    color = Color(0xFFE11D48),
                    isActive = currentTab == 0,
                    modifier = Modifier.weight(1f)
                ) {
                    currentTab = 0
                }

                // ✅ Onglet Lacs traduit
                CommunityFeatureCard(
                    title = stringResource(R.string.community_tab_lakes_title),
                    subtitle = stringResource(R.string.community_tab_lakes_subtitle),
                    color = Color(0xFF0EA5E9),
                    isActive = currentTab == 1,
                    modifier = Modifier.weight(1f)
                ) {
                    currentTab = 1
                }

                // ✅ Onglet Bugs traduit
                CommunityFeatureCard(
                    title = stringResource(R.string.community_tab_bugs_title),
                    subtitle = stringResource(R.string.community_tab_bugs_subtitle),
                    color = Color(0xFFEF4444),
                    isActive = currentTab == 2,
                    modifier = Modifier.weight(1f)
                ) {
                    currentTab = 2
                }

                // ✅ Onglet Suggestions traduit
                CommunityFeatureCard(
                    title = stringResource(R.string.community_tab_suggestions_title),
                    subtitle = stringResource(R.string.community_tab_suggestions_subtitle),
                    color = Color(0xFF10B981),
                    isActive = currentTab == 3,
                    modifier = Modifier.weight(1f)
                ) {
                    currentTab = 3
                }
            }

            // ✅ CONTENU SELON L'ONGLET
            when (currentTab) {
                0 -> CommunityBaitsTab(viewModel = viewModel)
                1 -> CommunityLakesTab()
                2 -> { // Onglet signalements de bugs
                    if (isLoadingNewFeatures) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFFEF4444))
                        }
                    } else {
                        BugReportsTab(
                            bugReports = myBugReports,
                            onRefresh = {
                                coroutineScope.launch {
                                    try {
                                        myBugReports = communityRepo.getMyBugReports()
                                    } catch (e: Exception) {
                                        // Gérer silencieusement
                                    }
                                }
                            }
                        )
                    }
                }
                3 -> { // Onglet suggestions de poissons
                    if (isLoadingNewFeatures) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF10B981))
                        }
                    } else {
                        FishSuggestionsTab(
                            suggestions = fishSuggestions,
                            userVotes = userVotes,
                            onVote = { suggestionId, voteType ->
                                coroutineScope.launch {
                                    try {
                                        communityRepo.voteForFishSuggestion(suggestionId, voteType)
                                        // Rafraîchir les données
                                        fishSuggestions = communityRepo.getAllFishSuggestions()
                                        val votes = communityRepo.getUserVotes(
                                            com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                        )
                                        userVotes = votes.map { it.suggestionId }.toSet()
                                    } catch (e: Exception) {
                                        // Gérer silencieusement
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// ✅ FONCTION COMMUNITYFEATURECARD TRADUITE
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
        modifier = modifier.height(70.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) color else color.copy(alpha = 0.3f)
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 1
            )
        }
    }
}

// ✅ ONGLET DES SIGNALEMENTS DE BUGS TRADUIT
@Composable
fun BugReportsTab(
    bugReports: List<BugReport>,
    onRefresh: () -> Unit
) {
    Column {
        // Header avec bouton rafraîchir
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.community_my_bug_reports),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            IconButton(onClick = onRefresh) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.action_refresh),
                    tint = Color.Gray
                )
            }
        }

        if (bugReports.isEmpty()) {
            // État vide traduit
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.BugReport,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.bug_reports_empty_title),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.bug_reports_empty_message),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Liste des signalements
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bugReports) { bugReport ->
                    BugReportCard(bugReport = bugReport)
                }
            }
        }
    }
}

// ✅ ONGLET DES SUGGESTIONS DE POISSONS TRADUIT
@Composable
fun FishSuggestionsTab(
    suggestions: List<FishSuggestion>,
    userVotes: Set<String>,
    onVote: (String, VoteType) -> Unit
) {
    Column {
        // Header traduit
        Text(
            text = stringResource(R.string.community_fish_suggestions_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (suggestions.isEmpty()) {
            // État vide traduit
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Pets,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.fish_suggestions_empty_title),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.fish_suggestions_empty_message),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Liste des suggestions
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(suggestions) { suggestion ->
                    FishSuggestionCard(
                        suggestion = suggestion,
                        hasVoted = suggestion.id in userVotes,
                        onVote = onVote
                    )
                }
            }
        }
    }
}

// ✅ CARTE D'AFFICHAGE D'UN SIGNALEMENT DE BUG TRADUITE
@Composable
fun BugReportCard(bugReport: BugReport) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header avec type et statut
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = bugReport.bugType.displayName,
                        fontSize = 12.sp,
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(
                        status = bugReport.status.displayName,
                        color = Color(bugReport.status.colorValue)
                    )
                }
                Text(
                    text = formatDate(bugReport.createdAt),
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Titre
            Text(
                text = bugReport.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Description courte
            Text(
                text = bugReport.description,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Notes admin si présentes
            if (bugReport.adminNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0x33F59E0B)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            contentDescription = stringResource(R.string.admin_note_icon_desc),
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = bugReport.adminNotes,
                            fontSize = 11.sp,
                            color = Color(0xFFF59E0B)
                        )
                    }
                }
            }
        }
    }
}

// ✅ CARTE D'AFFICHAGE D'UNE SUGGESTION DE POISSON TRADUITE
@Composable
fun FishSuggestionCard(
    suggestion: FishSuggestion,
    hasVoted: Boolean,
    onVote: (String, VoteType) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header avec rareté et statut
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadge(
                        status = suggestion.rarity.displayName,
                        color = Color(suggestion.rarity.colorValue)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(
                        status = suggestion.status.displayName,
                        color = Color(suggestion.status.colorValue)
                    )
                }
                Text(
                    text = formatDate(suggestion.createdAt),
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nom et nom scientifique
            Text(
                text = suggestion.fishName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            if (suggestion.scientificName.isNotBlank()) {
                Text(
                    text = suggestion.scientificName,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description courte
            Text(
                text = suggestion.description,
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Informations supplémentaires traduites
            if (suggestion.suggestedLakes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        R.string.fish_suggestion_lakes_format,
                        suggestion.suggestedLakes.take(3).joinToString(", ") +
                                if (suggestion.suggestedLakes.size > 3) "..." else ""
                    ),
                    fontSize = 11.sp,
                    color = Color(0xFF0EA5E9)
                )
            }

            if (suggestion.preferredBaits.isNotEmpty()) {
                Text(
                    text = stringResource(
                        R.string.fish_suggestion_baits_format,
                        suggestion.preferredBaits.take(3).joinToString(", ") +
                                if (suggestion.preferredBaits.size > 3) "..." else ""
                    ),
                    fontSize = 11.sp,
                    color = Color(0xFFE11D48)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer avec votes et boutons traduits
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Informations sur l'auteur et votes
                Column {
                    Text(
                        text = stringResource(R.string.community_by_author, suggestion.userName),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.ThumbUp,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = stringResource(R.string.community_votes_count, suggestion.votes),
                            fontSize = 12.sp,
                            color = Color(0xFF10B981),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                // Boutons de vote traduits
                if (!hasVoted && suggestion.status == SuggestionStatus.PENDING) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = { onVote(suggestion.id, VoteType.UPVOTE) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.ThumbUp,
                                contentDescription = stringResource(R.string.community_vote_up),
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        IconButton(
                            onClick = { onVote(suggestion.id, VoteType.DOWNVOTE) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.ThumbDown,
                                contentDescription = stringResource(R.string.community_vote_down),
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else if (hasVoted) {
                    Text(
                        text = stringResource(R.string.community_voted),
                        fontSize = 11.sp,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ✅ BADGE DE STATUT RÉUTILISABLE
@Composable
fun StatusBadge(
    status: String,
    color: Color
) {
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = status,
            fontSize = 10.sp,
            color = color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

// ✅ FORMATEUR DE DATE
fun formatDate(timestamp: Long): String {
    val format = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    return format.format(Date(timestamp))
}

// ✅ ONGLET DES APPÂTS COMMUNAUTAIRES TRADUIT
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
                        baits.sumOf { it.second }
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
                    text = stringResource(R.string.community_baits_ranking_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.community_baits_ranking_subtitle),
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
                            Text(
                                text = stringResource(R.string.community_baits_empty_title),
                                color = Color.White
                            )
                            Text(
                                text = stringResource(R.string.community_baits_empty_message),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
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
                            text = stringResource(R.string.community_votes_count, totalVotes.toInt()),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                RarityBadge(
                    text = fish.rarity.displayName,
                    color = Color(fish.rarity.colorValue)
                )
            }

            // Top 3 des appâts traduits
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
                                text = stringResource(R.string.community_votes_count, voteCount.toInt()),
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
                    text = stringResource(R.string.community_no_votes_fish),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// ✅ BADGE DE RARETÉ RÉUTILISABLE (suppression du doublon)


// ✅ ONGLET LACS TRADUIT
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
                text = stringResource(R.string.community_lakes_construction_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.community_lakes_construction_message),
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}