package com.rf4.fishingrf4.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.rf4.fishingrf4.R
import com.rf4.fishingrf4.data.models.Lake

/**
 * Extensions pour la traduction des lacs
 */

/**
 * Obtient le nom traduit du lac dans un Composable
 */
@Composable
fun Lake.getLocalizedName(): String {
    return when (this.id) {
        "mosquito_lake" -> stringResource(R.string.lake_mosquito_lake)
        "winding_rivulet" -> stringResource(R.string.lake_winding_rivulet)
        "old_burg_lake" -> stringResource(R.string.lake_old_burg_lake)
        "belaya_river" -> stringResource(R.string.lake_belaya_river)
        "kuori_lake" -> stringResource(R.string.lake_kuori_lake)
        "amber_lake" -> stringResource(R.string.lake_amber_lake)
        "volkhov_river" -> stringResource(R.string.lake_volkhov_river)
        "copper_lake" -> stringResource(R.string.lake_copper_lake)
        "ladoga_lake" -> stringResource(R.string.lake_ladoga_lake)
        "ladoga_archipelago" -> stringResource(R.string.lake_ladoga_archipelago)
        "sura_river" -> stringResource(R.string.lake_sura_river)
        "tunguska_river" -> stringResource(R.string.lake_tunguska_river)
        else -> this.name // Fallback vers le nom original
    }
}

/**
 * Obtient la description traduite du lac dans un Composable
 */
@Composable
fun Lake.getLocalizedDescription(): String {
    return when (this.id) {
        "mosquito_lake" -> stringResource(R.string.lake_mosquito_lake_desc)
        "winding_rivulet" -> stringResource(R.string.lake_winding_rivulet_desc)
        "old_burg_lake" -> stringResource(R.string.lake_old_burg_lake_desc)
        "belaya_river" -> stringResource(R.string.lake_belaya_river_desc)
        "kuori_lake" -> stringResource(R.string.lake_kuori_lake_desc)
        "amber_lake" -> stringResource(R.string.lake_amber_lake_desc)
        "volkhov_river" -> stringResource(R.string.lake_volkhov_river_desc)
        "copper_lake" -> stringResource(R.string.lake_copper_lake_desc)
        "ladoga_lake" -> stringResource(R.string.lake_ladoga_lake_desc)
        "ladoga_archipelago" -> stringResource(R.string.lake_ladoga_archipelago_desc)
        "sura_river" -> stringResource(R.string.lake_sura_river_desc)
        "tunguska_river" -> stringResource(R.string.lake_tunguska_river_desc)
        else -> this.description // Fallback vers la description originale
    }
}

/**
 * Version non-Composable pour utilisation dans le ViewModel ou autres classes
 */
fun Lake.getLocalizedName(context: Context): String {
    return when (this.id) {
        "mosquito_lake" -> context.getString(R.string.lake_mosquito_lake)
        "winding_rivulet" -> context.getString(R.string.lake_winding_rivulet)
        "old_burg_lake" -> context.getString(R.string.lake_old_burg_lake)
        "belaya_river" -> context.getString(R.string.lake_belaya_river)
        "kuori_lake" -> context.getString(R.string.lake_kuori_lake)
        "amber_lake" -> context.getString(R.string.lake_amber_lake)
        "volkhov_river" -> context.getString(R.string.lake_volkhov_river)
        "copper_lake" -> context.getString(R.string.lake_copper_lake)
        "ladoga_lake" -> context.getString(R.string.lake_ladoga_lake)
        "ladoga_archipelago" -> context.getString(R.string.lake_ladoga_archipelago)
        "sura_river" -> context.getString(R.string.lake_sura_river)
        "tunguska_river" -> context.getString(R.string.lake_tunguska_river)
        else -> this.name
    }
}

/**
 * Version non-Composable pour la description
 */
fun Lake.getLocalizedDescription(context: Context): String {
    return when (this.id) {
        "mosquito_lake" -> context.getString(R.string.lake_mosquito_lake_desc)
        "winding_rivulet" -> context.getString(R.string.lake_winding_rivulet_desc)
        "old_burg_lake" -> context.getString(R.string.lake_old_burg_lake_desc)
        "belaya_river" -> context.getString(R.string.lake_belaya_river_desc)
        "kuori_lake" -> context.getString(R.string.lake_kuori_lake_desc)
        "amber_lake" -> context.getString(R.string.lake_amber_lake_desc)
        "volkhov_river" -> context.getString(R.string.lake_volkhov_river_desc)
        "copper_lake" -> context.getString(R.string.lake_copper_lake_desc)
        "ladoga_lake" -> context.getString(R.string.lake_ladoga_lake_desc)
        "ladoga_archipelago" -> context.getString(R.string.lake_ladoga_archipelago_desc)
        "sura_river" -> context.getString(R.string.lake_sura_river_desc)
        "tunguska_river" -> context.getString(R.string.lake_tunguska_river_desc)
        else -> this.description
    }
}

/**
 * Extensions pour la traduction des appâts
 */

@Composable
fun String.getLocalizedBaitName(): String {
    return when (this) {
        // Vers et larves
        "Ver de terre" -> stringResource(R.string.bait_earthworm)
        "Ver rouge" -> stringResource(R.string.bait_red_worm)
        "Lombric" -> stringResource(R.string.bait_earthworm_large)
        "Ver de vase" -> stringResource(R.string.bait_bloodworm)
        "Asticots" -> stringResource(R.string.bait_maggots)
        "Chrysalides" -> stringResource(R.string.bait_chrysalis)
        "Ver haché" -> stringResource(R.string.bait_chopped_worm)
        "Larve de scolyte" -> stringResource(R.string.bait_bark_beetle_larvae)
        "Larve d'éphémère" -> stringResource(R.string.bait_mayfly_larvae)
        "Larve de plécoptère" -> stringResource(R.string.bait_stonefly_larvae)
        "Larve de phrygane" -> stringResource(R.string.bait_caddisfly_larvae)
        "Sangsues" -> stringResource(R.string.bait_leeches)

        // Pâtes artisanales
        "Pain mouillé" -> stringResource(R.string.bait_wet_bread)
        "Pâte à l'ail" -> stringResource(R.string.bait_garlic_paste)
        "Pâte sucrée" -> stringResource(R.string.bait_sweet_paste)
        "Pâte au miel" -> stringResource(R.string.bait_honey_paste)
        "Pâte aux œufs" -> stringResource(R.string.bait_egg_paste)
        "Pâte au fromage blanc" -> stringResource(R.string.bait_cottage_cheese_paste)

        // Graines et céréales
        "Orge perlé" -> stringResource(R.string.bait_pearl_barley)
        "Maïs" -> stringResource(R.string.bait_corn)
        "Semoule" -> stringResource(R.string.bait_semolina)
        "Flocons d'avoine" -> stringResource(R.string.bait_oat_flakes)
        "Grains de blé" -> stringResource(R.string.bait_wheat_grains)
        "Bouillie de pois" -> stringResource(R.string.bait_pea_porridge)
        "Bouillie de semoule" -> stringResource(R.string.bait_semolina_porridge)
        "Bouillie de millet" -> stringResource(R.string.bait_millet_porridge)
        "Tournesol" -> stringResource(R.string.bait_sunflower_seeds)
        "Graines de lin" -> stringResource(R.string.bait_flax_seeds)
        "Chènevis" -> stringResource(R.string.bait_hemp_seeds)

        // Appâts vivants
        "Vif" -> stringResource(R.string.bait_live_bait)
        "Morceaux de poisson" -> stringResource(R.string.bait_fish_pieces)
        "Écrevisse" -> stringResource(R.string.bait_crayfish)
        "Moule de rivière" -> stringResource(R.string.bait_river_mussel)
        "Moule zébrée" -> stringResource(R.string.bait_zebra_mussel)
        "Grenouille" -> stringResource(R.string.bait_frog)

        // Appâts spéciaux
        "Cubes de pomme de terre" -> stringResource(R.string.bait_potato_cubes)
        "Cubes de fromage" -> stringResource(R.string.bait_cheese_cubes)
        "Polenta" -> stringResource(R.string.bait_polenta)
        "Poisson mort" -> stringResource(R.string.bait_dead_fish)
        "Viande de pétoncle" -> stringResource(R.string.bait_scallop_meat)
        "Crabe" -> stringResource(R.string.bait_crab)

        // Appâts marins
        "Ver marin" -> stringResource(R.string.bait_marine_worm)
        "Crevette" -> stringResource(R.string.bait_shrimp)
        "Calamar" -> stringResource(R.string.bait_squid)

        // Autres
        "Bouillettes" -> stringResource(R.string.bait_boilies)
        "Pellets" -> stringResource(R.string.bait_pellets)
        "Autre" -> stringResource(R.string.bait_other)

        else -> this // Fallback vers le nom original
    }
}

fun String.getLocalizedBaitName(context: Context): String {
    return when (this) {
        // Vers et larves
        "Ver de terre" -> context.getString(R.string.bait_earthworm)
        "Ver rouge" -> context.getString(R.string.bait_red_worm)
        "Lombric" -> context.getString(R.string.bait_earthworm_large)
        "Ver de vase" -> context.getString(R.string.bait_bloodworm)
        "Asticots" -> context.getString(R.string.bait_maggots)
        "Chrysalides" -> context.getString(R.string.bait_chrysalis)
        "Ver haché" -> context.getString(R.string.bait_chopped_worm)
        "Larve de scolyte" -> context.getString(R.string.bait_bark_beetle_larvae)
        "Larve d'éphémère" -> context.getString(R.string.bait_mayfly_larvae)
        "Larve de plécoptère" -> context.getString(R.string.bait_stonefly_larvae)
        "Larve de phrygane" -> context.getString(R.string.bait_caddisfly_larvae)
        "Sangsues" -> context.getString(R.string.bait_leeches)

        // Pâtes artisanales
        "Pain mouillé" -> context.getString(R.string.bait_wet_bread)
        "Pâte à l'ail" -> context.getString(R.string.bait_garlic_paste)
        "Pâte sucrée" -> context.getString(R.string.bait_sweet_paste)
        "Pâte au miel" -> context.getString(R.string.bait_honey_paste)
        "Pâte aux œufs" -> context.getString(R.string.bait_egg_paste)
        "Pâte au fromage blanc" -> context.getString(R.string.bait_cottage_cheese_paste)

        // Graines et céréales
        "Orge perlé" -> context.getString(R.string.bait_pearl_barley)
        "Maïs" -> context.getString(R.string.bait_corn)
        "Semoule" -> context.getString(R.string.bait_semolina)
        "Flocons d'avoine" -> context.getString(R.string.bait_oat_flakes)
        "Grains de blé" -> context.getString(R.string.bait_wheat_grains)
        "Bouillie de pois" -> context.getString(R.string.bait_pea_porridge)
        "Bouillie de semoule" -> context.getString(R.string.bait_semolina_porridge)
        "Bouillie de millet" -> context.getString(R.string.bait_millet_porridge)
        "Tournesol" -> context.getString(R.string.bait_sunflower_seeds)
        "Graines de lin" -> context.getString(R.string.bait_flax_seeds)
        "Chènevis" -> context.getString(R.string.bait_hemp_seeds)

        // Appâts vivants
        "Vif" -> context.getString(R.string.bait_live_bait)
        "Morceaux de poisson" -> context.getString(R.string.bait_fish_pieces)
        "Écrevisse" -> context.getString(R.string.bait_crayfish)
        "Moule de rivière" -> context.getString(R.string.bait_river_mussel)
        "Moule zébrée" -> context.getString(R.string.bait_zebra_mussel)
        "Grenouille" -> context.getString(R.string.bait_frog)

        // Appâts spéciaux
        "Cubes de pomme de terre" -> context.getString(R.string.bait_potato_cubes)
        "Cubes de fromage" -> context.getString(R.string.bait_cheese_cubes)
        "Polenta" -> context.getString(R.string.bait_polenta)
        "Poisson mort" -> context.getString(R.string.bait_dead_fish)
        "Viande de pétoncle" -> context.getString(R.string.bait_scallop_meat)
        "Crabe" -> context.getString(R.string.bait_crab)

        // Appâts marins
        "Ver marin" -> context.getString(R.string.bait_marine_worm)
        "Crevette" -> context.getString(R.string.bait_shrimp)
        "Calamar" -> context.getString(R.string.bait_squid)

        // Autres
        "Bouillettes" -> context.getString(R.string.bait_boilies)
        "Pellets" -> context.getString(R.string.bait_pellets)
        "Autre" -> context.getString(R.string.bait_other)

        else -> this
    }
}