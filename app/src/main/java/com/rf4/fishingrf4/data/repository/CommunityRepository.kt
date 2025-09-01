// ==========================================
// FICHIER: data/repository/CommunityRepository.kt
// Gestion des signalements de bugs et suggestions de poissons
// ==========================================

package com.rf4.fishingrf4.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rf4.fishingrf4.data.models.*
import kotlinx.coroutines.tasks.await

/**
 * Repository pour gérer les fonctionnalités communautaires
 * (signalements de bugs et suggestions de poissons)
 */
class CommunityRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    // Collections Firestore
    private val bugReports = db.collection("bug_reports")
    private val fishSuggestions = db.collection("fish_suggestions")
    private val fishVotes = db.collection("fish_votes")
    private val suggestionComments = db.collection("suggestion_comments")

    // Utilitaires d'authentification
    private fun uid(): String = auth.currentUser?.uid.orEmpty()
    private fun displayName(): String =
        auth.currentUser?.displayName ?: auth.currentUser?.email ?: "Anonyme"
    private fun userEmail(): String = auth.currentUser?.email.orEmpty()

    // ==========================================
    // GESTION DES SIGNALEMENTS DE BUGS
    // ==========================================

    /**
     * Soumet un nouveau signalement de bug
     */
    suspend fun submitBugReport(
        title: String,
        description: String,
        bugType: BugType,
        reproductionSteps: String,
        expectedBehavior: String,
        actualBehavior: String,
        deviceInfo: String,
        appVersion: String
    ): String {
        val userId = uid()
        if (userId.isBlank()) throw Exception("Vous devez être connecté pour signaler un bug")

        val bugReport = BugReport(
            userId = userId,
            userName = displayName(),
            email = userEmail(),
            title = title,
            description = description,
            bugType = bugType,
            reproductionSteps = reproductionSteps,
            expectedBehavior = expectedBehavior,
            actualBehavior = actualBehavior,
            deviceInfo = deviceInfo,
            appVersion = appVersion
        )

        val docRef = bugReports.add(bugReport.toFirestoreMap()).await()
        return docRef.id
    }

    /**
     * Récupère les signalements de l'utilisateur connecté
     */
    // ✅ CORRECTION dans CommunityRepository.kt pour getMyBugReports

    // ✅ AJOUT DE LOGS pour déboguer les bugs
    suspend fun getMyBugReports(): List<BugReport> {
        val userId = uid()
        android.util.Log.d("CommunityRepo", "getMyBugReports - userId: $userId")

        if (userId.isBlank()) {
            android.util.Log.w("CommunityRepo", "Utilisateur non connecté")
            return emptyList()
        }

        try {
            android.util.Log.d("CommunityRepo", "Récupération des bugs pour userId: $userId")

            val snapshot = bugReports
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            android.util.Log.d("CommunityRepo", "Nombre de bugs trouvés: ${snapshot.documents.size}")

            // Log détaillé de chaque document
            snapshot.documents.forEach { doc ->
                android.util.Log.d("CommunityRepo", "Bug document: ${doc.id} - data: ${doc.data}")
            }

            val reports = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.data?.let { data ->
                        android.util.Log.d("CommunityRepo", "Traitement bug: ${doc.id}")
                        val adminNotes = data["adminNotes"] as? String ?: ""
                        android.util.Log.d("CommunityRepo", "Bug ${doc.id} - adminNotes: '$adminNotes' - length: ${adminNotes.length}")
                        BugReport(
                            id = doc.id,
                            userId = data["userId"] as? String ?: "",
                            userName = data["userName"] as? String ?: "",
                            email = data["email"] as? String ?: "",
                            title = data["title"] as? String ?: "",
                            description = data["description"] as? String ?: "",
                            bugType = try {
                                BugType.valueOf(data["bugType"] as? String ?: "OTHER")
                            } catch (e: Exception) {
                                BugType.OTHER
                            },
                            priority = try {
                                BugPriority.valueOf(data["priority"] as? String ?: "LOW")
                            } catch (e: Exception) {
                                BugPriority.LOW
                            },
                            status = try {
                                BugStatus.valueOf(data["status"] as? String ?: "OPEN")
                            } catch (e: Exception) {
                                BugStatus.OPEN
                            },
                            deviceInfo = data["deviceInfo"] as? String ?: "",
                            appVersion = data["appVersion"] as? String ?: "",
                            reproductionSteps = data["reproductionSteps"] as? String ?: "",
                            expectedBehavior = data["expectedBehavior"] as? String ?: "",
                            actualBehavior = data["actualBehavior"] as? String ?: "",
                            createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate()?.time ?: System.currentTimeMillis(),
                            updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate()?.time ?: System.currentTimeMillis(),
                            adminNotes = data["adminNotes"] as? String ?: ""
                        )
                    }
                } catch (e: Exception) {
                    android.util.Log.e("CommunityRepo", "Erreur traitement bug ${doc.id}: ${e.message}")
                    null
                }
            }

            android.util.Log.d("CommunityRepo", "Bugs récupérés avec succès: ${reports.size}")
            return reports

        } catch (e: Exception) {
            android.util.Log.e("CommunityRepo", "Erreur getMyBugReports: ${e.message}", e)
            return emptyList()
        }
    }

    /**
     * Récupère tous les signalements de bugs (pour les admins)
     */
    suspend fun getAllBugReports(limit: Int = 50): List<BugReport> {
        val snapshot = bugReports
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            try {
                doc.data?.let { data ->
                    BugReport(
                        id = doc.id,
                        userId = data["userId"] as? String ?: "",
                        userName = data["userName"] as? String ?: "",
                        email = data["email"] as? String ?: "",
                        title = data["title"] as? String ?: "",
                        description = data["description"] as? String ?: "",
                        bugType = BugType.valueOf(data["bugType"] as? String ?: "OTHER"),
                        priority = BugPriority.valueOf(data["priority"] as? String ?: "LOW"),
                        status = BugStatus.valueOf(data["status"] as? String ?: "OPEN"),
                        deviceInfo = data["deviceInfo"] as? String ?: "",
                        appVersion = data["appVersion"] as? String ?: "",
                        reproductionSteps = data["reproductionSteps"] as? String ?: "",
                        expectedBehavior = data["expectedBehavior"] as? String ?: "",
                        actualBehavior = data["actualBehavior"] as? String ?: "",
                        createdAt = (data["createdAt"] as? Timestamp)?.toDate()?.time ?: 0L,
                        updatedAt = (data["updatedAt"] as? Timestamp)?.toDate()?.time ?: 0L,
                        adminNotes = data["adminNotes"] as? String ?: ""
                    )
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    // ==========================================
    // GESTION DES SUGGESTIONS DE POISSONS
    // ==========================================

    /**
     * Soumet une nouvelle suggestion de poisson
     */
    suspend fun submitFishSuggestion(
        fishName: String,
        scientificName: String,
        description: String,
        suggestedLakes: List<String>,
        preferredBaits: List<String>,
        rarity: FishRarity,
        minWeight: Double,
        maxWeight: Double,
        bestHours: List<Int>,
        bestWeather: List<WeatherType>,
        justification: String,
        sourceUrl: String?
    ): String {
        val userId = uid()
        if (userId.isBlank()) throw Exception("Vous devez être connecté pour suggérer un poisson")

        val suggestion = FishSuggestion(
            userId = userId,
            userName = displayName(),
            email = userEmail(),
            fishName = fishName,
            scientificName = scientificName,
            description = description,
            suggestedLakes = suggestedLakes,
            preferredBaits = preferredBaits,
            rarity = rarity,
            minWeight = minWeight,
            maxWeight = maxWeight,
            bestHours = bestHours,
            bestWeather = bestWeather,
            justification = justification,
            sourceUrl = sourceUrl
        )

        val docRef = fishSuggestions.add(suggestion.toFirestoreMap()).await()
        return docRef.id
    }

    /**
     * Récupère toutes les suggestions de poissons
     */
    // ✅ CORRECTION dans CommunityRepository.kt
// Dans la fonction getAllFishSuggestions, ajoutez des logs :

    suspend fun getAllFishSuggestions(limit: Int = 50): List<FishSuggestion> {
        try {
            android.util.Log.d("CommunityRepo", "Récupération des suggestions...")

            val snapshot = fishSuggestions
                .orderBy("votes", Query.Direction.DESCENDING)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            android.util.Log.d("CommunityRepo", "Nombre de documents trouvés: ${snapshot.documents.size}")

            val suggestions = snapshot.documents.mapNotNull { doc ->
                try {
                    android.util.Log.d("CommunityRepo", "Traitement du document: ${doc.id}")
                    doc.data?.let { data ->
                        FishSuggestion(
                            id = doc.id,
                            userId = data["userId"] as? String ?: "",
                            userName = data["userName"] as? String ?: "",
                            email = data["email"] as? String ?: "",
                            fishName = data["fishName"] as? String ?: "",
                            scientificName = data["scientificName"] as? String ?: "",
                            description = data["description"] as? String ?: "",
                            suggestedLakes = (data["suggestedLakes"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                            preferredBaits = (data["preferredBaits"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                            rarity = try {
                                FishRarity.valueOf(data["rarity"] as? String ?: "COMMON")
                            } catch (e: Exception) {
                                FishRarity.COMMON
                            },
                            minWeight = (data["minWeight"] as? Number)?.toDouble() ?: 0.0,
                            maxWeight = (data["maxWeight"] as? Number)?.toDouble() ?: 0.0,
                            bestHours = (data["bestHours"] as? List<*>)?.mapNotNull { (it as? Number)?.toInt() } ?: emptyList(),
                            bestWeather = (data["bestWeather"] as? List<*>)?.mapNotNull {
                                try { WeatherType.valueOf(it as String) } catch (e: Exception) { null }
                            } ?: emptyList(),
                            justification = data["justification"] as? String ?: "",
                            sourceUrl = data["sourceUrl"] as? String,
                            votes = (data["votes"] as? Number)?.toInt() ?: 0,
                            status = try {
                                SuggestionStatus.valueOf(data["status"] as? String ?: "PENDING")
                            } catch (e: Exception) {
                                SuggestionStatus.PENDING
                            },
                            createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate()?.time ?: System.currentTimeMillis(),
                            updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate()?.time ?: System.currentTimeMillis(),
                            adminNotes = data["adminNotes"] as? String ?: ""
                        )
                    }
                } catch (e: Exception) {
                    android.util.Log.e("CommunityRepo", "Erreur traitement document ${doc.id}: ${e.message}")
                    null
                }
            }

            android.util.Log.d("CommunityRepo", "Suggestions récupérées: ${suggestions.size}")
            return suggestions

        } catch (e: Exception) {
            android.util.Log.e("CommunityRepo", "Erreur getAllFishSuggestions: ${e.message}", e)
            return emptyList()
        }
    }

    /**
     * Vote pour une suggestion de poisson
     */
    suspend fun voteForFishSuggestion(suggestionId: String, voteType: VoteType) {
        val userId = uid()
        if (userId.isBlank()) throw Exception("Vous devez être connecté pour voter")

        // Vérifier si l'utilisateur a déjà voté
        val existingVote = fishVotes
            .whereEqualTo("userId", userId)
            .whereEqualTo("suggestionId", suggestionId)
            .get()
            .await()

        if (!existingVote.isEmpty) {
            throw Exception("Vous avez déjà voté pour cette suggestion")
        }

        // Ajouter le vote
        val vote = FishVote(
            userId = userId,
            suggestionId = suggestionId,
            voteType = voteType
        )

        fishVotes.add(vote.toFirestoreMap()).await()

        // Mettre à jour le compteur de votes
        updateVoteCount(suggestionId)
    }

    /**
     * Récupère les votes d'un utilisateur
     */
    suspend fun getUserVotes(userId: String): List<FishVote> {
        val snapshot = fishVotes
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            try {
                doc.data?.let { data ->
                    FishVote(
                        id = doc.id,
                        userId = data["userId"] as? String ?: "",
                        suggestionId = data["suggestionId"] as? String ?: "",
                        voteType = VoteType.valueOf(data["voteType"] as? String ?: "UPVOTE"),
                        createdAt = (data["createdAt"] as? Timestamp)?.toDate()?.time ?: 0L
                    )
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Met à jour le compteur de votes d'une suggestion
     */
    private suspend fun updateVoteCount(suggestionId: String) {
        val upvotes = fishVotes
            .whereEqualTo("suggestionId", suggestionId)
            .whereEqualTo("voteType", VoteType.UPVOTE.name)
            .get()
            .await()
            .size()

        val downvotes = fishVotes
            .whereEqualTo("suggestionId", suggestionId)
            .whereEqualTo("voteType", VoteType.DOWNVOTE.name)
            .get()
            .await()
            .size()

        val totalVotes = upvotes - downvotes

        fishSuggestions.document(suggestionId)
            .update("votes", totalVotes)
            .await()
    }

    // ==========================================
    // FONCTIONS D'EXTENSION POUR FIRESTORE
    // ==========================================

    /**
     * Convertit un BugReport en Map pour Firestore
     */
    private fun BugReport.toFirestoreMap(): Map<String, Any> = mapOf(
        "userId" to userId,
        "userName" to userName,
        "email" to email,
        "title" to title,
        "description" to description,
        "bugType" to bugType.name,
        "priority" to priority.name,
        "status" to status.name,
        "deviceInfo" to deviceInfo,
        "appVersion" to appVersion,
        "reproductionSteps" to reproductionSteps,
        "expectedBehavior" to expectedBehavior,
        "actualBehavior" to actualBehavior,
        "createdAt" to Timestamp(createdAt / 1000, 0),
        "updatedAt" to Timestamp(updatedAt / 1000, 0),
        "adminNotes" to adminNotes
    )

    /**
     * Convertit une FishSuggestion en Map pour Firestore
     */
    private fun FishSuggestion.toFirestoreMap(): Map<String, Any> = mapOf(
        "userId" to userId,
        "userName" to userName,
        "email" to email,
        "fishName" to fishName,
        "scientificName" to scientificName,
        "description" to description,
        "suggestedLakes" to suggestedLakes,
        "preferredBaits" to preferredBaits,
        "rarity" to rarity.name,
        "minWeight" to minWeight,
        "maxWeight" to maxWeight,
        "bestHours" to bestHours,
        "bestWeather" to bestWeather.map { it.name },
        "justification" to justification,
        "sourceUrl" to (sourceUrl ?: ""),
        "votes" to votes,
        "status" to status.name,
        "createdAt" to Timestamp(createdAt / 1000, 0),
        "updatedAt" to Timestamp(updatedAt / 1000, 0),
        "adminNotes" to adminNotes
    )

    /**
     * Convertit un FishVote en Map pour Firestore
     */
    private fun FishVote.toFirestoreMap(): Map<String, Any> = mapOf(
        "userId" to userId,
        "suggestionId" to suggestionId,
        "voteType" to voteType.name,
        "createdAt" to Timestamp(createdAt / 1000, 0)
    )
}