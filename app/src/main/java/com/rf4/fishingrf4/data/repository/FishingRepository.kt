package com.rf4.fishingrf4.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.rf4.fishingrf4.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FishingRepository(private val context: Context? = null) {

    private val prefs: SharedPreferences? by lazy { context?.getSharedPreferences("fishing_data", Context.MODE_PRIVATE) }
    private val customBaitsPrefs: SharedPreferences? by lazy { context?.getSharedPreferences("fishing_custom_baits", Context.MODE_PRIVATE) }
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    private val _fishingEntries = MutableStateFlow<List<FishingEntry>>(emptyList())
    val fishingEntries: StateFlow<List<FishingEntry>> = _fishingEntries.asStateFlow()
    private val _playerStats = MutableStateFlow(PlayerStats())
    val playerStats: StateFlow<PlayerStats> = _playerStats.asStateFlow()

    init {
        loadAllData()
    }

    fun loadAllData() {
        try {
            prefs?.getString("fishing_entries", null)?.let { _fishingEntries.value = json.decodeFromString(it) }
            prefs?.getString("player_stats", null)?.let { _playerStats.value = json.decodeFromString(it) }
        } catch (e: Exception) { println("ERROR: Erreur chargement données: ${e.message}") }
    }

    private suspend fun saveFishingEntries() = withContext(Dispatchers.IO) { prefs?.edit()?.putString("fishing_entries", json.encodeToString(_fishingEntries.value))?.apply() }
    private suspend fun savePlayerStats() = withContext(Dispatchers.IO) { prefs?.edit()?.putString("player_stats", json.encodeToString(_playerStats.value))?.apply() }

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
        withContext(Dispatchers.IO) { customBaitsPrefs?.edit()?.putString("baits_$fishId", baits.joinToString("|"))?.apply() }
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
        withContext(Dispatchers.IO) { prefs?.edit()?.putString("modified_lakes", json.encodeToString(lakes))?.apply() }
    }

    fun loadFavoriteLakes(): List<String> {
        return try {
            prefs?.getString("favorite_lakes_ids", "")?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        } catch (e: Exception) { emptyList() }
    }

    suspend fun saveFavoriteLakes(favoriteIds: List<String>) {
        withContext(Dispatchers.IO) { prefs?.edit()?.putString("favorite_lakes_ids", favoriteIds.joinToString(","))?.apply() }
    }

    suspend fun clearEntriesAndStats() {
        _fishingEntries.value = emptyList()
        _playerStats.value = PlayerStats()
        withContext(Dispatchers.IO) { prefs?.edit()?.remove("fishing_entries")?.remove("player_stats")?.apply() }
    }

    suspend fun clearModifiedLakes() = withContext(Dispatchers.IO) { prefs?.edit()?.remove("modified_lakes")?.apply() }
    suspend fun clearFavoriteLakes() = withContext(Dispatchers.IO) { prefs?.edit()?.remove("favorite_lakes_ids")?.apply() }
    suspend fun clearAllCustomBaits() = withContext(Dispatchers.IO) { customBaitsPrefs?.edit()?.clear()?.apply() }

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
}