package com.rf4.fishingrf4.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rf4.fishingrf4.data.FishingData
import com.rf4.fishingrf4.data.models.*
import com.rf4.fishingrf4.data.repository.FishingRepository
import com.rf4.fishingrf4.data.utils.GameTimeManager // L'import est correct
import com.rf4.fishingrf4.ui.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime

class FishingViewModel(context: Context) : ViewModel() {
    private val repository = FishingRepository(context)

    // --- États Internes ---
    private val _currentScreen = MutableStateFlow(Screen.LAKE_SELECTION)
    private val _modifiedLakes = MutableStateFlow<Map<String, Lake>>(emptyMap())
    private val _customBaits = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    private val _favoriteLakeIds = MutableStateFlow<List<String>>(emptyList())
    private val _saveCompleted = MutableStateFlow(false)
    private val _userSpots = MutableStateFlow<List<UserSpot>>(emptyList())

    // --- États Exposés à l'UI ---
    val saveCompleted: StateFlow<Boolean> = _saveCompleted.asStateFlow()
    val userSpots: StateFlow<List<UserSpot>> = _userSpots.asStateFlow()
    val gameTime: StateFlow<LocalTime> = GameTimeManager.gameTime
    // ✅ AJOUT : Un état qui calcule le timestamp du début de la journée de jeu actuelle
    val startOfCurrentGameDayTimestamp: StateFlow<Long> = gameTime.map { currentGameTime ->
        // Calcule combien de "vraies" millisecondes se sont écoulées depuis le dernier minuit en jeu
        val gameSecondsIntoDay = currentGameTime.toSecondOfDay()
        val realMillisecondsIntoDay = (gameSecondsIntoDay / (24.0 * 60.0 * 60.0)) * (60.0 * 60.0 * 1000.0)

        // On soustrait cette durée du temps réel actuel pour trouver le début de la journée
        System.currentTimeMillis() - realMillisecondsIntoDay.toLong()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), System.currentTimeMillis())
    val uiState: StateFlow<FishingUiState> = combine(
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
        viewModelScope.launch {
            GameTimeManager.start()
        }
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

    // --- Fonctions de l'horloge ---
    fun adjustInGameTime(newTime: LocalTime) {
        viewModelScope.launch {
            val realTime = LocalTime.now()
            val realSecondsFromMidnight = realTime.toSecondOfDay().toDouble()
            val gameMinutesPerRealMinute = 24.0
            val gameSecondsFromMidnight_base = (realSecondsFromMidnight / 60.0) * (gameMinutesPerRealMinute * 60.0)
            val desiredGameSeconds = newTime.toSecondOfDay()
            val offsetInSeconds = (desiredGameSeconds - gameSecondsFromMidnight_base).toLong()

            GameTimeManager.setTimeOffset(Duration.ofSeconds(offsetInSeconds))
            repository.saveTimeOffset(offsetInSeconds)
        }
    }
    fun getCaptureStatsByTimeOfDay(fishId: String): Map<String, Int> {
        return repository.fishingEntries.value
            .filter { it.fish.id == fishId && it.timeOfDay != null } // On garde les bonnes captures
            .groupBy { it.timeOfDay!! } // On les regroupe par période
            .mapValues { it.value.size } // On compte combien il y en a dans chaque groupe
    }
    fun catchFish(fish: Fish, lake: Lake, position: String) {
        val currentTime = GameTimeManager.gameTime.value
        val entry = FishingEntry(
            lake = lake,
            position = position,
            fish = fish,
            hour = currentTime.hour,
            timeOfDay = GameTimeManager.getTimeOfDay(currentTime)
        )
        viewModelScope.launch { repository.addFishingEntry(entry) }
    }

    // --- Le reste du ViewModel ---

    fun onDialogDismissed() { _saveCompleted.value = false }

    fun resetData(options: Set<ResetOption>) {
        viewModelScope.launch {
            if (ResetOption.ENTRIES_STATS in options) repository.clearEntriesAndStats()
            if (ResetOption.LAKES in options) repository.clearModifiedLakes()
            if (ResetOption.BAITS in options) repository.clearAllCustomBaits()
            if (ResetOption.FAVORITES in options) repository.clearFavoriteLakes()
            loadAllDataSources()
        }
    }

    fun addUserSpot(spot: UserSpot) {
        viewModelScope.launch {
            val updatedSpots = _userSpots.value + spot
            _userSpots.value = updatedSpots
            repository.saveUserSpots(updatedSpots)
        }
    }

    fun deleteUserSpot(spotId: String) {
        viewModelScope.launch {
            val updatedSpots = _userSpots.value.filterNot { it.id == spotId }
            _userSpots.value = updatedSpots
            repository.saveUserSpots(updatedSpots)
        }
    }

    fun updateLake(updatedLake: Lake) {
        val currentModifications = _modifiedLakes.value.toMutableMap()
        currentModifications[updatedLake.id] = updatedLake
        _modifiedLakes.value = currentModifications
        viewModelScope.launch {
            repository.saveModifiedLakes(currentModifications)
            _saveCompleted.value = true
        }
    }

    fun toggleFavoriteLake(lakeId: String) {
        viewModelScope.launch {
            val currentFavorites = _favoriteLakeIds.value.toMutableList()
            if (currentFavorites.contains(lakeId)) currentFavorites.remove(lakeId) else currentFavorites.add(lakeId)
            repository.saveFavoriteLakes(currentFavorites)
            _favoriteLakeIds.value = currentFavorites
        }
    }

    fun setPlayerLevel(level: Int) {
        viewModelScope.launch { repository.setPlayerLevel(level) }
    }

    fun getAllLakes(): List<Lake> {
        val originalLakes = FishingData.getAllLakes()
        val modifiedLakes = _modifiedLakes.value
        return originalLakes.map { originalLake ->
            modifiedLakes[originalLake.id] ?: originalLake
        }
    }

    fun getAllAvailableFish(): List<Fish> {
        return getAllLakes()
            .flatMap { it.availableFish }
            .distinctBy { it.name }
            .sortedBy { it.name }
    }

    fun getAllGameBaits(): List<String> {
        return getAllLakes()
            .flatMap { it.availableFish }
            .flatMap { it.preferredBait }
            .distinct()
            .sorted()
    }

    fun getLakesForFish(fish: Fish): List<Lake> {
        return getAllLakes().filter { lake ->
            lake.availableFish.any { it.id == fish.id }
        }
    }

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

    fun navigateTo(screen: Screen) { _currentScreen.value = screen }

    fun removeEntry(entryId: String) {
        viewModelScope.launch { repository.removeFishingEntry(entryId) }
    }

    fun getCustomBaitsForFish(fishId: String): StateFlow<List<String>> = _customBaits.map { it[fishId] ?: emptyList() }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCustomBaitToFish(fishId: String, bait: String) {
        viewModelScope.launch {
            val currentBaits = _customBaits.value.toMutableMap()
            val fishBaits = currentBaits[fishId]?.toMutableList() ?: mutableListOf()
            if (!fishBaits.contains(bait)) {
                fishBaits.add(bait)
                currentBaits[fishId] = fishBaits
                _customBaits.value = currentBaits
                repository.saveCustomBaitsForFish(fishId, fishBaits)
            }
        }
    }

    fun removeCustomBaitFromFish(fishId: String, bait: String) {
        viewModelScope.launch {
            val currentBaits = _customBaits.value.toMutableMap()
            val fishBaits = currentBaits[fishId]?.toMutableList()
            if (fishBaits != null && fishBaits.remove(bait)) {
                currentBaits[fishId] = fishBaits
                _customBaits.value = currentBaits
                repository.saveCustomBaitsForFish(fishId, fishBaits)
            }
        }
    }
}

data class FishingUiState(
    val currentScreen: Screen = Screen.LAKE_SELECTION,
    val fishingEntries: List<FishingEntry> = emptyList(),
    val playerStats: PlayerStats = PlayerStats(),
    val allLakes: List<Lake> = emptyList(),
    val availableLakes: List<Lake> = emptyList(),
    val favoriteLakeIds: List<String> = emptyList()
)