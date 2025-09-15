// ==========================================
// FICHIER: ui/screens/SettingsScreen.kt
// Écran des paramètres avec gestionnaire d'heure
// ==========================================

package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.components.TimePickerDialog
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import com.rf4.fishingrf4.utils.LanguageManager
import com.rf4.fishingrf4.data.utils.GameTimeManager
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: FishingViewModel? = null
) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var keepScreenOn by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val currentLanguage = LanguageManager.getCurrentLanguage(context)

    // ✅ CORRECTION : Utiliser le GameTimeManager au lieu d'un état local
    val gameTime by GameTimeManager.gameTime.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                BackButton(onClick = onBack)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.settings_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Section Langue
            Text(
                text = stringResource(R.string.settings_language_section),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showLanguageDialog = true },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Language,
                        contentDescription = stringResource(R.string.desc_language_icon),
                        tint = Color(0xFF10B981)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.settings_language_interface),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            stringResource(R.string.settings_language_desc),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentLanguage.displayName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section Temps de jeu
            Text(
                text = stringResource(R.string.settings_game_time_section),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimePickerDialog = true },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = stringResource(R.string.desc_time_icon),
                        tint = Color(0xFF10B981)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.settings_game_time_current),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            stringResource(R.string.settings_game_time_desc),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        // ✅ NOUVEAU : Affichage de la période de la journée
                        Text(
                            text = GameTimeManager.getTimeOfDay(gameTime),
                            fontSize = 11.sp,
                            color = Color(0xFF10B981),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = gameTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "⚙️ Régler",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section Affichage
            Text(
                text = stringResource(R.string.settings_display_section),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = Color(0xFF10B981)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.settings_keep_screen_on),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            stringResource(R.string.settings_keep_screen_desc),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = keepScreenOn,
                        onCheckedChange = { keepScreenOn = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF10B981),
                            checkedTrackColor = Color(0xFF064E3B)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section Données
            Text(
                text = stringResource(R.string.settings_data_section),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showResetDialog = true },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DeleteSweep,
                        contentDescription = stringResource(R.string.desc_reset_icon),
                        tint = Color(0xFFEF4444)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.settings_reset_data),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            stringResource(R.string.settings_reset_desc),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section À propos
            Text(
                text = stringResource(R.string.settings_about_section),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFF10B981)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "RF4 Assistant",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.version, "1.0.0"),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Assistant pour Russian Fishing 4",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    // ==========================================
    // DIALOGS
    // ==========================================

    // Dialog de sélection de langue
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(stringResource(R.string.settings_language)) },
            text = {
                Column {
                    // Option Français
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                LanguageManager.setAppLanguage(context, LanguageManager.Language.FRENCH)
                                showLanguageDialog = false
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (currentLanguage == LanguageManager.Language.FRENCH)
                                Color(0xFF10B981) else Color(0xFF374151)
                        )
                    ) {
                        Text(
                            text = "Français",
                            modifier = Modifier.padding(16.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option English
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                LanguageManager.setAppLanguage(context, LanguageManager.Language.ENGLISH)
                                showLanguageDialog = false
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (currentLanguage == LanguageManager.Language.ENGLISH)
                                Color(0xFF10B981) else Color(0xFF374151)
                        )
                    ) {
                        Text(
                            text = "English",
                            modifier = Modifier.padding(16.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // Dialog de confirmation de reset
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.dialog_reset_title)) },
            text = { Text(stringResource(R.string.dialog_reset_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        // TODO: Implémenter la fonction de reset via viewModel
                        showResetDialog = false
                    }
                ) {
                    Text(stringResource(R.string.confirm), color = Color(0xFFEF4444))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // ✅ NOUVEAU : Dialog de sélection d'heure avec TimePickerDialog
    if (showTimePickerDialog) {
        TimePickerDialog(
            currentTime = gameTime,
            onTimeSelected = { newTime ->
                // Appeler la méthode du ViewModel pour ajuster l'heure
                viewModel?.adjustInGameTime(newTime)
            },
            onDismiss = { showTimePickerDialog = false }
        )
    }
}