package com.rf4.fishingrf4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import com.rf4.fishingrf4.data.online.SpeciesCount
import com.rf4.fishingrf4.ui.components.BackButton

@Composable
fun TopFiveScreen(
    speciesTop5: List<SpeciesCount>,
    playersTop5: List<Pair<String, Long>>,
    lakesTop5: List<Pair<String, Long>>,
    communityTop5: List<Pair<String, Long>>, // On garde le paramètre pour éviter les erreurs
    onBack: () -> Unit
) {
    var tabIndex by remember { mutableStateOf(0) }
// ✅ MODIFIÉ : Seulement 3 onglets maintenant
    val tabs = listOf("Espèces", "Joueurs", "Lacs")
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackButton(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("🏆 Top 5 du jour", style = MaterialTheme.typography.titleLarge, color = Color.White)
        }
        Spacer(Modifier.height(8.dp))

        TabRow(selectedTabIndex = tabIndex, containerColor = Color(0xFF3B82F6)) {
            tabs.forEachIndexed { i, title ->
                Tab(
                    selected = tabIndex == i,
                    onClick = { tabIndex = i },
                    text = { Text(title, color = Color.White) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // ✅ MODIFIÉ : Seulement 3 cas maintenant
        when (tabIndex) {
            0 -> Top5List(speciesTop5.map { it.species to it.count })
            1 -> Top5List(playersTop5)
            2 -> Top5List(lakesTop5)
            // ❌ SUPPRIMÉ : Le cas 3 pour les appâts communautaires
        }
    }
}
@Composable
private fun Top5List(data: List<Pair<String, Long>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2563EB)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
// Affichage de chaque élément de la liste
            data.forEachIndexed { index, (label, value) ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2455AF)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
// Affichage du rang et du label (nom de l'espèce, joueur, lac)
                        Text("${index + 1}. $label", color = Color.White)
// Affichage du nombre de captures
                        AssistChip(
                            onClick = { /* Action lors du clic sur le nombre (optionnel) */ },
                            label = { Text("$value", color = Color.White) },
                            colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFF3B82F6))
                        )
                    }
                }
            }
// Si la liste est vide, on affiche un message
            if (data.isEmpty()) {
                Text("Aucune donnée pour aujourd'hui.", color = Color(0xFFE5E7EB))
            }
        }
    }
}