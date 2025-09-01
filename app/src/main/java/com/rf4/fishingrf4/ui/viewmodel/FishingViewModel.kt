package com.rf4.fishingrf4.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rf4.fishingrf4.data.FishingData
import com.rf4.fishingrf4.data.models.*
import com.rf4.fishingrf4.data.online.FishingOnlineRepository
import com.rf4.fishingrf4.data.online.OnlineEntry
import com.rf4.fishingrf4.data.online.SpeciesCount
import com.rf4.fishingrf4.data.repository.FishingRepository
import com.rf4.fishingrf4.data.utils.GameTimeManager
import com.rf4.fishingrf4.ui.navigation.Screen
import java.time.Duration
import java.time.LocalTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FishingViewModel(context: Context) : ViewModel() {

    private val repository = FishingRepository(context)
    private val onlineRepo = FishingOnlineRepository()

    // --- State interne ---
    private val _currentScreen = MutableStateFlow(Screen.LAKE_SELECTION)
    private val _modifiedLakes = MutableStateFlow<Map<String, Lake>>(emptyMap())
    private val _customBaits = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    private val _favoriteLakeIds = MutableStateFlow<List<String>>(emptyList())
    private val _saveCompleted = MutableStateFlow(false)
    private val _userSpots = MutableStateFlow<List<UserSpot>>(emptyList())

    // --- State exposé ---
    val saveCompleted: StateFlow<Boolean> = _saveCompleted.asStateFlow()
    val userSpots: StateFlow<List<UserSpot>> = _userSpots.asStateFlow()
    val gameTime: StateFlow<LocalTime> = GameTimeManager.gameTime

    /** Début de la journée *de jeu* en timestamp réel (ms) */
    val startOfCurrentGameDayTimestamp: StateFlow<Long> =
        gameTime
            .map { currentGameTime ->
                val gameSecondsIntoDay = currentGameTime.toSecondOfDay()
                val realMillisecondsIntoDay =
                    (gameSecondsIntoDay / (24.0 * 60.0 * 60.0)) * (60.0 * 60.0 * 1000.0)
                System.currentTimeMillis() - realMillisecondsIntoDay.toLong()
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), System.currentTimeMillis())

    val uiState: StateFlow<FishingUiState> =
        combine(
            repository.fishingEntries,
            repository.playerStats,
            _currentScreen,
            _modifiedLakes,
            _favoriteLakeIds
        ) { entries, stats, screen, modifiedLakes, favoriteIds ->
            val allLakes = getAllLakes()
            FishingUiState(
                currentScreen = screen,
                fishingEntries = entries,
                playerStats = stats,
                allLakes = allLakes,
                availableLakes = allLakes.filter { it.unlockLevel <= stats.level },
                favoriteLakeIds = favoriteIds
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FishingUiState()
        )

    init {
        loadAllDataSources()
        viewModelScope.launch { GameTimeManager.start() }
    }

    private fun loadAllDataSources() {
        repository.loadAllData()
        viewModelScope.launch {
            _modifiedLakes.value = repository.loadModifiedLakes()
            _favoriteLakeIds.value = repository.loadFavoriteLakes()
            _customBaits.value = repository.loadAllCustomBaits()
            _userSpots.value = repository.loadUserSpots()

            val offset = repository.loadTimeOffset()
            GameTimeManager.setTimeOffset(Duration.ofSeconds(offset))
        }
    }

    // ==========================================
    // GESTION DE L'HORLOGE
    // ==========================================
    fun adjustInGameTime(newTime: LocalTime) {
        viewModelScope.launch {
            val realTime = LocalTime.now()
            val realSecondsFromMidnight = realTime.toSecondOfDay().toDouble()
            val gameMinutesPerRealMinute = 24.0
            val base = (realSecondsFromMidnight / 60.0) * (gameMinutesPerRealMinute * 60.0)
            val desired = newTime.toSecondOfDay()
            val offsetInSeconds = (desired - base).toLong()

            GameTimeManager.setTimeOffset(Duration.ofSeconds(offsetInSeconds))
            repository.saveTimeOffset(offsetInSeconds)
        }
    }

    // ==========================================
    // FONCTIONS ONLINE
    // ==========================================

    fun fetchTop5SpeciesCountsToday(onResult: (List<SpeciesCount>) -> Unit) {
        viewModelScope.launch {
            val list: List<SpeciesCount> = withContext(Dispatchers.IO) {
                try {
                    onlineRepo.getTop5SpeciesCountsToday(startOfCurrentGameDayTimestamp.value)
                } catch (_: Exception) {
                    emptyList()
                }
            }
            onResult(list)
        }
    }

    fun fetchTop5PlayersOfDay(startOfDay: Long, onResult: (List<Pair<String, Long>>) -> Unit) {
        viewModelScope.launch {
            val list: List<Pair<String, Long>> = withContext(Dispatchers.IO) {
                try {
                    onlineRepo.getTop5PlayersOfDay(startOfDay)
                } catch (_: Exception) {
                    emptyList()
                }
            }
            onResult(list)
        }
    }

    fun fetchTop5LakesOfDay(startOfDay: Long, onResult: (List<Pair<String, Long>>) -> Unit) {
        viewModelScope.launch {
            val list: List<Pair<String, Long>> = withContext(Dispatchers.IO) {
                try {
                    onlineRepo.getTop5LakesOfDay(startOfDay)
                } catch (_: Exception) {
                    emptyList()
                }
            }
            onResult(list)
        }
    }

    fun fetchTop5PositionsForLakeToday(
        lakeId: String,
        onResult: (List<Pair<String, Long>>) -> Unit
    ) {
        viewModelScope.launch {
            val start = startOfCurrentGameDayTimestamp.value
            val list = withContext(Dispatchers.IO) {
                try {
                    onlineRepo.getTop5PositionsForLakeToday(lakeId, start)
                } catch (_: Exception) {
                    emptyList()
                }
            }
            onResult(list)
        }
    }

    fun fetchTopCommunityBaits(
        fishId: String,
        onResult: (List<Pair<String, Long>>) -> Unit
    ) {
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    onlineRepo.getTopCommunityBaitsToday(fishId, 5)
                } catch (_: Exception) {
                    emptyList()
                }
            }
            onResult(list)
        }
    }

    fun fetchTopCommunityBaitsThisMonth(
        fishId: String,
        onResult: (List<Pair<String, Long>>) -> Unit
    ) {
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    onlineRepo.getTopCommunityBaitsThisMonth(fishId, 3)
                } catch (_: Exception) {
                    emptyList()
                }
            }
            onResult(list)
        }
    }

    fun addCommunityBaitForFish(
        fishId: String,
        baitName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    onlineRepo.addCommunityBaitForFish(fishId, baitName)
                }
                onSuccess()
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("already voted", ignoreCase = true) == true ->
                        "Vous avez déjà voté pour cet appât sur ce poisson"
                    e.message?.contains("permission", ignoreCase = true) == true ->
                        "Vous devez être connecté pour voter"
                    else ->
                        "Erreur lors du vote : ${e.message}"
                }
                onError(errorMessage)
            }
        }
    }

    fun pushEntryOnline(
        species: String,
        weight: Double,
        spot: String,
        caughtAtMs: Long
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    onlineRepo.addEntry(species, weight, spot, caughtAtMs)
                } catch (_: Exception) {}
            }
        }
    }

    // ==========================================
    // GESTION DES CAPTURES
    // ==========================================

    fun catchFish(fish: Fish, lake: Lake, position: String) {
        val currentTime = GameTimeManager.gameTime.value
        val entry = FishingEntry(
            lake = lake,
            position = position,
            fish = fish,
            hour = currentTime.hour,
            timeOfDay = GameTimeManager.getTimeOfDay(currentTime)
        )
        viewModelScope.launch {
            repository.addFishingEntry(entry)
            pushEntryOnline(
                species = fish.name,
                weight = entry.weight ?: 0.0,
                spot = lake.name,
                caughtAtMs = System.currentTimeMillis()
            )
        }
    }

    fun removeEntry(entryId: String) {
        viewModelScope.launch {
            repository.removeFishingEntry(entryId)
        }
    }

    // ==========================================
    // GESTION DES DONNÉES (LACS ET POISSONS)
    // ==========================================

    /**
     * ✅ CORRIGÉ : Retourne tous les lacs (original + modifications)
     */
    fun getAllLakes(): List<Lake> {
        val originalLakes = FishingData.lakes // ✅ Accès direct à la liste des lacs
        val modifiedLakes = _modifiedLakes.value
        return originalLakes.map { originalLake ->
            modifiedLakes[originalLake.id] ?: originalLake
        }
    }

    /**
     * ✅ CORRIGÉ : Retourne tous les poissons disponibles
     */
    fun getAllAvailableFish(): List<Fish> {
        return FishingData.getAllFish() // ✅ Utilise la fonction FishingData
            .distinctBy { it.name }
            .sortedBy { it.name }
    }

    /**
     * ✅ NOUVEAU : Alias pour compatibilité avec les écrans existants
     */
    fun getAllFish(): List<Fish> {
        return getAllAvailableFish()
    }

    /**
     * ✅ CORRIGÉ : Retourne tous les appâts du jeu
     */
    fun getAllGameBaits(): List<String> {
        return getAllLakes()
            .flatMap { it.availableFish }
            .flatMap { it.preferredBait }
            .distinct()
            .sorted()
    }

    /**
     * ✅ CORRIGÉ : Trouve les lacs où un poisson peut être pêché
     */
    fun getLakesForFish(fish: Fish): List<Lake> {
        return getAllLakes().filter { lake ->
            lake.availableFish.any { it.id == fish.id }
        }
    }

    // ==========================================
    // STATISTIQUES DES CAPTURES
    // ==========================================

    fun getCaptureStatsForFishByLake(fishId: String): Map<String, Int> {
        return repository.fishingEntries.value
            .filter { it.fish.id == fishId }
            .groupBy { it.lake.id }
            .mapValues { (_, entries) -> entries.size }
    }

    fun getTopSpotsForFish(fishId: String): List<Pair<String, Int>> {
        return repository.fishingEntries.value
            .filter { it.fish.id == fishId }
            .groupBy { "${it.lake.name} - ${it.position}" }
            .map { (spot, entries) -> spot to entries.size }
            .sortedByDescending { it.second }
            .take(5)
    }

    fun getCaptureStatsByTimeOfDay(fishId: String): Map<String, Int> {
        return repository.fishingEntries.value
            .filter { it.fish.id == fishId && it.timeOfDay != null }
            .groupBy { it.timeOfDay!! }
            .mapValues { it.value.size }
    }

    // ==========================================
    // GESTION DES APPÂTS PERSONNALISÉS
    // ==========================================

    fun getCustomBaitsForFish(fishId: String): StateFlow<List<String>> {
        return _customBaits
            .map { it[fishId] ?: emptyList() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun addCustomBaitToFish(fishId: String, bait: String) {
        viewModelScope.launch {
            val current = _customBaits.value.toMutableMap()
            val list = current[fishId]?.toMutableList() ?: mutableListOf()
            if (!list.contains(bait)) {
                list.add(bait)
                current[fishId] = list
                _customBaits.value = current
                repository.saveCustomBaitsForFish(fishId, list)
            }
        }
    }

    fun removeCustomBaitFromFish(fishId: String, bait: String) {
        viewModelScope.launch {
            val current = _customBaits.value.toMutableMap()
            val list = current[fishId]?.toMutableList()
            if (list != null && list.remove(bait)) {
                current[fishId] = list
                _customBaits.value = current
                repository.saveCustomBaitsForFish(fishId, list)
            }
        }
    }

    // ==========================================
    // GESTION DES LACS (FAVORIS ET MODIFICATIONS)
    // ==========================================

    fun updateLake(updatedLake: Lake) {
        val current = _modifiedLakes.value.toMutableMap()
        current[updatedLake.id] = updatedLake
        _modifiedLakes.value = current
        viewModelScope.launch {
            repository.saveModifiedLakes(current)
            _saveCompleted.value = true
        }
    }

    fun toggleFavoriteLake(lakeId: String) {
        viewModelScope.launch {
            val current = _favoriteLakeIds.value.toMutableList()
            if (current.contains(lakeId)) {
                current.remove(lakeId)
            } else {
                current.add(lakeId)
            }
            repository.saveFavoriteLakes(current)
            _favoriteLakeIds.value = current
        }
    }

    // ==========================================
    // GESTION DES SPOTS PERSONNALISÉS
    // ==========================================

    fun addUserSpot(spot: UserSpot) {
        viewModelScope.launch {
            val updated = _userSpots.value + spot
            _userSpots.value = updated
            repository.saveUserSpots(updated)
        }
    }

    fun deleteUserSpot(spotId: String) {
        viewModelScope.launch {
            val updated = _userSpots.value.filterNot { it.id == spotId }
            _userSpots.value = updated
            repository.saveUserSpots(updated)
        }
    }

    // ==========================================
    // GESTION DU PROFIL JOUEUR
    // ==========================================

    fun setPlayerLevel(level: Int) {
        viewModelScope.launch {
            repository.setPlayerLevel(level)
        }
    }

    // ==========================================
    // NAVIGATION ET UTILITAIRES
    // ==========================================

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun onDialogDismissed() {
        _saveCompleted.value = false
    }
// ✅ NOUVEAU : Dans votre FishingViewModel.kt, ajouter cette fonction

    /**
     * Récupère tous les appâts disponibles dans le jeu
     */

    fun resetData(options: Set<ResetOption>) {
        viewModelScope.launch {
            if (ResetOption.ENTRIES_STATS in options) repository.clearEntriesAndStats()
            if (ResetOption.LAKES in options) repository.clearModifiedLakes()
            if (ResetOption.BAITS in options) repository.clearAllCustomBaits()
            if (ResetOption.FAVORITES in options) repository.clearFavoriteLakes()
            loadAllDataSources()
        }
    }
}

// ==========================================
// DATA CLASS UI STATE
// ==========================================
data class FishingUiState(
    val currentScreen: Screen = Screen.LAKE_SELECTION,
    val fishingEntries: List<FishingEntry> = emptyList(),
    val playerStats: PlayerStats = PlayerStats(),
    val allLakes: List<Lake> = emptyList(),
    val availableLakes: List<Lake> = emptyList(),
    val favoriteLakeIds: List<String> = emptyList()
)