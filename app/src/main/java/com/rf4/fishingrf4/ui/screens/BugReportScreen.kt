// ==========================================
// FICHIER: ui/screens/BugReportScreen.kt
// Écran pour signaler les bugs et problèmes
// ==========================================

package com.rf4.fishingrf4.ui.screens

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.models.BugType
import com.rf4.fishingrf4.data.repository.CommunityRepository
import com.rf4.fishingrf4.ui.components.BackButton
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import com.rf4.fishingrf4.R

/**
 * Écran de signalement de bugs
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BugReportScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val communityRepo = remember { CommunityRepository() }

    // États du formulaire
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedBugType by remember { mutableStateOf(BugType.GAMEPLAY) }
    var reproductionSteps by remember { mutableStateOf("") }
    var expectedBehavior by remember { mutableStateOf("") }
    var actualBehavior by remember { mutableStateOf("") }

    // États de l'interface
    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showBugTypeDropdown by remember { mutableStateOf(false) }

    // Informations système automatiques
    val deviceInfo = remember {
        "Marque: ${Build.MANUFACTURER}, " +
                "Modèle: ${Build.MODEL}, " +
                "Android: ${Build.VERSION.RELEASE}, " +
                "API: ${Build.VERSION.SDK_INT}"
    }
    val appVersion = "1.0.0" // À adapter selon votre système de versioning

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
                Column {
                    Text(
                        text = stringResource(R.string.bug_report_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.bug_report_subtitle),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Message d'information
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Comment bien signaler un bug ?",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Soyez précis dans votre description\n" +
                                    "• Indiquez les étapes pour reproduire le problème\n" +
                                    "• Décrivez ce qui devrait se passer normalement\n" +
                                    "• Plus d'informations = correction plus rapide !",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // Formulaire
            // Titre du bug
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.bug_title_label), color = Color.Gray) },
                placeholder = { Text(stringResource(R.string.bug_title_placeholder), color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Type de bug
            ExposedDropdownMenuBox(
                expanded = showBugTypeDropdown,
                onExpandedChange = { showBugTypeDropdown = it }
            ) {
                OutlinedTextField(
                    value = selectedBugType.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.bug_type_label), color = Color.Gray) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showBugTypeDropdown) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.Gray
                    )
                )

                ExposedDropdownMenu(
                    expanded = showBugTypeDropdown,
                    onDismissRequest = { showBugTypeDropdown = false },
                    modifier = Modifier.background(Color(0xFF1E3A5F))
                ) {
                    BugType.entries.forEach { bugType ->
                        DropdownMenuItem(
                            text = { Text(bugType.displayName, color = Color.White) },
                            onClick = {
                                selectedBugType = bugType
                                showBugTypeDropdown = false
                            },
                            modifier = Modifier.background(Color(0xFF1E3A5F))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description détaillée
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.bug_description_label), color = Color.Gray) },
                placeholder = {
                    Text(
                        stringResource(R.string.bug_description_placeholder),
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Étapes de reproduction
            OutlinedTextField(
                value = reproductionSteps,
                onValueChange = { reproductionSteps = it },
                label = { Text(stringResource(R.string.bug_reproduction_label), color = Color.Gray) },
                placeholder = {
                    Text(
                        stringResource(R.string.bug_reproduction_placeholder),
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Comportement attendu
            OutlinedTextField(
                value = expectedBehavior,
                onValueChange = { expectedBehavior = it },
                label = { Text(stringResource(R.string.bug_expected_label), color = Color.Gray) },
                placeholder = {
                    Text(
                        stringResource(R.string.bug_expected_placeholder),
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Comportement actuel
            OutlinedTextField(
                value = actualBehavior,
                onValueChange = { actualBehavior = it },
                label = { Text(stringResource(R.string.bug_actual_label), color = Color.Gray) },
                placeholder = {
                    Text(
                        stringResource(R.string.bug_actual_placeholder),
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Informations système (lecture seule)
            Text(
                text = stringResource(R.string.bug_device_info),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Appareil: $deviceInfo",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Version de l'app: $appVersion",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Message d'erreur
            if (errorMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x33EF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = errorMessage,
                            color = Color(0xFFEF4444),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Bouton d'envoi
            Button(
                onClick = {
                    if (title.isBlank() || description.isBlank()) {
                        errorMessage = context.getString(R.string.required_field)
                        return@Button
                    }

                    isSubmitting = true
                    errorMessage = ""

                    // Dans BugReportScreen.kt, dans le onClick du bouton d'envoi :

                    coroutineScope.launch {
                        try {
                            android.util.Log.d("BugReportScreen", "Envoi du signalement...")

                            val reportId = communityRepo.submitBugReport(
                                title = title,
                                description = description,
                                bugType = selectedBugType,
                                reproductionSteps = reproductionSteps,
                                expectedBehavior = expectedBehavior,
                                actualBehavior = actualBehavior,
                                deviceInfo = deviceInfo,
                                appVersion = appVersion
                            )

                            android.util.Log.d("BugReportScreen", "Signalement créé avec ID: $reportId")
                            showSuccessDialog = true
                        } catch (e: Exception) {
                            android.util.Log.e("BugReportScreen", "Erreur envoi: ${e.message}", e)
                            errorMessage = e.message ?: "Erreur lors de l'envoi du signalement"
                        } finally {
                            isSubmitting = false
                        }
                    }
                },
                enabled = !isSubmitting && title.isNotBlank() && description.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444),
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Envoyer le signalement",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialog de succès
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            containerColor = Color(0xFF1E3A5F),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.bug_success_title), color = Color.White)
                }
            },
            text = {
                Text(
                    "Merci pour votre signalement ! Notre équipe va examiner le problème " +
                            "et vous contacter si besoin. Vous pouvez suivre l'état de votre " +
                            "signalement dans la section communauté.",
                    color = Color.Gray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("OK")
                }
            }
        )
    }
}