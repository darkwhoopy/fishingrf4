package com.rf4.fishingrf4.data.models

import kotlinx.serialization.Serializable
import androidx.compose.ui.graphics.Color

@Serializable
data class Lake(
    val id: String,
    val name: String,
    val type: LakeType,
    val difficulty: Difficulty,
    val availableFish: List<Fish>,
    val description: String = "",
    val coordinates: Map<String, String> = emptyMap(),
    val unlockLevel: Int = 1
)

@Serializable
enum class LakeType(val displayName: String, val emoji: String) {
    LAKE("Lac", "🏞️"),
    RIVER("Rivière", "🌊"),
    SEA("Mer", "🌊"),
    POND("Étang", "💧"),
    CREEK("Ruisseau", "🏔️")
}

@Serializable
// ✅ CORRECTION : On remplace 'color: Color' par 'colorValue: Long'
enum class Difficulty(val displayName: String, val colorValue: Long) {
    BEGINNER("Débutant", 0xFF4CAF50),
    INTERMEDIATE("Intermédiaire", 0xFFFF9800),
    ADVANCED("Avancé", 0xFFE91E63),
    EXPERT("Expert", 0xFF9C27B0)
}