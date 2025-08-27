package com.rf4.fishingrf4.data.models

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
data class Fish(
    val name: String,
    val species: String,
    val rarity: FishRarity,
    @Serializable(with = DoubleRangeSerializer::class) // Gardez bien cette ligne !
    val weight: ClosedFloatingPointRange<Double>? = null,
    val preferredBait: List<String> = emptyList(),
    val bestHours: List<Int> = emptyList(),
    val bestWeather: List<WeatherType> = emptyList()
) {
    val id: String = "${name}_${species}"
}

@Serializable
// ✅ CORRECTION : On remplace 'color: Color' par 'colorValue: Long'
enum class FishRarity(val colorValue: Long, val displayName: String, val points: Int) {
    COMMON(0xFF4CAF50, "Commun", 1),
    UNCOMMON(0xFF2196F3, "Peu commun", 2),
    RARE(0xFF9C27B0, "Rare", 5),
    EPIC(0xFFFF9800, "Épique", 10),
    LEGENDARY(0xFFE91E63, "Légendaire", 25)
}

@Serializable
enum class WeatherType(val displayName: String, val emoji: String) {
    SUNNY("Ensoleillé", "☀️"),
    CLOUDY("Nuageux", "☁️"),
    OVERCAST("Couvert", "🌫️"),
    LIGHT_RAIN("Pluie légère", "🌦️"),
    RAIN("Pluie", "🌧️"),
    FOG("Brouillard", "🌫️"),
    WIND("Venteux", "💨"),
    ANY("Toutes conditions", "🌤️")
}