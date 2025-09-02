package com.rf4.fishingrf4.data.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class FishingEntry(
    val id: String = UUID.randomUUID().toString(),
    val lake: Lake,
    val position: String,
    val fish: Fish,
    val timestamp: Long = System.currentTimeMillis(),
    val weight: Double? = null,
    val length: Double? = null,
    val hour: Int? = null,
    val timeOfDay: String? = null,
    val notes: String = "",
    val bait: String = ""
) {
    // ✅ On supprime complètement la logique de calcul complexe.
    // Chaque prise vaut maintenant 0 point, ce qui évite le crash.
    val points: Int = 0
}

@Serializable
data class PlayerStats(
    val totalCatches: Int = 0,
    val totalPoints: Int = 0,
    val favoriteSpot: String = "",
    val biggestFish: FishingEntry? = null,
    val rareFishCount: Map<FishRarity, Int> = emptyMap(),
    val level: Int = 1
)