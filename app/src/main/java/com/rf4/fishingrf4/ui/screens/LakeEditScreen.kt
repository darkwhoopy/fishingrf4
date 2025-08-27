package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.models.*
import com.rf4.fishingrf4.ui.components.BackButton
import com.rf4.fishingrf4.ui.viewmodel.FishingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LakeEditScreen(
    lake: Lake,
    viewModel: FishingViewModel,
    onNavigateBack: () -> Unit,
    onSaveLake: (Lake) -> Unit
) {
    var editedLake by remember { mutableStateOf(lake) }
    var showAddFishDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val allFish = viewModel.getAllAvailableFish()
    val availableFishToAdd = remember(searchQuery, editedLake.availableFish) {
        allFish.filter { fish ->
            editedLake.availableFish.none { it.name == fish.name } &&
                    (searchQuery.isBlank() ||
                            fish.name.contains(searchQuery, ignoreCase = true) ||
                            fish.species.contains(searchQuery, ignoreCase = true))
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0D1B2A)).padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            BackButton(onClick = onNavigateBack)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Éditer ${lake.name}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { showAddFishDialog = true }, modifier = Modifier.padding(end = 8.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Ajouter poisson")
            }
            Button(onClick = { onSaveLake(editedLake) }) {
                Icon(Icons.Default.Check, contentDescription = "Sauvegarder")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Sauvegarder")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Poissons dans ce lac:", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            // ✅ LA CORRECTION EST ICI
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(editedLake.availableFish, key = { it.id }) { fish ->
                FishListItem(
                    fish = fish,
                    onRemove = {
                        editedLake = editedLake.copy(
                            availableFish = editedLake.availableFish.filter { it.id != fish.id }
                        )
                    }
                )
            }
        }
    }

    if (showAddFishDialog) {
        AddFishDialog(
            availableFish = availableFishToAdd,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onFishSelected = { selectedFish ->
                editedLake = editedLake.copy(
                    availableFish = (editedLake.availableFish + selectedFish).sortedBy { it.name }
                )
                showAddFishDialog = false
                searchQuery = ""
            },
            onDismiss = {
                showAddFishDialog = false
                searchQuery = ""
            }
        )
    }
}

@Composable
fun FishListItem(fish: Fish, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = fish.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = fish.species, fontSize = 12.sp, color = Color.Gray)
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color(0xFFE57373))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFishDialog(
    availableFish: List<Fish>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onFishSelected: (Fish) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter un poisson", color = Color.White) },
        text = {
            Column {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    label = { Text("Rechercher un poisson") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Rechercher") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items(availableFish) { fish ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onFishSelected(fish) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = fish.name, color = Color.White, modifier = Modifier.weight(1f))
                            Card(colors = CardDefaults.cardColors(containerColor = Color(fish.rarity.colorValue))) {
                                Text(
                                    text = fish.rarity.displayName,
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Annuler") } },
        containerColor = Color(0xFF1E3A5F)
    )
}