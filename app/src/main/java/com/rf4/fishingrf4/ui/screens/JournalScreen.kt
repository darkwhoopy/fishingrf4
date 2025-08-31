package com.rf4.fishingrf4.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.data.models.FishingEntry
import com.rf4.fishingrf4.ui.components.BackButton
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun JournalScreen(
    entries: List<FishingEntry>,
    onDeleteEntry: (String) -> Unit,
    onBack: () -> Unit
) {
    val groupedEntries = remember(entries) {
        entries.sortedByDescending { it.timestamp }
            .groupBy {
                Instant.ofEpochMilli(it.timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
            }
    }

    var expandedDays by remember { mutableStateOf(setOf<java.time.LocalDate>()) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(16.dp))
            Text("📖 Journal des captures", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        if (entries.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aucune capture enregistrée.", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                groupedEntries.forEach { (date, entriesForDay) ->
                    item(key = date) {
                        DayHeader(
                            date = date,
                            entriesForDay = entriesForDay, // ✅ Nouveau : on passe toutes les entrées
                            isExpanded = date in expandedDays,
                            onClick = {
                                expandedDays = if (date in expandedDays) expandedDays - date else expandedDays + date
                            }
                        )
                    }

                    items(entriesForDay, key = { it.id }) { entry ->
                        AnimatedVisibility(visible = date in expandedDays) {
                            Box(modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 8.dp)) {
                                JournalEntryCard(entry = entry, onDelete = { onDeleteEntry(entry.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}

// ✅ EN-TÊTE AMÉLIORÉE AVEC STATISTIQUES
@Composable
fun DayHeader(
    date: java.time.LocalDate,
    entriesForDay: List<FishingEntry>, // Nouveau : on passe toutes les entrées
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRENCH) }
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "rotation")

    // Calculs des statistiques du jour
    val totalCatches = entriesForDay.size
    val uniqueSpecies = entriesForDay.map { it.fish.name }.distinct().size
    val totalPoints = entriesForDay.sumOf { it.fish.rarity.points }
    val topLake = entriesForDay.groupBy { it.lake.name }.maxByOrNull { it.value.size }?.key
    val rarityBreakdown = entriesForDay.groupBy { it.fish.rarity }.mapValues { it.value.size }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Ligne principale avec date et flèche
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = date.format(formatter).replaceFirstChar { it.titlecase(Locale.FRENCH) },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Déplier",
                    tint = Color.White,
                    modifier = Modifier.rotate(rotationAngle)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Statistiques principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatisticItem(
                    label = "Prises",
                    value = totalCatches.toString(),
                    icon = "🎣",
                    color = Color(0xFF10B981)
                )
                StatisticItem(
                    label = "Espèces",
                    value = uniqueSpecies.toString(),
                    icon = "🐟",
                    color = Color(0xFF3B82F6)
                )
                StatisticItem(
                    label = "Points",
                    value = totalPoints.toString(),
                    icon = "⭐",
                    color = Color(0xFFFFC107)
                )
            }

            if (topLake != null && totalCatches > 1) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Lac productif : $topLake",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // Badges de rareté si il y a des prises intéressantes
            if (rarityBreakdown.any { it.key.points > 1 }) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    items(rarityBreakdown.entries.sortedByDescending { it.key.points }) { (rarity, count) ->
                        if (count > 0) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(rarity.colorValue)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${rarity.displayName.take(1)}×$count",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatisticItem(
    label: String,
    value: String,
    icon: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}

// ✅ CARTE DE CAPTURE (inchangée)
@Composable
fun JournalEntryCard(entry: FishingEntry, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .border(1.dp, Color(entry.fish.rarity.colorValue).copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(entry.fish.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                Text("${entry.lake.name} (${entry.position})", fontSize = 12.sp, color = Color.Gray)
            }

            entry.timeOfDay?.let {
                val (emoji, color) = when(it) {
                    "Matinée" -> "☀️" to Color(0xFFFFC107)
                    "Journée" -> "☀️" to Color(0xFF4CAF50)
                    "Soirée" -> "🌙" to Color(0xFF9C27B0)
                    else -> "🌃" to Color(0xFF3F51B5)
                }
                InfoBadge(text = "$emoji $it", color = color)
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun InfoBadge(
    text: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}