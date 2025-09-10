// ==========================================
// FICHIER: data/FishingData.kt - DONNÉES COMPLÈTES RF4 AVEC TRADUCTIONS ANGLAISES
// ==========================================
package com.rf4.fishingrf4.data

import android.content.Context
import com.rf4.fishingrf4.data.models.*
import com.rf4.fishingrf4.utils.LanguageManager

object FishingData {

    // ==========================================
    // POISSONS COMMUNS - AVEC TRADUCTIONS ANGLAISES
    // ==========================================
    private val commonFresh = listOf(
        Fish(
            name = "Gardon",
            nameEn = "Roach",
            species =  "Rutilus rutilus",
            rarity = FishRarity.COMMON,
            minWeight = 0.05,
            maxWeight = 0.3,
            preferredBaits = listOf("Pain", "Asticot", "Blé"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Poisson blanc très commun, idéal pour débuter la pêche.",
            descriptionEn = "Very common white fish, ideal for beginners."
        ),
        Fish(
            name = "Ablette",
            nameEn = "Bleak",
            species =  "Alburnus alburnus",
            rarity = FishRarity.COMMON,
            minWeight = 0.02,
            maxWeight = 0.1,
            preferredBaits = listOf("Asticot", "Ver de vase"),
            preferredTime = listOf("Matin"),
            preferredWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY),
            description = "Petit poisson argenté très vif, se pêche en surface.",
            descriptionEn = "Small silvery fish, very lively, caught near surface."
        ),
        Fish(
            name = "Brème",
            nameEn = "Bream",
            species =  "Abramis brama",
            rarity = FishRarity.COMMON,
            minWeight = 0.3,
            maxWeight = 2.0,
            preferredBaits = listOf("Ver de vase", "Pâte", "Maïs"),
            preferredTime = listOf("Nuit", "Matin"),
            preferredWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN),
            description = "Poisson de fond, se pêche principalement la nuit.",
            descriptionEn = "Bottom feeder, mainly caught at night."
        ),
        Fish(
            name = "Rotengle",
            nameEn = "Rudd",
            species =  "Scardinius erythrophthalmus",
            rarity = FishRarity.COMMON,
            minWeight = 0.1,
            maxWeight = 0.8,
            preferredBaits = listOf("Pain", "Ver rouge", "Blé"),
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
            maxWeight = 1.5,
            preferredBaits = listOf("Ver de terre", "Pain", "Maïs"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Petit cyprinidé résistant, proche de la carpe.",
            descriptionEn = "Small hardy cyprinid, related to carp."
        ),
        Fish(
            name = "Carassin argenté",
            nameEn = "Silver Crucian Carp",
            species =  "Carassius argentus",
            rarity = FishRarity.COMMON,
            minWeight = 0.2,
            maxWeight = 1.5,
            preferredBaits = listOf("Ver de terre", "Pain", "Maïs"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Variété argentée du carassin commun.",
            descriptionEn = "Silver variety of common crucian carp."
        ),
        Fish(
            name = "Goujon",
            nameEn = "Gudgeon",
            species =  "Gobio gobio",
            rarity = FishRarity.COMMON,
            minWeight = 0.05,
            maxWeight = 0.2,
            preferredBaits = listOf("Ver rouge", "Asticot"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Petit poisson de fond aux barbillons caractéristiques.",
            descriptionEn = "Small bottom fish with characteristic barbels."
        ),
        Fish(
            name = "Vairon",
            nameEn = "Minnow",
            species =  "Phoxinus phoxinus",
            rarity = FishRarity.COMMON,
            minWeight = 0.01,
            maxWeight = 0.08,
            preferredBaits = listOf("Asticot", "Ver rouge"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Très petit poisson grégaire des eaux claires.",
            descriptionEn = "Very small schooling fish of clear waters."
        ),
        Fish(
            name = "Épinoche",
            nameEn = "Stickleback",
            species =  "Gasterosteus aculeatus",
            rarity = FishRarity.COMMON,
            minWeight = 0.01,
            maxWeight = 0.05,
            preferredBaits = listOf("Asticot", "Ver rouge"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Minuscule poisson aux épines dorsales caractéristiques.",
            descriptionEn = "Tiny fish with characteristic dorsal spines."
        ),
        Fish(
            name = "Loche d'étang",
            nameEn = "Pond Loach",
            species =  "Misgurnus fossilis",
            rarity = FishRarity.COMMON,
            minWeight = 0.1,
            maxWeight = 0.3,
            preferredBaits = listOf("Ver de vase", "Asticot"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.RAIN),
            description = "Poisson de fond très résistant aux conditions difficiles.",
            descriptionEn = "Bottom fish very resistant to harsh conditions."
        )
    )

    // ==========================================
    // POISSONS INTERMÉDIAIRES - AVEC TRADUCTIONS ANGLAISES
    // ==========================================
    private val intermediateFresh = listOf(
        Fish(
            name = "Perche",
            nameEn = "Perch",
            species =  "Perca fluviatilis",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.1,
            maxWeight = 2.5,
            preferredBaits = listOf("Ver rouge", "Petit leurre", "Poisson vif"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY),
            description = "Carnassier aux rayures noires caractéristiques.",
            descriptionEn = "Predator with characteristic black stripes."
        ),
        Fish(
            name = "Tanche",
            nameEn = "Tench",
            species =  "Tinca tinca",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.5,
            maxWeight = 4.0,
            preferredBaits = listOf("Ver de terre", "Maïs", "Bouillette"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Poisson aux nageoires arrondies, amateur d'herbiers.",
            descriptionEn = "Fish with rounded fins, loves weed beds."
        ),
        Fish(
            name = "Brochet",
            nameEn = "Pike",
            species =  "Esox lucius",
            rarity = FishRarity.RARE,
            minWeight = 1.0,
            maxWeight = 15.0,
            preferredBaits = listOf("Poisson vif", "Leurre", "Grenouille"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Grand prédateur aux dents acérées, roi des eaux douces.",
            descriptionEn = "Large predator with sharp teeth, king of freshwater."
        ),
        Fish(
            name = "Carpe commune",
            nameEn = "Common Carp",
            species =  "Cyprinus carpio",
            rarity = FishRarity.UNCOMMON,
            minWeight = 2.0,
            maxWeight = 25.0,
            preferredBaits = listOf("Bouillette", "Maïs", "Pellets"),
            preferredTime = listOf("Nuit", "Matin"),
            preferredWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN),
            description = "Poisson puissant et méfiant, rêve de tout carpiste.",
            descriptionEn = "Powerful and wary fish, dream of every carp angler."
        ),
        Fish(
            name = "Carpe miroir",
            nameEn = "Mirror Carp",
            species =  "Cyprinus carpio",
            rarity = FishRarity.UNCOMMON,
            minWeight = 2.5,
            maxWeight = 30.0,
            preferredBaits = listOf("Bouillette", "Maïs", "Pellets"),
            preferredTime = listOf("Nuit", "Matin"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Variété de carpe aux écailles irrégulières.",
            descriptionEn = "Carp variety with irregular scales."
        ),
        Fish(
            name = "Chevesne",
            nameEn = "Chub",
            species =  "Squalius cephalus",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.5,
            maxWeight = 4.0,
            preferredBaits = listOf("Pain", "Fromage", "Cerise"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.SUNNY),
            description = "Poisson omnivore à la grosse tête caractéristique.",
            descriptionEn = "Omnivorous fish with characteristic large head."
        ),
        Fish(
            name = "Ide mélanote",
            nameEn = "Ide",
            species =  "Leuciscus idus",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.8,
            maxWeight = 6.0,
            preferredBaits = listOf("Ver rouge", "Pain", "Petit leurre"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY),
            description = "Cyprinidé doré aux reflets métalliques.",
            descriptionEn = "Golden cyprinid with metallic reflections."
        ),
        Fish(
            name = "Truite arc-en-ciel",
            nameEn = "Rainbow Trout",
            species =  "Oncorhynchus mykiss",
            rarity = FishRarity.RARE,
            minWeight = 0.3,
            maxWeight = 3.0,
            preferredBaits = listOf("Ver rouge", "Mouche", "Petit leurre"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY, WeatherType.LIGHT_RAIN),
            description = "Salmonidé aux couleurs chatoyantes, très combatif.",
            descriptionEn = "Salmonid with shimmering colors, very combative."
        ),
        Fish(
            name = "Loche franche",
            nameEn = "Stone Loach",
            species =  "Barbatula barbatula",
            rarity = FishRarity.COMMON,
            minWeight = 0.05,
            maxWeight = 0.15,
            preferredBaits = listOf("Ver de vase", "Asticot"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Petit poisson de fond aux barbillons sensibles.",
            descriptionEn = "Small bottom fish with sensitive barbels."
        ),
        Fish(
            name = "Barbeau",
            nameEn = "Barbel",
            species =  "Barbus barbus",
            rarity = FishRarity.UNCOMMON,
            minWeight = 1.0,
            maxWeight = 8.0,
            preferredBaits = listOf("Ver rouge", "Fromage", "Pellets"),
            preferredTime = listOf("Nuit", "Matin"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Poisson puissant des eaux courantes aux 4 barbillons.",
            descriptionEn = "Powerful fish of running waters with 4 barbels."
        )
    )

    // ==========================================
    // POISSONS AVANCÉS - AVEC TRADUCTIONS ANGLAISES
    // ==========================================
    private val advancedFresh = listOf(
        Fish(
            name = "Sandre",
            nameEn = "Zander",
            species =  "Sander lucioperca",
            rarity = FishRarity.RARE,
            minWeight = 1.5,
            maxWeight = 12.0,
            preferredBaits = listOf("Poisson vif", "Leurre souple", "Ver rouge"),
            preferredTime = listOf("Crépuscule", "Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Prédateur crépusculaire aux yeux vitreux caractéristiques.",
            descriptionEn = "Twilight predator with characteristic glassy eyes."
        ),
        Fish(
            name = "Silure",
            nameEn = "Catfish",
            species =  "Silurus glanis",
            rarity = FishRarity.EPIC,
            minWeight = 5.0,
            maxWeight = 80.0,
            preferredBaits = listOf("Poisson vif", "Boudin", "Pellets"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST, WeatherType.RAIN),
            description = "Géant des eaux douces aux énormes barbillons.",
            descriptionEn = "Freshwater giant with huge barbels."
        ),
        Fish(
            name = "Carpe fantôme",
            nameEn = "Ghost Carp",
            species =  "Cyprinus carpio",
            rarity = FishRarity.RARE,
            minWeight = 3.0,
            maxWeight = 20.0,
            preferredBaits = listOf("Bouillette", "Tigernuts", "Pellets"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.FOG),
            description = "Variété de carpe aux couleurs pâles et mystérieuses.",
            descriptionEn = "Carp variety with pale and mysterious colors."
        ),
        Fish(
            name = "Amour Blanc",
            nameEn = "Grass Carp",
            species =  "Ctenopharyngodon idella",
            rarity = FishRarity.RARE,
            minWeight = 4.0,
            maxWeight = 35.0,
            preferredBaits = listOf("Herbe", "Maïs", "Bouillette végétale"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.SUNNY),
            description = "Grande carpe herbivore originaire d'Asie.",
            descriptionEn = "Large herbivorous carp from Asia."
        ),
        Fish(
            name = "Carpe à grosse tête",
            nameEn = "Bighead Carp",
            species =  "Hypophthalmichthys nobilis",
            rarity = FishRarity.RARE,
            minWeight = 3.0,
            maxWeight = 25.0,
            preferredBaits = listOf("Plancton", "Bouillette", "Pellets"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.CLOUDY),
            description = "Carpe asiatique à la tête disproportionnée.",
            descriptionEn = "Asian carp with disproportionate head."
        ),
        Fish(
            name = "Esturgeon",
            nameEn = "Sturgeon",
            species =  "Acipenser sturio",
            rarity = FishRarity.LEGENDARY,
            minWeight = 10.0,
            maxWeight = 100.0,
            preferredBaits = listOf("Ver marin", "Poisson vif", "Crevette"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Poisson préhistorique rarissime, véritable trésor.",
            descriptionEn = "Extremely rare prehistoric fish, a true treasure."
        ),
        Fish(
            name = "Sterlet",
            nameEn = "Sterlet",
            species =  "Acipenser ruthenus",
            rarity = FishRarity.EPIC,
            minWeight = 2.0,
            maxWeight = 8.0,
            preferredBaits = listOf("Ver rouge", "Poisson vif"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Petit esturgeon aux plaques osseuses caractéristiques.",
            descriptionEn = "Small sturgeon with characteristic bony plates."
        ),
        Fish(
            name = "Béluga",
            nameEn = "Beluga Sturgeon",
            species =  "Huso huso",
            rarity = FishRarity.LEGENDARY,
            minWeight = 20.0,
            maxWeight = 200.0,
            preferredBaits = listOf("Gros poisson vif", "Crevette géante"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Le plus grand esturgeon, géant mythique des rivières.",
            descriptionEn = "The largest sturgeon, mythical giant of rivers."
        ),
        Fish(
            name = "Nase (ou hotu)",
            nameEn = "Nase",
            species =  "Chondrostoma nasus",
            rarity = FishRarity.UNCOMMON,
            minWeight = 0.3,
            maxWeight = 2.0,
            preferredBaits = listOf("Ver rouge", "Asticot"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.SUNNY),
            description = "Poisson au museau caractéristique des eaux claires.",
            descriptionEn = "Fish with characteristic snout of clear waters."
        ),
        Fish(
            name = "Aspe",
            nameEn = "Asp",
            species =  "Aspius aspius",
            rarity = FishRarity.RARE,
            minWeight = 2.0,
            maxWeight = 12.0,
            preferredBaits = listOf("Poisson vif", "Leurre", "Mouche"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.WIND),
            description = "Prédateur de surface aux attaques spectaculaires.",
            descriptionEn = "Surface predator with spectacular attacks."
        )
    )

    // ==========================================
    // POISSONS MARINS - AVEC TRADUCTIONS ANGLAISES
    // ==========================================
    private val marineFish = listOf(
        Fish(
            name = "Hareng",
            nameEn = "Herring",
            species =  "Clupea harengus",
            rarity = FishRarity.COMMON,
            minWeight = 0.2,
            maxWeight = 0.8,
            preferredBaits = listOf("Ver marin", "Petit leurre"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Poisson pélagique formant d'immenses bancs.",
            descriptionEn = "Pelagic fish forming huge schools."
        ),
        Fish(
            name = "Plie",
            nameEn = "Plaice",
            species =  "Pleuronectes platessa",
            rarity = FishRarity.COMMON,
            minWeight = 0.5,
            maxWeight = 3.0,
            preferredBaits = listOf("Ver marin", "Crevette"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Poisson plat aux taches oranges caractéristiques.",
            descriptionEn = "Flatfish with characteristic orange spots."
        ),
        Fish(
            name = "Flet",
            nameEn = "Flounder",
            species =  "Platichthys flesus",
            rarity = FishRarity.COMMON,
            minWeight = 0.3,
            maxWeight = 2.0,
            preferredBaits = listOf("Ver marin", "Crevette"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Poisson plat commun des estuaires et côtes.",
            descriptionEn = "Common flatfish of estuaries and coasts."
        ),
        Fish(
            name = "Morue",
            nameEn = "Cod",
            species =  "Gadus morhua",
            rarity = FishRarity.UNCOMMON,
            minWeight = 2.0,
            maxWeight = 15.0,
            preferredBaits = listOf("Poisson vif", "Ver marin", "Crevette"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Poisson emblématique de l'Atlantique Nord.",
            descriptionEn = "Emblematic fish of the North Atlantic."
        ),
        Fish(
            name = "Merlan",
            nameEn = "Whiting",
            species =  "Merlangius merlangus",
            rarity = FishRarity.COMMON,
            minWeight = 0.3,
            maxWeight = 2.5,
            preferredBaits = listOf("Ver marin", "Petits poissons"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Gadidé commun aux eaux peu profondes.",
            descriptionEn = "Common gadid of shallow waters."
        ),
        Fish(
            name = "Lieu noir",
            nameEn = "Saithe",
            species =  "Pollachius virens",
            rarity = FishRarity.UNCOMMON,
            minWeight = 1.0,
            maxWeight = 8.0,
            preferredBaits = listOf("Poisson vif", "Leurre", "Ver marin"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Gadidé sombre des eaux froides nordiques.",
            descriptionEn = "Dark gadid of cold northern waters."
        ),
        Fish(
            name = "Turbot",
            nameEn = "Turbot",
            species =  "Psetta maxima",
            rarity = FishRarity.RARE,
            minWeight = 3.0,
            maxWeight = 25.0,
            preferredBaits = listOf("Poisson vif", "Crevette", "Ver marin"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Poisson plat noble aux chairs délicates.",
            descriptionEn = "Noble flatfish with delicate flesh."
        ),
        Fish(
            name = "Cabillaud",
            nameEn = "Atlantic Cod",
            species =  "Gadus morhua",
            rarity = FishRarity.UNCOMMON,
            minWeight = 1.5,
            maxWeight = 10.0,
            preferredBaits = listOf("Poisson vif", "Ver marin"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Variété nordique de la morue commune.",
            descriptionEn = "Northern variety of common cod."
        ),
        Fish(
            name = "Maquereau",
            nameEn = "Mackerel",
            species =  "Scomber scombrus",
            rarity = FishRarity.COMMON,
            minWeight = 0.3,
            maxWeight = 1.5,
            preferredBaits = listOf("Petit leurre", "Ver marin"),
            preferredTime = listOf("Matin", "Soir"),
            preferredWeather = listOf(WeatherType.SUNNY),
            description = "Poisson pélagique rapide aux rayures caractéristiques.",
            descriptionEn = "Fast pelagic fish with characteristic stripes."
        ),
        Fish(
            name = "Flétan",
            nameEn = "Halibut",
            species =  "Hippoglossus hippoglossus",
            rarity = FishRarity.EPIC,
            minWeight = 20.0,
            maxWeight = 200.0,
            preferredBaits = listOf("Gros poisson vif", "Crevette géante"),
            preferredTime = listOf("Journée"),
            preferredWeather = listOf(WeatherType.ANY),
            description = "Géant des mers, le plus grand poisson plat.",
            descriptionEn = "Ocean giant, the largest flatfish."
        ),
        Fish(
            name = "Requin épineux",
            nameEn = "Spiny Dogfish",
            species =  "Squalus acanthias",
            rarity = FishRarity.RARE,
            minWeight = 5.0,
            maxWeight = 40.0,
            preferredBaits = listOf("Poisson vif", "Calamar"),
            preferredTime = listOf("Nuit"),
            preferredWeather = listOf(WeatherType.OVERCAST),
            description = "Petit requin aux épines venimeuses.",
            descriptionEn = "Small shark with venomous spines."
        )
    )

    data class Bait(
        val name: String,
        val nameEn: String,
        val category: String = "",
        val categoryEn: String = ""
    )

    val ALL_BAITS = listOf(
        "Ablette", "Asticot", "Bouillettes", "Calamar", "Chironome",
        "Crabe", "Crevette", "Cuiller", "Devon", "Épinoche",
        "Fromage", "Gammare", "Graine de mais", "Hareng", "Jig",
        "Leurre souple", "Maïs", "Maquereau", "Mouche sèche",
        "Nymphe", "Pain", "Pellets", "Pâte", "Poisson vif",
        "Popper", "Porte-bois", "Sardine", "Spinnerbait", "Sprat",
        "Streamer", "Ver de terre", "Ver de vase", "Ver marin",
        "Ver rouge", "Wobbler", "Pate à l'ail", "Autre"
    )

    // Extension pour obtenir le nom localisé d'un appât
    fun Bait.getLocalizedName(context: Context): String {
        return when (LanguageManager.getCurrentLanguage(context)) {
            LanguageManager.Language.ENGLISH -> this.nameEn
            LanguageManager.Language.FRENCH -> this.name
        }
    }

    // Extension pour obtenir la catégorie localisée d'un appât
    fun Bait.getLocalizedCategory(context: Context): String {
        return when (LanguageManager.getCurrentLanguage(context)) {
            LanguageManager.Language.ENGLISH -> this.categoryEn
            LanguageManager.Language.FRENCH -> this.category
        }
    }

    // Fonction pour obtenir tous les noms d'appâts (pour compatibilité)
    fun getAllBaitNames(): List<String> {
        return ALL_BAITS  // ✅ Si ALL_BAITS est déjà List<String>
    }

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
                    "Loche d'étang","Carassin argenté"
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
            type = LakeType.CREEK,
            difficulty = Difficulty.BEGINNER,
            availableFish = getFishByNames(
                listOf(
                    "Gardon", "Ablette", "Goujon", "Vairon", "Chevesne",
                    "Ide mélanote", "Perche", "Brochet", "Truite arc-en-ciel",
                    "Loche franche", "Barbeau"
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
            id = "bear_lake",
            name = "Lac de l'Ours",
            type = LakeType.LAKE,
            difficulty = Difficulty.INTERMEDIATE,
            availableFish = getFishByNames(
                listOf(
                    "Gardon", "Brème", "Carpe commune", "Carpe miroir", "Perche",
                    "Sandre", "Brochet", "Tanche", "Silure", "Barbeau"
                )
            ),
            description = "Lac de taille moyenne avec de beaux carnassiers et quelques silures.",
            unlockLevel = 5,
            coordinates = mapOf(
                "75:95" to "Herbier dense, tanches et carpes.",
                "110:140" to "Tombant rocheux, sandres et perches.",
                "45:120" to "Arbre mort immergé, brochets.",
                "85:65" to "Pleine eau, bancs de gardons."
            )
        ),

        Lake(
            id = "falcon_lake",
            name = "Lac du Faucon",
            type = LakeType.LAKE,
            difficulty = Difficulty.INTERMEDIATE,
            availableFish = getFishByNames(
                listOf(
                    "Perche", "Sandre", "Brochet", "Carpe commune", "Barbeau",
                    "Chevesne", "Ide mélanote", "Truite arc-en-ciel"
                )
            ),
            description = "Lac montagnard aux eaux cristallines, royaume des truites et perches.",
            unlockLevel = 7,
            coordinates = mapOf(
                "60:45" to "Anse rocheuse, truites actives.",
                "95:78" to "Plateau de 6m, perches et sandres.",
                "35:90" to "Bordure d'épicéas, brochets à l'affût.",
                "78:115" to "Chenal principal, chevesnes."
            )
        ),

        Lake(
            id = "rocky_lake",
            name = "Lac Rocheux",
            type = LakeType.LAKE,
            difficulty = Difficulty.ADVANCED,
            availableFish = getFishByNames(
                listOf(
                    "Sandre", "Brochet", "Perche", "Silure", "Carpe fantôme",
                    "Amour Blanc", "Esturgeon", "Aspe"
                )
            ),
            description = "Lac profond aux structures rocheuses complexes, repaire de gros poissons.",
            unlockLevel = 10,
            coordinates = mapOf(
                "100:80" to "Éboulis rocheux (12m), gros sandres.",
                "65:115" to "Plateau immergé, silures en chasse.",
                "85:50" to "Faille profonde (18m), esturgeons.",
                "45:95" to "Haut-fond, aspes en surface."
            )
        ),

        Lake(
            id = "old_burg_lake",
            name = "Lac du Vieux Bourg",
            type = LakeType.LAKE,
            difficulty = Difficulty.ADVANCED,
            availableFish = getFishByNames(
                listOf(
                    "Carpe commune", "Carpe miroir", "Carpe fantôme", "Amour Blanc",
                    "Carpe à grosse tête", "Silure", "Sandre", "Brochet",
                    "Esturgeon", "Sterlet"
                )
            ),
            description = "Ancien étang de château converti en lac de pêche premium.",
            unlockLevel = 12,
            coordinates = mapOf(
                "70:105" to "Ancien ponton du château, carpes méfiantes.",
                "115:75" to "Fondations immergées, silures géants.",
                "50:140" to "Roselière dense, amours blancs.",
                "90:60" to "Fosse centrale (15m), esturgeons."
            )
        ),

        Lake(
            id = "kuori_lake",
            name = "Lac Kuori",
            type = LakeType.LAKE,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Sandre", "Perche", "Brochet", "Truite arc-en-ciel", "Silure",
                    "Esturgeon", "Sterlet", "Béluga", "Aspe"
                )
            ),
            description = "Lac nordique sauvage aux conditions extrêmes et poissons trophées.",
            unlockLevel = 15,
            coordinates = mapOf(
                "85:90" to "Île rocheuse, gros brochets.",
                "125:110" to "Fosse glaciaire (25m), bélugas.",
                "60:125" to "Cascade, truites sauvages.",
                "105:70" to "Banc de sable, aspes chasseurs."
            )
        ),

        Lake(
            id = "white_moose_lake",
            name = "Lac de l'Élan Blanc",
            type = LakeType.LAKE,
            difficulty = Difficulty.EXPERT,
            availableFish = getFishByNames(
                listOf(
                    "Brochet", "Sandre", "Perche", "Silure", "Esturgeon",
                    "Béluga", "Truite arc-en-ciel", "Aspe"
                )
            ),
            description = "Lac légendaire de la taïga, habitat des plus gros spécimens.",
            unlockLevel = 18,
            coordinates = mapOf(
                "95:135" to "Forêt noyée, brochets métrés.",
                "130:85" to "Abîme central (30m), bélugas géants.",
                "75:160" to "Marais bordier, silures nocturnes.",
                "110:115" to "Récif de pierres, gros sandres."
            )
        )
    )

    // ==========================================
    // FONCTIONS UTILITAIRES
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
}