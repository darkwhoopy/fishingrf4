// ==========================================
// FICHIER: data/models/CommunityModels.kt
// Modèles pour les signalements de bugs et suggestions de poissons
// ==========================================

package com.rf4.fishingrf4.data.models

import kotlinx.serialization.Serializable

/**
 * Modèle pour un signalement de bug
 */
@Serializable
data class BugReport(
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "",
    val userName: String = "",
    val email: String = "",
    val title: String = "",
    val description: String = "",
    val bugType: BugType = BugType.GAMEPLAY,
    val priority: BugPriority = BugPriority.LOW,
    val status: BugStatus = BugStatus.OPEN,
    val deviceInfo: String = "",
    val appVersion: String = "",
    val reproductionSteps: String = "",
    val expectedBehavior: String = "",
    val actualBehavior: String = "",
    val screenshot: String? = null, // Base64 ou URL
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val adminNotes: String = ""
)

/**
 * Types de bugs possibles
 */
enum class BugType(val displayName: String) {
    GAMEPLAY("🎮 Gameplay"),
    UI_UX("🖱️ Interface"),
    DATA("📊 Données"),
    PERFORMANCE("⚡ Performance"),
    CRASH("💥 Plantage"),
    NETWORK("🌐 Réseau"),
    AUDIO("🔊 Audio"),
    VISUAL("👁️ Visuel"),
    OTHER("🔧 Autre")
}

/**
 * Priorités des bugs
 */
enum class BugPriority(val displayName: String, val colorValue: Long) {
    LOW("🟢 Faible", 0xFF10B981),
    MEDIUM("🟡 Moyenne", 0xFFF59E0B),
    HIGH("🟠 Élevée", 0xFFEF4444),
    CRITICAL("🔴 Critique", 0xFF991B1B)
}

/**
 * Statuts des bugs
 */
enum class BugStatus(val displayName: String, val colorValue: Long) {
    OPEN("🆕 Ouvert", 0xFF3B82F6),
    IN_PROGRESS("⏳ En cours", 0xFFF59E0B),
    TESTING("🧪 En test", 0xFF8B5CF6),
    RESOLVED("✅ Résolu", 0xFF10B981),
    CLOSED("🔒 Fermé", 0xFF6B7280),
    WONT_FIX("❌ Non corrigé", 0xFFEF4444)
}

/**
 * Modèle pour une suggestion de poisson
 */
@Serializable
data class FishSuggestion(
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "",
    val userName: String = "",
    val email: String = "",
    val fishName: String = "",
    val scientificName: String = "",
    val description: String = "",
    val suggestedLakes: List<String> = emptyList(),
    val preferredBaits: List<String> = emptyList(),
    val rarity: FishRarity = FishRarity.COMMON,
    val minWeight: Double = 0.0,
    val maxWeight: Double = 0.0,
    val bestHours: List<Int> = emptyList(),
    val bestWeather: List<WeatherType> = emptyList(),
    val imageUrl: String? = null,
    val sourceUrl: String? = null, // Source d'information
    val justification: String = "", // Pourquoi ce poisson devrait être ajouté
    val votes: Int = 0,
    val status: SuggestionStatus = SuggestionStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val adminNotes: String = ""
)

/**
 * Statuts des suggestions
 */
enum class SuggestionStatus(val displayName: String, val colorValue: Long) {
    PENDING("⏳ En attente", 0xFFF59E0B),
    UNDER_REVIEW("🔍 En révision", 0xFF3B82F6),
    APPROVED("✅ Approuvée", 0xFF10B981),
    REJECTED("❌ Rejetée", 0xFFEF4444),
    IMPLEMENTED("🎉 Implémentée", 0xFF8B5CF6)
}

/**
 * Vote sur une suggestion de poisson
 */
@Serializable
data class FishVote(
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "",
    val suggestionId: String = "",
    val voteType: VoteType = VoteType.UPVOTE,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Types de votes
 */
enum class VoteType {
    UPVOTE, // Pour
    DOWNVOTE // Contre
}

/**
 * Commentaire sur une suggestion
 */
@Serializable
data class SuggestionComment(
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "",
    val userName: String = "",
    val suggestionId: String = "",
    val content: String = "",
    val isAdmin: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

// ==========================================
// À AJOUTER dans data/models/CommunityModels.kt
// Modèles pour les spots communautaires
// ==========================================

/**
 * Spot communautaire partagé par un utilisateur
 */
data class CommunitySpot(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val lakeName: String = "",
    val lakeId: String = "",
    val position: String = "",
    val name: String = "",
    val description: String = "",
    val fishNames: List<String> = emptyList(),
    val baits: List<String> = emptyList(),
    val distance: Int = 0,
    val votes: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toFirestoreMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "userName" to userName,
            "lakeName" to lakeName,
            "lakeId" to lakeId,
            "position" to position,
            "name" to name,
            "description" to description,
            "fishNames" to fishNames,
            "baits" to baits,
            "distance" to distance,
            "votes" to votes,
            "createdAt" to com.google.firebase.Timestamp(java.util.Date(createdAt)),
            "updatedAt" to com.google.firebase.Timestamp(java.util.Date(updatedAt))
        )
    }
}

/**
 * Vote pour un spot communautaire
 */
data class SpotVote(
    val id: String = "",
    val userId: String = "",
    val spotId: String = "",
    val voteType: VoteType = VoteType.UPVOTE,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toFirestoreMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "spotId" to spotId,
            "voteType" to voteType.name,
            "createdAt" to com.google.firebase.Timestamp(java.util.Date(createdAt))
        )
    }
}