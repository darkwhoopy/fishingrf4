package com.rf4.fishingrf4.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.ScreenLockPortrait
import androidx.compose.material.icons.filled.ScreenLockRotation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.models.ResetOption
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
import com.rf4.fishingrf4.utils.LanguageManager
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// ✅ VERSION PRINCIPALE avec tous les paramètres
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLanguageChange: (LanguageManager.Language) -> Unit,
    onResetData: () -> Unit,
    // ✅ Paramètres optionnels pour compatibilité
    viewModel: FishingViewModel? = null
) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var keepScreenOn by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val currentLanguage = LanguageManager.getCurrentLanguage(context)

    // Pour la gestion du temps de jeu (si viewModel fourni)
    val gameTime by (viewModel?.gameTime?.collectAsState() ?: remember { mutableStateOf(LocalTime.now()) })

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
                    text = "⚙️ Paramètres",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Section Langue
            Text(
                text = "Langue",
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
                    Icon(Icons.Default.Language, contentDescription = "Langue", tint = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Langue de l'interface", fontWeight = FontWeight.Bold, color = Color.White)
                        Text(currentLanguage.displayName, fontSize = 12.sp, color = Color.Gray)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section Temps de jeu (si viewModel disponible)
            if (viewModel != null) {
                Text(
                    text = "Temps de jeu",
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

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Section Affichage
            Text(
                text = "Affichage",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        keepScreenOn = !keepScreenOn
                        // TODO: Sauvegarder dans SharedPreferences
                    },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF374151))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (keepScreenOn) Icons.Default.ScreenLockPortrait
                        else Icons.Default.ScreenLockRotation,
                        contentDescription = "Verrouillage écran",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Garder l'écran allumé", fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Empêche l'écran de se verrouiller", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = keepScreenOn,
                        onCheckedChange = { keepScreenOn = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF10B981),
                            checkedTrackColor = Color(0xFF10B981).copy(alpha = 0.5f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section Données
            Text(
                text = "Données",
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
                    Icon(Icons.Default.DeleteSweep, contentDescription = "Réinitialiser", tint = Color(0xFFEF4444))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Réinitialiser les données", fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Effacer les captures, favoris, etc.", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }

        // Dialogs
        if (showLanguageDialog) {
            AlertDialog(
                onDismissRequest = { showLanguageDialog = false },
                title = { Text("Choisir la langue", color = Color.White) },
                text = {
                    Column {
                        LanguageManager.Language.values().forEach { language ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onLanguageChange(language)
                                        showLanguageDialog = false
                                        (context as? Activity)?.recreate()
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

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Confirmer la réinitialisation", color = Color.White) },
                text = {
                    Text(
                        "Cette action supprimera définitivement toutes vos données : " +
                                "captures, statistiques, favoris, etc. Cette action est irréversible.",
                        color = Color.Gray
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onResetData()
                            showResetDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                    ) {
                        Text("Réinitialiser")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Annuler", color = Color.White)
                    }
                },
                containerColor = Color(0xFF1E3A5F)
            )
        }

        if (showTimePickerDialog && viewModel != null) {
            TimePickerDialog(
                initialTime = gameTime,
                onDismiss = { showTimePickerDialog = false },
                onConfirm = { newTime ->
                    viewModel.adjustInGameTime(newTime)
                    showTimePickerDialog = false
                }
            )
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
                Text(
                    String.format("%02d:%02d", selectedHour.toInt(), selectedMinute.toInt()),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Heure : ${selectedHour.toInt()}")
                Slider(
                    value = selectedHour,
                    onValueChange = { selectedHour = it },
                    valueRange = 0f..23f,
                    steps = 22
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Minute : ${selectedMinute.toInt()}")
                Slider(
                    value = selectedMinute,
                    onValueChange = { selectedMinute = it },
                    valueRange = 0f..59f,
                    steps = 58
                )
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
                            )
                            .padding(vertical = 8.dp),
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