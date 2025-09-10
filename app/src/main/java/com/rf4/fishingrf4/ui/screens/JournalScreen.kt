package com.rf4.fishingrf4.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.models.FishingEntry
import com.rf4.fishingrf4.data.models.getLocalizedName // 🆕 AJOUTÉ
import com.rf4.fishingrf4.ui.components.BackButton
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

// Classe pour organiser les captures par moment de la journée
data class TimeSlot(
    val nameResId: Int, // ID de ressource pour la traduction
    val hourRange: IntRange,
    val icon: String,
    val color: Color
)

// Classe pour regrouper les captures par poisson
data class FishGroup(
    val fish: com.rf4.fishingrf4.data.models.Fish,
    val entries: List<FishingEntry>,
    val count: Int,
    val bestWeight: Double?,
    val bestLocation: String
)

@Composable
fun JournalScreen(
    entries: List<FishingEntry>,
    onDeleteEntry: (String) -> Unit,
    onBack: () -> Unit
) {
    // Context pour accéder aux ressources de traduction
    val context = LocalContext.current

    // État pour la popup de détails
    var selectedFishGroup by remember { mutableStateOf<FishGroup?>(null) }
    val haptic = LocalHapticFeedback.current

    // Créneaux horaires pour organiser les captures (avec traductions)
    val timeSlots = listOf(
        TimeSlot(R.string.time_morning, 5..10, "🌅", Color(0xFF10B981)),
        TimeSlot(R.string.time_day, 11..16, "☀️", Color(0xFFF59E0B)),
        TimeSlot(R.string.time_evening, 17..21, "🌇", Color(0xFFEF4444)),
        TimeSlot(R.string.time_night, 22..4, "🌙", Color(0xFF8B5CF6))
    )

    // Fonction pour déterminer le créneau d'une capture
    fun getTimeSlot(entry: FishingEntry): TimeSlot {
        val hour = entry.hour ?: LocalDateTime.ofInstant(
            Instant.ofEpochMilli(entry.timestamp),
            ZoneId.systemDefault()
        ).hour

        return timeSlots.find { timeSlot ->
            when {
                timeSlot.hourRange.first <= timeSlot.hourRange.last ->
                    hour in timeSlot.hourRange
                else -> // Cas de la nuit (22-4)
                    hour >= timeSlot.hourRange.first || hour <= timeSlot.hourRange.last
            }
        } ?: timeSlots[1] // Par défaut : journée
    }

    // Regroupement des entrées par jour
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
            Text(
                text = stringResource(R.string.journal_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        if (entries.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.journal_empty),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedEntries.forEach { (date, dayEntries) ->
                item {
                    val isExpanded = date in expandedDays
                    val rotation by animateFloatAsState(
                        targetValue = if (isExpanded) 180f else 0f,
                        label = "rotation"
                    )

                    // En-tête du jour
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedDays = if (isExpanded) {
                                    expandedDays - date
                                } else {
                                    expandedDays + date
                                }
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF334155)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = date.format(DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.getDefault())),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${stringResource(R.string.journal_captures_count, dayEntries.size)} • ${stringResource(R.string.journal_species_count, dayEntries.distinctBy { it.fish.name }.size)}",
                                    fontSize = 12.sp,
                                    color = Color(0xFFE2E8F0)
                                )
                            }

                            Icon(
                                Icons.Default.ExpandMore,
                                contentDescription = stringResource(R.string.journal_expand),
                                tint = Color.White,
                                modifier = Modifier.rotate(rotation)
                            )
                        }
                    }
                }

                if (date in expandedDays) {
                    // Regroupement par créneau horaire
                    val entriesByTimeSlot = dayEntries.groupBy { getTimeSlot(it) }

                    entriesByTimeSlot.forEach { (timeSlot, timeEntries) ->
                        item {
                            // En-tête du créneau horaire
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = timeSlot.color.copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(timeSlot.nameResId),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = timeSlot.color
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(R.string.journal_captures_count, timeEntries.size),
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        // Regroupement par poisson dans ce créneau
                        val fishGroups = timeEntries
                            .groupBy { it.fish.name } // ✅ Garde le nom français pour la logique
                            .map { (fishName, fishEntries) ->
                                FishGroup(
                                    fish = fishEntries.first().fish,
                                    entries = fishEntries,
                                    count = fishEntries.size,
                                    bestWeight = null, // Poids désactivé
                                    bestLocation = fishEntries
                                        .groupBy { "${it.lake.name} - ${it.position}" }
                                        .maxByOrNull { it.value.size }?.key ?: "Position inconnue"
                                )
                            }
                            .sortedByDescending { it.count }

                        items(fishGroups) { fishGroup ->
                            FishGroupCard(
                                fishGroup = fishGroup,
                                context = context, // 🆕 AJOUTÉ
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedFishGroup = fishGroup
                                },
                                onDeleteEntry = onDeleteEntry
                            )
                        }
                    }
                }
            }
        }
    }

    // Popup de détails pour un groupe de poissons
    selectedFishGroup?.let { fishGroup ->
        FishDetailDialog(
            fishGroup = fishGroup,
            context = context, // 🆕 AJOUTÉ
            onDismiss = { selectedFishGroup = null },
            onDeleteEntry = { entryId ->
                onDeleteEntry(entryId)
                selectedFishGroup = null
            }
        )
    }
}

@Composable
fun FishGroupCard(
    fishGroup: FishGroup,
    context: android.content.Context, // 🆕 AJOUTÉ
    onClick: () -> Unit,
    onDeleteEntry: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF475569).copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge de rareté
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(fishGroup.fish.rarity.colorValue)
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = stringResource(
                            when(fishGroup.fish.rarity) {
                                com.rf4.fishingrf4.data.models.FishRarity.COMMON -> R.string.rarity_common
                                com.rf4.fishingrf4.data.models.FishRarity.UNCOMMON -> R.string.rarity_uncommon
                                com.rf4.fishingrf4.data.models.FishRarity.RARE -> R.string.rarity_rare
                                com.rf4.fishingrf4.data.models.FishRarity.EPIC -> R.string.rarity_epic
                                com.rf4.fishingrf4.data.models.FishRarity.LEGENDARY -> R.string.rarity_legendary
                            }
                        ).take(1),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = fishGroup.fish.getLocalizedName(context), // 🔥 MODIFIÉ
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.fish_count_format, fishGroup.count),
                        fontSize = 12.sp,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Bouton info pour plus de détails
            IconButton(onClick = onClick) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = stringResource(R.string.journal_details),
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun FishDetailDialog(
    fishGroup: FishGroup,
    context: android.content.Context, // 🆕 AJOUTÉ
    onDismiss: () -> Unit,
    onDeleteEntry: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // En-tête avec le nom du poisson
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = fishGroup.fish.getLocalizedName(context), // 🔥 MODIFIÉ
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = fishGroup.fish.species, // Garde species pour nom scientifique
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(fishGroup.fish.rarity.colorValue)
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = stringResource(
                                when(fishGroup.fish.rarity) {
                                    com.rf4.fishingrf4.data.models.FishRarity.COMMON -> R.string.rarity_common
                                    com.rf4.fishingrf4.data.models.FishRarity.UNCOMMON -> R.string.rarity_uncommon
                                    com.rf4.fishingrf4.data.models.FishRarity.RARE -> R.string.rarity_rare
                                    com.rf4.fishingrf4.data.models.FishRarity.EPIC -> R.string.rarity_epic
                                    com.rf4.fishingrf4.data.models.FishRarity.LEGENDARY -> R.string.rarity_legendary
                                }
                            ),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Statistiques du groupe
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FishStatItem(stringResource(R.string.stat_captures), fishGroup.count.toString(), "🎣")
                    FishStatItem(stringResource(R.string.stat_species), "1", "🐠")
                    FishStatItem(stringResource(R.string.stat_locations), fishGroup.entries.distinctBy { "${it.lake.name}-${it.position}" }.size.toString(), "📍")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Meilleur spot
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = stringResource(R.string.stat_locations),
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.stat_best_spot, fishGroup.bestLocation),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Liste des captures détaillées
                Text(
                    text = stringResource(R.string.journal_details) + " :",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(fishGroup.entries.sortedByDescending { it.timestamp }) { entry ->
                        FishDetailItem(
                            entry = entry,
                            onDelete = { onDeleteEntry(entry.id) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bouton fermer
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text(stringResource(R.string.journal_close), color = Color.White)
                }
            }
        }
    }
}

@Composable
fun FishStatItem(label: String, value: String, icon: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 16.sp
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun FishDetailItem(
    entry: FishingEntry,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF334155).copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = stringResource(R.string.time_hour, 0),
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = DateTimeFormatter.ofPattern("HH:mm")
                            .format(LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(entry.timestamp),
                                ZoneId.systemDefault()
                            )),
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = stringResource(R.string.stat_locations),
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${entry.lake.name} - ${entry.position}",
                        fontSize = 11.sp,
                        color = Color(0xFFE2E8F0)
                    )
                }

                if (entry.bait.isNotEmpty()) {
                    Text(
                        text = "🎯 ${entry.bait}",
                        fontSize = 11.sp,
                        color = Color(0xFF64B5F6)
                    )
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.journal_delete),
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}