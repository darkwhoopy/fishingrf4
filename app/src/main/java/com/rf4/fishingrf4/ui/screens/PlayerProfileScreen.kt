package com.rf4.fishingrf4.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.rf4.fishingrf4.auth.AuthManager
import com.rf4.fishingrf4.data.models.FishRarity
import com.rf4.fishingrf4.data.models.FishingEntry
import com.rf4.fishingrf4.data.models.PlayerStats
import com.rf4.fishingrf4.ui.components.BackButton

@Composable
fun PlayerProfileScreen(
    playerStats: PlayerStats,
    fishingEntries: List<FishingEntry>,
    onBack: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(16.dp))
            Text("👤 Profil du Pêcheur", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // ── Bloc Compte Google (connexion / déconnexion) ──────────────────────
            item { GoogleAccountCard() }

            // Tes cartes existantes
            item { PlayerStatsSummaryCard(playerStats) }
            item { LevelCard(playerStats) }
            item { RarityStatsCard(playerStats.rareFishCount) }
            if (playerStats.biggestFish != null) {
                item { BiggestFishCard(playerStats.biggestFish) }
            }
        }
    }
}

/** Carte de gestion du compte Google (affichée en haut du Profil) */
@Composable
private fun GoogleAccountCard() {
    val context = LocalContext.current
    val activity = context as? Activity
    // Si jamais ce composable est pré-visualisé sans Activity, on ne casse rien
    if (activity == null) {
        Text("Prévisualisation…")
        return
    }

    val authManager = remember { AuthManager(activity) }
    val firebaseAuth = remember { FirebaseAuth.getInstance() }
    var currentUser by remember { mutableStateOf(firebaseAuth.currentUser) }

    // Écoute les changements d'authentification
    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        firebaseAuth.addAuthStateListener(listener)
        onDispose { firebaseAuth.removeAuthStateListener(listener) }
    }

    // Launcher pour récupérer le résultat de Google Sign-In
    val googleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        authManager.handleSignInResult(
            data = result.data,
            onSuccess = { /* currentUser se mettra à jour via l'AuthStateListener */ },
            onError = { /* TODO: afficher un Snackbar si besoin */ }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Compte Google", style = MaterialTheme.typography.titleMedium, color = Color.White)

            if (currentUser == null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Connecte-toi pour sauvegarder en ligne et participer aux Top 5.",
                    color = Color.White
                )
                Spacer(Modifier.height(12.dp))
                Button(onClick = {
                    val intent = authManager.googleClient().signInIntent
                    googleLauncher.launch(intent)
                }) {
                    Text("Se connecter avec Google")
                }
            } else {
                Spacer(Modifier.height(8.dp))
                Text("Connecté en tant que :", color = Color.White)
                Text(currentUser?.displayName ?: currentUser?.email ?: "Utilisateur", color = Color.White)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { authManager.signOut() }) { Text("Se déconnecter") }
                }
            }
        }
    }
}

// ── TES COMPOSABLES EXISTANTS ──────────────────────────────────────────────────

@Composable
private fun PlayerStatsSummaryCard(playerStats: PlayerStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatColumn("🐟", "${playerStats.totalCatches}", "Prises")
            StatColumn("🏆", "${playerStats.totalPoints}", "Points")
            StatColumn("📍", playerStats.favoriteSpot.ifEmpty { "N/A" }, "Spot favori")
        }
    }
}

@Composable
private fun LevelCard(playerStats: PlayerStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3B82F6))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏆 Niveau ${playerStats.level}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun BiggestFishCard(biggestFish: FishingEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🐟 Plus grosse prise",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text("${biggestFish.fish.name} - ${biggestFish.weight ?: "N/A"} kg", color = Color.White)
        }
    }
}

@Composable
private fun StatsCard(playerStats: PlayerStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📊 Statistiques",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatColumn("🐟", "${playerStats.totalCatches}", "Prises totales")
                StatColumn("🏆", "${playerStats.totalPoints}", "Points totaux")
                StatColumn("📍", playerStats.favoriteSpot.ifEmpty { "Aucun" }, "Spot favori")
            }
        }
    }
}

@Composable
private fun StatColumn(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 20.sp)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = label, fontSize = 10.sp, color = Color(0xFF9CA3AF))
    }
}

@Composable
private fun RarityStatsCard(rareFishCount: Map<FishRarity, Int>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🌟 Poissons par rareté",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            FishRarity.values().forEach { rarity ->
                val count = rareFishCount[rarity] ?: 0
                if (count > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(rarity.colorValue)),
                                shape = CircleShape,
                                modifier = Modifier.size(12.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = rarity.displayName, fontSize = 14.sp, color = Color.White)
                        }
                        Text(text = count.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
