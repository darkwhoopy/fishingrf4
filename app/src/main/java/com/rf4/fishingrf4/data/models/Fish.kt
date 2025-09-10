package com.rf4.fishingrf4.data.models

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.rf4.fishingrf4.utils.LanguageManager
import kotlinx.serialization.Serializable

@Serializable
data class Fish(
    val name: String,           // Nom français (existant)
    val nameEn: String,         // 🆕 NOUVEAU : Nom anglais
    val scientificName: String,
    val rarity: FishRarity,
    val minWeight: Double,
    val maxWeight: Double,
    val preferredBaits: List<String>,
    val preferredTime: List<String>,
    val preferredWeather: List<WeatherType>,
    val description: String,
    val descriptionEn: String   // 🆕 NOUVEAU : Description anglaise
)

// 🆕 NOUVEAU : Extension pour obtenir le nom selon la langue
fun Fish.getLocalizedName(context: Context): String {
    return when (LanguageManager.getCurrentLanguage(context)) {
        LanguageManager.Language.ENGLISH -> this.nameEn
        LanguageManager.Language.FRENCH -> this.name
    }
}

// 🆕 NOUVEAU : Extension pour obtenir la description selon la langue
fun Fish.getLocalizedDescription(context: Context): String {
    return when (LanguageManager.getCurrentLanguage(context)) {
        LanguageManager.Language.ENGLISH -> this.descriptionEn
        LanguageManager.Language.FRENCH -> this.description
    }
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