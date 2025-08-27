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

    val uiState by viewModel.uiState.collectAsState()
    val saveCompleted by viewModel.saveCompleted.collectAsState()

    var favoritesOnly by remember { mutableStateOf(false) }
    var lakeToInteract by remember { mutableStateOf<Lake?>(null) }
    var fishToInteract by remember { mutableStateOf<Fish?>(null) }
    var positionToInteract by remember { mutableStateOf("") }
    var previousScreen by remember { mutableStateOf(Screen.LAKE_SELECTION) }


    val displayedLakes = if (favoritesOnly) {
        uiState.availableLakes.filter { uiState.favoriteLakeIds.contains(it.id) }
    } else {
        uiState.availableLakes
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E3A8A))
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        if (saveCompleted) {
            SaveConfirmationDialog(onDismiss = {
                viewModel.onDialogDismissed()
                viewModel.navigateTo(Screen.LAKE_SELECTION)
            })
        }

        val mainScreensWithHeader = listOf(
            Screen.LAKE_SELECTION,
            Screen.FISH_SEARCH,
            Screen.PLAYER_PROFILE,
            Screen.JOURNAL,
            Screen.SETTINGS
        )

        if (uiState.currentScreen in mainScreensWithHeader) {
            AppHeader(
                playerStats = uiState.playerStats,
                onNavigate = viewModel::navigateTo,
                onSettingsClick = { viewModel.navigateTo(Screen.SETTINGS) },
                onLevelChange = viewModel::setPlayerLevel
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        when (uiState.currentScreen) {
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
                    favoriteLakeIds = uiState.favoriteLakeIds,
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
                    // ✅ On passe le viewModel à l'écran
                    viewModel = viewModel,
                    onFishSelected = { fish -> viewModel.catchFish(fish, lakeToInteract!!, positionToInteract) },
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
                    playerStats = uiState.playerStats,
                    onDeleteEntry = viewModel::removeEntry,
                    onBack = { viewModel.navigateTo(Screen.LAKE_SELECTION) }
                )
            }
            Screen.PLAYER_PROFILE -> {
                PlayerProfileScreen(
                    playerStats = uiState.playerStats,
                    fishingEntries = uiState.fishingEntries,
                    onBack = { viewModel.navigateTo(Screen.LAKE_SELECTION) }
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
        }
    }
}

@Composable
fun SaveConfirmationDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmation") },
        text = { Text("Les modifications ont été sauvegardées.") },
        confirmButton = { Button(onClick = onDismiss) { Text("OK") } }
    )
}