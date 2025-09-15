// ==========================================
// FICHIER: ui/components/TimePickerDialog.kt
// Dialog pour choisir l'heure de jeu RF4
// ==========================================

package com.rf4.fishingrf4.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.utils.GameTimeManager
import kotlinx.coroutines.launch
import java.time.LocalTime

/**
 * Dialog pour sélectionner l'heure de jeu RF4
 */
@Composable
fun TimePickerDialog(
    currentTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedHour by remember { mutableIntStateOf(currentTime.hour) }
    var selectedMinute by remember { mutableIntStateOf(currentTime.minute) }

    val hourListState = rememberLazyListState()
    val minuteListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Effet pour centrer les listes sur les valeurs actuelles
    LaunchedEffect(currentTime) {
        coroutineScope.launch {
            hourListState.animateScrollToItem(currentTime.hour.coerceAtMost(21))
            minuteListState.animateScrollToItem((currentTime.minute / 5).coerceAtMost(11))
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2937)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Titre avec icône
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.time_picker_title),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Affichage de l'heure actuelle
                Text(
                    text = "Heure actuelle : ${GameTimeManager.getTimeOfDay()}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Sélecteurs d'heure et minute
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sélecteur d'heure
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Heure",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                                .background(
                                    Color(0xFF374151),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            LazyColumn(
                                state = hourListState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(vertical = 40.dp)
                            ) {
                                items(24) { hour ->
                                    val isSelected = hour == selectedHour

                                    Text(
                                        text = String.format("%02d", hour),
                                        fontSize = if (isSelected) 20.sp else 16.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color(0xFF10B981) else Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedHour = hour }
                                            .padding(vertical = 8.dp)
                                            .background(
                                                if (isSelected) Color(0xFF064E3B) else Color.Transparent,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Sélecteur de minute (par pas de 5)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Minute",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                                .background(
                                    Color(0xFF374151),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            LazyColumn(
                                state = minuteListState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(vertical = 40.dp)
                            ) {
                                items(12) { index ->
                                    val minute = index * 5
                                    val isSelected = minute == selectedMinute

                                    Text(
                                        text = String.format("%02d", minute),
                                        fontSize = if (isSelected) 20.sp else 16.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color(0xFF10B981) else Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedMinute = minute }
                                            .padding(vertical = 8.dp)
                                            .background(
                                                if (isSelected) Color(0xFF064E3B) else Color.Transparent,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Affichage de l'heure sélectionnée
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Heure sélectionnée",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = String.format("%02d:%02d", selectedHour, selectedMinute),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )

                        // Période de la journée
                        val timeOfDay = when (selectedHour) {
                            in 5..7 -> "🌅 Aube"
                            in 8..17 -> "☀️ Jour"
                            in 18..20 -> "🌆 Crépuscule"
                            else -> "🌙 Nuit"
                        }

                        Text(
                            text = timeOfDay,
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Boutons d'action - Version améliorée
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Bouton principal Confirmer
                    Button(
                        onClick = {
                            onTimeSelected(LocalTime.of(selectedHour, selectedMinute))
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.time_confirm),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }

                    // Boutons secondaires
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Bouton Reset (auto)
                        OutlinedButton(
                            onClick = {
                                onTimeSelected(LocalTime.now())
                                onDismiss()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF10B981)
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF10B981))
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Auto",
                                fontSize = 14.sp
                            )
                        }

                        // Bouton Annuler
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Gray
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(Color.Gray)
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.time_cancel),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}