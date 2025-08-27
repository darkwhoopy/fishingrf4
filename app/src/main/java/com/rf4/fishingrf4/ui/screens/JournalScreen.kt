package com.rf4.fishingrf4.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.rf4.fishingrf4.data.models.PlayerStats
import com.rf4.fishingrf4.ui.components.BackButton
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun JournalScreen(
    entries: List<FishingEntry>,
    playerStats: PlayerStats,
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
                            entryCount = entriesForDay.size,
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

// ✅ CARTE DE CAPTURE SIMPLIFIÉE
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
fun DayHeader(
    date: java.time.LocalDate,
    entryCount: Int,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRENCH) }
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "rotation")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A5F))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = date.format(formatter).replaceFirstChar { it.titlecase(Locale.FRENCH) },
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$entryCount prises",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(end = 8.dp)
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = "Déplier",
                tint = Color.White,
                modifier = Modifier.rotate(rotationAngle)
            )
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