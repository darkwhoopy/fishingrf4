// ==========================================
// FICHIER: data/models/FavoriteSpot.kt
// Modèle pour les spots favoris
// ==========================================

package com.rf4.fishingrf4.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteSpot(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val position: String, // Format "X:Y"
    val lakeName: String,
    val lakeId: String,
    val fishNames: List<String>, // Noms des poissons cibles
    val baits: List<String>, // Appâts recommandés
    val distance: Int, // Distance en mètres
    val createdAt: Long = System.currentTimeMillis(),
    val notes: String = ""
)