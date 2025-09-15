package com.rf4.fishingrf4.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.rf4.fishingrf4.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.rf4.fishingrf4.data.models.FavoriteSpot

class FishingRepository(private val context: Context? = null) {

    private val prefs: SharedPreferences? by lazy { context?.getSharedPreferences("fishing_data", Context.MODE_PRIVATE) }
    private val customBaitsPrefs: SharedPreferences? by lazy { context?.getSharedPreferences("fishing_custom_baits", Context.MODE_PRIVATE) }
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    private val _fishingEntries = MutableStateFlow<List<FishingEntry>>(emptyList())
    val fishingEntries: StateFlow<List<FishingEntry>> = _fishingEntries.asStateFlow()
    private val _playerStats = MutableStateFlow(PlayerStats())
    val playerStats: StateFlow<PlayerStats> = _playerStats.asStateFlow()

    private val KEY_FAVORITE_SPOTS = "favorite_spots"

    // StateFlow pour les spots favoris
    private val _favoriteSpots = MutableStateFlow<List<FavoriteSpot>>(emptyList())
    val favoriteSpots: StateFlow<List<FavoriteSpot>> = _favoriteSpots.asStateFlow()

    companion object {
        private const val RECENT_BAITS_KEY = "recent_baits_ordered"
        private const val MAX_RECENT_BAITS = 3
        private const val BAIT_SEPARATOR = "|BAIT|" // Séparateur unique
    }

    init {
        loadAllData()
    }

    fun loadAllData() {
        try {
            prefs?.getString("fishing_entries", null)?.let { _fishingEntries.value = json.decodeFromString(it) }
            prefs?.getString("player_stats", null)?.let { _playerStats.value = json.decodeFromString(it) }
        } catch (e: Exception) {
            println("ERROR: Erreur chargement données: ${e.message}")
        }
    }

    private suspend fun saveFishingEntries() = withContext(Dispatchers.IO) {
        prefs?.edit()?.putString("fishing_entries", json.encodeToString(_fishingEntries.value))?.apply()
    }

    private suspend fun savePlayerStats() = withContext(Dispatchers.IO) {
        prefs?.edit()?.putString("player_stats", json.encodeToString(_playerStats.value))?.apply()
    }

    suspend fun addFishingEntry(entry: FishingEntry) {
        _fishingEntries.value += entry
        saveFishingEntries()
        updatePlayerStats()
    }

    suspend fun removeFishingEntry(entryId: String) {
        _fishingEntries.value = _fishingEntries.value.filterNot { it.id == entryId }
        saveFishingEntries()
        updatePlayerStats()
    }

    private suspend fun updatePlayerStats() {
        val entries = _fishingEntries.value
        val currentStats = _playerStats.value
        _playerStats.value = PlayerStats(
            totalCatches = entries.size,
            totalPoints = entries.sumOf { it.points },
            favoriteSpot = entries.groupBy { "${it.lake.name} - ${it.position}" }.maxByOrNull { it.value.size }?.key ?: "",
            biggestFish = entries.maxByOrNull { it.weight ?: 0.0 },
            rareFishCount = entries.groupingBy { it.fish.rarity }.eachCount(),
            level = currentStats.level
        )
        savePlayerStats()
    }

    suspend fun setPlayerLevel(level: Int) {
        _playerStats.value = _playerStats.value.copy(level = level)
        savePlayerStats()
    }

    suspend fun saveCustomBaitsForFish(fishId: String, baits: List<String>) {
        withContext(Dispatchers.IO) {
            customBaitsPrefs?.edit()?.putString("baits_$fishId", baits.joinToString("|"))?.apply()
        }
    }

    suspend fun loadAllCustomBaits(): Map<String, List<String>> {
        return withContext(Dispatchers.IO) {
            customBaitsPrefs?.all?.mapNotNull { (key, value) ->
                if (key.startsWith("baits_") && value is String) {
                    key.removePrefix("baits_") to value.split("|").filter { it.isNotBlank() }
                } else null
            }?.toMap() ?: emptyMap()
        }
    }

    fun loadModifiedLakes(): Map<String, Lake> {
        return try {
            prefs?.getString("modified_lakes", null)?.let { json.decodeFromString<Map<String, Lake>>(it) } ?: emptyMap()
        } catch (e: Exception) { emptyMap() }
    }

    suspend fun saveModifiedLakes(lakes: Map<String, Lake>) {
        withContext(Dispatchers.IO) {
            prefs?.edit()?.putString("modified_lakes", json.encodeToString(lakes))?.apply()
        }
    }

    fun loadFavoriteLakes(): List<String> {
        return try {
            prefs?.getString("favorite_lakes_ids", "")?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        } catch (e: Exception) { emptyList() }
    }

    suspend fun saveFavoriteLakes(favoriteIds: List<String>) {
        withContext(Dispatchers.IO) {
            prefs?.edit()?.putString("favorite_lakes_ids", favoriteIds.joinToString(","))?.apply()
        }
    }

    suspend fun clearEntriesAndStats() {
        _fishingEntries.value = emptyList()
        _playerStats.value = PlayerStats()
        withContext(Dispatchers.IO) {
            prefs?.edit()?.remove("fishing_entries")?.remove("player_stats")?.apply()
        }
    }

    suspend fun clearModifiedLakes() = withContext(Dispatchers.IO) {
        prefs?.edit()?.remove("modified_lakes")?.apply()
    }

    suspend fun clearFavoriteLakes() = withContext(Dispatchers.IO) {
        prefs?.edit()?.remove("favorite_lakes_ids")?.apply()
    }

    suspend fun clearAllCustomBaits() = withContext(Dispatchers.IO) {
        customBaitsPrefs?.edit()?.clear()?.apply()
    }

    fun loadUserSpots(): List<UserSpot> {
        return try {
            prefs?.getString("user_spots", null)?.let { json.decodeFromString<List<UserSpot>>(it) } ?: emptyList()
        } catch (e: Exception) { emptyList() }
    }

    suspend fun saveUserSpots(spots: List<UserSpot>) {
        withContext(Dispatchers.IO) {
            prefs?.edit()?.putString("user_spots", json.encodeToString(spots))?.apply()
        }
    }

    fun loadTimeOffset(): Long {
        return prefs?.getLong("time_offset_seconds", 0L) ?: 0L
    }

    suspend fun saveTimeOffset(offsetInSeconds: Long) {
        withContext(Dispatchers.IO) {
            prefs?.edit()?.putLong("time_offset_seconds", offsetInSeconds)?.apply()
        }
    }

    // ============================================================================
    // MÉTHODES POUR LES APPÂTS RÉCENTS
    // ============================================================================

    /**
     * Sauvegarde les 3 derniers appâts utilisés
     */
    suspend fun saveRecentBait(bait: String) = withContext(Dispatchers.IO) {
        if (bait.isBlank()) return@withContext

        val currentBaits = getRecentBaits().toMutableList()

        // Retirer l'appât s'il existe déjà pour le remettre en première position
        currentBaits.remove(bait)

        // Ajouter en première position
        currentBaits.add(0, bait)

        // Garder seulement les 3 derniers
        val recentBaits = currentBaits.take(MAX_RECENT_BAITS)

        // ✅ CORRECTION : Sauvegarder comme String ordonnée au lieu d'un Set
        val orderedString = recentBaits.joinToString(BAIT_SEPARATOR)
        prefs?.edit()?.putString(RECENT_BAITS_KEY, orderedString)?.apply()
    }

    /**
     * Récupère les 3 derniers appâts utilisés
     */
    fun getRecentBaits(): List<String> {
        val orderedString = prefs?.getString(RECENT_BAITS_KEY, "") ?: ""
        return if (orderedString.isBlank()) {
            emptyList()
        } else {
            orderedString.split(BAIT_SEPARATOR).filter { it.isNotBlank() }
        }
    }

    // ============================================================================
    // MÉTHODES POUR LES STATISTIQUES D'APPÂTS LOCALES
    // ============================================================================

    /**
     * Récupère le top 5 des appâts utilisés pour un poisson donné aujourd'hui
     * Basé sur vos propres captures locales
     */
    fun getTopBaitsForFishToday(fishName: String, startOfDayTimestamp: Long): List<Pair<String, Int>> {
        return _fishingEntries.value
            .filter {
                it.fish.name == fishName &&
                        it.timestamp >= startOfDayTimestamp &&
                        it.bait.isNotBlank()
            }
            .groupBy { it.bait }
            .map { (bait, entries) -> bait to entries.size }
            .sortedByDescending { it.second }
            .take(5)
    }

    /**
     * Récupère le top 5 des appâts utilisés pour un poisson donné (tout temps)
     * Basé sur vos propres captures locales
     */
    fun getTopBaitsForFishAllTime(fishName: String): List<Pair<String, Int>> {
        return _fishingEntries.value
            .filter {
                it.fish.name == fishName &&
                        it.bait.isNotBlank()
            }
            .groupBy { it.bait }
            .map { (bait, entries) -> bait to entries.size }
            .sortedByDescending { it.second }
            .take(5)
    }

    /**
     * Récupère les statistiques générales d'appâts aujourd'hui
     * Retourne le top 10 des appâts les plus utilisés (tous poissons confondus)
     */
    fun getTopBaitsOverallToday(startOfDayTimestamp: Long): List<Triple<String, String, Int>> {
        return _fishingEntries.value
            .filter {
                it.timestamp >= startOfDayTimestamp &&
                        it.bait.isNotBlank()
            }
            .groupBy { "${it.fish.name}-${it.bait}" }
            .map { (key, entries) ->
                val parts = key.split("-", limit = 2)
                Triple(parts[0], parts[1], entries.size) // (poisson, appât, nombre)
            }
            .sortedByDescending { it.third }
            .take(10)
    }

    suspend fun loadFavoriteSpots() {
        withContext(Dispatchers.IO) {
            try {
                val spotsJson = prefs?.getString(KEY_FAVORITE_SPOTS, "[]")
                val spots = Json.decodeFromString<List<FavoriteSpot>>(spotsJson ?: "[]")
                _favoriteSpots.value = spots
            } catch (e: Exception) {
                Log.e("FishingRepository", "Erreur lors du chargement des spots favoris", e)
                _favoriteSpots.value = emptyList()
            }
        }
    }

    /**
     * Sauvegarde tous les spots favoris dans les préférences
     */
    private suspend fun saveFavoriteSpots() {
        withContext(Dispatchers.IO) {
            try {
                val spotsJson = Json.encodeToString(_favoriteSpots.value)
                prefs?.edit()?.putString(KEY_FAVORITE_SPOTS, spotsJson)?.apply()
            } catch (e: Exception) {
                Log.e("FishingRepository", "Erreur lors de la sauvegarde des spots favoris", e)
            }
        }
    }

    /**
     * Ajoute un nouveau spot favori
     */
    suspend fun addFavoriteSpot(spot: FavoriteSpot) {
        val currentSpots = _favoriteSpots.value.toMutableList()
        currentSpots.add(spot)
        _favoriteSpots.value = currentSpots
        saveFavoriteSpots()
    }

    /**
     * Supprime un spot favori par ID
     */
    suspend fun deleteFavoriteSpot(spotId: String) {
        val currentSpots = _favoriteSpots.value.toMutableList()
        currentSpots.removeAll { it.id == spotId }
        _favoriteSpots.value = currentSpots
        saveFavoriteSpots()
    }

    /**
     * Met à jour un spot favori existant
     */
    suspend fun updateFavoriteSpot(updatedSpot: FavoriteSpot) {
        val currentSpots = _favoriteSpots.value.toMutableList()
        val index = currentSpots.indexOfFirst { it.id == updatedSpot.id }
        if (index != -1) {
            currentSpots[index] = updatedSpot
            _favoriteSpots.value = currentSpots
            saveFavoriteSpots()
        }
    }

    /**
     * Récupère les spots favoris pour un lac spécifique
     */
    fun getFavoriteSpotsForLake(lakeId: String): List<FavoriteSpot> {
        return _favoriteSpots.value.filter { it.lakeId == lakeId }
    }

    /**
     * Recherche des spots favoris par nom
     */
    fun searchFavoriteSpots(query: String): List<FavoriteSpot> {
        return _favoriteSpots.value.filter { spot ->
            spot.name.contains(query, ignoreCase = true) ||
                    spot.lakeName.contains(query, ignoreCase = true) ||
                    spot.fishNames.any { it.contains(query, ignoreCase = true) } ||
                    spot.baits.any { it.contains(query, ignoreCase = true) }
        }
    }


    /**
     * Récupère le nombre total de poissons capturés par espèce aujourd'hui
     * Pour afficher "35 gardons pris aujourd'hui"
     */
    fun getFishCountsToday(startOfDayTimestamp: Long): Map<String, Int> {
        return _fishingEntries.value
            .filter { it.timestamp >= startOfDayTimestamp }
            .groupBy { it.fish.name }
            .mapValues { (_, entries) -> entries.size }
    }
}