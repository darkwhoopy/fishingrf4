package com.rf4.fishingrf4.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rf4.fishingrf4.data.FishingData
import com.rf4.fishingrf4.data.models.FavoriteSpot
import com.rf4.fishingrf4.data.models.Fish
import com.rf4.fishingrf4.data.models.FishingEntry
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.data.models.PlayerStats
import com.rf4.fishingrf4.data.models.UserSpot
import com.rf4.fishingrf4.data.online.FishingOnlineRepository
import com.rf4.fishingrf4.data.online.SpeciesCount
import com.rf4.fishingrf4.data.repository.FishingRepository
import com.rf4.fishingrf4.data.utils.GameTimeManager
import com.rf4.fishingrf4.ui.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalTime

class FishingViewModel(context: Context) : ViewModel() {

    // Repositories
    val repository = FishingRepository(context)
    private val onlineRepo = FishingOnlineRepository()

    // État interne
    private val _currentScreen = MutableStateFlow(Screen.LAKE_SELECTION)
    private val _modifiedLakes = MutableStateFlow<Map<String, Lake>>(emptyMap())
    private val _customBaits = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    private val _favoriteLakeIds = MutableStateFlow<List<String>>(emptyList())
    private val _saveCompleted = MutableStateFlow(false)
    private val _userSpots = MutableStateFlow<List<UserSpot>>(emptyList())
    private val _recentBaits = MutableStateFlow<List<String>>(emptyList())
    private val _currentUser = MutableStateFlow<FirebaseUser?>(FirebaseAuth.getInstance().currentUser)

    // État exposé
    val saveCompleted: StateFlow<Boolean> = _saveCompleted.asStateFlow()
    val userSpots: StateFlow<List<UserSpot>> = _userSpots.asStateFlow()
    val recentBaits: StateFlow<List<String>> = _recentBaits.asStateFlow()
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()
    val gameTime: StateFlow<LocalTime> = GameTimeManager.gameTime
    val favoriteSpots: StateFlow<List<FavoriteSpot>> = repository.favoriteSpots

    val startOfCurrentGameDayTimestamp: StateFlow<Long> =
        gameTime.map { currentGameTime ->
            val gameSecondsIntoDay = currentGameTime.toSecondOfDay()
            val realMillisecondsIntoDay = (gameSecondsIntoDay / (24.0 * 60.0 * 60.0)) * (60.0 * 60.0 * 1000.0)
            System.currentTimeMillis() - realMillisecondsIntoDay.toLong()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            System.currentTimeMillis()
        )

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
        loadRecentBaits()
        loadFavoriteSpots()

        // Écouter les changements d'authentification Firebase
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            _currentUser.value = auth.currentUser
        }
    }

    // ==========================================
    // CHARGEMENT DES DONNÉES
    // ==========================================

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
    fun debugBaitsData() {
        viewModelScope.launch {
            val debug = withContext(Dispatchers.IO) {
                onlineRepo.debugBaitsInEntries()
            }
            println(debug)
        }
    }
    private fun loadRecentBaits() {
        _recentBaits.value = repository.getRecentBaits()
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
    // FONCTIONS ONLINE - TOP 5
    // ==========================================

    fun fetchTop5PlayersOfDay(
        timestampStart: Long,
        onResult: (List<Pair<String, Long>>) -> Unit
    ) {
        viewModelScope.launch {
            val players: List<Pair<String, Long>> = withContext(Dispatchers.IO) {
                try {
                    onlineRepo.getTop5PlayersOfDay(timestampStart)
                } catch (e: Exception) {
                    println("❌ Erreur fetchTop5PlayersOfDay: ${e.message}")
                    emptyList()
                }
            }
            onResult(players)
        }
    }

    fun debugBaitsInDatabase(onResult: (String) -> Unit) {
        viewModelScope.launch {
            val debugInfo: String = withContext(Dispatchers.IO) {
                try {
                    onlineRepo.debugBaitsInEntries()
                } catch (e: Exception) {
                    "Erreur debug: ${e.message}"
                }
            }
            onResult(debugInfo)
        }
    }


    fun fetchTop5LakesOfDay(
        timestampStart: Long,
        onResult: (List<Pair<String, Long>>) -> Unit
    ) {
        viewModelScope.launch {
            val lakes: List<Pair<String, Long>> = withContext(Dispatchers.IO) {
                try {
                    onlineRepo.getTop5LakesOfDay(timestampStart)
                } catch (e: Exception) {
                    println("❌ Erreur fetchTop5LakesOfDay: ${e.message}")
                    emptyList()
                }
            }
            onResult(lakes)
        }
    }

    fun fetchTop5SpeciesCountsToday(
        timestampStart: Long = startOfCurrentGameDayTimestamp.value,
        onResult: (List<SpeciesCount>) -> Unit
    ) {
        viewModelScope.launch {
            val species: List<SpeciesCount> = withContext(Dispatchers.IO) {
                try {
                    onlineRepo.getTop5SpeciesCountsToday(timestampStart)
                } catch (e: Exception) {
                    println("❌ Erreur fetchTop5SpeciesCountsToday: ${e.message}")
                    emptyList()
                }
            }
            onResult(species)
        }
    }

    fun fetchTop5PositionsForLakeToday(lakeId: String, onResult: (List<Pair<String, Long>>) -> Unit) {
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

    fun fetchSpeciesWithBaitStats(
        timestampStart: Long = startOfCurrentGameDayTimestamp.value,
        onResult: (Map<String, Pair<Long, List<Pair<String, Long>>>>) -> Unit
    ) {
        viewModelScope.launch {
            val stats: Map<String, Pair<Long, List<Pair<String, Long>>>> = withContext(Dispatchers.IO) {
                try {
                    onlineRepo.getSpeciesWithBaitStats(timestampStart)
                } catch (e: Exception) {
                    println("❌ Erreur fetchSpeciesWithBaitStats: ${e.message}")
                    e.printStackTrace()
                    emptyMap()
                }
            }
            onResult(stats)
        }
    }

    // ==========================================
    // FONCTIONS ONLINE - APPÂTS COMMUNAUTAIRES
    // ==========================================

    fun fetchTopCommunityBaits(fishId: String, onResult: (List<Pair<String, Long>>) -> Unit) {
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

    fun fetchTopCommunityBaitsThisMonth(fishId: String, onResult: (List<Pair<String, Long>>) -> Unit) {
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

    // ==========================================
    // GESTION DES CAPTURES
    // ==========================================

    private suspend fun pushEntryOnline(
        species: String,
        weight: Double,
        spot: String,
        caughtAtMs: Long,
        bait: String = ""  // ✅ NOUVEAU paramètre
    ) {
        try {
            onlineRepo.addEntry(
                species = species,
                weight = weight,
                spot = spot,
                caughtAtMs = caughtAtMs,
                lakeId = null, // Optionnel : vous pouvez passer l'ID du lac si nécessaire
                bait = bait    // ✅ PRINCIPAL : Passer l'appât à Firebase
            )
            println("✅ Données envoyées à Firebase: $species avec appât '$bait'")
        } catch (e: Exception) {
            println("❌ Erreur push online: ${e.message}")
            e.printStackTrace()
        }
    }

    fun catchFish(fish: Fish, lake: Lake, position: String, bait: String = "") {
        viewModelScope.launch {
            try {
                val currentTime = GameTimeManager.gameTime.value

                // ✅ CRÉATION de l'entrée locale avec l'appât
                val entry = FishingEntry(
                    id = java.util.UUID.randomUUID().toString(),
                    lake = lake,
                    position = position,
                    fish = fish,
                    userName = "Joueur Local", // ✅ AJOUT : nom utilisateur par défaut
                    timestamp = System.currentTimeMillis(),
                    weight = fish.weight?.start?.plus((fish.weight!!.endInclusive - fish.weight!!.start) * Math.random()),
                    hour = currentTime.hour,
                    timeOfDay = GameTimeManager.getTimeOfDay(currentTime),
                    bait = bait // ✅ AJOUT : stocker l'appât dans l'entrée locale
                )

                // ✅ SAUVEGARDE locale
                repository.addFishingEntry(entry)

                // ✅ ENVOI vers Firebase avec l'appât
                pushEntryOnline(
                    species = fish.name,
                    weight = entry.weight ?: 0.0,
                    spot = "${lake.name} - $position",
                    caughtAtMs = System.currentTimeMillis(),
                    bait = bait  // ✅ PRINCIPAL : Passer l'appât à Firebase
                )

                // ✅ OPTIONNEL : Sauvegarder l'appât dans les récents
                if (bait.isNotBlank()) {
                    repository.saveRecentBait(bait)
                    loadRecentBaits() // Recharger la liste des appâts récents
                }

                println("✅ Poisson capturé: ${fish.name} avec appât: '$bait'")

            } catch (e: Exception) {
                println("❌ Erreur lors de la capture: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun catchFishWithBait(fish: Fish, lake: Lake, position: String?, bait: String) {
        viewModelScope.launch {
            try {
                val currentTime = GameTimeManager.gameTime.value
                val entry = FishingEntry(
                    id = java.util.UUID.randomUUID().toString(),
                    fish = fish,
                    lake = lake,
                    position = position ?: "",
                    timestamp = System.currentTimeMillis(),
                    weight = fish.weight?.start?.plus((fish.weight!!.endInclusive - fish.weight!!.start) * Math.random()),
                    hour = currentTime.hour,
                    timeOfDay = GameTimeManager.getTimeOfDay(currentTime),
                    bait = bait
                )

                repository.addFishingEntry(entry)

                pushEntryOnline(
                    species = fish.name,
                    weight = entry.weight ?: 0.0,
                    spot = "${lake.name} - ${position ?: ""}",
                    caughtAtMs = System.currentTimeMillis(),
                    bait = bait
                )

                if (bait.isNotBlank()) {
                    repository.saveRecentBait(bait)
                    loadRecentBaits()
                }

            } catch (e: Exception) {
                println("Erreur lors de la capture: ${e.message}")
            }
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

    fun getAllLakes(): List<Lake> {
        val originalLakes = FishingData.lakes
        val modifiedLakes = _modifiedLakes.value
        return originalLakes.map { originalLake ->
            modifiedLakes[originalLake.id] ?: originalLake
        }
    }

    fun getAllAvailableFish(): List<Fish> {
        return FishingData.getAllFish()
            .distinctBy { it.name }
            .sortedBy { it.name }
    }

    fun getAllFish(): List<Fish> {
        return getAllAvailableFish()
    }

    fun getAllGameBaits(): List<String> {
        return FishingData.ALL_BAITS
    }

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

    fun resetUserData() {
        viewModelScope.launch {
            try {
                repository.clearEntriesAndStats()
                _modifiedLakes.value = emptyMap()
                _favoriteLakeIds.value = emptyList()
                _customBaits.value = emptyMap()
                _userSpots.value = emptyList()
                _recentBaits.value = emptyList()
                println("Données utilisateur réinitialisées")
            } catch (e: Exception) {
                println("Erreur lors de la réinitialisation: ${e.message}")
            }
        }
    }
    fun addFavoriteSpot(
        name: String,
        position: String,
        lake: Lake,
        fishNames: List<String>,
        baits: List<String>,
        distance: Int,
        notes: String = ""
    ) {
        viewModelScope.launch {
            try {
                val spot = FavoriteSpot(
                    name = name,
                    position = position,
                    lakeName = lake.name,
                    lakeId = lake.id,
                    fishNames = fishNames,
                    baits = baits,
                    distance = distance,
                    notes = notes
                )
                repository.addFavoriteSpot(spot)
            } catch (e: Exception) {
                Log.e("FishingViewModel", "Erreur lors de l'ajout du spot favori", e)
            }
        }
    }

    /**
     * Supprime un spot favori
     */
    fun deleteFavoriteSpot(spotId: String) {
        viewModelScope.launch {
            try {
                repository.deleteFavoriteSpot(spotId)
            } catch (e: Exception) {
                Log.e("FishingViewModel", "Erreur lors de la suppression du spot favori", e)
            }
        }
    }

    /**
     * Met à jour un spot favori
     */
    fun updateFavoriteSpot(spot: FavoriteSpot) {
        viewModelScope.launch {
            try {
                repository.updateFavoriteSpot(spot)
            } catch (e: Exception) {
                Log.e("FishingViewModel", "Erreur lors de la mise à jour du spot favori", e)
            }
        }
    }

    /**
     * Récupère les spots favoris pour un lac
     */
    fun getFavoriteSpotsForLake(lakeId: String): List<FavoriteSpot> {
        return repository.getFavoriteSpotsForLake(lakeId)
    }

    /**
     * Charge les spots favoris au démarrage
     */
    private fun loadFavoriteSpots() {
        viewModelScope.launch {
            repository.loadFavoriteSpots()
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
}