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

    /** Top appâts du jour pour un poisson (par compte) */
    suspend fun getTopBaitsForFishToday(fishId: String, limit: Int): List<Pair<String, Long>> {
        val dayStart = todayStartMs()
        return getTopBaitsForFishInternal(fishId, limit, dayStart)
    }

    /** Top appâts global pour un poisson (par compte) */
    suspend fun getTopBaitsForFish(fishId: String, limit: Int): List<Pair<String, Long>> {
        return getTopBaitsForFishInternal(fishId, limit, null)
    }

    /** Ajoute/vote un appât communautaire pour un poisson */
    suspend fun addCommunityBaitForFish(fishId: String, bait: String) {
        if (bait.isBlank()) return  // Assurez-vous que l'appât n'est pas vide
        val docRef = baitsVotes.document(fishId).collection("votes").document(bait.lowercase()) // Référence Firestore pour l'appât

        // Vérifier l'existence actuelle du document de l'appât
        val cur = docRef.get().await()
        val newCount = (cur.getLong("count") ?: 0L) + 1L  // Incrémenter le compteur de l'appât
        val voters = cur.get("voters") as? Map<String, Boolean> ?: emptyMap() // Si aucun "voters" n'existe, initialiser comme une carte vide

        // Mettre à jour les données : ajouter le voter et incrémenter le compteur
        val payload = hashMapOf(
            "bait" to bait,
            "count" to newCount,
            "voters" to voters.plus(Pair(uid(), true)), // Ajouter l'utilisateur actuel aux votants
            "lastVoteAt" to Timestamp.now()
        )
        docRef.set(payload).await() // Mettre à jour le document Firestore
    }

    // Récupérer le Top N appâts d'un poisson pour la journée
    suspend fun getTopCommunityBaitsToday(fishId: String, limit: Int = 5): List<Pair<String, Long>> {
        val todayId = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE) // ex: 20250301
        val snap = db.collection("fish_bait_votes")
            .document(fishId)
            .collection("votes")
            .get()
            .await()

        return snap.documents.map { doc ->
            val count = (doc.get("days.$todayId.count") as? Long) ?: 0L
            doc.id to count
        }
            .sortedByDescending { it.second }
            .take(limit)
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
        startOfDayMs: Long?
    ): List<Pair<String, Long>> {
        // Structure: baits_votes/{fishId}/votes/{bait} => { bait, count, lastVoteAt }
        var q = baitsVotes.document(fishId).collection("votes").orderBy("count", Query.Direction.DESCENDING)
        // Si tu veux filtrer “du jour”, on peut lire tout et filtrer sur lastVoteAt >= startOfDay
        val snap = q.get().await()

        val all = snap.documents.mapNotNull { d ->
            val bait = d.getString("bait") ?: return@mapNotNull null
            val count = d.getLong("count") ?: 0L
            val lastAt = d.getTimestamp("lastVoteAt")?.toDate()?.time ?: 0L
            Triple(bait, count, lastAt)
        }

        val filtered = if (startOfDayMs != null) {
            all.filter { it.third >= startOfDayMs }
        } else {
            all
        }

        return filtered
            .sortedByDescending { it.second }
            .take(limit)
            .map { it.first to it.second }
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
