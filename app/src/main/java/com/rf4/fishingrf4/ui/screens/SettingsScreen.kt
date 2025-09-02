package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.models.ResetOption
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.filled.Language
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.rf4.fishingrf4.utils.LanguageManager
import com.rf4.fishingrf4.R

@Composable
fun SettingsScreen(
    viewModel: FishingViewModel,
    onBack: () -> Unit,
    // ✅ Le paramètre manquant est ici
    onResetData: (Set<ResetOption>) -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    val gameTime by viewModel.gameTime.collectAsState()

    if (showResetDialog) {
        ResetConfirmationDialog(
            onDismiss = { showResetDialog = false },
            onConfirm = { selectedOptions ->
                onResetData(selectedOptions)
                showResetDialog = false
            }
        )
    }

    if (showTimePickerDialog) {
        TimePickerDialog(
            initialTime = gameTime,
            onDismiss = { showTimePickerDialog = false },
            onConfirm = { newTime ->
                viewModel.adjustInGameTime(newTime)
                showTimePickerDialog = false
            }
        )
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(16.dp))
            Text("⚙️ Paramètres", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth().clickable { showTimePickerDialog = true },
            colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = "Horloge", tint = Color(0xFF0EA5E9))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Horloge du jeu", fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Cliquez pour ajuster l'heure en cas de décalage.", fontSize = 12.sp, color = Color.Gray)
                }
                Text(
                    text = gameTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        val context = LocalContext.current
        var currentLanguage by remember { mutableStateOf(LanguageManager.getSavedLanguage(context)) }
        var showLanguageDialog by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier.fillMaxWidth().clickable { showLanguageDialog = true },
            colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Language, contentDescription = "Langue", tint = Color(0xFF10B981))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(context.getString(R.string.language_setting), fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Choisir la langue de l'application", fontSize = 12.sp, color = Color.Gray)
                }
                Text(
                    text = currentLanguage.displayName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

// Dialog de sélection de langue
        if (showLanguageDialog) {
            AlertDialog(
                onDismissRequest = { showLanguageDialog = false },
                title = { Text(context.getString(R.string.language_setting), color = Color.White) },
                text = {
                    Column {
                        LanguageManager.Language.values().forEach { language ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        currentLanguage = language
                                        LanguageManager.setAppLanguage(context, language)
                                        showLanguageDialog = false
                                        // Redémarrer l'activité pour appliquer la langue
                                        (context as? android.app.Activity)?.recreate()
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentLanguage == language,
                                    onClick = null
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(language.displayName, color = Color.White)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showLanguageDialog = false }) {
                        Text("Fermer", color = Color.White)
                    }
                },
                containerColor = Color(0xFF1E3A5F)
            )
        }


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
                Icon(Icons.Default.DeleteSweep, contentDescription = "Réinitialiser", tint = Color(0xFFEF4444))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Réinitialiser les données", fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Effacer les captures, favoris, etc.", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun TimePickerDialog(
    initialTime: LocalTime,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit
) {
    var selectedHour by remember { mutableStateOf(initialTime.hour.toFloat()) }
    var selectedMinute by remember { mutableStateOf(initialTime.minute.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Régler l'heure du jeu") },
        text = {
            Column {
                Text(String.format("%02d:%02d", selectedHour.toInt(), selectedMinute.toInt()), style = MaterialTheme.typography.headlineMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Heure : ${selectedHour.toInt()}")
                Slider(value = selectedHour, onValueChange = { selectedHour = it }, valueRange = 0f..23f, steps = 22)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Minute : ${selectedMinute.toInt()}")
                Slider(value = selectedMinute, onValueChange = { selectedMinute = it }, valueRange = 0f..59f, steps = 58)
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(LocalTime.of(selectedHour.toInt(), selectedMinute.toInt())) }) {
                Text("Confirmer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annuler") }
        }
    )
}

@Composable
fun ResetConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: (Set<ResetOption>) -> Unit
) {
    var selectedOptions by remember { mutableStateOf(setOf<ResetOption>()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Réinitialiser les données") },
        text = {
            Column {
                Text("Quelles données souhaitez-vous effacer ?", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(16.dp))
                ResetOption.values().forEach { option ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = selectedOptions.contains(option),
                                onValueChange = {
                                    selectedOptions = if (it) selectedOptions + option else selectedOptions - option
                                },
                                role = Role.Checkbox
                            ).padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = selectedOptions.contains(option), onCheckedChange = null)
                        Spacer(Modifier.width(8.dp))
                        Text(option.displayName)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedOptions) },
                enabled = selectedOptions.isNotEmpty()
            ) { Text("Confirmer") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annuler") } }
    )
}