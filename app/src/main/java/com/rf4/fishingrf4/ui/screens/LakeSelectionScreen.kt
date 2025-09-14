package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.models.Lake
import com.rf4.fishingrf4.utils.getLocalizedDescription
import com.rf4.fishingrf4.utils.getLocalizedName

@Composable
fun LakeSelectionScreen(
    lakes: List<Lake>,
    playerLevel: Int,
    onLakeSelected: (Lake) -> Unit,
    onLakeEdit: (Lake) -> Unit = {},
    favoriteLakeIds: Set<String>,
    onToggleFavorite: (String) -> Unit,
    isFavoritesFilterActive: Boolean,
    onToggleFavoritesFilter: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.screen_lake_selection_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.lake_selection_subtitle),
                    fontSize = 14.sp,
                    color = Color(0xFFE2E8F0)
                )
            }
            IconButton(onClick = onToggleFavoritesFilter) {
                Icon(
                    imageVector = if (isFavoritesFilterActive) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = if (isFavoritesFilterActive)
                        stringResource(R.string.lake_remove_favorite)
                    else
                        stringResource(R.string.lake_add_favorite),
                    tint = if (isFavoritesFilterActive) Color(0xFFFFD700) else Color.Gray
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val sortedLakes = lakes.sortedByDescending { favoriteLakeIds.contains(it.id) }
            items(sortedLakes) { lake ->
                LakeCard(
                    lake = lake,
                    isUnlocked = playerLevel >= lake.unlockLevel,
                    isFavorite = favoriteLakeIds.contains(lake.id),
                    onClick = { if (playerLevel >= lake.unlockLevel) onLakeSelected(lake) },
                    onEdit = { onLakeEdit(lake) },
                    onToggleFavorite = { onToggleFavorite(lake.id) }
                )
            }
        }
    }
}

@Composable
private fun LakeCard(
    lake: Lake,
    isUnlocked: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit = {},
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isUnlocked) { onClick() },
        colors = CardDefaults.cardColors(
            // ✅ VRAIES COULEURS DU PROJET
            containerColor = if (isUnlocked) {
                Color(lake.difficulty.colorValue).copy(alpha = 0.8f)
            } else {
                Color(0xFF4B5563)
            }
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = if (isFavorite)
                                stringResource(R.string.lake_remove_favorite)
                            else
                                stringResource(R.string.lake_add_favorite),
                            tint = if (isFavorite) Color(0xFFFFD700) else Color.Gray
                        )
                    }
                    Text(text = lake.type.emoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = lake.getLocalizedName(), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = lake.type.displayName, fontSize = 12.sp, color = Color(0xFFE2E8F0))
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Card(
                            colors = CardDefaults.cardColors(
                                // ✅ COULEURS ORIGINALES DE DIFFICULTÉ
                                containerColor = if (isUnlocked) Color(lake.difficulty.colorValue) else Color(0xFF6B7280)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = stringResource(
                                    when (lake.difficulty) {
                                        com.rf4.fishingrf4.data.models.Difficulty.BEGINNER -> R.string.lake_difficulty_beginner
                                        com.rf4.fishingrf4.data.models.Difficulty.INTERMEDIATE -> R.string.lake_difficulty_intermediate
                                        com.rf4.fishingrf4.data.models.Difficulty.ADVANCED -> R.string.lake_difficulty_advanced
                                        com.rf4.fishingrf4.data.models.Difficulty.EXPERT -> R.string.lake_difficulty_expert
                                    }
                                ),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isUnlocked) "Débloqué" else stringResource(R.string.lake_unlock_level, lake.unlockLevel),
                            fontSize = 10.sp,
                            color = if (isUnlocked) Color(0xFF10B981) else Color(0xFF9CA3AF)
                        )
                    }
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit), tint = Color(0xFFFFB74D))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = lake.getLocalizedDescription(), fontSize = 13.sp, color = Color(0xFFE2E8F0))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.lake_species_count, lake.availableFish.size), fontSize = 12.sp, color = Color(0xFF9CA3AF))
                if (lake.coordinates.isNotEmpty()) {
                    Text(text = "${lake.coordinates.size} spots", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                }
            }
        }
    }
}