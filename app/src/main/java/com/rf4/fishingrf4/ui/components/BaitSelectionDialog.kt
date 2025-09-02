// ============================================================================
// FICHIER: ui/components/BaitSelectionDialog.kt (VERSION AMÉLIORÉE)
// Remplacer votre fichier existant par cette version
// ============================================================================

package com.rf4.fishingrf4.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rf4.fishingrf4.data.models.Fish
import com.rf4.fishingrf4.data.models.FishingEntry

/**
 * Dialog pour sélectionner l'appât utilisé pour capturer un poisson
 * ✅ AMÉLIORATION : Clic direct sur l'appât = validation automatique
 */
@Composable
fun BaitSelectionDialog(
    fish: Fish,
    recentBaits: List<String>,
    fishingEntries: List<FishingEntry> = emptyList(),
    onBaitSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var customBait by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(false) }

    // Liste complète des appâts possibles
    val allBaits = listOf(
        "Ver de terre", "Ver rouge", "Ver de vase", "Asticot", "Pain", "Maïs",
        "Pellets", "Bouillettes", "Fromage", "Pâte", "Épinoche", "Ablette",
        "Poisson vif", "Ver marin", "Crevette", "Crabe", "Calamar", "Sardine",
        "Hareng", "Maquereau", "Sprat", "Leurre souple", "Cuiller", "Popper",
        "Wobbler", "Spinnerbait", "Jig", "Devon", "Mouche sèche", "Nymphe",
        "Streamer", "Chironome", "Gammare", "Porte-bois", "Autre"
    )

    // Séparer les appâts : récents + préférés du poisson + autres
    val preferredBaits = fish.preferredBait
    val otherBaits = allBaits.filterNot { it in preferredBaits || it in recentBaits }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 650.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Titre avec fermeture
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🎣 Appât utilisé",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Poisson : ${fish.name}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fermer", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Instructions
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "💡 Cliquez directement sur un appât pour valider la capture",
                        fontSize = 13.sp,
                        color = Color(0xFF10B981),
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Section : Derniers appâts utilisés (si disponibles)
                    if (recentBaits.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "🕐 Derniers utilisés (ordre d'usage)",
                                color = Color(0xFF10B981)
                            )
                        }

                        items(recentBaits) { bait ->
                            // ✅ Calculer le nombre d'utilisations de cet appât
                            val usageCount = fishingEntries.count { it.bait == bait }

                            QuickBaitOption(
                                baitName = bait,
                                isRecent = true,
                                usageCount = usageCount, // ✅ NOUVEAU
                                onClick = {
                                    onBaitSelected(bait)
                                }
                            )
                        }

                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }

                    // Section : Appâts préférés du poisson
                    if (preferredBaits.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "⭐ Recommandés pour ${fish.name}",
                                color = Color(0xFFF59E0B)
                            )
                        }

                        items(preferredBaits.filterNot { it in recentBaits }) { bait ->
                            QuickBaitOption(
                                baitName = bait,
                                isRecommended = true,
                                onClick = {
                                    // ✅ VALIDATION DIRECTE
                                    onBaitSelected(bait)
                                }
                            )
                        }

                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }

                    // Section : Autres appâts
                    item {
                        SectionHeader(
                            title = "🎯 Autres appâts",
                            color = Color.White
                        )
                    }

                    items(otherBaits) { bait ->
                        QuickBaitOption(
                            baitName = bait,
                            onClick = {
                                if (bait == "Autre") {
                                    showCustomInput = true
                                } else {
                                    // ✅ VALIDATION DIRECTE
                                    onBaitSelected(bait)
                                }
                            }
                        )
                    }

                    // Input pour appât personnalisé
                    if (showCustomInput) {
                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = customBait,
                                onValueChange = { customBait = it },
                                label = { Text("Appât personnalisé", color = Color.Gray) },
                                placeholder = { Text("Entrez le nom de l'appât", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFF3B82F6),
                                    unfocusedBorderColor = Color.Gray
                                ),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    if (customBait.isNotBlank()) {
                                        onBaitSelected(customBait)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3B82F6)
                                ),
                                enabled = customBait.isNotBlank()
                            ) {
                                Text("✅ Valider l'appât personnalisé")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bouton d'annulation seulement
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Text("❌ Annuler")
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, color: Color) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = color,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

/**
 * Option d'appât cliquable avec validation directe
 * ✅ PLUS GROS et PLUS LISIBLE pour vos yeux
 */
@Composable
private fun QuickBaitOption(
    baitName: String,
    isSelected: Boolean = false,
    isRecent: Boolean = false,
    isRecommended: Boolean = false,
    usageCount: Int = 0, // ✅ NOUVEAU paramètre
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> Color(0xFF3B82F6).copy(alpha = 0.2f)
        isRecent -> Color(0xFF10B981).copy(alpha = 0.15f)
        isRecommended -> Color(0xFFF59E0B).copy(alpha = 0.15f)
        else -> Color(0xFF374151).copy(alpha = 0.8f)
    }

    val borderColor = when {
        isSelected -> Color(0xFF3B82F6)
        isRecent -> Color(0xFF10B981)
        isRecommended -> Color(0xFFF59E0B)
        else -> Color.Gray.copy(alpha = 0.5f)
    }

    val icon = when {
        isRecent -> "🕐"
        isRecommended -> "⭐"
        baitName == "Autre" -> "✏️"
        else -> "🎣"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            width = if (isSelected) 2.dp else 1.dp,
            brush = androidx.compose.ui.graphics.SolidColor(borderColor)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icône
            Text(
                text = icon,
                fontSize = 24.sp,
                modifier = Modifier.size(32.dp)
            )

            // Nom de l'appât
            Text(
                text = baitName,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            // ✅ NOUVEAU : Compteur d'usage pour les appâts récents
            if (isRecent && usageCount > 0) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF10B981).copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "×$usageCount",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Badge de type
            if (isRecommended) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF59E0B).copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Conseillé",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59E0B),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Flèche d'action
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Sélectionner",
                tint = Color(0xFF10B981),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
