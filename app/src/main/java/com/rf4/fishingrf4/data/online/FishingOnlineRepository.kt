package com.rf4.fishingrf4.data.online

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/** Document Firestore “entries” (prises) */
data class OnlineEntry(
    val id: String? = null,
    val userId: String = "",
    val userName: String = "",        // affichage lisible dans l’UI
    val species: String = "",
    val weight: Double = 0.0,
    val spot: String = "",            // ex: “Lac Ladoga - 45:55”
    val lakeId: String? = null,       // id interne du lac (pour les tops par lac/position)
    val caughtAt: Timestamp = Timestamp.now()
)

/** POJO pour le Top “par espèces” (compte) */
data class SpeciesCount(
    val species: String = "",
    val count: Long = 0
)

class FishingOnlineRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val entries = db.collection("entries")
    private val baitsVotes = db.collection("baits_votes") // structure: baits_votes/{fishId}/votes/{bait}

    private fun uid(): String = auth.currentUser?.uid.orEmpty()
    private fun displayName(): String =
        auth.currentUser?.displayName ?: auth.currentUser?.email ?: "Inconnu"

    // ---------------------------
    // Ajout / lecture des prises
    // ---------------------------

    /** Ajoute une prise en ligne (si l’utilisateur est connecté) */
    suspend fun addEntry(
        species: String,
        weight: Double,
        spot: String,
        caughtAtMs: Long,
        lakeId: String? = null
    ) {
        val u = uid()
        if (u.isBlank()) return

        val doc = hashMapOf(
            "userId" to u,
            "userName" to displayName(),
            "species" to species,
            "weight" to weight,
            "spot" to spot,
            "lakeId" to lakeId,
            "caughtAt" to Timestamp(caughtAtMs / 1000, 0)
        )
        entries.add(doc).await()
    }

    /** (Facultatif) Mes prises triées par date décroissante */
    suspend fun getMyEntries(): List<OnlineEntry> {
        val u = uid(); if (u.isBlank()) return emptyList()
        val snap = entries
            .whereEqualTo("userId", u)
            .orderBy("caughtAt", Query.Direction.DESCENDING)
            .get().await()

        return snap.documents.map { d ->
            OnlineEntry(
                id = d.id,
                userId = d.getString("userId") ?: "",
                userName = d.getString("userName") ?: "",
                species = d.getString("species") ?: "",
                weight = d.getDouble("weight") ?: 0.0,
                spot = d.getString("spot") ?: "",
                lakeId = d.getString("lakeId"),
                caughtAt = d.getTimestamp("caughtAt") ?: Timestamp.now()
            )
        }
    }

    // -------------------------------------
    // TOPS DU JOUR (filtrés par startOfDay)
    // -------------------------------------

    /** Top 5 “par espèces” du jour (par nombre de prises) */
    suspend fun getTop5SpeciesByCountToday(startOfDayMs: Long): List<SpeciesCount> {
        val snap = entries
            .whereGreaterThanOrEqualTo("caughtAt", Timestamp(startOfDayMs / 1000, 0))
            .get().await()

        return snap.documents
            .groupBy { it.getString("species") ?: "?" }
            .map { (species, docs) -> SpeciesCount(species, docs.size.toLong()) }
            .sortedByDescending { it.count }
            .take(5)
    }
    suspend fun getTop5SpeciesCountsToday(startOfDayMs: Long): List<SpeciesCount> =
        getTop5SpeciesByCountToday(startOfDayMs)
    /** Top 5 “par joueurs” du jour (nb de prises) – affiche userName */
    suspend fun getTop5PlayersOfDay(startOfDayMs: Long):    List<Pair<String, Long>> {
        val snap = entries
            .whereGreaterThanOrEqualTo("caughtAt", Timestamp(startOfDayMs / 1000, 0))
            .get().await()

        return snap.documents
            .groupBy { it.getString("userName") ?: "Inconnu" }
            .map { (name, docs) -> name to docs.size.toLong() }
            .sortedByDescending { it.second }
            .take(5)
    }

    /** Top 5 “par lacs/spots” du jour (nb de prises) – regroupe sur le champ “spot” */
    suspend fun getTop5LakesOfDay(startOfDayMs: Long): List<Pair<String, Long>> {
        val snap = entries
            .whereGreaterThanOrEqualTo("caughtAt", Timestamp(startOfDayMs / 1000, 0))
            .get().await()

        return snap.documents
            .groupBy { it.getString("spot") ?: "N/A" }
            .map { (spot, docs) -> spot to docs.size.toLong() }
            .sortedByDescending { it.second }
            .take(5)
    }

    /** Top 5 positions (ex: “45:55”) du jour pour un lac précis (via lakeId) */
    suspend fun getTop5PositionsForLakeToday(lakeId: String, startOfDayMs: Long): List<Pair<String, Long>> {
        val snap = entries
            .whereEqualTo("lakeId", lakeId)
            .whereGreaterThanOrEqualTo("caughtAt", Timestamp(startOfDayMs / 1000, 0))
            .get().await()

        // On suppose que “spot” contient soit “Nom du lac - position” soit seulement la position.
        // Si tu stockes la position séparément, adapte ici.
        return snap.documents
            .mapNotNull { it.getString("spot") }
            .map { it.substringAfter(" - ", it) } // garde la partie position si format "Lac - 45:55"
            .groupBy { it }
            .map { (pos, docs) -> pos to docs.size.toLong() }
            .sortedByDescending { it.second }
            .take(5)
    }

    // -------------------------------------------------
    // Tops “appâts communautaires” (votes par poisson)
    // -------------------------------------------------
    suspend fun getTopCommunityBaitsThisMonth(fishId: String, limit: Int = 5): List<Pair<String, Long>> {
        val monthStartMs = thisMonthStartMs()
        return getTopBaitsForFishInternal(fishId, limit, monthStartMs)
    }
    /** Top appâts du jour pour un poisson (par compte) */
    suspend fun getTopBaitsForFishToday(fishId: String, limit: Int): List<Pair<String, Long>> {
        val dayStart = todayStartMs()
        return getTopBaitsForFishInternal(fishId, limit, dayStart)
    }

    /** Top appâts global pour un poisson (par compte) */
    suspend fun getTopBaitsForFish(fishId: String, limit: Int): List<Pair<String, Long>> {
        return getTopBaitsForFishInternal(fishId, limit, null)
    }
    private fun thisMonthStartMs(): Long {
        val now = java.time.LocalDateTime.now()
        val monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
        return monthStart.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    /** Ajoute/vote un appât communautaire pour un poisson */
    suspend fun addCommunityBaitForFish(fishId: String, bait: String) {
        if (bait.isBlank()) throw IllegalArgumentException("L'appât ne peut pas être vide")

        val currentUserId = uid()
        if (currentUserId.isBlank()) throw IllegalArgumentException("Vous devez être connecté pour voter")

        val docRef = baitsVotes.document(fishId).collection("votes").document(bait.lowercase())

        // Vérifier si l'utilisateur a déjà voté pour cet appât sur ce poisson
        val currentDoc = docRef.get().await()
        val currentVoters = currentDoc.get("voters") as? Map<String, Boolean> ?: emptyMap()

        // ✅ VÉRIFICATION DU DOUBLE VOTE
        if (currentVoters.containsKey(currentUserId)) {
            throw IllegalArgumentException("Vous avez déjà voté pour cet appât sur ce poisson")
        }

        // Incrémenter le compteur et ajouter le votant
        val newCount = (currentDoc.getLong("count") ?: 0L) + 1L
        val newVoters = currentVoters.plus(Pair(currentUserId, true))

        val payload = hashMapOf(
            "bait" to bait,
            "count" to newCount,
            "voters" to newVoters,
            "lastVoteAt" to com.google.firebase.Timestamp.now()
        )

        docRef.set(payload).await()
    }

    // Récupérer le Top N appâts d'un poisson pour la journée
    suspend fun getTopCommunityBaitsToday(fishId: String, limit: Int = 5): List<Pair<String, Long>> {
        val dayStartMs = todayStartMs()
        return getTopBaitsForFishInternal(fishId, limit, dayStartMs)
    }

    // -----------------------
    // Anciennes API (poids)
    // -----------------------

    /** Top 5 global (par poids) */
    suspend fun getTop5Overall(): List<OnlineEntry> {
        val snap = entries
            .orderBy("weight", Query.Direction.DESCENDING)
            .limit(5)
            .get().await()
        return snap.toObjects(OnlineEntry::class.java)
    }


    /** Top 5 par espèce (par poids) */
    suspend fun getTop5BySpecies(species: String): List<OnlineEntry> {
        val snap = entries
            .whereEqualTo("species", species)
            .orderBy("weight", Query.Direction.DESCENDING)
            .limit(5)
            .get().await()
        return snap.toObjects(OnlineEntry::class.java)
    }

    // -----------------------
    // Helpers internes
    // -----------------------

    private suspend fun getTopBaitsForFishInternal(
        fishId: String,
        limit: Int,
        startOfPeriodMs: Long?
    ): List<Pair<String, Long>> {
        val snap = baitsVotes.document(fishId).collection("votes")
            .orderBy("count", Query.Direction.DESCENDING)
            .get()
            .await()

        val all = snap.documents.mapNotNull { doc ->
            val bait = doc.getString("bait") ?: return@mapNotNull null
            val count = doc.getLong("count") ?: 0L
            val lastVoteAt = doc.getTimestamp("lastVoteAt")?.toDate()?.time ?: 0L
            Triple(bait, count, lastVoteAt)
        }

        val filtered = if (startOfPeriodMs != null) {
            // Filtrer par période (jour ou mois)
            all.filter { it.third >= startOfPeriodMs }
        } else {
            all
        }

        return filtered
            .sortedByDescending { it.second }
            .take(limit)
            .map { it.first to it.second }
    }
    suspend fun addEntryWithBait(
        species: String,
        weight: Double,
        spot: String,
        caughtAtMs: Long,
        bait: String,
        lakeId: String? = null
    ) {
        val u = uid()
        if (u.isBlank()) return

        val doc = hashMapOf(
            "userId" to u,
            "userName" to displayName(),
            "species" to species,
            "weight" to weight,
            "spot" to spot,
            "lakeId" to lakeId,
            "bait" to bait,  // ✅ Le champ appât sera maintenant rempli
            "caughtAt" to Timestamp(caughtAtMs / 1000, 0)
        )
        entries.add(doc).await()
    }

    // ✅ NOUVELLE méthode pour récupérer les vrais appâts utilisés :
    suspend fun getTopBaitsUsedToday(species: String, startOfDayMs: Long, limit: Int = 5): List<Pair<String, Long>> {
        val snap = entries
            .whereEqualTo("species", species)
            .whereGreaterThanOrEqualTo("caughtAt", Timestamp(startOfDayMs / 1000, 0))
            .get().await()

        return snap.documents
            .mapNotNull { it.getString("bait") }
            .filter { it.isNotBlank() }
            .groupBy { it }
            .map { (bait, docs) -> bait to docs.size.toLong() }
            .sortedByDescending { it.second }
            .take(limit)
    }

    // ✅ NOUVELLE méthode pour récupérer les stats par espèce avec appâts :

    suspend fun getSpeciesWithBaitStats(startOfDayMs: Long): Map<String, Pair<Long, List<Pair<String, Long>>>> {
        val snap = entries
            .whereGreaterThanOrEqualTo("caughtAt", Timestamp(startOfDayMs / 1000, 0))
            .get().await()

        val speciesData = snap.documents
            .groupBy { it.getString("species") ?: "?" }
            .mapValues { (species, docs) ->
                val totalCount = docs.size.toLong()
                val baitStats = docs
                    .mapNotNull { it.getString("bait") }
                    .filter { it.isNotBlank() } // Filtrer les appâts vides
                    .groupBy { it }
                    .map { (bait, baitDocs) -> bait to baitDocs.size.toLong() }
                    .sortedByDescending { it.second }
                    .take(5)

                totalCount to baitStats
            }

        return speciesData
    }
    private fun todayStartMs(): Long {
        // “Aujourd’hui” côté réel (pas “in-game”!)
        val nowMs = System.currentTimeMillis()
        val secondsInDay = 24 * 60 * 60
        // Simple: minuit UTC approx (pour faire court). Adapte si tu veux TZ locale.
        val dayStartSec = (nowMs / 1000) / secondsInDay * secondsInDay
        return dayStartSec * 1000
    }
}
