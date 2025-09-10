package com.rf4.fishingrf4.data.models

import android.content.Context
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import com.rf4.fishingrf4.utils.LanguageManager

@Serializable
data class Fish(
    val name: String,                    // 🇫🇷 Nom français (OBLIGATOIRE)
    val nameEn: String = "",             // 🇬🇧 Nom anglais (NOUVEAU)
    val species: String,                 // 🧬 Nom scientifique (ANCIEN FORMAT - gardé pour compatibilité)
    val scientificName: String = "",     // 🧬 Nom scientifique (NOUVEAU FORMAT - optionnel)
    val rarity: FishRarity,

    // 📏 POIDS - Double format pour compatibilité
    @Serializable(with = DoubleRangeSerializer::class)
    val weight: ClosedFloatingPointRange<Double>? = null,  // ✅ ANCIEN FORMAT (gardé)
    val minWeight: Double = weight?.start ?: 0.0,          // 🆕 NOUVEAU FORMAT
    val maxWeight: Double = weight?.endInclusive ?: 0.0,   // 🆕 NOUVEAU FORMAT

    // 🎣 APPÂTS - Double format pour compatibilité
    val preferredBait: List<String> = emptyList(),         // ✅ ANCIEN FORMAT (gardé)
    val preferredBaits: List<String> = preferredBait,      // 🆕 NOUVEAU FORMAT (alias)

    // ⏰ HORAIRES - Double format pour compatibilité
    val bestHours: List<Int> = emptyList(),                // ✅ ANCIEN FORMAT (gardé)
    val preferredTime: List<String> = emptyList(),         // 🆕 NOUVEAU FORMAT

    // 🌤️ MÉTÉO - Double format pour compatibilité
    val bestWeather: List<WeatherType> = emptyList(),      // ✅ ANCIEN FORMAT (gardé)
    val preferredWeather: List<WeatherType> = bestWeather, // 🆕 NOUVEAU FORMAT (alias)

    // 📝 DESCRIPTIONS (NOUVELLES)
    val description: String = "",        // 🇫🇷 Description française
    val descriptionEn: String = ""       // 🇬🇧 Description anglaise
) {
    // 🆔 ID du poisson (ANCIEN FORMAT - gardé pour compatibilité)
    val id: String = "${name}_${species}"

    // 🧬 Nom scientifique unifié (priorité au nouveau format)
    val finalScientificName: String get() = scientificName.ifEmpty { species }

    // 📏 Poids min/max calculés automatiquement
    val finalMinWeight: Double get() = if (minWeight > 0.0) minWeight else (weight?.start ?: 0.0)
    val finalMaxWeight: Double get() = if (maxWeight > 0.0) maxWeight else (weight?.endInclusive ?: 0.0)

    // 🎣 Appâts unifiés
    val finalPreferredBaits: List<String> get() = preferredBaits.ifEmpty { preferredBait }

    // 🌤️ Météo unifiée
    val finalPreferredWeather: List<WeatherType> get() = preferredWeather.ifEmpty { bestWeather }
}

// 🌍 EXTENSIONS POUR LA TRADUCTION
fun Fish.getLocalizedName(context: Context): String {
    return when (LanguageManager.getCurrentLanguage(context)) {
        LanguageManager.Language.ENGLISH -> this.nameEn.ifEmpty { this.name }
        LanguageManager.Language.FRENCH -> this.name
    }
}

fun Fish.getLocalizedDescription(context: Context): String {
    return when (LanguageManager.getCurrentLanguage(context)) {
        LanguageManager.Language.ENGLISH -> this.descriptionEn.ifEmpty { this.description }
        LanguageManager.Language.FRENCH -> this.description
    }
}

// 🎨 ÉNUMÉRATIONS GARDÉES IDENTIQUES
@Serializable
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