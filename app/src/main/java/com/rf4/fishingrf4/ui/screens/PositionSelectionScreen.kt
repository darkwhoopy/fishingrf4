package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.data.models.UserSpot
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel
// ✅ L'IMPORT MANQUANT EST ICI
import androidx.compose.foundation.BorderStroke

@Composable
fun PositionSelectionScreen(
    lake: Lake,
    viewModel: FishingViewModel,
    onPositionSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    var selectedNumber by remember { mutableStateOf<String?>(null) }
    var showAddSpotDialog by remember { mutableStateOf(false) }

    val userSpots by viewModel.userSpots.collectAsState()
    val userSpotsForThisLake = remember(userSpots, lake.id) {
        userSpots.filter { it.lakeId == lake.id }
    }

    val completePosition = if (selectedLetter != null && selectedNumber != null) {
        "$selectedLetter$selectedNumber"
    } else null

    if (showAddSpotDialog) {
        AddSpotDialog(
            onDismiss = { showAddSpotDialog = false },
            onConfirm = { position, comment ->
                viewModel.addUserSpot(UserSpot(lakeId = lake.id, position = position, comment = comment))
                showAddSpotDialog = false
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = lake.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("📍 Choisissez votre position", fontSize = 14.sp, color = Color(0xFFE2E8F0))
            }
        }

        if (completePosition != null) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF10B981))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Position : $completePosition", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Button(onClick = { onPositionSelected(completePosition) }) {
                        Icon(Icons.Default.Check, contentDescription = "Confirmer")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Confirmer")
                    }
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text("🔤 Sélectionnez une lettre", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val letters = ('A'..'J').map { it.toString() }
                    items(letters) { letter ->
                        GridButton(
                            text = letter,
                            isSelected = selectedLetter == letter,
                            onClick = { selectedLetter = if (selectedLetter == letter) null else letter }
                        )
                    }
                }
            }

            item {
                Text("🔢 Sélectionnez un chiffre", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val numbers = (1..10).map { it.toString() }
                    items(numbers) { number ->
                        GridButton(
                            text = number,
                            isSelected = selectedNumber == number,
                            onClick = { selectedNumber = if (selectedNumber == number) null else number }
                        )
                    }
                }
            }

            item {
                if (lake.coordinates.isNotEmpty() || userSpotsForThisLake.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("⭐ Positions recommandées", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        IconButton(onClick = { showAddSpotDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Ajouter un spot", tint = Color(0xFF4CAF50))
                        }
                    }
                }
            }

            items(lake.coordinates.toList()) { (position, description) ->
                RecommendedPositionCard(
                    position = position,
                    description = description,
                    onClick = { onPositionSelected(position) }
                )
            }

            items(userSpotsForThisLake) { spot ->
                UserPositionCard(
                    spot = spot,
                    onClick = { onPositionSelected(spot.position) },
                    onDelete = { viewModel.deleteUserSpot(spot.id) }
                )
            }
        }
    }
}

@Composable
fun GridButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.size(50.dp).clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFF3B82F6) else Color(0xFF475569)),
        border = if (isSelected) BorderStroke(2.dp, Color.White) else null
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

// ... Les autres composants (RecommendedPositionCard, UserPositionCard, AddSpotDialog) sont inchangés

// ... Les autres composants (RecommendedPositionCard, UserPositionCard, AddSpotDialog) sont inchangés

// ✅ NOUVEAU COMPOSANT : La carte pour un spot personnel
@Composable
fun UserPositionCard(
    spot: UserSpot,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3B82F6)), // Couleur bleue
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ... (Badge de position, description, etc. comme RecommendedPositionCard)
            Column(modifier = Modifier.weight(1f)) {
                Text(spot.position, color = Color.White, fontWeight = FontWeight.Bold)
                Text(spot.comment, color = Color(0xFFE2E8F0), fontSize = 14.sp)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer le spot", tint = Color(0xFFE57373))
            }
        }
    }
}

// ✅ NOUVEAU COMPOSANT : Le pop-up d'ajout de spot
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSpotDialog(
    onDismiss: () -> Unit,
    onConfirm: (position: String, comment: String) -> Unit
) {
    var position by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter un spot personnel") },
        text = {
            Column {
                OutlinedTextField(
                    value = position,
                    onValueChange = { position = it },
                    label = { Text("Position (ex: 45:55)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Commentaire (ex: Trophées brèmes)") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(position, comment) },
                enabled = position.isNotBlank() && comment.isNotBlank()
            ) { Text("Ajouter") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annuler") }
        }
    )
}

@Composable
fun LetterCard(
    letter: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(50.dp)
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF3B82F6) else Color(0xFF475569)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = letter,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun NumberCard(
    number: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(50.dp)
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFEF4444) else Color(0xFF475569)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = number,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun RecommendedPositionCard(
    position: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF059669)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Badge de position
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = position,
                    color = Color(0xFF059669),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Description
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = description,
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
            }

            // Flèche ou icône
            Text(
                text = "→",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}