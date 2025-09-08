package com.rf4.fishingrf4.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.models.PlayerStats
import com.rf4.fishingrf4.ui.navigation.Screen
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.rf4.fishingrf4.R


@Composable
fun AppHeader(
    playerStats: PlayerStats,
    onNavigate: (Screen) -> Unit,
    onSettingsClick: () -> Unit,
    onLevelChange: (Int) -> Unit
) {
    Column {
// ========== BULLE DU HAUT (TITRE + NIVEAU) - INCHANGÉE ==========
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween, // ✅ AJOUTÉ
            modifier = Modifier.fillMaxWidth() // ✅ AJOUTÉ
        ) {
            // Partie gauche (logo + titre)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // ✅ NOUVEAU : Logo stylisé avec gradient
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFD700), // Or
                                    Color(0xFFFF8C00), // Orange doré
                                    Color(0xFFFF4500)  // Rouge orangé
                                ),
                                radius = 50f
                            ),
                            shape = CircleShape
                        )
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF1E40AF),
                                        Color(0xFF3B82F6)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎣",
                            fontSize = 24.sp,
                            modifier = Modifier.offset(y = (-1).dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // ✅ NOUVEAU : Titre stylisé avec effets
                Column {
                    // Titre principal avec gradient
                    Text(
                        text = "RF4 ASSISTANT",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // ✅ NOUVEAU : Sous-titre avec badge de connexion ET version
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Badge de connexion stylisé
                        val user = FirebaseAuth.getInstance().currentUser
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (user != null) Color(0xFF10B981) else Color(0xFFF59E0B)
                            ),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(24.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            color = Color.White,
                                            shape = CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (user != null) "ONLINE" else "OFFLINE",
                                    fontSize = 8.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // ✅ NOUVEAU : Badge de version
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF6366F1) // Violet/indigo
                            ),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(24.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "v0.5",
                                    fontSize = 8.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.3.sp
                                )
                            }
                        }
                    }
                }
            }

            // ✅ NOUVEAU : Partie droite (sélecteur de niveau)
            LevelSelector(level = playerStats.level, onLevelChange = onLevelChange)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // ========== PREMIÈRE LIGNE (3 BULLES) ==========
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NavButton(
                text = "Recherche",
                icon = Icons.Default.Search,
                color = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.FISH_SEARCH)
            }

            NavButton(
                text = "Profil",
                icon = Icons.Default.Person,
                color = Color(0xFF8B5CF6),
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.PLAYER_PROFILE)
            }

            NavButton(
                text = "Journal",
                icon = Icons.Default.Book,
                color = Color(0xFF0EA5E9),
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.JOURNAL)
            }
        }



        Spacer(modifier = Modifier.height(8.dp))

        // ========== DEUXIÈME LIGNE (3 BULLES) ==========
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NavButton(
                text = "Paramètres",
                icon = Icons.Default.Settings,
                color = Color(0xFF6B7280),
                modifier = Modifier.weight(1f)
            ) {
                onSettingsClick()
            }

            NavButton(
                text = "Communauté",
                icon = Icons.Default.Group,
                color = Color(0xFFE11D48),
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.COMMUNITY) // ✅ Maintenant ça fonctionne !
            }

            NavButton(
                text = "Top 5",
                icon = Icons.Default.EmojiEvents,
                color = Color(0xFFFFB74D),
                modifier = Modifier.weight(1f)
            ) {
                onNavigate(Screen.TOP_FIVE)
            }
        }
    }
}
@Composable
private fun LevelSelector(level: Int, onLevelChange: (Int) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Text(LocalContext.current.getString(R.string.level_label), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.8f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LevelChangeButton(icon = Icons.Default.Remove, enabled = level > 1) { onLevelChange(level - 1) }
                Text(text = "$level", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 12.dp))
                LevelChangeButton(icon = Icons.Default.Add, enabled = level < 50) { onLevelChange(level + 1) }
            }
        }
    }
}
@Composable
private fun LevelChangeButton(icon: ImageVector, enabled: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick, enabled = enabled, modifier = Modifier.size(28.dp)) {
        Icon(icon, contentDescription = null, tint = if (enabled) Color.White else Color.Gray)
    }
}
@Composable
fun NavButton(text: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, contentDescription = text)
            Text(text = text, fontSize = 12.sp)
        }
    }
}

