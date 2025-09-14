package com.rf4.fishingrf4.ui.components

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.rf4.fishingrf4.data.models.PlayerStats
import com.rf4.fishingrf4.ui.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.auth.AuthManager
import com.rf4.fishingrf4.utils.LanguageManager
import kotlinx.coroutines.delay
import com.rf4.fishingrf4.utils.getLocalizedName

@Composable
fun AppHeader(
    playerStats: PlayerStats,
    onNavigate: (Screen) -> Unit,
    onSettingsClick: () -> Unit,
    onLevelChange: (Int) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    // ✅ États existants pour la recomposition
    var refreshTrigger by remember { mutableStateOf(0) }
    var currentLanguage by remember { mutableStateOf(LanguageManager.getCurrentLanguage(context)) }
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    // ✅ NOUVEAUX états pour le popup de connexion
    val authManager = remember { if (activity != null) AuthManager(activity) else null }
    var showConnectionPopup by remember { mutableStateOf(false) }
    var hasShownPopupOnce by remember { mutableStateOf(false) }
    var authMessage by remember { mutableStateOf<String?>(null) }
    var isAuthLoading by remember { mutableStateOf(false) }

    // ✅ Launcher pour Google Sign-In
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        authManager?.let { manager ->
            isAuthLoading = true
            manager.handleSignInResult(
                data = result.data,
                onSuccess = {
                    isAuthLoading = false
                    val user = FirebaseAuth.getInstance().currentUser
                    authMessage = "Connexion réussie ! Bienvenue ${user?.displayName ?: user?.email} 🎣"
                    showConnectionPopup = false
                    currentUser = user
                },
                onError = { error ->
                    isAuthLoading = false
                    authMessage = "Erreur de connexion: $error"
                }
            )
        }
    }

    // ✅ Surveillance existante du changement de langue et de connexion
    LaunchedEffect(refreshTrigger) {
        while (true) {
            delay(500) // Vérification toutes les 500ms
            val newLanguage = LanguageManager.getCurrentLanguage(context)
            val newUser = FirebaseAuth.getInstance().currentUser

            if (newLanguage != currentLanguage || newUser != currentUser) {
                currentLanguage = newLanguage
                currentUser = newUser
                refreshTrigger++
            }
        }
    }

    // ✅ NOUVEAU : Afficher le popup automatiquement au démarrage si pas connecté
    LaunchedEffect(currentUser, hasShownPopupOnce) {
        if (currentUser == null && !hasShownPopupOnce && authManager != null) {
            // Délai pour laisser l'app se charger complètement
            delay(2000)
            showConnectionPopup = true
            hasShownPopupOnce = true
        }
    }

    // ✅ NOUVEAU : Gestion de l'affichage des messages d'auth
    LaunchedEffect(authMessage) {
        if (authMessage != null) {
            delay(3000)
            authMessage = null
        }
    }

    Box {
        Column {
            // ========== BULLE DU HAUT (TITRE + NIVEAU) - TRADUITE ==========
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Partie gauche (logo + titre)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Logo stylisé avec gradient
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

                    // Titre stylisé avec effets - TRADUIT
                    Column {
                        // Titre principal avec gradient
                        Text(
                            text = stringResource(R.string.app_title),  // ✅ SE MET À JOUR
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Sous-titre avec badge de connexion ET version
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // ✅ MODIFIÉ : Badge de connexion stylisé - CLIQUABLE
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (currentUser != null) Color(0xFF10B981) else Color(0xFFF59E0B)
                                ),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier
                                    .height(24.dp)
                                    .clickable {
                                        if (currentUser != null) {
                                            // Déconnexion
                                            authManager?.signOut()
                                            authMessage = "Déconnexion réussie 👋"
                                            currentUser = null
                                        } else {
                                            // Connexion
                                            showConnectionPopup = true
                                        }
                                    }
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
                                        text = if (currentUser != null) stringResource(R.string.status_online) else stringResource(R.string.status_offline),  // ✅ SE MET À JOUR
                                        fontSize = 8.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Badge de version - TRADUIT
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
                                        text = "v0.7",  // ✅ SE MET À JOUR
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

                // Partie droite (sélecteur de niveau) - TRADUIT
                LevelSelector(level = playerStats.level, onLevelChange = onLevelChange)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // ========== PREMIÈRE LIGNE (3 BOUTONS TRADUITS) ==========
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NavButton(
                    text = stringResource(R.string.nav_search),  // ✅ SE MET À JOUR
                    icon = Icons.Default.Search,
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigate(Screen.FISH_SEARCH)
                }

                NavButton(
                    text = stringResource(R.string.nav_profile),  // ✅ SE MET À JOUR
                    icon = Icons.Default.Person,
                    color = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigate(Screen.PLAYER_PROFILE)
                }

                NavButton(
                    text = stringResource(R.string.nav_journal),  // ✅ SE MET À JOUR
                    icon = Icons.Default.Book,
                    color = Color(0xFF0EA5E9),
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigate(Screen.JOURNAL)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ========== DEUXIÈME LIGNE (3 BOUTONS TRADUITS) ==========
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NavButton(
                    text = stringResource(R.string.nav_settings),  // ✅ SE MET À JOUR
                    icon = Icons.Default.Settings,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.weight(1f)
                ) {
                    onSettingsClick()
                }

                NavButton(
                    text = stringResource(R.string.nav_community),  // ✅ SE MET À JOUR
                    icon = Icons.Default.Group,
                    color = Color(0xFFE11D48),
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigate(Screen.COMMUNITY)
                }

                NavButton(
                    text = stringResource(R.string.nav_top5),  // ✅ SE MET À JOUR
                    icon = Icons.Default.EmojiEvents,
                    color = Color(0xFFFFB74D),
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigate(Screen.TOP_FIVE)
                }
            }
        }

        // ✅ NOUVEAU : Message d'authentification
        authMessage?.let { message ->
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
                    .zIndex(10f),
                colors = CardDefaults.cardColors(
                    containerColor = if (message.contains("Erreur")) Color(0xFFDC2626) else Color(0xFF10B981)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // ✅ NOUVEAU : Popup de connexion non-intrusif
        if (showConnectionPopup && authManager != null) {
            Dialog(
                onDismissRequest = { showConnectionPopup = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2937))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header avec bouton fermer
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🌐 Connexion communautaire",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            IconButton(
                                onClick = { showConnectionPopup = false }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Fermer",
                                    tint = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Icône principale
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF10B981),
                                            Color(0xFF059669)
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Message principal
                        Text(
                            text = "Rejoindre la communauté ?",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Description des avantages
                        Text(
                            text = "• Partager vos captures avec d'autres pêcheurs\n• Voter pour les meilleurs appâts\n• Accéder aux classements en temps réel\n• Découvrir de nouveaux spots de pêche",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Start,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Note de non-obligation
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "💡 La connexion est totalement optionnelle.\nVous pouvez utiliser l'app sans vous connecter !",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 12.sp,
                                color = Color(0xFF10B981),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Boutons d'action
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Bouton "Peut-être plus tard"
                            OutlinedButton(
                                onClick = { showConnectionPopup = false },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.Gray
                                )
                            ) {
                                Text(
                                    text = "Plus tard",
                                    fontSize = 14.sp
                                )
                            }

                            // Bouton "Se connecter avec Google"
                            Button(
                                onClick = {
                                    if (activity != null) {
                                        isAuthLoading = true
                                        val intent = authManager.googleClient().signInIntent
                                        googleSignInLauncher.launch(intent)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF10B981)
                                ),
                                enabled = !isAuthLoading
                            ) {
                                if (isAuthLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = Color.White
                                    )
                                } else {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "🚀",
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Rejoindre",
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
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
            Text(
                text = stringResource(R.string.level_label),  // ✅ SE MET À JOUR
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.8f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                LevelChangeButton(
                    icon = Icons.Default.Remove,
                    enabled = level > 1
                ) {
                    onLevelChange(level - 1)
                }
                Text(
                    text = "$level",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                LevelChangeButton(
                    icon = Icons.Default.Add,
                    enabled = level < 50
                ) {
                    onLevelChange(level + 1)
                }
            }
        }
    }
}

@Composable
private fun LevelChangeButton(
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(28.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) Color.White else Color.Gray
        )
    }
}

@Composable
fun NavButton(
    text: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text
            )
            Text(
                text = text,
                fontSize = 12.sp
            )
        }
    }
}