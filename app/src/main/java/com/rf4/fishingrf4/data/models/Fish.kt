package com.rf4.fishingrf4.data.models

import android.content.Context
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import com.rf4.fishingrf4.utils.LanguageManager
import com.rf4.fishingrf4.R

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
    val preferredTime: List<String> = emptyList(),         // 🆕 ANCIEN FORMAT (gardé)
    val preferredTimePeriods: List<TimePeriod> = emptyList(), // 🆕 NOUVEAU FORMAT

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

    // 🆕 NOUVELLE PROPRIÉTÉ : Périodes de temps unifiées
    val finalPreferredTimePeriods: List<TimePeriod> get() {
        return if (preferredTimePeriods.isNotEmpty()) {
            preferredTimePeriods // Nouveau format
        } else if (preferredTime.isNotEmpty()) {
            // Conversion de l'ancien format string vers énumération
            preferredTime.mapNotNull { timeString ->
                when (timeString) {
                    "Matin", "Matinée" -> TimePeriod.MORNING
                    "Journée", "Jour" -> TimePeriod.DAY
                    "Soirée" -> TimePeriod.EVENING
                    "Nuit" -> TimePeriod.NIGHT
                    else -> null
                }
            }
        } else {
            emptyList()
        }
    }
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

// 🎨 ÉNUMÉRATIONS
@Serializable
enum class FishRarity(
    val colorValue: Long,
    val displayName: String,
    val stringResId: Int,  // 🆕 AJOUTÉ pour la traduction
    val points: Int
) {
    COMMON(0xFF4CAF50, "Commun", R.string.rarity_common, 1),
    UNCOMMON(0xFF2196F3, "Peu commun", R.string.rarity_uncommon, 2),
    RARE(0xFF9C27B0, "Rare", R.string.rarity_rare, 5),
    EPIC(0xFFFF9800, "Épique", R.string.rarity_epic, 10),
    LEGENDARY(0xFFE91E63, "Légendaire", R.string.rarity_legendary, 25)
}

@Serializable
enum class WeatherType(
    val displayName: String,
    val stringResId: Int,  // 🆕 AJOUTÉ pour la traduction
    val emoji: String
) {
    SUNNY("Ensoleillé", R.string.weather_sunny, "☀️"),
    CLOUDY("Nuageux", R.string.weather_cloudy, "☁️"),
    OVERCAST("Couvert", R.string.weather_overcast, "🌫️"),
    LIGHT_RAIN("Pluie légère", R.string.weather_light_rain, "🌦️"),
    RAIN("Pluie", R.string.weather_rain, "🌧️"),
    FOG("Brouillard", R.string.weather_fog, "🌫️"),
    WIND("Venteux", R.string.weather_wind, "💨"),
    ANY("Toutes conditions", R.string.weather_any, "🌤️")
}

@Serializable
enum class TimePeriod(
    val displayName: String,
    val stringResId: Int,  // 🆕 Pour la traduction
    val emoji: String
) {
    MORNING("Matin", R.string.time_morning, "🌅"),
    DAY("Journée", R.string.time_day, "☀️"),
    EVENING("Soirée", R.string.time_evening, "🌇"),
    NIGHT("Nuit", R.string.time_night, "🌙")
}