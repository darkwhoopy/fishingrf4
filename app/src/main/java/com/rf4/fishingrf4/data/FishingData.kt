// ==========================================
// FICHIER: data/FishingData.kt - DONNÉES COMPLÈTES RF4 MISE À JOUR CORRIGÉE
// Tous les poissons manquants ajoutés selon les sources officielles
// ==========================================
package com.rf4.fishingrf4.data

import com.rf4.fishingrf4.data.models.*

object FishingData {

    // ==========================================
    // POISSONS COMMUNS (EXISTANTS + NOUVEAUX)
    // ==========================================
    private val commonFresh = listOf(
        // Poissons existants conservés
        Fish(
            "Gardon", "Rutilus rutilus", FishRarity.COMMON,
            weight = 0.05..0.3, preferredBait = listOf("Pain", "Asticot", "Blé"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Ablette", "Alburnus alburnus", FishRarity.COMMON,
            weight = 0.02..0.1, preferredBait = listOf("Asticot", "Ver de vase"),
            bestHours = listOf(5, 6, 7, 17, 18, 19), bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Brème", "Abramis brama", FishRarity.COMMON,
            weight = 0.3..2.0, preferredBait = listOf("Ver de vase", "Pâte", "Maïs"),
            bestHours = listOf(20, 21, 22, 5, 6), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Rotengle", "Scardinius erythrophthalmus", FishRarity.COMMON,
            weight = 0.1..0.8, preferredBait = listOf("Pain", "Ver rouge", "Blé"),
            bestHours = listOf(7, 8, 9, 16, 17, 18), bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Carassin", "Carassius carassius", FishRarity.COMMON,
            weight = 0.2..1.5, preferredBait = listOf("Ver de terre", "Pain", "Maïs"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Goujon", "Gobio gobio", FishRarity.COMMON,
            weight = 0.05..0.2, preferredBait = listOf("Ver de vase", "Asticot"),
            bestHours = listOf(6, 7, 8, 17, 18), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Vairon", "Phoxinus phoxinus", FishRarity.COMMON,
            weight = 0.02..0.1, preferredBait = listOf("Asticot", "Pain"),
            bestHours = listOf(7, 8, 9, 16, 17), bestWeather = listOf(WeatherType.SUNNY)
        ),
        Fish(
            "Épinoche", "Gasterosteus aculeatus", FishRarity.COMMON,
            weight = 0.01..0.05, preferredBait = listOf("Ver rouge", "Asticot"),
            bestHours = listOf(8, 9, 10, 15, 16), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Brème bleue", "Ballerus ballerus", FishRarity.COMMON,
            weight = 0.1..0.5, preferredBait = listOf("Ver de vase", "Asticot"),
            bestHours = listOf(19, 20, 21, 4, 5), bestWeather = listOf(WeatherType.OVERCAST)
        ),

        // ==========================================
        // NOUVEAUX POISSONS MANQUANTS AJOUTÉS
        // ==========================================

        // ✅ Loche d'étang - CONFIRMÉ MANQUANT
        Fish(
            "Loche d'étang", "Misgurnus fossilis", FishRarity.UNCOMMON,
            weight = 0.1..0.8, preferredBait = listOf("Ver de terre", "Ver de vase", "Asticot"),
            bestHours = listOf(20, 21, 22, 23, 4, 5), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),

        // ✅ Loche franche - CONFIRMÉ MANQUANT
        Fish(
            "Loche franche", "Barbatula barbatula", FishRarity.COMMON,
            weight = 0.02..0.15, preferredBait = listOf("Ver de vase", "Asticot"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.ANY)
        ),

        // ✅ Vimbe - CONFIRMÉ MANQUANT
        Fish(
            "Vimbe", "Vimba vimba", FishRarity.UNCOMMON,
            weight = 0.3..2.5, preferredBait = listOf("Ver rouge", "Pâte", "Maïs"),
            bestHours = listOf(6, 7, 8, 17, 18, 19), bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),

        // ✅ Gardon de la Caspienne - CONFIRMÉ MANQUANT
        Fish(
            "Gardon de la Caspienne", "Rutilus caspicus", FishRarity.UNCOMMON,
            weight = 0.08..0.6, preferredBait = listOf("Pain", "Blé", "Asticot"),
            bestHours = listOf(6, 7, 8, 16, 17, 18), bestWeather = listOf(WeatherType.SUNNY)
        ),

        // ✅ Gardon de Sibérie - CONFIRMÉ MANQUANT
        Fish(
            "Gardon de Sibérie", "Rutilus lacustris", FishRarity.UNCOMMON,
            weight = 0.1..0.7, preferredBait = listOf("Pain", "Blé", "Ver rouge"),
            bestHours = listOf(7, 8, 9, 17, 18, 19), bestWeather = listOf(WeatherType.CLOUDY)
        ),

        // ✅ Brème du Danube - CONFIRMÉ MANQUANT
        Fish(
            "Brème du Danube", "Abramis sapa", FishRarity.UNCOMMON,
            weight = 0.4..3.0, preferredBait = listOf("Ver de vase", "Pâte", "Maïs"),
            bestHours = listOf(20, 21, 22, 4, 5, 6), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),

        // ✅ Shemaya - CONFIRMÉ MANQUANT
        Fish(
            "Shemaya", "Alburnus chalcoides", FishRarity.RARE,
            weight = 0.1..0.4, preferredBait = listOf("Asticot", "Ver rouge", "Pain"),
            bestHours = listOf(6, 7, 8, 17, 18), bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),

        // ✅ Shemaya de la mer Noire - CONFIRMÉ MANQUANT
        Fish(
            "Shemaya de la mer Noire", "Alburnus mento", FishRarity.RARE,
            weight = 0.15..0.5, preferredBait = listOf("Asticot", "Ver rouge"),
            bestHours = listOf(7, 8, 9, 16, 17), bestWeather = listOf(WeatherType.SUNNY)
        ),

        // ✅ Goujon de l'Amour - CONFIRMÉ MANQUANT
        Fish(
            "Goujon de l'Amour", "Gobio cynocephalus", FishRarity.UNCOMMON,
            weight = 0.08..0.3, preferredBait = listOf("Ver de vase", "Asticot", "Pain"),
            bestHours = listOf(6, 7, 8, 17, 18), bestWeather = listOf(WeatherType.ANY)
        ),

        // ✅ Meunier rouge - CONFIRMÉ MANQUANT
        Fish(
            "Meunier rouge", "Catostomus catostomus", FishRarity.UNCOMMON,
            weight = 0.5..2.0, preferredBait = listOf("Ver de terre", "Asticot"),
            bestHours = listOf(6, 7, 8, 18, 19), bestWeather = listOf(WeatherType.CLOUDY)
        ),

        // ✅ Ménomini rond - CONFIRMÉ MANQUANT
        Fish(
            "Ménomini rond", "Prosopium cylindraceum", FishRarity.UNCOMMON,
            weight = 0.3..1.5, preferredBait = listOf("Ver rouge", "Asticot", "Petit leurre"),
            bestHours = listOf(7, 8, 9, 17, 18, 19), bestWeather = listOf(WeatherType.CLOUDY)
        ),

        // ✅ Petite épinoche méridionale - CONFIRMÉ MANQUANT
        Fish(
            "Petite épinoche méridionale", "Pungitius platygaster", FishRarity.COMMON,
            weight = 0.01..0.04, preferredBait = listOf("Asticot", "Ver rouge"),
            bestHours = listOf(8, 9, 10, 15, 16), bestWeather = listOf(WeatherType.ANY)
        ),

        // ✅ Perche Soleil - CONFIRMÉ MANQUANT
        Fish(
            "Perche Soleil", "Lepomis gibbosus", FishRarity.UNCOMMON,
            weight = 0.1..0.5, preferredBait = listOf("Ver rouge", "Asticot", "Petit leurre"),
            bestHours = listOf(8, 9, 10, 16, 17, 18), bestWeather = listOf(WeatherType.SUNNY)
        )
    )

    // ==========================================
    // POISSONS INTERMÉDIAIRES (EXISTANTS + NOUVEAUX)
    // ==========================================
    private val intermediateFresh = listOf(
        // Conserve tous les poissons existants...
        Fish(
            "Perche", "Perca fluviatilis", FishRarity.COMMON,
            weight = 0.1..1.5, preferredBait = listOf("Ver rouge", "Petit leurre", "Poisson vif"),
            bestHours = listOf(7, 8, 9, 16, 17, 18), bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Tanche", "Tinca tinca", FishRarity.UNCOMMON,
            weight = 0.5..3.0, preferredBait = listOf("Ver de terre", "Maïs", "Pain"),
            bestHours = listOf(5, 6, 7, 19, 20, 21), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Brochet", "Esox lucius", FishRarity.UNCOMMON,
            weight = 1.0..15.0, preferredBait = listOf("Poisson vif", "Leurre", "Grenouille"),
            bestHours = listOf(6, 7, 8, 17, 18, 19), bestWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST)
        ),
        Fish(
            "Sandre", "Sander lucioperca", FishRarity.UNCOMMON,
            weight = 0.8..8.0, preferredBait = listOf("Poisson vif", "Leurre", "Ver rouge"),
            bestHours = listOf(19, 20, 21, 5, 6, 7), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Carpe commune", "Cyprinus carpio", FishRarity.RARE,
            weight = 2.0..25.0, preferredBait = listOf("Maïs", "Bouillette", "Pain", "Ver de terre"),
            bestHours = listOf(5, 6, 7, 19, 20, 21), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Carpe miroir", "Cyprinus carpio", FishRarity.RARE,
            weight = 3.0..30.0, preferredBait = listOf("Bouillette", "Maïs", "Pain"),
            bestHours = listOf(4, 5, 6, 20, 21, 22), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Ide mélanote", "Leuciscus idus", FishRarity.UNCOMMON,
            weight = 0.5..4.0, preferredBait = listOf("Ver rouge", "Pain", "Maïs"),
            bestHours = listOf(6, 7, 8, 17, 18, 19), bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Chevesne", "Squalius cephalus", FishRarity.UNCOMMON,
            weight = 0.5..6.0, preferredBait = listOf("Pain", "Cerise", "Sauterelle", "Leurre"),
            bestHours = listOf(8, 9, 10, 16, 17, 18), bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Barbeau", "Barbus barbus", FishRarity.UNCOMMON,
            weight = 1.0..8.0, preferredBait = listOf("Ver de terre", "Maïs", "Bouillette"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Corégone", "Coregonus lavaretus", FishRarity.RARE,
            weight = 0.8..5.0, preferredBait = listOf("Ver rouge", "Petit leurre"),
            bestHours = listOf(5, 6, 7, 20, 21, 22), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Lotte", "Lota lota", FishRarity.RARE,
            weight = 1.0..8.0, preferredBait = listOf("Poisson vif", "Ver de terre", "Grenouille"),
            bestHours = listOf(22, 23, 0, 1, 2, 3), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Omble chevalier", "Salvelinus alpinus", FishRarity.RARE,
            weight = 1.5..8.0, preferredBait = listOf("Ver rouge", "Poisson vif", "Leurre"),
            bestHours = listOf(5, 6, 7, 19, 20, 21), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Truite arc-en-ciel", "Oncorhynchus mykiss", FishRarity.UNCOMMON,
            weight = 0.5..5.0, preferredBait = listOf("Ver rouge", "Mouche", "Leurre"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST)
        ),
        Fish(
            "Truite de lac", "Salmo trutta lacustris", FishRarity.RARE,
            weight = 2.0..12.0, preferredBait = listOf("Poisson vif", "Leurre", "Ver rouge"),
            bestHours = listOf(5, 6, 7, 19, 20, 21), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Ombre", "Thymallus thymallus", FishRarity.UNCOMMON,
            weight = 0.3..2.0, preferredBait = listOf("Mouche", "Ver rouge"),
            bestHours = listOf(7, 8, 9, 17, 18, 19), bestWeather = listOf(WeatherType.CLOUDY)
        ),

        // ✅ NOUVEAUX POISSONS INTERMÉDIAIRES MANQUANTS
        Fish(
            "Omble à points blancs", "Salvelinus leucomaenis", FishRarity.RARE,
            weight = 0.8..4.0, preferredBait = listOf("Ver rouge", "Petits poissons", "Leurre"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST)
        ),

        Fish(
            "Omble de l'Arctique", "Salvelinus alpinus", FishRarity.RARE,
            weight = 1.0..6.0, preferredBait = listOf("Poisson vif", "Ver rouge", "Leurre"),
            bestHours = listOf(5, 6, 7, 19, 20, 21), bestWeather = listOf(WeatherType.OVERCAST)
        ),

        Fish(
            "Omble du Pacifique", "Salvelinus malma", FishRarity.RARE,
            weight = 1.5..7.0, preferredBait = listOf("Poisson vif", "Ver rouge", "Leurre"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.CLOUDY)
        ),

        Fish(
            "Omble gris", "Salvelinus namaycush", FishRarity.RARE,
            weight = 2.0..12.0, preferredBait = listOf("Poisson vif", "Gros leurre"),
            bestHours = listOf(5, 6, 7, 19, 20, 21), bestWeather = listOf(WeatherType.OVERCAST)
        ),

        Fish(
            "Omble Kuori", "Salvelinus kuori", FishRarity.EPIC,
            weight = 1.8..6.5, preferredBait = listOf("Ver rouge", "Poisson vif", "Leurre"),
            bestHours = listOf(6, 7, 8, 19, 20), bestWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST)
        ),

        Fish(
            "Omble rouge", "Salvelinus alpinus", FishRarity.RARE,
            weight = 1.2..5.0, preferredBait = listOf("Ver rouge", "Poisson vif"),
            bestHours = listOf(6, 7, 8, 18, 19), bestWeather = listOf(WeatherType.CLOUDY)
        ),

        Fish(
            "Muksun", "Coregonus muksun", FishRarity.RARE,
            weight = 1.0..8.0, preferredBait = listOf("Ver rouge", "Poisson vif"),
            bestHours = listOf(5, 6, 7, 20, 21, 22), bestWeather = listOf(WeatherType.OVERCAST)
        ),

        Fish(
            "Ombre Arctique", "Thymallus arcticus", FishRarity.UNCOMMON,
            weight = 0.5..2.5, preferredBait = listOf("Mouche", "Ver rouge", "Petit leurre"),
            bestHours = listOf(6, 7, 8, 17, 18, 19), bestWeather = listOf(WeatherType.CLOUDY)
        ),

        Fish(
            "Ombre de la Sibérie orientale", "Thymallus pallasi", FishRarity.UNCOMMON,
            weight = 0.4..2.0, preferredBait = listOf("Mouche", "Ver rouge"),
            bestHours = listOf(7, 8, 9, 18, 19), bestWeather = listOf(WeatherType.CLOUDY)
        ),

        Fish(
            "Omul du Baïkal", "Coregonus migratorius", FishRarity.RARE,
            weight = 0.5..3.0, preferredBait = listOf("Ver rouge", "Petit leurre"),
            bestHours = listOf(6, 7, 8, 19, 20), bestWeather = listOf(WeatherType.CLOUDY)
        )
    )

    // ==========================================
    // POISSONS AVANCÉS ET LÉGENDAIRES (EXISTANTS + NOUVEAUX)
    // ==========================================
    private val advancedFresh = listOf(
        Fish(
            "Silure", "Silurus glanis", FishRarity.EPIC,
            weight = 10.0..100.0, preferredBait = listOf("Poisson vif", "Grenouille", "Ver de terre"),
            bestHours = listOf(20, 21, 22, 23, 4, 5), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.RAIN)
        ),
        Fish(
            "Esturgeon", "Acipenser sturio", FishRarity.LEGENDARY,
            weight = 20.0..200.0, preferredBait = listOf("Ver de terre", "Poisson vif", "Bouillette"),
            bestHours = listOf(4, 5, 6, 20, 21, 22), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Béluga", "Huso huso", FishRarity.LEGENDARY,
            weight = 50.0..500.0, preferredBait = listOf("Gros poisson vif", "Bouillette spéciale"),
            bestHours = listOf(3, 4, 5, 21, 22, 23), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.RAIN)
        ),
        Fish(
            "Sterlet", "Acipenser ruthenus", FishRarity.EPIC,
            weight = 2.0..15.0, preferredBait = listOf("Ver de terre", "Ver rouge", "Crevette"),
            bestHours = listOf(5, 6, 7, 19, 20, 21), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Aspe", "Aspius aspius", FishRarity.RARE,
            weight = 3.0..20.0, preferredBait = listOf("Poisson vif", "Leurre de surface"),
            bestHours = listOf(7, 8, 9, 17, 18, 19), bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Saumon atlantique", "Salmo salar", FishRarity.EPIC,
            weight = 5.0..40.0, preferredBait = listOf("Poisson vif", "Gros leurre"),
            bestHours = listOf(5, 6, 7, 19, 20, 21), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Anguille", "Anguilla anguilla", FishRarity.RARE,
            weight = 0.8..6.0, preferredBait = listOf("Ver de terre", "Poisson vif"),
            bestHours = listOf(21, 22, 23, 0, 1, 2), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Carpe fantôme", "Cyprinus carpio", FishRarity.LEGENDARY,
            weight = 15.0..60.0, preferredBait = listOf("Bouillette premium", "Maïs tiger", "Pain spécial"),
            bestHours = listOf(2, 3, 4, 22, 23, 0), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Amour Blanc", "Ctenopharyngodon idella", FishRarity.EPIC,
            weight = 8.0..50.0, preferredBait = listOf("Herbe", "Maïs", "Bouillette végétale"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Carpe à grosse tête", "Hypophthalmichthys nobilis", FishRarity.EPIC,
            weight = 10.0..40.0, preferredBait = listOf("Plancton", "Pain", "Bouillette"),
            bestHours = listOf(5, 6, 7, 19, 20, 21), bestWeather = listOf(WeatherType.OVERCAST)
        ),

        // ✅ NOUVEAUX POISSONS AVANCÉS MANQUANTS
        Fish(
            "Nase (ou hotu)", "Chondrostoma nasus", FishRarity.UNCOMMON,
            weight = 0.8..3.5, preferredBait = listOf("Ver rouge", "Algues", "Pain"),
            bestHours = listOf(6, 7, 8, 17, 18, 19), bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),

        Fish(
            "Omble Drjagini", "Salvelinus drjagini", FishRarity.EPIC,
            weight = 2.0..8.0, preferredBait = listOf("Poisson vif", "Gros leurre"),
            bestHours = listOf(5, 6, 7, 20, 21, 22), bestWeather = listOf(WeatherType.OVERCAST)
        ),

        Fish(
            "Omble Levanidov", "Salvelinus levanidovi", FishRarity.LEGENDARY,
            weight = 3.0..15.0, preferredBait = listOf("Poisson vif", "Gros leurre"),
            bestHours = listOf(5, 6, 21, 22, 23), bestWeather = listOf(WeatherType.OVERCAST)
        ),

        Fish(
            "Omble neiva", "Salvelinus neiva", FishRarity.EPIC,
            weight = 2.5..9.0, preferredBait = listOf("Poisson vif", "Leurre"),
            bestHours = listOf(6, 7, 19, 20, 21), bestWeather = listOf(WeatherType.CLOUDY)
        ),

        Fish(
            "Nelma", "Stenodus leucichthys", FishRarity.EPIC,
            weight = 5.0..30.0, preferredBait = listOf("Poisson vif", "Gros leurre"),
            bestHours = listOf(5, 6, 21, 22, 23), bestWeather = listOf(WeatherType.OVERCAST)
        ),

        Fish(
            "Poisson-chat de l'Amour", "Silurus asotus", FishRarity.RARE,
            weight = 3.0..25.0, preferredBait = listOf("Poisson vif", "Ver de terre", "Grenouille"),
            bestHours = listOf(20, 21, 22, 23, 4, 5), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        )
    )

    // ==========================================
    // POISSONS MARINS COMPLETS (EXISTANTS + NOUVEAUX)
    // ==========================================
    private val marineFish = listOf(
        Fish(
            "Hareng", "Clupea harengus", FishRarity.COMMON,
            weight = 0.2..0.8, preferredBait = listOf("Ver marin", "Petit leurre"),
            bestHours = listOf(8, 9, 10, 16, 17, 18), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Plie", "Pleuronectes platessa", FishRarity.COMMON,
            weight = 0.5..3.0, preferredBait = listOf("Ver marin", "Crevette"),
            bestHours = listOf(9, 10, 11, 15, 16, 17), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Flet", "Platichthys flesus", FishRarity.COMMON,
            weight = 0.3..2.0, preferredBait = listOf("Ver marin", "Crevette"),
            bestHours = listOf(10, 11, 12, 14, 15, 16), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Morue", "Gadus morhua", FishRarity.UNCOMMON,
            weight = 2.0..15.0, preferredBait = listOf("Poisson vif", "Ver marin", "Crevette"),
            bestHours = listOf(10, 11, 12, 13, 14, 15), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Merlan", "Merlangius merlangus", FishRarity.COMMON,
            weight = 0.3..2.5, preferredBait = listOf("Ver marin", "Petits poissons"),
            bestHours = listOf(9, 10, 11, 15, 16, 17), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Lieu noir", "Pollachius virens", FishRarity.UNCOMMON,
            weight = 1.0..8.0, preferredBait = listOf("Poisson vif", "Leurre", "Ver marin"),
            bestHours = listOf(8, 9, 10, 16, 17, 18), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Turbot", "Psetta maxima", FishRarity.RARE,
            weight = 3.0..25.0, preferredBait = listOf("Poisson vif", "Crevette", "Ver marin"),
            bestHours = listOf(11, 12, 13, 14, 15), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Cabillaud", "Gadus morhua", FishRarity.UNCOMMON,
            weight = 1.5..10.0, preferredBait = listOf("Poisson vif", "Ver marin"),
            bestHours = listOf(10, 11, 12, 13, 14), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Maquereau", "Scomber scombrus", FishRarity.COMMON,
            weight = 0.3..1.5, preferredBait = listOf("Petit leurre", "Ver marin"),
            bestHours = listOf(8, 9, 10, 16, 17, 18), bestWeather = listOf(WeatherType.SUNNY)
        ),
        Fish(
            "Flétan", "Hippoglossus hippoglossus", FishRarity.EPIC,
            weight = 20.0..200.0, preferredBait = listOf("Gros poisson vif", "Crevette géante"),
            bestHours = listOf(11, 12, 13, 14, 15), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Requin épineux", "Squalus acanthias", FishRarity.RARE,
            weight = 5.0..40.0, preferredBait = listOf("Poisson vif", "Calamar"),
            bestHours = listOf(20, 21, 22, 23, 0, 1), bestWeather = listOf(WeatherType.OVERCAST)
        ),

        // ✅ NOUVEAUX POISSONS MARINS MANQUANTS
        Fish(
            "Lompe", "Cyclopterus lumpus", FishRarity.UNCOMMON,
            weight = 0.5..3.0, preferredBait = listOf("Ver marin", "Crevette"),
            bestHours = listOf(10, 11, 12, 13, 14), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Loquette d'Europe", "Zoarces viviparus", FishRarity.COMMON,
            weight = 0.1..0.4, preferredBait = listOf("Ver marin", "Asticot"),
            bestHours = listOf(8, 9, 10, 15, 16), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Loup de l'Atlantique", "Anarhichas lupus", FishRarity.RARE,
            weight = 2.0..15.0, preferredBait = listOf("Poisson vif", "Crevette", "Crabe"),
            bestHours = listOf(10, 11, 12, 13, 14, 15), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Loup gélatineux", "Anarhichas minor", FishRarity.RARE,
            weight = 1.5..12.0, preferredBait = listOf("Poisson vif", "Ver marin"),
            bestHours = listOf(9, 10, 11, 14, 15, 16), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Loup tacheté", "Anarhichas minor", FishRarity.RARE,
            weight = 3.0..18.0, preferredBait = listOf("Poisson vif", "Crevette"),
            bestHours = listOf(11, 12, 13, 14, 15), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Lycode à oreille", "Lycodes esmarkii", FishRarity.UNCOMMON,
            weight = 0.2..1.0, preferredBait = listOf("Ver marin", "Petits poissons"),
            bestHours = listOf(9, 10, 11, 15, 16), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Lycode d'Esmark", "Lycodes esmarkii", FishRarity.UNCOMMON,
            weight = 0.3..1.2, preferredBait = listOf("Ver marin", "Crevette"),
            bestHours = listOf(10, 11, 12, 14, 15), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Merlan bleu", "Micromesistius poutassou", FishRarity.COMMON,
            weight = 0.2..1.5, preferredBait = listOf("Ver marin", "Petits poissons"),
            bestHours = listOf(8, 9, 10, 16, 17), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Merlu européen", "Merluccius merluccius", FishRarity.UNCOMMON,
            weight = 1.0..8.0, preferredBait = listOf("Poisson vif", "Crevette"),
            bestHours = listOf(11, 12, 13, 14, 15), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Petit sébaste", "Sebastes viviparus", FishRarity.UNCOMMON,
            weight = 0.2..1.0, preferredBait = listOf("Ver marin", "Crevette"),
            bestHours = listOf(10, 11, 12, 14, 15), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Pétoncle d'Islande", "Chlamys islandica", FishRarity.UNCOMMON,
            weight = 0.05..0.2, preferredBait = listOf("Ver marin"),
            bestHours = listOf(9, 10, 11, 14, 15, 16), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Poisson-football de l'Atlantique", "Cyclopterus lumpus", FishRarity.UNCOMMON,
            weight = 0.8..4.0, preferredBait = listOf("Ver marin", "Crevette"),
            bestHours = listOf(10, 11, 12, 13, 14), bestWeather = listOf(WeatherType.ANY)
        ),

        // ✅ Raies et requins manquants
        Fish(
            "Raie boréale", "Amblyraja hyperborea", FishRarity.RARE,
            weight = 5.0..40.0, preferredBait = listOf("Poisson vif", "Crevette", "Crabe"),
            bestHours = listOf(10, 11, 12, 13, 14, 15), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Raie radiée", "Amblyraja radiata", FishRarity.RARE,
            weight = 3.0..25.0, preferredBait = listOf("Poisson vif", "Ver marin"),
            bestHours = listOf(11, 12, 13, 14, 15), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Requin du Groenland", "Somniosus microcephalus", FishRarity.LEGENDARY,
            weight = 50.0..500.0, preferredBait = listOf("Gros poisson vif", "Viande"),
            bestHours = listOf(22, 23, 0, 1, 2, 3), bestWeather = listOf(WeatherType.OVERCAST, WeatherType.RAIN)
        ),

        Fish(
            "Requin pèlerin", "Cetorhinus maximus", FishRarity.LEGENDARY,
            weight = 100.0..1000.0, preferredBait = listOf("Plancton", "Petits poissons"),
            bestHours = listOf(10, 11, 12, 13, 14), bestWeather = listOf(WeatherType.SUNNY)
        ),

        Fish(
            "Requin-Lézard", "Chlamydoselachus anguineus", FishRarity.LEGENDARY,
            weight = 20.0..200.0, preferredBait = listOf("Poisson vif", "Calamar"),
            bestHours = listOf(22, 23, 0, 1, 2), bestWeather = listOf(WeatherType.ANY)
        ),

        // ✅ Mollusques et crustacés
        Fish(
            "Moule", "Mytilus edulis", FishRarity.COMMON,
            weight = 0.02..0.1, preferredBait = listOf("Plancton"),
            bestHours = listOf(8, 9, 10, 14, 15, 16), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Moule de rivière", "Unio pictorum", FishRarity.COMMON,
            weight = 0.03..0.15, preferredBait = listOf("Matière organique"),
            bestHours = listOf(9, 10, 11, 15, 16), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Moule zébrée", "Dreissena polymorpha", FishRarity.COMMON,
            weight = 0.01..0.05, preferredBait = listOf("Plancton"),
            bestHours = listOf(8, 9, 10, 15, 16), bestWeather = listOf(WeatherType.ANY)
        )
    )

    // ==========================================
    // FONCTION POUR RÉCUPÉRER TOUS LES POISSONS
    // ==========================================
    fun getAllFish(): List<Fish> {
        return commonFresh + intermediateFresh + advancedFresh + marineFish
    }

    // ==========================================
    // FONCTION POUR RÉCUPÉRER LES POISSONS PAR NOM
    // ==========================================
    fun getFishByNames(names: List<String>): List<Fish> {
        val allFish = getAllFish()
        return names.mapNotNull { fishName ->
            allFish.find { it.name.equals(fishName, ignoreCase = true) }
        }
    }

    // ==========================================
    // FONCTION POUR RECHERCHER UN POISSON
    // ==========================================
    fun searchFish(query: String): List<Fish> {
        val allFish = getAllFish()
        return allFish.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.species.contains(query, ignoreCase = true)
        }
    }

    // ==========================================
    // LACS AVEC MISE À JOUR DES POISSONS DISPONIBLES
    // ==========================================
    val lakes = listOf(
        Lake(
            id = "mosquito_lake",
            name = "Lac aux moustiques",
            type = LakeType.LAKE,
            difficulty = Difficulty.BEGINNER,
            availableFish = getFishByNames(
                listOf(
                    "Gardon", "Ablette", "Brème", "Rotengle", "Carassin",
                    "Goujon", "Vairon", "Épinoche", "Perche", "Tanche",
                    "Brochet", "Carpe commune", "Carpe miroir",
                    "Loche d'étang" // ✅ AJOUTÉ selon les sources officielles
                )
            ),
            description = "Lac idéal pour débuter, avec une grande variété de poissons blancs et quelques carnassiers.",
            unlockLevel = 1,
            coordinates = mapOf(
                "40:65" to "Zone peu profonde, gardons et ablettes très actifs.",
                "68:45" to "Proximité des roseaux, tanches et carpes.",
                "25:80" to "Ponton en bois, perches et brochets.",
                "55:25" to "Fosse de 4m, brèmes en soirée."
            )
        ),

        Lake(
            id = "winding_rivulet",
            name = "Ruisselet qui serpente",
            type = LakeType.CREEK, // ✅ CORRIGÉ : utilisé CREEK au lieu de STREAM
            difficulty = Difficulty.BEGINNER,
            availableFish = getFishByNames(
                listOf(
                    "Gardon", "Ablette", "Goujon", "Vairon", "Chevesne",
                    "Ide mélanote", "Perche", "Brochet", "Truite arc-en-ciel",
                    "Loche franche", "Barbeau" // ✅ AJOUTÉS
                )
            ),
            description = "Ruisseau paisible parfait pour l'apprentissage du spinning et de la pêche à la mouche.",
            unlockLevel = 3,
            coordinates = mapOf(
                "88:120" to "Méandre profond, truites et chevesnes.",
                "45:68" to "Radier peu profond, vairons et goujons.",
                "118:80" to "Sous-berge, repaire des perches.",
                "70:140" to "Confluence, zone mixte très active."
            )
        ),

        Lake(
            id = "old_burg_lake",
            name = "Lac du Vieux bourg",
            type = LakeType.LAKE,
            difficulty = Difficulty.INTERMEDIATE,
            availableFish = getFishByNames(
                listOf(
                    "Gardon", "Ablette", "Brème", "Rotengle", "Carassin",
                    "Perche", "Tanche", "Brochet", "Sandre", "Carpe commune",
                    "Carpe miroir", "Ide mélanote", "Corégone", "Lotte",
                    "Vimbe", "Brème du Danube" // ✅ AJOUTÉS
                )
            ),
            description = "Lac historique aux eaux claires. Maîtrise des appâts requise.",
            unlockLevel = 12,
            coordinates = mapOf(
                "24:68" to "La fameuse fosse de 7m, brèmes et carpes.",
                "46:26" to "Bordure de l'île, idéal pour les carnassiers.",
                "59:15" to "Zone de roseaux, tanches et amours.",
                "21:28" to "Ponton abandonné, perches."
            )
        ),

        Lake(
            id = "belaya_river",
            name = "Rivière Belaya",
            type = LakeType.RIVER,
            difficulty = Difficulty.INTERMEDIATE,
            availableFish = getFishByNames(
                listOf(
                    "Ablette", "Gardon", "Brème", "Chevesne", "Aspe",
                    "Barbeau", "Sandre", "Silure", "Sterlet",
                    "Gardon de la Caspienne", "Vimbe", "Shemaya" // ✅ AJOUTÉS
                )
            ),
            description = "Belle rivière avec un courant modéré, connue pour ses aspes et sterlets.",
            unlockLevel = 12,
            coordinates = mapOf(
                "70:125" to "Berge abrupte, chasse des aspes.",
                "45:68" to "Fosse de 7.5m, sterlets au fond.",
                "88:88" to "Herbiers, repaire des sandres.",
                "34:104" to "Zone de courant pour les barbeaux."
            )
        ),

        Lake(
            id = "kuori",
            name = "Lac Kuori",
            type = LakeType.LAKE,
            difficulty = Difficulty.INTERMEDIATE,
            availableFish = getFishByNames(
                listOf(
                    "Gardon", "Ablette", "Brème", "Rotengle", "Carassin",
                    "Perche", "Tanche", "Brochet", "Corégone", "Lotte",
                    "Omble chevalier", "Omble Kuori", "Ombre", "Muksun" // ✅ AJOUTÉS
                )
            ),
            description = "Lac paisible avec des fosses profondes, idéal pour la pêche au feeder.",
            unlockLevel = 16,
            coordinates = mapOf(
                "108:120" to "Fosse de 16m, lottes et ombles.",
                "64:90" to "Proximité de l'île, perches actives.",
                "140:100" to "Fond rocheux pour les corégones.",
                "88:125" to "Pente douce, brèmes en soirée."
            )
        ),

        Lake(
            id = "bear_lake",
            name = "Le lac des ours",
            type = LakeType.LAKE,
            difficulty = Difficulty.ADVANCED,
            availableFish = getFishByNames(
                listOf(
                    "Perche", "Brochet", "Sandre", "Corégone", "Lotte",
                    "Omble chevalier", "Truite arc-en-ciel", "Truite de lac",
                    "Omble de l'Arctique", "Omble à points blancs", "Omble du Pacifique",
                    "Omble gris", "Ombre Arctique", "Nelma" // ✅ AJOUTÉS
                )
            ),
            description = "Lac sauvage dans la taïga. Grosses truites et ombles rares.",
            unlockLevel = 18,
            coordinates = mapOf(
                "45:88" to "Baie profonde, truites de lac.",
                "118:45" to "Tombant rocheux, gros ombles.",
                "80:120" to "Embouchure du ruisseau, sandres.",
                "25:34" to "Plage de galets, perches géantes."
            )
        ),

        Lake(
            id = "volkhov_river",
            name = "Rivière Volkhov",
            type = LakeType.RIVER,
            difficulty = Difficulty.ADVANCED,
            availableFish = getFishByNames(
                listOf(
                    "Gardon", "Brème", "Chevesne", "Sandre", "Brochet",
                    "Silure", "Saumon atlantique", "Anguille", "Sterlet",
                    "Gardon de Sibérie", "Shemaya de la mer Noire", "Poisson-chat de l'Amour" // ✅ AJOUTÉS
                )
            ),
            description = "Rivière historique russe. Célèbre pour ses saumons atlantiques.",
            unlockLevel = 20,
            coordinates = mapOf(
                "71:100" to "La fosse près du fort, silures.",
                "80:60" to "Piliers du pont, sandres au crépuscule.",
                "118:108" to "Berge abrupte, passage des saumons.",
                "90:130" to "Zone d'herbiers, brochets à l'affût."
            )
        ),

        Lake(
            id = "severski_donets",
            name = "Severski Donets",
            type = LakeType.RIVER,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Gardon", "Brème", "Ablette", "Sandre", "Brochet",
                    "Carpe commune", "Nase (ou hotu)", "Aspe", "Silure",
                    "Esturgeon", "Goujon de l'Amour", "Meunier rouge" // ✅ AJOUTÉS
                )
            ),
            description = "Fleuve européen majeur. Esturgeons géants et silures.",
            unlockLevel = 22,
            coordinates = mapOf(
                "116:34" to "Haut-fond, idéal pour l'aspe.",
                "44:60" to "Fosse à esturgeons (9m).",
                "80:100" to "Méandre, repaire des sandres.",
                "130:58" to "Bordure d'herbiers, brochets."
            )
        ),

        Lake(
            id = "sura_river",
            name = "Rivière Sura",
            type = LakeType.RIVER,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Ablette", "Goujon", "Vairon", "Chevesne", "Ide mélanote",
                    "Perche", "Brochet", "Barbeau", "Sandre", "Silure",
                    "Esturgeon", "Sterlet", "Béluga", "Ombre de la Sibérie orientale",
                    "Omul du Baïkal", "Ménomini rond" // ✅ AJOUTÉS
                )
            ),
            description = "Rivière très exigeante avec un fort courant, réputée pour ses esturgeons.",
            unlockLevel = 24,
            coordinates = mapOf(
                "45:68" to "Fosse de 7.5m, sterlets et esturgeons.",
                "80:30" to "Zone de courant fort, barbeaux.",
                "101:54" to "Virage profond, silures.",
                "55:43" to "Berge sableuse, sandres."
            )
        ),

        Lake(
            id = "amber_lake",
            name = "Lac d'Ambre",
            type = LakeType.LAKE,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Carpe commune", "Carpe miroir", "Carpe fantôme",
                    "Amour Blanc", "Carpe à grosse tête", "Carassin", "Tanche",
                    "Perche Soleil" // ✅ AJOUTÉ
                )
            ),
            description = "Lac mystique aux eaux ambrées, spécialisé dans les grosses carpes.",
            unlockLevel = 26,
            coordinates = mapOf(
                "110:100" to "Maison du pêcheur, spot à carpes.",
                "138:138" to "L'île aux carpes.",
                "88:148" to "Le 'jardin' submergé.",
                "40:120" to "Fosse profonde, carpes fantômes."
            )
        ),

        Lake(
            id = "ladoga_lake",
            name = "Lac Ladoga",
            type = LakeType.LAKE,
            difficulty = Difficulty.ADVANCED,
            availableFish = getFishByNames(
                listOf(
                    "Gardon", "Ablette", "Brème", "Perche", "Brochet",
                    "Sandre", "Anguille", "Truite arc-en-ciel", "Silure",
                    "Truite de lac", "Saumon atlantique", "Omble chevalier",
                    "Omble Drjagini", "Omble Levanidov", "Omble neiva", "Omble rouge" // ✅ AJOUTÉS
                )
            ),
            description = "Grand lac avec une belle diversité. Techniques variées requises.",
            unlockLevel = 26,
            coordinates = mapOf(
                "58:38" to "Archipel, saumons et truites.",
                "80:70" to "Fosse de 20m, ombles chevaliers.",
                "34:80" to "Baie rocheuse, sandres.",
                "60:90" to "Haut-fond, perches."
            )
        ),

        Lake(
            id = "akhtuba_river",
            name = "Rivière Akhtouba",
            type = LakeType.RIVER,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Brème", "Carpe commune", "Chevesne", "Sandre",
                    "Silure", "Esturgeon", "Béluga", "Aspe"
                )
            ),
            description = "Affluent de la Volga, une zone experte pour les plus gros spécimens.",
            unlockLevel = 28,
            coordinates = mapOf(
                "108:100" to "Le 'vieux lit', esturgeons.",
                "71:148" to "Fosse à silures (13m).",
                "40:110" to "Plage de sable, sandres.",
                "138:70" to "Zone de courant, bélugas."
            )
        ),

        Lake(
            id = "norwegian_sea",
            name = "Mer de Norvège",
            type = LakeType.SEA,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Hareng", "Plie", "Flet", "Morue", "Merlan", "Lieu noir",
                    "Turbot", "Cabillaud", "Maquereau", "Flétan", "Requin épineux",
                    "Lompe", "Loquette d'Europe", "Loup de l'Atlantique", "Loup gélatineux",
                    "Loup tacheté", "Lycode à oreille", "Lycode d'Esmark", "Merlan bleu",
                    "Merlu européen", "Petit sébaste", "Petite épinoche méridionale",
                    "Pétoncle d'Islande", "Poisson-football de l'Atlantique",
                    "Raie boréale", "Raie radiée", "Requin du Groenland", "Requin pèlerin",
                    "Requin-Lézard", "Moule", "Moule zébrée" // ✅ TOUS LES POISSONS MARINS AJOUTÉS
                )
            ),
            description = "Pêche en mer norvégienne. Espèces uniques et conditions difficiles.",
            unlockLevel = 30,
            coordinates = mapOf(
                "45:80" to "Plateau continental, morues et flétans.",
                "88:120" to "Eaux profondes, requins et raies.",
                "25:45" to "Zone côtière, harengs et maquereaux.",
                "110:65" to "Tombant abyssal, espèces rares."
            )
        )
    )

    // ==========================================
    // FONCTIONS UTILITAIRES CORRIGÉES
    // ==========================================

    /**
     * Retourne le nombre total de poissons dans la base de données
     */
    fun getTotalFishCount(): Int = getAllFish().size

    /**
     * Retourne les statistiques par rareté
     */
    fun getFishStatsByRarity(): Map<FishRarity, Int> {
        return getAllFish().groupBy { it.rarity }.mapValues { it.value.size }
    }

    /**
     * Retourne les poissons par lac
     */
    fun getFishByLake(lakeId: String): List<Fish> {
        return lakes.find { it.id == lakeId }?.availableFish ?: emptyList()
    }

    /**
     * Retourne les meilleurs spots pour un poisson donné
     */
    fun getBestSpotsForFish(fishName: String): List<Lake> {
        return lakes.filter { lake ->
            lake.availableFish.any { it.name.equals(fishName, ignoreCase = true) }
        }
    }

    /**
     * Retourne les poissons actifs à une heure donnée
     */
    fun getActiveFishAtHour(hour: Int): List<Fish> {
        return getAllFish().filter { fish ->
            fish.bestHours.contains(hour)
        }
    }

    /**
     * Retourne les poissons adaptés aux conditions météo
     */
    fun getFishByWeather(weather: WeatherType): List<Fish> {
        return getAllFish().filter { fish ->
            fish.bestWeather.contains(weather) || fish.bestWeather.contains(WeatherType.ANY)
        }
    }

    /**
     * Recommande les meilleurs appâts pour un poisson donné
     */
    fun recommendBaitsForFish(fishName: String): List<String> {
        return getAllFish().find { it.name.equals(fishName, ignoreCase = true) }
            ?.preferredBait ?: emptyList()
    }

    /**
     * Trouve les poissons compatibles avec un appât donné
     */
    fun getFishForBait(baitName: String): List<Fish> {
        return getAllFish().filter { fish ->
            fish.preferredBait.any { it.equals(baitName, ignoreCase = true) }
        }
    }

    /**
     * Calcule la difficulté de capture d'un poisson
     */
    fun getFishDifficulty(fish: Fish): Int {
        return when (fish.rarity) {
            FishRarity.COMMON -> 1
            FishRarity.UNCOMMON -> 2
            FishRarity.RARE -> 3
            FishRarity.EPIC -> 4
            FishRarity.LEGENDARY -> 5
        }
    }

    /**
     * Retourne les meilleurs créneaux horaires pour un lac
     */
    fun getBestHoursForLake(lakeId: String): List<Int> {
        val lakeFish = getFishByLake(lakeId)
        return lakeFish.flatMap { it.bestHours }
            .groupBy { it }
            .toList()
            .sortedByDescending { it.second.size }
            .take(6)
            .map { it.first }
    }

    /**
     * Génère des conseils de pêche pour un lac et des conditions données
     */
    fun getFishingTips(lakeId: String, weather: WeatherType, hour: Int): String {
        val lake = lakes.find { it.id == lakeId } ?: return "Lac non trouvé"
        val activeFish = lake.availableFish.filter { fish ->
            fish.bestHours.contains(hour) &&
                    (fish.bestWeather.contains(weather) || fish.bestWeather.contains(WeatherType.ANY))
        }

        if (activeFish.isEmpty()) {
            return "Conditions peu favorables pour ce lac à cette heure."
        }

        val commonBaits = activeFish.flatMap { it.preferredBait }
            .groupBy { it }
            .toList()
            .sortedByDescending { it.second.size }
            .take(3)
            .map { it.first }

        return "Poissons actifs : ${activeFish.joinToString(", ") { it.name }}. " +
                "Appâts recommandés : ${commonBaits.joinToString(", ")}."
    }

    /**
     * Calcule le score d'expérience pour une capture
     */
    fun calculateExperiencePoints(fish: Fish, weight: Double): Int {
        val basePoints = when (fish.rarity) {
            FishRarity.COMMON -> 10
            FishRarity.UNCOMMON -> 25
            FishRarity.RARE -> 50
            FishRarity.EPIC -> 100
            FishRarity.LEGENDARY -> 200
        }

        // ✅ CORRECTION : Gestion de la valeur nullable du poids
        val weightRange = fish.weight
        if (weightRange != null) {
            val weightMultiplier = (weight / weightRange.start).coerceAtLeast(1.0)
            return (basePoints * weightMultiplier).toInt()
        }
        return basePoints
    }

    /**
     * Vérifie si c'est un record personnel
     */
    fun isPersonalRecord(fish: Fish, weight: Double, previousRecords: Map<String, Double>): Boolean {
        val previousRecord = previousRecords[fish.name] ?: 0.0
        return weight > previousRecord
    }

    /**
     * Génère un rapport de session de pêche
     */
    fun generateSessionReport(catches: List<Pair<Fish, Double>>): String {
        if (catches.isEmpty()) return "Aucune capture cette session."

        val totalCatches = catches.size
        val totalWeight = catches.sumOf { it.second }
        val avgWeight = totalWeight / totalCatches
        val biggestCatch = catches.maxByOrNull { it.second }
        val speciesCount = catches.distinctBy { it.first.name }.size
        val totalExp = catches.sumOf { calculateExperiencePoints(it.first, it.second) }

        return """
            📊 RAPPORT DE SESSION 📊
            
            🎣 Captures totales : $totalCatches poissons
            ⚖️ Poids total : ${String.format("%.2f", totalWeight)} kg
            📏 Poids moyen : ${String.format("%.2f", avgWeight)} kg
            🏆 Plus grosse prise : ${biggestCatch?.first?.name} (${String.format("%.2f", biggestCatch?.second ?: 0.0)} kg)
            🐟 Espèces différentes : $speciesCount
            ⭐ Points d'expérience : $totalExp XP
            
            ${if (biggestCatch != null) {
            val weightRange = biggestCatch.first.weight
            if (weightRange != null && biggestCatch.second >= weightRange.endInclusive * 0.8) {
                "🎉 Excellent ! Vous avez capturé un spécimen de belle taille !"
            } else {
                "💪 Continuez comme ça, les gros poissons vous attendent !"
            }
        } else {
            "💪 Continuez comme ça, les gros poissons vous attendent !"
        }}
        """.trimIndent()
    }

    /**
     * Retourne la liste des poissons manquants dans un lac par rapport à la base complète
     */
    fun getMissingFishInLake(lakeId: String): List<Fish> {
        val lake = lakes.find { it.id == lakeId } ?: return emptyList()
        val currentFish = lake.availableFish.map { it.name }.toSet()
        return getAllFish().filter { it.name !in currentFish }
    }

    /**
     * Statistiques détaillées de la base de données
     */
    fun getDatabaseStats(): Map<String, Any> {
        val allFish = getAllFish()
        return mapOf(
            "total_fish" to allFish.size,
            "common_fish" to allFish.count { it.rarity == FishRarity.COMMON },
            "uncommon_fish" to allFish.count { it.rarity == FishRarity.UNCOMMON },
            "rare_fish" to allFish.count { it.rarity == FishRarity.RARE },
            "epic_fish" to allFish.count { it.rarity == FishRarity.EPIC },
            "legendary_fish" to allFish.count { it.rarity == FishRarity.LEGENDARY },
            "total_lakes" to lakes.size,
            "beginner_lakes" to lakes.count { it.difficulty == Difficulty.BEGINNER },
            "intermediate_lakes" to lakes.count { it.difficulty == Difficulty.INTERMEDIATE },
            "advanced_lakes" to lakes.count { it.difficulty == Difficulty.ADVANCED },
            "expert_lakes" to lakes.count { it.difficulty == Difficulty.EXPERT }
        )
    }
}