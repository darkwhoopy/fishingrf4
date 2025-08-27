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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishSearchScreen(
    allLakes: List<Lake>,
    fishingEntries: List<FishingEntry>,
    onBack: () -> Unit,
    onFishDetail: (Fish, Lake) -> Unit = { _, _ -> }
) {
    var searchQuery by remember { mutableStateOf("") }

    val allFish = remember(allLakes) {
        allLakes.flatMap { it.availableFish }.distinctBy { it.name }.sortedBy { it.name }
    }
    val fishCaptureStats = remember(fishingEntries) {
        fishingEntries.groupBy { it.fish.name }.mapValues { (_, entries) -> entries.size }
    }
    val filteredFish = remember(allFish, searchQuery) {
        if (searchQuery.isBlank()) allFish
        else allFish.filter { fish ->
            fish.name.contains(searchQuery, ignoreCase = true) ||
                    fish.species.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0D1B2A)).padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "🔍 Recherche de poissons", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Rechercher un poisson") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Rechercher") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filteredFish) { fish ->
                val fishLakes = allLakes.filter { lake ->
                    lake.availableFish.any { it.name == fish.name }
                }
                SearchFishCard(
                    fish = fish,
                    captureCount = fishCaptureStats[fish.name] ?: 0,
                    lakes = fishLakes,
                    onClick = {
                        fishLakes.firstOrNull()?.let { onFishDetail(fish, it) }
                    }
                )
            }
        }
    }
}

@Composable
fun SearchFishCard(
    fish: Fish,
    captureCount: Int,
    lakes: List<Lake>,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(2.dp, Color(fish.rarity.colorValue), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(fish.rarity.colorValue).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = fish.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = fish.species, fontSize = 12.sp, color = Color(0xFFB0BEC5), fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                }
                Column(horizontalAlignment = Alignment.End) {
                    SearchBadge(text = fish.rarity.displayName, color = Color(fish.rarity.colorValue))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (captureCount > 0) "Capturé $captureCount fois" else "Jamais capturé",
                        fontSize = 10.sp,
                        color = if (captureCount > 0) Color(0xFF4CAF50) else Color(0xFFFFB74D)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (fish.weight != null) {
                Text(text = "⚖️ ${fish.weight.start}-${fish.weight.endInclusive} kg", fontSize = 12.sp, color = Color(0xFFB0BEC5))
            }
            if (fish.preferredBait.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "🎣 ${fish.preferredBait.take(3).joinToString(", ")}${if (fish.preferredBait.size > 3) "..." else ""}", fontSize = 12.sp, color = Color(0xFFB0BEC5))
            }
            if (lakes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "📍 Disponible dans:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF90A4AE))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                    lakes.take(3).forEach { lake -> SearchBadge(text = lake.name, color = Color(0xFF607D8B)) }
                    if (lakes.size > 3) {
                        Text(text = "+${lakes.size - 3}", fontSize = 10.sp, color = Color(0xFF90A4AE))
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBadge(text: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(text = text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
    }
}