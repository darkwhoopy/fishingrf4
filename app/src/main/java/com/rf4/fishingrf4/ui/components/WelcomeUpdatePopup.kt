package com.rf4.fishingrf4.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rf4.fishingrf4.R

/**
 * Mode du popup
 */
enum class PopupMode {
    WELCOME,    // Accueil nouveaux utilisateurs
    UPDATE      // Mise à jour
}

/**
 * Popup intelligent qui s'adapte :
 * - WELCOME mode : Accueil nouveaux utilisateurs
 * - UPDATE mode : Annonce de mise à jour
 */
@Composable
fun WelcomeUpdatePopup(
    mode: PopupMode = PopupMode.WELCOME,
    currentVersion: String = "v1.0.0",
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2937))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header avec icône et bouton fermer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icône adaptative
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = when (mode) {
                                        PopupMode.WELCOME -> listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))
                                        PopupMode.UPDATE -> listOf(Color(0xFF10B981), Color(0xFF059669))
                                    }
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (mode) {
                                PopupMode.WELCOME -> Icons.Default.EmojiPeople
                                PopupMode.UPDATE -> Icons.Default.NewReleases
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.button_close),
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Titre adaptatif
                Text(
                    text = when (mode) {
                        PopupMode.WELCOME -> stringResource(R.string.welcome_title)
                        PopupMode.UPDATE -> stringResource(R.string.update_title, currentVersion)
                    },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sous-titre adaptatif
                Text(
                    text = when (mode) {
                        PopupMode.WELCOME -> stringResource(R.string.welcome_subtitle)
                        PopupMode.UPDATE -> stringResource(R.string.update_subtitle)
                    },
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Contenu principal
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF374151)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = when (mode) {
                                PopupMode.WELCOME -> stringResource(R.string.welcome_features_title)
                                PopupMode.UPDATE -> stringResource(R.string.update_whats_new)
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when (mode) {
                                PopupMode.WELCOME -> Color(0xFF3B82F6)
                                PopupMode.UPDATE -> Color(0xFF10B981)
                            },
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Fonctionnalités adaptatives
                        when (mode) {
                            PopupMode.WELCOME -> {
                                WelcomeFeature(
                                    icon = "🎣",
                                    title = stringResource(R.string.welcome_feature_1_title),
                                    description = stringResource(R.string.welcome_feature_1_desc)
                                )

                                WelcomeFeature(
                                    icon = "🏞️",
                                    title = stringResource(R.string.welcome_feature_2_title),
                                    description = stringResource(R.string.welcome_feature_2_desc)
                                )

                                WelcomeFeature(
                                    icon = "📖",
                                    title = stringResource(R.string.welcome_feature_3_title),
                                    description = stringResource(R.string.welcome_feature_3_desc)
                                )

                                WelcomeFeature(
                                    icon = "🌐",
                                    title = stringResource(R.string.welcome_feature_4_title),
                                    description = stringResource(R.string.welcome_feature_4_desc)
                                )
                            }
                            PopupMode.UPDATE -> {
                                UpdateFeature(
                                    icon = "🌐",
                                    title = stringResource(R.string.update_feature_1_title),
                                    description = stringResource(R.string.update_feature_1_desc)
                                )

                                UpdateFeature(
                                    icon = "⭐",
                                    title = stringResource(R.string.update_feature_2_title),
                                    description = stringResource(R.string.update_feature_2_desc)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Message adaptatif
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (mode) {
                            PopupMode.WELCOME -> Color(0xFF1E40AF).copy(alpha = 0.3f)
                            PopupMode.UPDATE -> Color(0xFF059669).copy(alpha = 0.3f)
                        }
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = when (mode) {
                            PopupMode.WELCOME -> stringResource(R.string.welcome_message)
                            PopupMode.UPDATE -> stringResource(R.string.update_thanks_message)
                        },
                        modifier = Modifier.padding(12.dp),
                        fontSize = 13.sp,
                        color = when (mode) {
                            PopupMode.WELCOME -> Color(0xFF60A5FA)
                            PopupMode.UPDATE -> Color(0xFF34D399)
                        },
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bouton adaptatif
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (mode) {
                            PopupMode.WELCOME -> Color(0xFF3B82F6)
                            PopupMode.UPDATE -> Color(0xFF10B981)
                        }
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = when (mode) {
                            PopupMode.WELCOME -> stringResource(R.string.welcome_button_start)
                            PopupMode.UPDATE -> stringResource(R.string.update_button_continue)
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeFeature(
    icon: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = icon,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 12.dp, top = 2.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun UpdateFeature(
    icon: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = icon,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 12.dp, top = 2.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}