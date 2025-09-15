// ==========================================
// FICHIER: ui/components/CoordinatePickerDialog.kt
// Dialog pour choisir les coordonnées de spot RF4
// ==========================================

package com.rf4.fishingrf4.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rf4.fishingrf4.R
import kotlinx.coroutines.launch

/**
 * Dialog pour sélectionner les coordonnées de spot dans RF4
 * Format : X:Y (exemple 80:95)
 */
@Composable
fun CoordinatePickerDialog(
    lakeName: String,
    currentX: Int = 0,
    currentY: Int = 0,
    maxCoordinate: Int = 200,
    onCoordinateSelected: (x: Int, y: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedX by remember { mutableStateOf("") }
    var selectedY by remember { mutableStateOf("") }

    val xListState = rememberLazyListState()
    val yListState = rememberLazyListState()
    var showKeyboardInput by remember { mutableStateOf(false) }
    var keyboardInputX by remember { mutableStateOf("") }
    var keyboardInputY by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Générer les valeurs par pas de 1 (0 à 200)
    val coordinates = remember(maxCoordinate) {
        (0..maxCoordinate).toList()
    }

    // Effet pour centrer les listes sur les valeurs actuelles
    LaunchedEffect(currentX, currentY) {
        coroutineScope.launch {
            val xIndex = coordinates.indexOfFirst { it >= currentX }.coerceAtLeast(0)
            val yIndex = coordinates.indexOfFirst { it >= currentY }.coerceAtLeast(0)
            xListState.animateScrollToItem(xIndex.coerceIn(0, coordinates.size - 5))
            yListState.animateScrollToItem(yIndex.coerceIn(0, coordinates.size - 5))
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
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Choisissez votre position",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = lakeName,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Info sur le format
                Text(
                    text = "Format : X:Y (0-$maxCoordinate)",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Saisie directe des coordonnées X et Y
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Champ X
                    OutlinedTextField(
                        value = selectedX,
                        onValueChange = { newValue ->
                            // Autorise seulement les chiffres et max 3 caractères
                            if (newValue.all { it.isDigit() } && newValue.length <= 3) {
                                selectedX = newValue
                            }
                        },
                        label = { Text("X", color = Color.Gray) },
                        placeholder = { Text("85", color = Color.Gray) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Gray
                        ),
                        singleLine = true
                    )

                    // Séparateur
                    Text(
                        text = ":",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )

                    // Champ Y
                    OutlinedTextField(
                        value = selectedY,
                        onValueChange = { newValue ->
                            // Autorise seulement les chiffres et max 3 caractères
                            if (newValue.all { it.isDigit() } && newValue.length <= 3) {
                                selectedY = newValue
                            }
                        },
                        label = { Text("Y", color = Color.Gray) },
                        placeholder = { Text("35", color = Color.Gray) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Gray
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Affichage de la coordonnée sélectionnée
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
                            text = "Position sélectionnée",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        // Coordonnées principales (format communautaire RF4)
                        Text(
                            text = if (selectedX.isNotEmpty() && selectedY.isNotEmpty()) {
                                "$selectedX:$selectedY"
                            } else {
                                "--:--"
                            },
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )

                        Text(
                            text = "📍 Format communautaire RF4",
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Boutons d'action
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Bouton principal Confirmer
                    Button(
                        onClick = {
                            val x = selectedX.toIntOrNull()
                            val y = selectedY.toIntOrNull()
                            if (x != null && y != null) {
                                onCoordinateSelected(x, y)
                                onDismiss()
                            }
                        },
                        enabled = selectedX.isNotEmpty() && selectedY.isNotEmpty() &&
                                selectedX.toIntOrNull() != null && selectedY.toIntOrNull() != null &&
                                selectedX.toInt() <= maxCoordinate && selectedY.toInt() <= maxCoordinate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981),
                            disabledContainerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Confirmer la position",
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
                        // Bouton Position auto/aléatoire
                        OutlinedButton(
                            onClick = {
                                val randomX = coordinates.random()
                                val randomY = coordinates.random()
                                onCoordinateSelected(randomX, randomY)
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
                                Icons.Default.MyLocation,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Aléatoire",
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
                                text = "Annuler",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}