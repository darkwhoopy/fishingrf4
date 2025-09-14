package com.rf4.fishingrf4.data.online

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/** Document Firestore "entries" (prises) */
data class OnlineEntry(
    val id: String? = null,
    val userId: String = "",
    val userName: String = "",        // affichage lisible dans l'UI
    val species: String = "",
    val weight: Double = 0.0,
    val spot: String = "",            // ex: "Lac Ladoga - 45:55"
    val lakeId: String? = null,       // id interne du lac (pour les tops par lac/position)
    val bait: String = "",            // ✅ AJOUT du champ appât
    val caughtAt: Timestamp = Timestamp.now()
)

/** POJO pour le Top "par espèces" (compte) */
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

    /** ✅ CORRIGÉ : Ajoute une prise en ligne AVEC l'appât */
    suspend fun addEntry(
        species: String,
        weight: Double,
        spot: String,
        caughtAtMs: Long,
        lakeId: String? = null,
        bait: String = ""  // ✅ AJOUT du paramètre appât
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
            "bait" to bait,  // ✅ AJOUT de l'appât dans les données
            "caughtAt" to Timestamp(caughtAtMs / 1000, 0)
        )
        entries.add(doc).await()
    }

    /** ✅ CORRIGÉ : Mes prises avec appât */
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
                bait = d.getString("bait") ?: "",  // ✅ RÉCUPÉRATION de l'appât
                caughtAt = d.getTimestamp("caughtAt") ?: Timestamp.now()
            )
        }
    }

    private fun getCurrentLanguage(): String {
        return java.util.Locale.getDefault().language // "fr", "en", etc.
    }

    private fun getDayStartTimestamp(timestamp: Long): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    suspend fun getTop5PlayersOfDay(timestampStart: Long): List<Pair<String, Long>> {
        return try {
            val snap = entries
                .whereGreaterThanOrEqualTo("caughtAt", Timestamp(timestampStart / 1000, 0))
                .get()
                .await()

            snap.documents
                .mapNotNull { doc ->
                    val playerName = doc.getString("userName") ?: ""
                    if (playerName.isNotBlank()) playerName else null
                }
                .groupBy { it }
                .map { (player, list) -> player to list.size.toLong() }
                .sortedByDescending { it.second }
                .take(5)
        } catch (e: Exception) {
            try {
                val snapAll = entries.get().await()
                snapAll.documents
                    .mapNotNull { doc ->
                        val playerName = doc.getString("userName") ?: ""
                        if (playerName.isNotBlank()) playerName else null
                    }
                    .groupBy { it }
                    .map { (player, list) -> player to list.size.toLong() }
                    .sortedByDescending { it.second }
                    .take(5)
            } catch (e2: Exception) {
                emptyList()
            }
        }
    }

    /** Top 5 "par espèces" du jour (par nombre de prises) */
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

    suspend fun getTop5SpeciesCountsToday(timestampStart: Long): List<SpeciesCount> {
        return try {
            val snap = entries
                .whereGreaterThanOrEqualTo("caughtAt", Timestamp(timestampStart / 1000, 0))
                .get()
                .await()

            snap.documents
                .mapNotNull { doc ->
                    val species = doc.getString("species") ?: ""
                    if (species.isNotBlank()) SpeciesCount(species, 1) else null
                }
                .groupBy { it.species }
                .map { (species, list) -> SpeciesCount(species, list.size.toLong()) }
                .sortedByDescending { it.count }
                .take(5)
        } catch (e: Exception) {
            try {
                val snapAll = entries.get().await()
                snapAll.documents
                    .mapNotNull { doc ->
                        val species = doc.getString("species") ?: ""
                        if (species.isNotBlank()) SpeciesCount(species, 1) else null
                    }
                    .groupBy { it.species }
                    .map { (species, list) -> SpeciesCount(species, list.size.toLong()) }
                    .sortedByDescending { it.count }
                    .take(5)
            } catch (e2: Exception) {
                emptyList()
            }
        }
    }

    /** Top 5 "par lacs/spots" du jour (nb de prises) – regroupe sur le champ "spot" */
    suspend fun getTop5LakesOfDay(startOfDayMs: Long): List<Pair<String, Long>> {
        return try {
            val snap = entries
                .whereGreaterThanOrEqualTo("caughtAt", Timestamp(startOfDayMs / 1000, 0))
                .get()
                .await()

            snap.documents
                .mapNotNull { doc ->
                    val spot = doc.getString("spot") ?: ""
                    if (spot.isNotBlank()) {
                        val lakeName = spot.split(" - ").firstOrNull() ?: spot
                        lakeName
                    } else null
                }
                .groupBy { it }
                .map { (lake, list) -> lake to list.size.toLong() }
                .sortedByDescending { it.second }
                .take(5)
        } catch (e: Exception) {
            try {
                val snapAll = entries.get().await()
                snapAll.documents
                    .mapNotNull { doc ->
                        val spot = doc.getString("spot") ?: ""
                        if (spot.isNotBlank()) {
                            val lakeName = spot.split(" - ").firstOrNull() ?: spot
                            lakeName
                        } else null
                    }
                    .groupBy { it }
                    .map { (lake, list) -> lake to list.size.toLong() }
                    .sortedByDescending { it.second }
                    .take(5)
            } catch (e2: Exception) {
                emptyList()
            }
        }
    }

    /** Top 5 positions (ex: "45:55") du jour pour un lac précis (via lakeId) */
    suspend fun getTop5PositionsForLakeToday(lakeId: String, startOfDayMs: Long): List<Pair<String, Long>> {
        val snap = entries
            .whereEqualTo("lakeId", lakeId)
            .whereGreaterThanOrEqualTo("caughtAt", Timestamp(startOfDayMs / 1000, 0))
            .get().await()

        return snap.documents
            .mapNotNull { it.getString("spot") }
            .map { it.substringAfter(" - ", it) }
            .groupBy { it }
            .map { (pos, docs) -> pos to docs.size.toLong() }
            .sortedByDescending { it.second }
            .take(5)
    }

    // -------------------------------------------------
    // Tops "appâts communautaires" (votes par poisson)
    // -------------------------------------------------
    suspend fun getTopCommunityBaitsThisMonth(fishId: String, limit: Int = 5): List<Pair<String, Long>> {
        val monthStartMs = thisMonthStartMs()
        return getTopBaitsForFishInternal(fishId, limit, monthStartMs)
    }

    suspend fun getTopBaitsForFishToday(fishId: String, limit: Int): List<Pair<String, Long>> {
        val dayStart = todayStartMs()
        return getTopBaitsForFishInternal(fishId, limit, dayStart)
    }

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

        val currentDoc = docRef.get().await()
        val currentVoters = currentDoc.get("voters") as? Map<String, Boolean> ?: emptyMap()

        if (currentVoters.containsKey(currentUserId)) {
            throw IllegalArgumentException("Vous avez déjà voté pour cet appât sur ce poisson")
        }

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

    suspend fun getTopCommunityBaitsToday(fishId: String, limit: Int = 5): List<Pair<String, Long>> {
        val dayStartMs = todayStartMs()
        return getTopBaitsForFishInternal(fishId, limit, dayStartMs)
    }

    // -----------------------
    // Anciennes API (poids)
    // -----------------------

    suspend fun getTop5Overall(): List<OnlineEntry> {
        val snap = entries
            .orderBy("weight", Query.Direction.DESCENDING)
            .limit(5)
            .get().await()
        return snap.toObjects(OnlineEntry::class.java)
    }

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
        species: String, speciesEn: String, weight: Double,
        spot: String, caughtAtMs: Long, bait: String,
        userId: String, playerName: String
    ) {
        val doc = hashMapOf(
            "species" to species,
            "species_en" to speciesEn,
            "weight" to weight,
            "spot" to spot,
            "timestamp" to caughtAtMs,
            "bait" to bait,
            "user_id" to userId,
            "player_name" to playerName,
            "day_start" to getDayStartTimestamp(caughtAtMs),
            "language" to getCurrentLanguage()
        )
        db.collection("fishing_entries").add(doc).await()
    }

    /** ✅ CORRIGÉ : Récupère les vrais appâts utilisés depuis la collection entries */
    suspend fun getTopBaitsUsedToday(species: String, startOfDayMs: Long, limit: Int = 5): List<Pair<String, Long>> {
        return try {
            val snap = entries
                .whereEqualTo("species", species)
                .whereGreaterThanOrEqualTo("caughtAt", Timestamp(startOfDayMs / 1000, 0))
                .get().await()

            println("=== DEBUG getTopBaitsUsedToday ===")
            println("Espèce recherchée: $species")
            println("Documents trouvés: ${snap.documents.size}")

            val baitsData = snap.documents
                .mapNotNull { doc ->
                    val baitValue = doc.getString("bait")
                    println("Document ${doc.id}: bait = '$baitValue'")
                    if (!baitValue.isNullOrBlank()) baitValue else null
                }
                .groupBy { it }
                .map { (bait, docs) -> bait to docs.size.toLong() }
                .sortedByDescending { it.second }
                .take(limit)

            println("Résultat final: $baitsData")
            baitsData

        } catch (e: Exception) {
            println("Erreur getTopBaitsUsedToday: ${e.message}")
            emptyList()
        }
    }

    /** ✅ PRINCIPALE CORRECTION : Récupère les stats par espèce avec appâts réels */
    suspend fun getSpeciesWithBaitStats(startOfDayMs: Long): Map<String, Pair<Long, List<Pair<String, Long>>>> {
        return try {
            val snap = entries
                .whereGreaterThanOrEqualTo("caughtAt", Timestamp(startOfDayMs / 1000, 0))
                .get()
                .await()

            println("=== DEBUG getSpeciesWithBaitStats ===")
            println("Documents trouvés: ${snap.documents.size}")

            // Grouper par espèce et extraire les appâts
            val speciesData = snap.documents
                .filter { doc ->
                    val species = doc.getString("species")
                    val hasSpecies = !species.isNullOrBlank()
                    if (!hasSpecies) {
                        println("Document sans espèce: ${doc.id}")
                    }
                    hasSpecies
                }
                .groupBy { doc -> doc.getString("species")!! }
                .mapValues { (species, docs) ->
                    val totalCount = docs.size.toLong()

                    // Extraire les appâts utilisés pour cette espèce
                    val baitStats = docs
                        .mapNotNull { doc ->
                            val baitValue = doc.getString("bait")
                            println("$species -> bait: '$baitValue'")
                            if (!baitValue.isNullOrBlank()) baitValue else null
                        }
                        .groupBy { it }
                        .map { (bait, baitList) -> bait to baitList.size.toLong() }
                        .sortedByDescending { it.second }
                        .take(3) // Top 3 appâts par espèce

                    println("$species: $totalCount captures, ${baitStats.size} appâts différents")
                    println("  Appâts: $baitStats")

                    totalCount to baitStats
                }

            println("=== RÉSULTAT FINAL ===")
            println("${speciesData.size} espèces trouvées")
            speciesData.forEach { (species, data) ->
                println("$species: ${data.first} captures, ${data.second.size} appâts")
            }

            speciesData

        } catch (e: Exception) {
            println("Erreur getSpeciesWithBaitStats: ${e.message}")
            e.printStackTrace()
            emptyMap()
        }
    }

    suspend fun debugBaitsInEntries(): String {
        return try {
            val snap = entries.get().await()
            val result = StringBuilder("=== DEBUG APPÂTS DANS ENTRIES ===\n")

            result.append("Nombre total d'entrées: ${snap.documents.size}\n\n")

            // Analyser les 5 premiers documents
            snap.documents.take(5).forEachIndexed { index, doc ->
                result.append("Document ${index + 1}:\n")
                result.append("  ID: ${doc.id}\n")
                result.append("  species: ${doc.getString("species")}\n")
                result.append("  bait: ${doc.getString("bait")}\n")
                result.append("  userName: ${doc.getString("userName")}\n")
                result.append("  spot: ${doc.getString("spot")}\n")
                result.append("  weight: ${doc.getDouble("weight")}\n")
                result.append("  Champs disponibles: ${doc.data?.keys?.joinToString(", ")}\n\n")
            }

            // Statistiques sur les appâts
            val baitsFound = snap.documents
                .mapNotNull { it.getString("bait") }
                .filter { it.isNotBlank() }

            result.append("=== STATISTIQUES APPÂTS ===\n")
            result.append("Documents avec appâts: ${baitsFound.size}/${snap.documents.size}\n")
            result.append("Appâts uniques: ${baitsFound.distinct().size}\n")

            if (baitsFound.isNotEmpty()) {
                result.append("Appâts trouvés: ${baitsFound.distinct().joinToString(", ")}\n")
            } else {
                result.append("⚠️ AUCUN APPÂT TROUVÉ dans les données existantes\n")
            }

            result.toString()
        } catch (e: Exception) {
            "Erreur debug: ${e.message}"
        }
    }

    private fun todayStartMs(): Long {
        val nowMs = System.currentTimeMillis()
        val secondsInDay = 24 * 60 * 60
        val dayStartSec = (nowMs / 1000) / secondsInDay * secondsInDay
        return dayStartSec * 1000
    }
}