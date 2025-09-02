package com.rf4.fishingrf4.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rf4.fishingrf4.data.models.*
import com.rf4.fishingrf4.ui.components.AppHeader
import com.rf4.fishingrf4.ui.navigation.Screen
import com.rf4.fishingrf4.ui.screens.*
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rf4.fishingrf4.data.online.SpeciesCount
import com.rf4.fishingrf4.ui.screens.TopFiveScreen
// Vue modèle pour la création de FishingViewModel
class FishingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FishingViewModel::class.java)) {
            return FishingViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
@Composable
fun FishingRF4App() {
    val context = LocalContext.current
    val viewModel: FishingViewModel = viewModel(factory = FishingViewModelFactory(context))
// État de l'utilisateur Firebase
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

// Écouteur pour l'état d'authentification
    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        FirebaseAuth.getInstance().addAuthStateListener(listener)
        onDispose { FirebaseAuth.getInstance().removeAuthStateListener(listener) }
    }

// Collecte de l'état de l'UI et des données
    val uiState by viewModel.uiState.collectAsState()
    val saveCompleted by viewModel.saveCompleted.collectAsState()

// Calcul du début du jour de jeu (timestamp réel)
    val startOfDay by viewModel.startOfCurrentGameDayTimestamp.collectAsState()

// États pour les données de Top 5
    var topSpecies by rememberSaveable { mutableStateOf<List<SpeciesCount>>(emptyList()) }
    var topPlayers by rememberSaveable { mutableStateOf<List<Pair<String, Long>>>(emptyList()) }
    var topLakes by rememberSaveable { mutableStateOf<List<Pair<String, Long>>>(emptyList()) }
    var communityTop by rememberSaveable { mutableStateOf<List<Pair<String, Long>>>(emptyList()) }

// Autres états pour l'interaction avec les écrans
    var favoritesOnly by remember { mutableStateOf(false) }
    var lakeToInteract by remember { mutableStateOf<Lake?>(null) }
    var fishToInteract by remember { mutableStateOf<Fish?>(null) }
    var positionToInteract by remember { mutableStateOf("") }
    var previousScreen by remember { mutableStateOf(Screen.LAKE_SELECTION) }

// Affichage des lacs filtrés selon les favoris
    val displayedLakes = if (favoritesOnly) {
        uiState.availableLakes.filter { uiState.favoriteLakeIds.contains(it.id) }
    } else {
        uiState.availableLakes
    }

// Récupération des données (Top 5) et votes communautaires
    LaunchedEffect(startOfDay) {
        viewModel.fetchTop5SpeciesCountsToday { topSpecies = it }
        viewModel.fetchTop5PlayersOfDay(startOfDay) { topPlayers = it }
        viewModel.fetchTop5LakesOfDay(startOfDay) { topLakes = it }
        // Récupère les votes communautaires

    }

// Structure principale de la colonne avec l'UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E3A8A))
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // Affichage de la confirmation de sauvegarde
        if (saveCompleted) {
            SaveConfirmationDialog(onDismiss = {
                viewModel.onDialogDismissed()
                viewModel.navigateTo(Screen.LAKE_SELECTION)
            })
        }

        // Liste des écrans principaux avec entête
        val mainScreensWithHeader = listOf(
            Screen.LAKE_SELECTION,
            Screen.FISH_SEARCH,
            Screen.PLAYER_PROFILE,
            Screen.JOURNAL,
            Screen.SETTINGS
        )

        // Affichage de l'en-tête si l'écran appartient aux écrans avec en-tête
        if (uiState.currentScreen in mainScreensWithHeader) {
            // ✅ CORRECTION ICI : AppHeader avec les bons paramètres
            AppHeader(
                playerStats = uiState.playerStats,
                onNavigate = viewModel::navigateTo,
                onSettingsClick = { viewModel.navigateTo(Screen.SETTINGS) },
                onLevelChange = viewModel::setPlayerLevel
            )

            // Badge de statut de connexion
            Spacer(Modifier.height(8.dp))

            // Espacement avant le contenu principal
            Spacer(Modifier.height(12.dp))
        }

        // Gestion des écrans spécifiques
        when (uiState.currentScreen) {
            Screen.TOP_FIVE -> {
                TopFiveScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(Screen.LAKE_SELECTION) }
                )
            }

            Screen.LAKE_SELECTION -> {
                LakeSelectionScreen(
                    lakes = displayedLakes,
                    playerLevel = uiState.playerStats.level,
                    onLakeSelected = { lake ->
                        lakeToInteract = lake
                        viewModel.navigateTo(Screen.POSITION_SELECTION)
                    },
                    onLakeEdit = { lake ->
                        lakeToInteract = lake
                        viewModel.navigateTo(Screen.LAKE_EDIT)
                    },
                    favoriteLakeIds = uiState.favoriteLakeIds.toSet(),
                    onToggleFavorite = viewModel::toggleFavoriteLake,
                    isFavoritesFilterActive = favoritesOnly,
                    onToggleFavoritesFilter = { favoritesOnly = !favoritesOnly }
                )
            }

            Screen.POSITION_SELECTION -> {
                PositionSelectionScreen(
                    lake = lakeToInteract!!,
                    viewModel = viewModel,
                    onPositionSelected = { position ->
                        positionToInteract = position
                        viewModel.navigateTo(Screen.FISH_SELECTION)
                    },
                    onBack = { viewModel.navigateTo(Screen.LAKE_SELECTION) }
                )
            }

            Screen.FISH_SELECTION -> {
                FishSelectionScreen(
                    lake = lakeToInteract!!,
                    position = positionToInteract,
                    fishingEntries = uiState.fishingEntries,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(Screen.POSITION_SELECTION) },
                    onViewJournal = { viewModel.navigateTo(Screen.JOURNAL) },
                    onFishDetail = { fish ->
                        fishToInteract = fish
                        previousScreen = uiState.currentScreen
                        viewModel.navigateTo(Screen.FISH_DETAIL)
                    }
                )
            }

            Screen.FISH_SEARCH -> {
                FishSearchScreen(
                    allLakes = uiState.allLakes,
                    fishingEntries = uiState.fishingEntries,
                    onBack = { viewModel.navigateTo(Screen.LAKE_SELECTION) },
                    onFishDetail = { fish, lake ->
                        fishToInteract = fish
                        lakeToInteract = lake
                        previousScreen = uiState.currentScreen
                        viewModel.navigateTo(Screen.FISH_DETAIL)
                    }
                )
            }

            Screen.LAKE_EDIT -> {
                LakeEditScreen(
                    lake = lakeToInteract!!,
                    viewModel = viewModel,
                    onNavigateBack = { viewModel.navigateTo(Screen.LAKE_SELECTION) },
                    onSaveLake = viewModel::updateLake
                )
            }

            Screen.FISH_DETAIL -> {
                FishInfoScreen(
                    fish = fishToInteract!!,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(previousScreen) }
                )
            }

            Screen.JOURNAL -> {
                JournalScreen(
                    entries = uiState.fishingEntries,
                    onDeleteEntry = viewModel::removeEntry,
                    onBack = { viewModel.navigateTo(Screen.LAKE_SELECTION) }
                )
            }
            Screen.COMMUNITY -> {
                CommunityScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(Screen.LAKE_SELECTION) }
                )
            }

            Screen.BUG_REPORT -> {
                BugReportScreen(
                    onBack = { viewModel.navigateTo(Screen.COMMUNITY) }
                )
            }

            Screen.FISH_SUGGESTION -> {
                FishSuggestionScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(Screen.COMMUNITY) }
                )
            }
            Screen.PLAYER_PROFILE -> {
                PlayerProfileScreen(
                    playerStats = uiState.playerStats,
                    fishingEntries = uiState.fishingEntries,
                    onBack = { viewModel.navigateTo(Screen.LAKE_SELECTION) },
                    onOpenTop5 = { viewModel.navigateTo(Screen.TOP_FIVE) }
                )
            }

            Screen.SETTINGS -> {
                SettingsScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(Screen.LAKE_SELECTION) },
                    onResetData = { selectedOptions ->
                        viewModel.resetData(selectedOptions)
                    }
                )
            }
            Screen.COMMUNITY -> {
                CommunityScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(Screen.LAKE_SELECTION) }
                )
            }
        }
    }
}
// Confirmation de la sauvegarde
@Composable
fun SaveConfirmationDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmation") },
        text = { Text("Les modifications ont été sauvegardées.") },
        confirmButton = { Button(onClick = onDismiss) { Text("OK") } }
    )
}
// Badge de statut de connexion
@Composable
private fun AuthStatusBadge(user: FirebaseUser?) {
    val bg = if (user != null) Color(0xFF065F46) else Color(0xFF7C2D12)
    val text = if (user != null) "Connecté" else "Hors ligne"
    val sub = user?.email ?: ""
    Card(
        colors = CardDefaults.cardColors(containerColor = bg.copy(alpha = 0.9f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (user != null) "✅ $text" else "⛔ $text",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            if (sub.isNotBlank()) {
                Spacer(Modifier.width(8.dp))
                Text(sub, color = Color(0xFFE5E7EB))
            }
        }
    }
}