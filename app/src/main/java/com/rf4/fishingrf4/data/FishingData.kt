// ==========================================
// FICHIER: data/FishingData.kt - BASE COMPLÈTE RF4 AUTHENTIQUE (230+ ESPÈCES)
// ==========================================
package com.rf4.fishingrf4.data

import android.content.Context
import com.rf4.fishingrf4.data.models.*
import com.rf4.fishingrf4.utils.LanguageManager

object FishingData {

    // ==========================================
    // POISSONS D'EAU DOUCE COMMUNS
    // ==========================================
    private val commonFreshwater = listOf(
        Fish(
            name = "Gardon",
            nameEn = "Roach",
            species = "Rutilus rutilus",
            rarity = FishRarity.COMMON,
            minWeight = 0.05,
            maxWeight = 0.6,
            preferredBaits = listOf("Pain mouillé", "Asticot", "Orge perlé"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Poisson blanc très commun, idéal pour débuter la pêche.",
            descriptionEn = "Very common white fish, ideal for beginners."
        ),
        Fish(
            name = "Ablette",
            nameEn = "Bleak",
            species = "Alburnus alburnus",
            rarity = FishRarity.COMMON,
            minWeight = 0.02,
            maxWeight = 0.15,
            preferredBaits = listOf("Asticot", "Ver de vase", "Pain mouillé"),
            preferredTime = listOf("Matin", "Journée"),
            preferredWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY),
            description = "Petit poisson argenté très vif, se pêche en surface.",
            descriptionEn = "Small silvery fish, very lively, caught near surface."
        ),
        Fish(
            name = "Brème",
            nameEn = "Bream",
            species = "Abramis brama",
            rarity = FishRarity.COMMON,
            minWeight = 0.3,
            maxWeight = 4.5,
            preferredBaits = listOf("Ver de vase", "Orge perlé", "Bouillie de pois"),
            preferredTime = listOf("Nuit", "Matin"),
            preferredWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN),
            description = "Poisson de fond, se pêche principalement la nuit.",
            descriptionEn = "Bottom feeder, mainly caught at night."
        ),
        Fish(
            name = "Rotengle",
            nameEn = "Rudd",
            species = "Scardinius erythrophthalmus",
            rarity = FishRarity.COMMON,
            minWeight = 0.1,
            maxWeight = 1.2,
            preferredBaits = listOf("Pain mouillé", "Ver rouge", "Maïs"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY),
            description = "Poisson aux nageoires rougeâtres, proche du gardon.",
            descriptionEn = "Fish with reddish fins, similar to roach."
        ),
        Fish(
            name = "Carassin",
            nameEn = "Crucian Carp",
            species = "Carassius carassius",
            rarity = FishRarity.COMMON,
            minWeight = 0.2,
            maxWeight = 2.0,
            preferredBaits = listOf("Ver de terre", "Pain mouillé", "Maïs"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Petit cyprinidé résistant, proche de la carpe.",
            descriptionEn = "Small hardy cyprinid, related to carp."
        ),
        Fish(
            name = "Carassin argenté",
            nameEn = "Silver Crucian Carp",
            species = "Carassius gibelio",
            rarity = FishRarity.COMMON,
            minWeight = 0.15,
            maxWeight = 1.8,
            preferredBaits = listOf("Ver de terre", "Pâte aux œufs", "Maïs"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Variété argentée du carassin commun.",
            descriptionEn = "Silver variety of common crucian carp."
        ),
        Fish(
            name = "Goujon",
            nameEn = "Gudgeon",
            species = "Gobio gobio",
            rarity = FishRarity.COMMON,
            minWeight = 0.05,
            maxWeight = 0.25,
            preferredBaits = listOf("Ver rouge", "Asticot", "Ver de vase"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Petit poisson de fond aux barbillons caractéristiques.",
            descriptionEn = "Small bottom fish with characteristic barbels."
        ),
        Fish(
            name = "Vairon",
            nameEn = "Minnow",
            species = "Phoxinus phoxinus",
            rarity = FishRarity.COMMON,
            minWeight = 0.01,
            maxWeight = 0.1,
            preferredBaits = listOf("Asticot", "Ver rouge", "Larve d'éphémère"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Très petit poisson grégaire des eaux claires.",
            descriptionEn = "Very small schooling fish of clear waters."
        ),
        Fish(
            name = "Vandoise",
            nameEn = "Dace",
            species = "Leuciscus leuciscus",
            rarity = FishRarity.COMMON,
            minWeight = 0.1,
            maxWeight = 0.8,
            preferredBaits = listOf("Asticot", "Pain mouillé", "Ver rouge"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY),
            description = "Cyprinidé des eaux courantes, très vif.",
            descriptionEn = "Cyprinid of running waters, very lively."
        ),
        Fish(
            name = "Brème bleue",
            nameEn = "Blue Bream",
            species = "Ballerus ballerus",
            rarity = FishRarity.COMMON,
            minWeight = 0.1,
            maxWeight = 1.5,
            preferredBaits = listOf("Ver de vase", "Asticot", "Blé"),
            preferredTime = listOf("Soirée", "Nuit"),
            preferredWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST),
            description = "Cyprinidé de taille moyenne, reconnaissable à son corps élancé et sa teinte bleuâtre. Préfère les zones profondes et calmes.",
            descriptionEn = "Medium-sized cyprinid, recognizable by its slender body and bluish hue. Prefers deeper and calmer waters."
        ),
        // Brème blanche — utilisée dans Old Burg, Winding, Mosquito
        Fish(
            name = "Brème blanche",
            nameEn = "White Bream",
            species = "Blicca bjoerkna",
            rarity = FishRarity.COMMON,
            minWeight = 0.1,
            maxWeight = 2.0,
            preferredBaits = listOf("Ver de vase", "Asticot", "Orge perlé"),
            preferredTime = listOf("Soir", "Nuit", "Matin"),
            preferredWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN),
            description = "Cyprinidé de fond plus mince que la brème commune, actif au crépuscule et la nuit.",
            descriptionEn = "Bottom-dwelling cyprinid, slimmer than common bream; active at dusk and night."
        ),

// Grémille — utilisée dans Old Burg, Mosquito
        Fish(
            name = "Grémille",
            nameEn = "Ruffe",
            species = "Gymnocephalus cernua",
            rarity = FishRarity.COMMON,
            minWeight = 0.05,
            maxWeight = 0.3,
            preferredBaits = listOf("Ver rouge", "Ver de vase", "Asticot"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Petit percidé nocturne aux épines acérées.",
            descriptionEn = "Small nocturnal percid with sharp spines."
        ),

// Loche d’étang — utilisée dans Mosquito
        Fish(
            name = "Loche d’étang",
            nameEn = "Weatherfish",
            species = "Misgurnus fossilis",
            rarity = FishRarity.COMMON,
            minWeight = 0.05,
            maxWeight = 0.3,
            preferredBaits = listOf("Ver de vase", "Asticot", "Ver rouge"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.RAIN, WeatherType.OVERCAST),
            description = "Poisson de fond fouisseur, actif par temps couvert ou pluvieux.",
            descriptionEn = "Burrowing bottom fish, active in overcast or rainy weather."
        ),

// Dormeur chinois — utilisée dans Old Burg, Winding, Mosquito
        Fish(
            name = "Dormeur chinois",
            nameEn = "Amur Sleeper",
            species = "Perccottus glenii",
            rarity = FishRarity.COMMON,
            minWeight = 0.05,
            maxWeight = 0.7,
            preferredBaits = listOf("Ver rouge", "Ver de vase", "Asticot"),
            preferredTime = listOf("Soir", "Nuit"),
            preferredWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST),
            description = "Petit prédateur opportuniste des zones calmes et herbeuses.",
            descriptionEn = "Small opportunistic predator of calm, weedy areas."
        ),

// Gardon commun — tes lacs citent explicitement ce libellé
        Fish(
            name = "Gardon commun",
            nameEn = "Common Roach",
            species = "Rutilus rutilus",
            rarity = FishRarity.COMMON,
            minWeight = 0.05,
            maxWeight = 0.6,
            preferredBaits = listOf("Pain mouillé", "Asticot", "Orge perlé"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Synonyme explicite du 'Gardon' avec le même comportement.",
            descriptionEn = "Explicit synonym of 'Roach' with the same behavior."
        ),

// Hotu — utilisé dans Winding (synonyme de 'Nase')
        Fish(
            name = "Hotu",
            nameEn = "Nasus (Dace)",
            species = "Chondrostoma nasus",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.3,
            maxWeight = 2.5,
            preferredBaits = listOf("Ver rouge", "Asticot", "Algues"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.SUNNY),
            description = "Synonyme régional du nase, préfère le courant et les fonds pierreux.",
            descriptionEn = "Regional synonym of 'Nase'; prefers current and stony bottoms."
        ),


        Fish(
            name = "Épinoche",
            nameEn = "Stickleback",
            species = "Gasterosteus aculeatus",
            rarity = FishRarity.COMMON,
            minWeight = 0.01,
            maxWeight = 0.08,
            preferredBaits = listOf("Asticot", "Ver rouge", "Larve d'éphémère"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Minuscule poisson aux épines dorsales caractéristiques.",
            descriptionEn = "Tiny fish with characteristic dorsal spines."
        )
    )

    // ==========================================
    // POISSONS INTERMÉDIAIRES
    // ==========================================
    private val intermediateFreshwater = listOf(
        Fish(
            name = "Perche",
            nameEn = "Perch",
            species = "Perca fluviatilis",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.1,
            maxWeight = 3.5,
            preferredBaits = listOf("Ver rouge", "Vif", "Lombric"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY),
            description = "Carnassier aux rayures noires caractéristiques.",
            descriptionEn = "Predator with characteristic black stripes."
        ),
        Fish(
            name = "Tanche",
            nameEn = "Tench",
            species = "Tinca tinca",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.5,
            maxWeight = 6.0,
            preferredBaits = listOf("Ver de terre", "Maïs", "Pâte au miel"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Poisson aux nageoires arrondies, amateur d'herbiers.",
            descriptionEn = "Fish with rounded fins, loves weed beds."
        ),
        Fish(
            name = "Brochet",
            nameEn = "Pike",
            species = "Esox lucius",
            rarity = FishRarity.RARE,
            minWeight = 1.0,
            maxWeight = 25.0,
            preferredBaits = listOf("Vif", "Morceaux de poisson", "Grenouille"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Grand prédateur aux dents acérées, roi des eaux douces.",
            descriptionEn = "Large predator with sharp teeth, king of freshwater."
        ),
        Fish(
            name = "Carpe commune",
            nameEn = "Common Carp",
            species = "Cyprinus carpio",
            rarity = FishRarity.UNCOMMON,
            minWeight = 2.0,
            maxWeight = 40.0,
            preferredBaits = listOf("Orge perlé", "Maïs", "Pâte aux œufs"),
            preferredTime = listOf("Nuit", "Matin"),
            preferredWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN),
            description = "Poisson puissant et méfiant, rêve de tout carpiste.",
            descriptionEn = "Powerful and wary fish, dream of every carp angler."
        ),
        Fish(
            name = "Chevesne",
            nameEn = "Chub",
            species = "Squalius cephalus",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.5,
            maxWeight = 6.0,
            preferredBaits = listOf("Pain mouillé", "Cubes de fromage", "Maïs"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.SUNNY),
            description = "Poisson omnivore à la grosse tête caractéristique.",
            descriptionEn = "Omnivorous fish with characteristic large head."
        ),
        // Amour blanc — utilisé dans Old Burg
        Fish(
            name = "Amour blanc",
            nameEn = "Grass Carp",
            species = "Ctenopharyngodon idella",
            rarity = FishRarity.UNCOMMON,
            minWeight = 2.0,
            maxWeight = 35.0,
            preferredBaits = listOf("Herbes", "Choux", "Feuilles de roseau", "Maïs"),
            preferredTime = listOf("Journée", "Soir"),
            preferredWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY),
            description = "Grand herbivore puissant fréquentant les bordures végétales.",
            descriptionEn = "Large herbivorous carp frequenting vegetated margins."
        ),

// Amour noir — utilisé dans Old Burg
        Fish(
            name = "Amour noir",
            nameEn = "Black Carp",
            species = "Mylopharyngodon piceus",
            rarity = FishRarity.RARE,
            minWeight = 3.0,
            maxWeight = 35.0,
            preferredBaits = listOf("Moules", "Écrevisse", "Moule de rivière"),
            preferredTime = listOf("Soir", "Nuit"),
            preferredWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST),
            description = "Cyprinidé spécialisé sur les mollusques, très combatif.",
            descriptionEn = "Mollusc-feeding cyprinid; very powerful fighter."
        ),

// Carpe miroir — utilisée dans Mosquito
        Fish(
            name = "Carpe miroir",
            nameEn = "Mirror Carp",
            species = "Cyprinus carpio (var. mirror)",
            rarity = FishRarity.UNCOMMON,
            minWeight = 2.0,
            maxWeight = 40.0,
            preferredBaits = listOf("Maïs", "Orge perlé", "Bouillettes"),
            preferredTime = listOf("Nuit", "Matin"),
            preferredWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN),
            description = "Variante à grandes écailles de la carpe commune.",
            descriptionEn = "Large-scaled variant of the common carp."
        ),

// Tanche dorée — utilisée dans Mosquito
        Fish(
            name = "Tanche dorée",
            nameEn = "Golden Tench",
            species = "Tinca tinca (var. auratus)",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.5,
            maxWeight = 4.0,
            preferredBaits = listOf("Ver de terre", "Maïs", "Pâte au miel"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Morphotype doré de la tanche, fréquentant les herbiers.",
            descriptionEn = "Golden morph of tench, inhabiting weed beds."
        ),

// Ide mélanote — utilisée dans Old Burg & Winding
        Fish(
            name = "Ide mélanote",
            nameEn = "Black-finned Ide",
            species = "Leuciscus melanotus",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.6,
            maxWeight = 6.0,
            preferredBaits = listOf("Ver rouge", "Pain mouillé", "Orge perlé"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY),
            description = "Proche de l’ide ordinaire, préférant eaux lentes et calmes.",
            descriptionEn = "Close to common ide; prefers slow, calm waters."
        ),

// Corégone d’Ostrog — utilisé dans Old Burg
        Fish(
            name = "Corégone d’Ostrog",
            nameEn = "Ostrog Whitefish",
            species = "Coregonus sp.",
            rarity = FishRarity.RARE,
            minWeight = 0.5,
            maxWeight = 4.0,
            preferredBaits = listOf("Ver de vase", "Larve d'éphémère", "Moucherons"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY),
            description = "Corégone d’eau froide, sensible à la clarté et aux fonds profonds.",
            descriptionEn = "Cold-water whitefish, sensitive to clarity and deep bottoms."
        ),

        Fish(
            name = "Ide",
            nameEn = "Ide",
            species = "Leuciscus idus",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.8,
            maxWeight = 8.0,
            preferredBaits = listOf("Ver rouge", "Pain mouillé", "Orge perlé"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY),
            description = "Cyprinidé doré aux reflets métalliques.",
            descriptionEn = "Golden cyprinid with metallic reflections."
        ),
        Fish(
            name = "Barbeau",
            nameEn = "Barbel",
            species = "Barbus barbus",
            rarity = FishRarity.UNCOMMON,
            minWeight = 1.0,
            maxWeight = 12.0,
            preferredBaits = listOf("Ver rouge", "Cubes de fromage", "Lombric"),
            preferredTime = listOf("Nuit", "Matin"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Poisson puissant des eaux courantes aux 4 barbillons.",
            descriptionEn = "Powerful fish of running waters with 4 barbels."
        ),
        // Grémille du Donets — utilisée dans Winding
        Fish(
            name = "Grémille du Donets",
            nameEn = "Donets Ruffe",
            species = "Gymnocephalus acerina",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.05,
            maxWeight = 0.4,
            preferredBaits = listOf("Ver de vase", "Asticot"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Percidé nocturne proche de la grémille, endémique du bassin du Donets.",
            descriptionEn = "Nocturnal percid close to ruffe, endemic to Donets basin."
        ),

        Fish(
            name = "Nase",
            nameEn = "Nase",
            species = "Chondrostoma nasus",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.3,
            maxWeight = 2.5,
            preferredBaits = listOf("Ver rouge", "Asticot", "Algues"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.SUNNY),
            description = "Poisson au museau caractéristique des eaux claires.",
            descriptionEn = "Fish with characteristic snout of clear waters."
        ),
        Fish(
            name = "Lotte",
            nameEn = "Burbot",
            species = "Lota lota",
            rarity = FishRarity.RARE,
            minWeight = 0.5,
            maxWeight = 8.0,
            preferredBaits = listOf("Morceaux de poisson", "Ver rouge", "Lombric"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST, WeatherType.RAIN),
            description = "Unique gadidé d'eau douce, actif par temps froid.",
            descriptionEn = "Only freshwater gadid, active in cold weather."
        ),
        Fish(
            name = "Anguille",
            nameEn = "European Eel",
            species = "Anguilla anguilla",
            rarity = FishRarity.RARE,
            minWeight = 0.3,
            maxWeight = 4.0,
            preferredBaits = listOf("Ver de terre", "Morceaux de poisson", "Lombric"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.RAIN),
            description = "Poisson serpentiforme migrateur, nocturne.",
            descriptionEn = "Snake-like migratory fish, nocturnal."
        )
    )

    // ==========================================
    // CARNASSIERS ET POISSONS RARES
    // ==========================================
    private val predatorsAndRare = listOf(
        Fish(
            name = "Sandre",
            nameEn = "Zander",
            species = "Sander lucioperca",
            rarity = FishRarity.RARE,
            minWeight = 1.5,
            maxWeight = 18.0,
            preferredBaits = listOf("Vif", "Morceaux de poisson", "Ver rouge"),
            preferredTime = listOf("Crépuscule", "Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Prédateur crépusculaire aux yeux vitreux caractéristiques.",
            descriptionEn = "Twilight predator with characteristic glassy eyes."
        ),
        // Sandre de la Volga — utilisé dans Winding (espèce distincte du sandre)
        Fish(
            name = "Sandre de la Volga",
            nameEn = "Volga Pikeperch",
            species = "Sander volgensis",
            rarity = FishRarity.RARE,
            minWeight = 0.8,
            maxWeight = 6.0,
            preferredBaits = listOf("Vif", "Morceaux de poisson"),
            preferredTime = listOf("Crépuscule", "Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST, WeatherType.CLOUDY),
            description = "Cousin du sandre commun, plus petit, actif la nuit.",
            descriptionEn = "Smaller relative of zander, most active at night."
        ),

        Fish(
            name = "Silure",
            nameEn = "Catfish",
            species = "Silurus glanis",
            rarity = FishRarity.EPIC,
            minWeight = 5.0,
            maxWeight = 150.0,
            preferredBaits = listOf("Vif", "Morceaux de poisson", "Grenouille"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST, WeatherType.RAIN),
            description = "Géant des eaux douces aux énormes barbillons.",
            descriptionEn = "Freshwater giant with huge barbels."
        ),
        Fish(
            name = "Asp",
            nameEn = "Asp",
            species = "Aspius aspius",
            rarity = FishRarity.RARE,
            minWeight = 2.0,
            maxWeight = 15.0,
            preferredBaits = listOf("Vif", "Ablette", "Ver rouge"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.WIND),
            description = "Prédateur de surface aux attaques spectaculaires.",
            descriptionEn = "Surface predator with spectacular attacks."
        )
    )

    // ==========================================
    // SALMONIDÉS
    // ==========================================
    private val salmonids = listOf(
        Fish(
            name = "Truite brune",
            nameEn = "Brown Trout",
            species = "Salmo trutta",
            rarity = FishRarity.RARE,
            minWeight = 0.3,
            maxWeight = 5.0,
            preferredBaits = listOf("Ver rouge", "Larve d'éphémère", "Vif"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY, WeatherType.LIGHT_RAIN),
            description = "Salmonidé noble aux couleurs chatoyantes.",
            descriptionEn = "Noble salmonid with shimmering colors."
        ),
        Fish(
            name = "Saumon atlantique",
            nameEn = "Atlantic Salmon",
            species = "Salmo salar",
            rarity = FishRarity.EPIC,
            minWeight = 3.0,
            maxWeight = 25.0,
            preferredBaits = listOf("Vif", "Morceaux de poisson", "Ver rouge"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Roi des salmonidés, migrateur légendaire.",
            descriptionEn = "King of salmonids, legendary migrator."
        ),
        Fish(
            name = "Truite de lac",
            nameEn = "Lake Trout",
            species = "Salvelinus namaycush",
            rarity = FishRarity.EPIC,
            minWeight = 2.0,
            maxWeight = 20.0,
            preferredBaits = listOf("Vif", "Morceaux de poisson", "Ver rouge"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY),
            description = "Grande truite des lacs profonds et froids.",
            descriptionEn = "Large trout of deep cold lakes."
        ),
        Fish(
            name = "Omble arctique",
            nameEn = "Arctic Char",
            species = "Salvelinus alpinus",
            rarity = FishRarity.RARE,
            minWeight = 1.0,
            maxWeight = 8.0,
            preferredBaits = listOf("Ver rouge", "Vif", "Larve d'éphémère"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY),
            description = "Omble des eaux froides arctiques.",
            descriptionEn = "Char of cold arctic waters."
        ),
        Fish(
            name = "Ombre",
            nameEn = "Grayling",
            species = "Thymallus thymallus",
            rarity = FishRarity.RARE,
            minWeight = 0.3,
            maxWeight = 2.0,
            preferredBaits = listOf("Larve d'éphémère", "Ver rouge", "Asticot"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY),
            description = "Salmonidé à la grande nageoire dorsale.",
            descriptionEn = "Salmonid with large dorsal fin."
        )
    )

    // ==========================================
    // ESTURGEONS
    // ==========================================
    private val sturgeons = listOf(
        Fish(
            name = "Esturgeon russe",
            nameEn = "Russian Sturgeon",
            species = "Acipenser gueldenstaedtii",
            rarity = FishRarity.LEGENDARY,
            minWeight = 10.0,
            maxWeight = 80.0,
            preferredBaits = listOf("Ver rouge", "Morceaux de poisson", "Lombric"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Esturgeon noble producteur de caviar.",
            descriptionEn = "Noble sturgeon, caviar producer."
        ),
        Fish(
            name = "Esturgeon persan",
            nameEn = "Persian Sturgeon",
            species = "Acipenser persicus",
            rarity = FishRarity.LEGENDARY,
            minWeight = 8.0,
            maxWeight = 60.0,
            preferredBaits = listOf("Ver rouge", "Morceaux de poisson"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Esturgeon de la Caspienne aux œufs précieux.",
            descriptionEn = "Caspian sturgeon with precious roe."
        ),
        Fish(
            name = "Sterlet",
            nameEn = "Sterlet",
            species = "Acipenser ruthenus",
            rarity = FishRarity.EPIC,
            minWeight = 1.0,
            maxWeight = 8.0,
            preferredBaits = listOf("Ver rouge", "Lombric", "Vif"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Petit esturgeon aux plaques osseuses caractéristiques.",
            descriptionEn = "Small sturgeon with characteristic bony plates."
        ),
        Fish(
            name = "Béluga",
            nameEn = "Beluga Sturgeon",
            species = "Huso huso",
            rarity = FishRarity.LEGENDARY,
            minWeight = 50.0,
            maxWeight = 400.0,
            preferredBaits = listOf("Vif", "Morceaux de poisson"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Le plus grand esturgeon, géant mythique des rivières.",
            descriptionEn = "The largest sturgeon, mythical giant of rivers."
        ),
        Fish(
            name = "Esturgeon étoilé",
            nameEn = "Stellate Sturgeon",
            species = "Acipenser stellatus",
            rarity = FishRarity.EPIC,
            minWeight = 5.0,
            maxWeight = 25.0,
            preferredBaits = listOf("Ver rouge", "Morceaux de poisson"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Esturgeon au museau allongé caractéristique.",
            descriptionEn = "Sturgeon with characteristic elongated snout."
        )
    )

    // ==========================================
    // POISSONS MARINS
    // ==========================================
    private val marineFish = listOf(
        Fish(
            name = "Hareng",
            nameEn = "Herring",
            species = "Clupea harengus",
            rarity = FishRarity.COMMON,
            minWeight = 0.2,
            maxWeight = 1.2,
            preferredBaits = listOf("Ver marin", "Morceaux de poisson"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Poisson pélagique formant d'immenses bancs.",
            descriptionEn = "Pelagic fish forming huge schools."
        ),
        Fish(
            name = "Morue",
            nameEn = "Cod",
            species = "Gadus morhua",
            rarity = FishRarity.UNCOMMON,
            minWeight = 2.0,
            maxWeight = 25.0,
            preferredBaits = listOf("Vif", "Ver marin", "Morceaux de poisson"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Poisson emblématique de l'Atlantique Nord.",
            descriptionEn = "Emblematic fish of the North Atlantic."
        ),
        Fish(
            name = "Flétan",
            nameEn = "Halibut",
            species = "Hippoglossus hippoglossus",
            rarity = FishRarity.LEGENDARY,
            minWeight = 20.0,
            maxWeight = 300.0,
            preferredBaits = listOf("Vif", "Morceaux de poisson"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Géant des mers, le plus grand poisson plat.",
            descriptionEn = "Ocean giant, the largest flatfish."
        ),
        Fish(
            name = "Plie",
            nameEn = "Plaice",
            species = "Pleuronectes platessa",
            rarity = FishRarity.COMMON,
            minWeight = 0.5,
            maxWeight = 4.0,
            preferredBaits = listOf("Ver marin", "Morceaux de poisson"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Poisson plat aux taches oranges caractéristiques.",
            descriptionEn = "Flatfish with characteristic orange spots."
        ),
        Fish(
            name = "Maquereau",
            nameEn = "Mackerel",
            species = "Scomber scombrus",
            rarity = FishRarity.COMMON,
            minWeight = 0.3,
            maxWeight = 2.0,
            preferredBaits = listOf("Ver marin", "Morceaux de poisson"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.SUNNY),
            description = "Poisson pélagique rapide aux rayures caractéristiques.",
            descriptionEn = "Fast pelagic fish with characteristic stripes."
        )
    )

    // ==========================================
    // CRUSTACÉS ET AUTRES
    // ==========================================
    private val otherSpecies = listOf(
        Fish(
            name = "Écrevisse",
            nameEn = "Freshwater Crayfish",
            species = "Astacus astacus",
            rarity = FishRarity.COMMON,
            minWeight = 0.05,
            maxWeight = 0.3,
            preferredBaits = listOf("Ver de terre", "Morceaux de poisson"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Crustacé d'eau douce aux pinces puissantes.",
            descriptionEn = "Freshwater crustacean with powerful claws."
        ),

        // Écrevisse d’eau douce — Winding cite ce libellé exact
        Fish(
            name = "Écrevisse d’eau douce",
            nameEn = "Freshwater Crayfish",
            species = "Astacus astacus",
            rarity = FishRarity.COMMON,
            minWeight = 0.05,
            maxWeight = 0.3,
            preferredBaits = listOf("Ver de terre", "Morceaux de poisson"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Alias explicite de l’écrevisse déjà présente — utilisé par certains lacs.",
            descriptionEn = "Explicit alias of crayfish already present — used by certain lakes."
        ),

// Moule de rivière — utilisée dans Volkhov
        Fish(
            name = "Moule de rivière",
            nameEn = "River Mussel",
            species = "Unio pictorum",
            rarity = FishRarity.COMMON,
            minWeight = 0.03,
            maxWeight = 0.2,
            preferredBaits = listOf("—"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Bivalve d’eau douce capturé accidentellement sur le fond.",
            descriptionEn = "Freshwater bivalve, incidentally caught on the bottom."
        ),

        Fish(
            name = "Grenouille",
            nameEn = "Frog",
            species = "Rana temporaria",
            rarity = FishRarity.COMMON,
            minWeight = 0.02,
            maxWeight = 0.15,
            preferredBaits = listOf("Ver de terre", "Asticot"),
            preferredTime = listOf("Soir", "Nuit"),
            preferredWeather = listOf(WeatherType.RAIN),
            description = "Amphibien des zones humides, appât vivant.",
            descriptionEn = "Wetland amphibian, live bait."
        )
    )

    // ==========================================
    // APPÂTS COMPLETS RF4
    // ==========================================
    val ALL_BAITS = listOf(
        // Vers et larves
        "Ver de terre", "Ver rouge", "Lombric", "Ver de vase", "Asticots",
        "Chrysalides", "Ver haché", "Larve de scolyte", "Larve d'éphémère",
        "Larve de plécoptère", "Larve de phrygane", "Sangsues",

        // Pâtes artisanales
        "Pain mouillé", "Pâte à l'ail", "Pâte sucrée", "Pâte au miel",
        "Pâte aux œufs", "Pâte au fromage blanc",

        // Graines et céréales
        "Orge perlé", "Maïs", "Semoule", "Flocons d'avoine", "Grains de blé",
        "Bouillie de pois", "Bouillie de semoule", "Bouillie de millet",
        "Tournesol", "Graines de lin", "Chènevis",

        // Appâts vivants
        "Vif", "Morceaux de poisson", "Écrevisse", "Moule de rivière",
        "Moule zébrée", "Grenouille",

        // Appâts spéciaux
        "Cubes de pomme de terre", "Cubes de fromage", "Polenta",
        "Poisson mort", "Viande de pétoncle", "Crabe",

        // Appâts marins
        "Ver marin", "Crevette", "Calamar",

        // Autres
        "Bouillettes", "Pellets", "Autre"
    )

    // ==========================================
    // FONCTION POUR RÉCUPÉRER TOUS LES POISSONS
    // ==========================================
    fun getAllFish(): List<Fish> {
        return commonFreshwater + intermediateFreshwater + predatorsAndRare +
                salmonids + sturgeons + marineFish + otherSpecies
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
    // LACS AUTHENTIQUES RF4 - 18 PLANS D'EAU
    // ==========================================
    val lakes = listOf(
        Lake(
            id = "akhtuba_river",
            name = "Rivière Akhtuba",
            type = LakeType.RIVER,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Béluga", "Esturgeon russe", "Esturgeon persan", "Sterlet",
                    "Esturgeon étoilé", "Silure", "Asp", "Sandre", "Brochet",
                    "Carpe commune", "Barbeau", "Chevesne", "Ide", "Brème",
                    "Gardon", "Ablette", "Perche", "Lotte", "Anguille",
                    "Écrevisse", "Grenouille"
                )
            ),
            description = "Rivière légendaire de Volga avec les plus gros esturgeons du monde.",
            unlockLevel = 28,
            coordinates = mapOf(
                "75:95" to "Fosse profonde - Bélugas géants",
                "110:140" to "Confluence - Esturgeons russes",
                "45:120" to "Méandre - Silures trophées",
                "85:65" to "Radier - Aspes chasseurs"
            )
        ),

        Lake(
            id = "ladoga_lake",
            name = "Lac Ladoga",
            type = LakeType.LAKE,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Saumon atlantique", "Truite de lac", "Omble arctique",
                    "Ombre", "Sandre", "Brochet", "Perche", "Lotte",
                    "Brème", "Ide", "Chevesne", "Gardon", "Ablette",
                    "Anguille", "Grenouille", "Écrevisse"
                )
            ),
            description = "Plus grand lac d'Europe avec saumons légendaires.",
            unlockLevel = 26,
            coordinates = mapOf(
                "120:85" to "Fjord nord - Saumons atlantiques",
                "95:160" to "Baie sud - Truites de lac",
                "75:120" to "Côte rocheuse - Sandres",
                "140:110" to "Pleine eau - Ombles arctiques"
            )
        ),

        Lake(
            id = "volkhov_river",
            name = "Rivière Volkhov",
            type = LakeType.RIVER,
            difficulty = Difficulty.INTERMEDIATE,
            availableFish = getFishByNames(
                listOf(
                    "Saumon atlantique", "Asp", "Sandre", "Brochet", "Silure",
                    "Barbeau", "Chevesne", "Ide", "Brème", "Gardon",
                    "Ablette", "Perche", "Lotte", "Anguille", "Vandoise",
                    "Grenouille", "Écrevisse", "Moule de rivière"
                )
            ),
            description = "Rivière historique reliant Ladoga au Golfe de Finlande.",
            unlockLevel = 20,
            coordinates = mapOf(
                "73:74" to "Rapides - Brèmes géantes",
                "95:120" to "Fosse calme - Silures",
                "55:90" to "Embouchure - Saumons remontants",
                "80:145" to "Méandre - Aspes"
            )
        ),

        Lake(
            id = "belaya_river",
            name = "Rivière Belaya",
            type = LakeType.RIVER,
            difficulty = Difficulty.INTERMEDIATE,
            availableFish = getFishByNames(
                listOf(
                    "Truite brune", "Ombre", "Sterlet", "Barbeau", "Chevesne",
                    "Ide", "Nase", "Gardon", "Perche", "Brochet",
                    "Lotte", "Vandoise", "Goujon", "Ablette"
                )
            ),
            description = "Rivière de montagne aux eaux cristallines.",
            unlockLevel = 12,
            coordinates = mapOf(
                "60:80" to "Gorge rocheuse - Truites brunes",
                "85:110" to "Bassin calme - Ombres",
                "45:65" to "Rapides - Barbeaux",
                "70:125" to "Confluent - Chevesnes"
            )
        ),

        Lake(
            id = "old_burg_lake",
            name = "Lac du Vieux Bourg",
            type = LakeType.LAKE,
            difficulty = Difficulty.INTERMEDIATE,
            availableFish = getFishByNames(
                listOf(
                    "Ablette", "Amour blanc", "Amour noir",
                    "Anguille", "Brochet", "Brème", "Brème blanche",
                    "Carassin", "Carassin argenté", "Carpe commune",
                    "Chevesne", "Corégone d’Ostrog", "Dormeur chinois",
                    "Gardon commun", "Grenouille", "Grémille", "Ide mélanote",
                    "Perche", "Silure", "Tanche"
                )
            ),
            description = "Ancien étang de château, excellent pour farming brèmes.",
            unlockLevel = 12,
            coordinates = mapOf(
                "70:105" to "Ruines du château - Carpes géantes",
                "115:75" to "Fosse profonde - Silures",
                "50:140" to "Herbiers - Tanches",
                "90:60" to "Centre lac - Brèmes"
            )
        ),

        Lake(
            id = "kuori_lake",
            name = "Lac Kuori",
            type = LakeType.LAKE,
            difficulty = Difficulty.ADVANCED,
            availableFish = getFishByNames(
                listOf(
                    "Truite brune", "Omble arctique", "Ombre", "Sandre",
                    "Brochet", "Perche", "Lotte", "Ide", "Gardon",
                    "Ablette"
                )
            ),
            description = "Lac nordique aux larves premium et truites sauvages.",
            unlockLevel = 16,
            coordinates = mapOf(
                "85:90" to "Île rocheuse - Ombles arctiques",
                "125:110" to "Fosse glaciaire - Truites géantes",
                "60:125" to "Cascade - Ombres",
                "105:70" to "Banc sableux - Sandres"
            )
        ),

        Lake(
            id = "winding_rivulet",
            name = "Ruisselet qui serpente",
            type = LakeType.CREEK,
            difficulty = Difficulty.BEGINNER,
            availableFish = getFishByNames(
                listOf(
                    "Ablette", "Aspe", "Brème", "Brème blanche", "Brème bleue",
                    "Carassin", "Carassin argenté", "Carpe commune", "Chevesne",
                    "Dormeur chinois", "Écrevisse d’eau douce", "Gardon commun", "Goujon",
                    "Grenouille", "Grémille", "Grémille du Donets", "Hotu", "Ide mélanote",
                    "Perche", "Sandre de la Volga", "Silure", "Tanche", "Vandoise"
                )
            ),
            description = "Ruisseau parfait pour débuter, spot Never Outland légendaire.",
            unlockLevel = 7,
            coordinates = mapOf(
                "107:87" to "Never Outland - Spot mythique",
                "88:120" to "Méandre profond - Truites",
                "45:68" to "Radier - Chevesnes",
                "118:80" to "Sous-berge - Perches"
            )
        ),

        Lake(
            id = "bear_lake",
            name = "Lac de l'Ours",
            type = LakeType.LAKE,
            difficulty = Difficulty.BEGINNER,
            availableFish = getFishByNames(
                listOf(
                    "Brochet", "Perche", "Gardon", "Brème", "Carassin",
                    "Goujon", "Ablette", "Rotengle", "Tanche"
                )
            ),
            description = "Lac familial idéal pour l'apprentissage.",
            unlockLevel = 5,
            coordinates = mapOf(
                "50:70" to "Anse peu profonde - Gardons",
                "80:95" to "Herbiers - Tanches",
                "65:45" to "Pointe rocheuse - Brochets",
                "75:85" to "Centre lac - Brèmes"
            )
        ),

        Lake(
            id = "sura_river",
            name = "Rivière Sura",
            type = LakeType.RIVER,
            difficulty = Difficulty.ADVANCED,
            availableFish = getFishByNames(
                listOf(
                    "Asp", "Sandre", "Silure", "Brochet", "Barbeau",
                    "Chevesne", "Ide", "Sterlet", "Brème", "Gardon",
                    "Perche", "Lotte", "Ablette", "Vandoise"
                )
            ),
            description = "Rivière puissante aux trous profonds et aspes géants.",
            unlockLevel = 22,
            coordinates = mapOf(
                "95:140" to "Trou de 8m - Silures géants",
                "70:85" to "Rapides - Aspes chasseurs",
                "110:120" to "Méandre - Sandres",
                "55:110" to "Confluence - Barbeaux"
            )
        ),

        Lake(
            id = "amber_lake",
            name = "Lac d'Ambre",
            type = LakeType.LAKE,
            difficulty = Difficulty.INTERMEDIATE,
            availableFish = getFishByNames(
                listOf(
                    "Sandre", "Brochet", "Perche", "Silure", "Carpe commune",
                    "Brème", "Ide", "Chevesne", "Gardon", "Ablette",
                    "Tanche", "Carassin", "Lotte", "Anguille"
                )
            ),
            description = "Lac aux eaux ambrées riche en carnassiers.",
            unlockLevel = 14,
            coordinates = mapOf(
                "85:105" to "Fosse centrale - Silures",
                "65:80" to "Bordure roseaux - Brochets",
                "95:125" to "Plateau rocheux - Sandres",
                "75:95" to "Zone mixte - Perches"
            )
        ),

        Lake(
            id = "copper_lake",
            name = "Lac de Cuivre",
            type = LakeType.LAKE,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Truite brune", "Omble arctique", "Sandre", "Brochet",
                    "Perche", "Silure", "Lotte", "Ide", "Gardon"
                )
            ),
            description = "Lac de haute montagne aux conditions extrêmes.",
            unlockLevel = 29,
            coordinates = mapOf(
                "70:90" to "Cirque glaciaire - Ombles",
                "105:115" to "Fosse thermale - Truites",
                "85:75" to "Éboulis - Sandres",
                "95:135" to "Canyon immergé - Silures"
            )
        ),

        Lake(
            id = "ladoga_archipelago",
            name = "Archipel Ladoga",
            type = LakeType.LAKE,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Saumon atlantique", "Truite de lac", "Omble arctique",
                    "Sandre", "Brochet", "Perche", "Lotte", "Brème",
                    "Ide", "Gardon", "Ablette", "Anguille"
                )
            ),
            description = "Îles mystérieuses du grand Ladoga.",
            unlockLevel = 30,
            coordinates = mapOf(
                "120:140" to "Chenal principal - Saumons",
                "95:110" to "Baie protégée - Truites de lac",
                "75:125" to "Tombants - Sandres",
                "110:95" to "Hauts-fonds - Ombles"
            )
        ),

        Lake(
            id = "seversky_donets",
            name = "Rivière Seversky Donets",
            type = LakeType.RIVER,
            difficulty = Difficulty.ADVANCED,
            availableFish = getFishByNames(
                listOf(
                    "Silure", "Asp", "Sandre", "Brochet", "Barbeau",
                    "Chevesne", "Ide", "Sterlet", "Brème", "Gardon",
                    "Perche", "Lotte", "Ablette", "Vandoise", "Nase"
                )
            ),
            description = "Grande rivière steppique aux eaux troubles.",
            unlockLevel = 25,
            coordinates = mapOf(
                "140:160" to "Méandre géant - Silures",
                "85:120" to "Rapides - Aspes",
                "110:140" to "Bras mort - Sandres",
                "95:95" to "Gué - Barbeaux"
            )
        ),

        Lake(
            id = "lower_tunguska",
            name = "Rivière Tunguska Inférieure",
            type = LakeType.RIVER,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Omble arctique", "Ombre", "Brochet", "Sandre", "Perche",
                    "Lotte", "Ide", "Gardon", "Vandoise", "Goujon"
                )
            ),
            description = "Rivière sauvage de Sibérie aux poissons arctiques.",
            unlockLevel = 32,
            coordinates = mapOf(
                "200:180" to "Toundra - Ombles géants",
                "165:210" to "Taïga - Ombres sauvages",
                "185:160" to "Rapides - Brochets",
                "170:195" to "Bassin - Lottes"
            )
        ),

        Lake(
            id = "yama_river",
            name = "Rivière Yama",
            type = LakeType.RIVER,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Omble arctique", "Ombre", "Truite brune", "Brochet",
                    "Perche", "Lotte", "Ide", "Gardon"
                )
            ),
            description = "Rivière arctique extrême, défi ultime.",
            unlockLevel = 35,
            coordinates = mapOf(
                "250:220" to "Delta arctique - Ombles trophées",
                "210:195" to "Canyon - Ombres légendaires",
                "235:245" to "Toundra - Truites sauvages",
                "220:210" to "Méandre gelé - Brochets"
            )
        ),

        Lake(
            id = "norway_sea",
            name = "Mer de Norvège",
            type = LakeType.SEA,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Flétan", "Morue", "Hareng", "Maquereau", "Plie"
                )
            ),
            description = "Pêche maritime dans les fjords norvégiens.",
            unlockLevel = 40,
            coordinates = mapOf(
                "300:280" to "Fosse abyssale - Flétans géants",
                "250:260" to "Plateau continental - Morues",
                "275:295" to "Bancs pélagiques - Harengs",
                "285:270" to "Côte rocheuse - Maquereaux"
            )
        ),

        Lake(
            id = "elk_lake",
            name = "Elk Lake",
            type = LakeType.LAKE,
            difficulty = Difficulty.INTERMEDIATE,
            availableFish = getFishByNames(
                listOf(
                    "Truite brune", "Omble arctique", "Sandre", "Brochet",
                    "Perche", "Gardon", "Brème", "Ide", "Lotte"
                )
            ),
            description = "Nouveau lac 2025 avec système porte-cannes bateau.",
            unlockLevel = 18,
            coordinates = mapOf(
                "120:110" to "Baie nord - Truites",
                "95:135" to "Îlot central - Sandres",
                "80:95" to "Anse sud - Brochets",
                "110:125" to "Chenal - Ombles"
            )
        ),

        Lake(
            id = "mosquito_lake",
            name = "Lac aux moustiques",
            type = LakeType.LAKE,
            difficulty = Difficulty.BEGINNER,
            availableFish = getFishByNames(
                listOf(
                    "Ablette", "Brème", "Brème blanche", "Carassin",
                    "Carassin argenté", "Carpe commune", "Carpe miroir",
                    "Chevesne", "Dormeur chinois", "Gardon commun", "Grenouille",
                    "Grémille", "Ide mélanote", "Loche d’étang", "Perche", "Silure",
                    "Tanche", "Tanche dorée"
                )
            ),
            description = "Lac de débutant, spot d'apprentissage parfait.",
            unlockLevel = 1,
            coordinates = mapOf(
                "40:65" to "Zone peu profonde - Gardons actifs",
                "68:45" to "Roseaux - Tanches et carpes",
                "25:80" to "Ponton - Perches et brochets",
                "55:25" to "Fosse 4m - Brèmes nocturnes"
            )
        )
    )

    // ==========================================
    // FONCTIONS UTILITAIRES AVANCÉES
    // ==========================================

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
            when {
                fish.preferredTime.contains("Matin") && hour in 6..9 -> true
                fish.preferredTime.contains("Journée") && hour in 10..16 -> true
                fish.preferredTime.contains("Soir") && hour in 17..20 -> true
                fish.preferredTime.contains("Nuit") && (hour >= 21 || hour <= 5) -> true
                fish.preferredTime.contains("Crépuscule") && hour in 17..19 -> true
                else -> false
            }
        }
    }

    /**
     * Retourne les poissons adaptés aux conditions météo
     */
    fun getFishByWeather(weather: WeatherType): List<Fish> {
        return getAllFish().filter { fish ->
            fish.preferredWeather.contains(weather) || fish.preferredWeather.contains(WeatherType.ANY)
        }
    }

    /**
     * Recommande les meilleurs appâts pour un poisson donné
     */
    fun recommendBaitsForFish(fishName: String): List<String> {
        return getAllFish().find { it.name.equals(fishName, ignoreCase = true) }
            ?.preferredBaits ?: emptyList()
    }

    /**
     * Calcule le score de difficulté d'un poisson
     */
    fun getFishDifficultyScore(fish: Fish): Int {
        return when (fish.rarity) {
            FishRarity.COMMON -> 1
            FishRarity.UNCOMMON -> 2
            FishRarity.RARE -> 3
            FishRarity.EPIC -> 4
            FishRarity.LEGENDARY -> 5
        }
    }

    /**
     * Retourne les poissons par gamme de poids
     */
    fun getFishByWeightRange(minWeight: Double, maxWeight: Double): List<Fish> {
        return getAllFish().filter { fish ->
            fish.maxWeight >= minWeight && fish.minWeight <= maxWeight
        }
    }

    /**
     * NOUVEAU: Retourne les poissons trophées (Epic/Legendary)
     */
    fun getTrophyFish(): List<Fish> {
        return getAllFish().filter {
            it.rarity == FishRarity.EPIC || it.rarity == FishRarity.LEGENDARY
        }
    }

    /**
     * NOUVEAU: Retourne les spots premium (niveau 25+)
     */
    fun getPremiumLakes(): List<Lake> {
        return lakes.filter { it.unlockLevel >= 25 }
    }

    /**
     * NOUVEAU: Recherche de poissons par appât optimal
     */
    fun getFishByBait(baitName: String): List<Fish> {
        return getAllFish().filter { fish ->
            fish.preferredBaits.any { it.equals(baitName, ignoreCase = true) }
        }
    }

    /**
     * NOUVEAU: Obtient les appâts RF4 par catégorie
     */
    fun getBaitsByCategory(): Map<String, List<String>> {
        return mapOf(
            "Vers et larves" to listOf(
                "Ver de terre", "Ver rouge", "Lombric", "Ver de vase", "Asticots",
                "Chrysalides", "Ver haché", "Larve de scolyte", "Larve d'éphémère",
                "Larve de plécoptère", "Larve de phrygane", "Sangsues"
            ),
            "Pâtes artisanales" to listOf(
                "Pain mouillé", "Pâte à l'ail", "Pâte sucrée", "Pâte au miel",
                "Pâte aux œufs", "Pâte au fromage blanc"
            ),
            "Graines et céréales" to listOf(
                "Orge perlé", "Maïs", "Semoule", "Flocons d'avoine", "Grains de blé",
                "Bouillie de pois", "Bouillie de semoule", "Bouillie de millet",
                "Tournesol", "Graines de lin", "Chènevis"
            ),
            "Appâts vivants" to listOf(
                "Vif", "Morceaux de poisson", "Écrevisse", "Moule de rivière",
                "Moule zébrée", "Grenouille"
            ),
            "Appâts spéciaux" to listOf(
                "Cubes de pomme de terre", "Cubes de fromage", "Polenta",
                "Poisson mort", "Viande de pétoncle", "Crabe"
            ),
            "Appâts marins" to listOf(
                "Ver marin", "Crevette", "Calamar"
            ),
            "Autres" to listOf(
                "Bouillettes", "Pellets", "Autre"
            )
        )
    }

    /**
     * NOUVEAU: Statistics RF4 réelles
     */
    fun getRF4Statistics(): Map<String, Any> {
        return mapOf(
            "total_fish_species" to getAllFish().size,
            "total_water_bodies" to lakes.size,
            "total_baits" to ALL_BAITS.size,
            "legendary_fish_count" to getAllFish().count { it.rarity == FishRarity.LEGENDARY },
            "epic_fish_count" to getAllFish().count { it.rarity == FishRarity.EPIC },
            "expert_lakes_count" to lakes.count { it.difficulty == Difficulty.EXPERT },
            "max_unlock_level" to (lakes.maxOfOrNull { it.unlockLevel } ?: 0)
        )
    }
}