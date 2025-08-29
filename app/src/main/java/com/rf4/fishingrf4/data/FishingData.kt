// ==========================================
// FICHIER: data/FishingData.kt - DONNÉES COMPLÈTES RF4
// ==========================================
package com.rf4.fishingrf4.data

import com.rf4.fishingrf4.data.models.*

object FishingData {

    // POISSONS COMMUNS
    private val commonFresh = listOf(
        Fish(
            "Gardon", "Rutilus rutilus", FishRarity.COMMON,
            weight = 0.05..0.3, preferredBait = listOf("Pain", "Asticot", "Blé"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Ablette",
            "Alburnus alburnus",
            FishRarity.COMMON,
            weight = 0.02..0.1,
            preferredBait = listOf("Asticot", "Ver de vase"),
            bestHours = listOf(5, 6, 7, 17, 18, 19),
            bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Brème",
            "Abramis brama",
            FishRarity.COMMON,
            weight = 0.3..2.0,
            preferredBait = listOf("Ver de vase", "Pâte", "Maïs"),
            bestHours = listOf(20, 21, 22, 5, 6),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Rotengle",
            "Scardinius erythrophthalmus",
            FishRarity.COMMON,
            weight = 0.1..0.8,
            preferredBait = listOf("Pain", "Ver rouge", "Blé"),
            bestHours = listOf(7, 8, 9, 16, 17, 18),
            bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
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
            bestHours = listOf(6, 7, 8, 18, 19), bestWeather = listOf(WeatherType.CLOUDY)
        ),
        Fish(
            "Grémille", "Gymnocephalus cernua", FishRarity.COMMON,
            weight = 0.05..0.15, preferredBait = listOf("Ver de vase", "Asticot", "Ver rouge"),
            bestHours = listOf(7, 8, 9, 17, 18), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Amour Blanc",
            "Ctenopharyngodon idella",
            FishRarity.UNCOMMON,
            weight = 1.0..50.0,
            preferredBait = listOf("Herbe", "Algue", "Maïs", "Pain"),
            bestHours = listOf(8, 9, 10, 15, 16, 17),
            bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),

        Fish(
            "Amour Noir", "Mylopharyngodon piceus", FishRarity.UNCOMMON,
            weight = 2.0..40.0, preferredBait = listOf("Moule", "Escargot", "Ver de terre"),
            bestHours = listOf(7, 8, 9, 16, 17, 18), bestWeather = listOf(WeatherType.CLOUDY)
        ),

        Fish(
            "Carassin argenté", "Carassius gibelio", FishRarity.UNCOMMON,
            weight = 0.3..3.0, preferredBait = listOf("Pain", "Ver de terre", "Maïs", "Pâte"),
            bestHours = listOf(6, 7, 8, 17, 18, 19), bestWeather = listOf(WeatherType.ANY)
        ),

        Fish(
            "Goujon de l'Amour", "Pseudogobio esocinus", FishRarity.UNCOMMON,
            weight = 0.1..0.4, preferredBait = listOf("Ver de vase", "Asticot", "Ver rouge"),
            bestHours = listOf(7, 8, 9, 16, 17, 18), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Grenouille",
            "Rana temporaria",
            FishRarity.COMMON,
            weight = 0.05..0.3,
            preferredBait = listOf("Mouche", "Insecte", "Petit ver"),
            bestHours = listOf(19, 20, 21, 22, 5, 6),
            bestWeather = listOf(WeatherType.RAIN, WeatherType.OVERCAST)
        )

    )

    // POISSONS PEU COMMUNS
    private val uncommonFresh = listOf(
        Fish(
            "Perche",
            "Perca fluviatilis",
            FishRarity.UNCOMMON,
            weight = 0.1..2.0,
            preferredBait = listOf("Ver rouge", "Cuillère", "Poisson mort"),
            bestHours = listOf(6, 7, 8, 17, 18, 19),
            bestWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST)
        ),
        Fish(
            "Brochet",
            "Esox lucius",
            FishRarity.UNCOMMON,
            weight = 0.5..15.0,
            preferredBait = listOf("Vif", "Cuillère", "Wobbler"),
            bestHours = listOf(5, 6, 7, 8, 17, 18, 19, 20),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Tanche",
            "Tinca tinca",
            FishRarity.UNCOMMON,
            weight = 0.3..4.0,
            preferredBait = listOf("Ver rouge", "Maïs", "Pâte"),
            bestHours = listOf(5, 6, 7, 18, 19, 20),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.RAIN)
        ),
        Fish(
            "Chevesne",
            "Squalius cephalus",
            FishRarity.UNCOMMON,
            weight = 0.2..3.0,
            preferredBait = listOf("Fromage", "Criquet", "Pain"),
            bestHours = listOf(7, 8, 9, 16, 17, 18),
            bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Carpe commune",
            "Cyprinus carpio",
            FishRarity.UNCOMMON,
            weight = 1.0..20.0,
            preferredBait = listOf("Bouillette", "Maïs", "Pomme de terre"),
            bestHours = listOf(6, 7, 8, 18, 19, 20, 21),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Ide mélanote",
            "Leuciscus idus",
            FishRarity.UNCOMMON,
            weight = 0.5..4.0,
            preferredBait = listOf("Fromage", "Pois", "Miel"),
            bestHours = listOf(6, 7, 8, 17, 18, 19),
            bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Barbeau", "Barbus barbus", FishRarity.UNCOMMON,
            weight = 0.5..8.0, preferredBait = listOf("Ver rouge", "Maïs", "Fromage"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.CLOUDY)
        ),
        Fish(
            "Rotang chinois", "Perccottus glenii", FishRarity.UNCOMMON,
            weight = 0.1..0.8, preferredBait = listOf("Ver rouge", "Ver de terre", "Petit poisson"),
            bestHours = listOf(6, 7, 8, 17, 18, 19), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Carpe argentée",
            "Hypophthalmichthys molitrix",
            FishRarity.UNCOMMON,
            weight = 1.0..25.0,
            preferredBait = listOf("Pâte", "Maïs", "Pain"),
            bestHours = listOf(8, 9, 10, 15, 16, 17),
            bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Carpe à grosse tête", "Hypophthalmichthys nobilis", FishRarity.UNCOMMON,
            weight = 2.0..30.0, preferredBait = listOf("Pâte", "Maïs", "Bouillette"),
            bestHours = listOf(9, 10, 11, 14, 15, 16), bestWeather = listOf(WeatherType.SUNNY)
        ),
        Fish(
            "Corégone de lac",
            "Coregonus clupeaformis",
            FishRarity.RARE,
            weight = 1.0..6.0,
            preferredBait = listOf("Ver de vase", "Mouche", "Teigne"),
            bestHours = listOf(5, 6, 7, 18, 19, 20),
            bestWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST)
        ),
        Fish(
            "Grand brochet",
            "Esox masquinongy",
            FishRarity.RARE, // Aussi connu comme Musky
            weight = 3.0..30.0,
            preferredBait = listOf("Gros vif", "Jerkbait", "Spinnerbait"),
            bestHours = listOf(6, 7, 8, 17, 18, 19),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
    )

    // POISSONS RARES
    private val rareFresh = listOf(
        Fish(
            "Sandre",
            "Sander lucioperca",
            FishRarity.RARE,
            weight = 0.5..8.0,
            preferredBait = listOf("Vif", "Jig", "Shad"),
            bestHours = listOf(5, 6, 7, 20, 21, 22),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Anguille",
            "Anguilla anguilla",
            FishRarity.RARE,
            weight = 0.3..3.0,
            preferredBait = listOf("Ver de terre", "Poisson mort"),
            bestHours = listOf(21, 22, 23, 0, 1, 2),
            bestWeather = listOf(WeatherType.RAIN, WeatherType.OVERCAST)
        ),
        Fish(
            "Truite arc-en-ciel",
            "Oncorhynchus mykiss",
            FishRarity.RARE,
            weight = 0.3..5.0,
            preferredBait = listOf("Mouche", "Cuillère", "Ver rouge"),
            bestHours = listOf(5, 6, 7, 8, 17, 18, 19),
            bestWeather = listOf(WeatherType.CLOUDY, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Aspe",
            "Aspius aspius",
            FishRarity.RARE,
            weight = 1.0..8.0,
            preferredBait = listOf("Cuillère", "Wobbler", "Vif"),
            bestHours = listOf(6, 7, 8, 17, 18, 19),
            bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Ombre arctique", "Thymallus arcticus", FishRarity.RARE,
            weight = 0.5..3.0, preferredBait = listOf("Mouche", "Ver rouge", "Cuillère"),
            bestHours = listOf(6, 7, 8, 17, 18), bestWeather = listOf(WeatherType.CLOUDY)
        ),
        Fish(
            "Truite fario", "Salmo trutta fario", FishRarity.RARE,
            weight = 0.3..4.0, preferredBait = listOf("Mouche", "Ver rouge", "Cuillère"),
            bestHours = listOf(5, 6, 7, 17, 18, 19), bestWeather = listOf(WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Hotu", "Chondrostoma nasus", FishRarity.RARE,
            weight = 0.5..2.0, preferredBait = listOf("Ver rouge", "Pâte"),
            bestHours = listOf(7, 8, 9, 16, 17), bestWeather = listOf(WeatherType.CLOUDY)
        ),
        Fish(
            "Omble de fontaine",
            "Salvelinus fontinalis",
            FishRarity.RARE,
            weight = 0.3..2.5,
            preferredBait = listOf("Mouche", "Ver rouge", "Cuillère"),
            bestHours = listOf(5, 6, 7, 17, 18, 19),
            bestWeather = listOf(WeatherType.CLOUDY, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Carpe noire", "Mylopharyngodon piceus", FishRarity.RARE,
            weight = 2.0..35.0, preferredBait = listOf("Moule", "Escargot", "Bouillette"),
            bestHours = listOf(8, 9, 10, 16, 17, 18), bestWeather = listOf(WeatherType.SUNNY)
        ),
        Fish(
            "Corégone", "Coregonus lavaretus", FishRarity.RARE,
            weight = 0.5..3.0, preferredBait = listOf("Ver de vase", "Mouche"),
            bestHours = listOf(6, 7, 8, 17, 18), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Corégone d'Ostrozhsky",
            "Coregonus ostrozhskiy",
            FishRarity.RARE,
            weight = 0.8..4.0,
            preferredBait = listOf("Ver de vase", "Mouche", "Petit leurre"),
            bestHours = listOf(5, 6, 7, 17, 18, 19),
            bestWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST)
        )
    )

    // POISSONS ÉPIQUES
    private val epicFresh = listOf(
        Fish(
            "Silure",
            "Silurus glanis",
            FishRarity.EPIC,
            weight = 5.0..100.0,
            preferredBait = listOf("Gros vif", "Poisson mort", "Bouillette géante"),
            bestHours = listOf(20, 21, 22, 23, 0, 1, 2, 3),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.RAIN)
        ),
        Fish(
            "Esturgeon",
            "Acipenser gueldenstaedtii",
            FishRarity.EPIC,
            weight = 10.0..50.0,
            preferredBait = listOf("Ver de terre", "Crevette", "Poisson mort"),
            bestHours = listOf(5, 6, 7, 8, 18, 19, 20),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Lotte", "Lota lota", FishRarity.EPIC,
            weight = 1.0..10.0, preferredBait = listOf("Poisson mort", "Ver de terre"),
            bestHours = listOf(22, 23, 0, 1, 2, 3, 4), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Truite de lac",
            "Salmo trutta lacustris",
            FishRarity.EPIC,
            weight = 2.0..15.0,
            preferredBait = listOf("Cuillère", "Wobbler", "Mouche"),
            bestHours = listOf(5, 6, 7, 8, 17, 18, 19),
            bestWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST)
        ),
        Fish(
            "Carpe miroir",
            "Cyprinus carpio speculum",
            FishRarity.EPIC,
            weight = 5.0..30.0,
            preferredBait = listOf("Bouillette", "Tigre nuts", "Maïs géant"),
            bestHours = listOf(6, 7, 8, 18, 19, 20, 21),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Brochet géant", "Esox lucius giganteus", FishRarity.EPIC,
            weight = 10.0..25.0, preferredBait = listOf("Gros wobbler", "Vif géant"),
            bestHours = listOf(5, 6, 7, 18, 19, 20), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Sandre géant", "Sander lucioperca giganteus", FishRarity.EPIC,
            weight = 5.0..15.0, preferredBait = listOf("Gros jig", "Vif"),
            bestHours = listOf(20, 21, 22, 5, 6), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Sterlet", "Acipenser ruthenus", FishRarity.EPIC,
            weight = 1.0..6.0, preferredBait = listOf("Ver rouge", "Crevette", "Caviar"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Omble chevalier", "Salvelinus alpinus", FishRarity.EPIC,
            weight = 1.0..8.0, preferredBait = listOf("Mouche", "Cuillère", "Ver rouge"),
            bestHours = listOf(5, 6, 7, 17, 18, 19), bestWeather = listOf(WeatherType.CLOUDY)
        ),
        Fish(
            "Touladi (Truite grise)",
            "Salvelinus namaycush",
            FishRarity.EPIC,
            weight = 2.0..25.0,
            preferredBait = listOf("Vif", "Cuillère oscillante", "Jig"),
            bestHours = listOf(5, 6, 7, 19, 20, 21),
            bestWeather = listOf(WeatherType.CLOUDY, WeatherType.FOG)
        ),
        Fish(
            "Splake",
            "Salvelinus fontinalis × Salvelinus namaycush",
            FishRarity.EPIC,
            weight = 1.0..9.0,
            preferredBait = listOf("Vif", "Cuillère", "Jig"),
            bestHours = listOf(6, 7, 8, 18, 19),
            bestWeather = listOf(WeatherType.CLOUDY, WeatherType.OVERCAST)
        ),
    )

    // POISSONS LÉGENDAIRES
    private val legendaryFresh = listOf(
        Fish(
            "Taimen",
            "Hucho taimen",
            FishRarity.LEGENDARY,
            weight = 10.0..60.0,
            preferredBait = listOf("Gros wobbler", "Mouche géante", "Vif"),
            bestHours = listOf(5, 6, 7, 8, 17, 18, 19),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Saumon atlantique",
            "Salmo salar",
            FishRarity.LEGENDARY,
            weight = 5.0..25.0,
            preferredBait = listOf("Mouche", "Cuillère", "Wobbler"),
            bestHours = listOf(5, 6, 7, 8, 18, 19, 20),
            bestWeather = listOf(WeatherType.CLOUDY, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Sterlet albinos",
            "Acipenser ruthenus albino",
            FishRarity.LEGENDARY,
            weight = 2.0..8.0,
            preferredBait = listOf("Ver rouge", "Crevette", "Caviar"),
            bestHours = listOf(6, 7, 8, 18, 19, 20),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.FOG)
        ),
        Fish(
            "Carpe fantôme",
            "Cyprinus carpio phantom",
            FishRarity.LEGENDARY,
            weight = 15.0..40.0,
            preferredBait = listOf("Bouillette speciale", "Appât mystère"),
            bestHours = listOf(22, 23, 0, 1, 2, 3),
            bestWeather = listOf(WeatherType.FOG, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Nelma", "Stenodus leucichthys", FishRarity.LEGENDARY,
            weight = 8.0..30.0, preferredBait = listOf("Cuillère", "Wobbler", "Vif"),
            bestHours = listOf(5, 6, 7, 17, 18), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Béluga",
            "Huso huso",
            FishRarity.LEGENDARY,
            weight = 50.0..200.0,
            preferredBait = listOf("Gros poisson mort", "Caviar géant"),
            bestHours = listOf(5, 6, 7, 18, 19, 20),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.FOG)
        )
    )
    private val specialFresh = listOf(
        Fish(
            "Moule zébrée", "Dreissena polymorpha", FishRarity.COMMON,
            weight = 0.01..0.05, preferredBait = listOf("Plancton", "Algue"),
            bestHours = listOf(8, 9, 10, 15, 16), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Grenouille", "Rana temporaria", FishRarity.COMMON,
            weight = 0.05..0.2, preferredBait = listOf("Mouche", "Petit ver"),
            bestHours = listOf(19, 20, 21, 22), bestWeather = listOf(WeatherType.RAIN)
        ),
        Fish(
            "Écrevisse", "Astacus astacus", FishRarity.UNCOMMON,
            weight = 0.1..0.5, preferredBait = listOf("Ver de terre", "Poisson mort"),
            bestHours = listOf(20, 21, 22, 23), bestWeather = listOf(WeatherType.ANY)
        )
    )

    // POISSONS MARINS
    private val marineCommon = listOf(
        Fish(
            "Sprat", "Sprattus sprattus", FishRarity.COMMON,
            weight = 0.01..0.02, preferredBait = listOf("Ver marin", "Plancton"),
            bestHours = listOf(6, 7, 8, 17, 18), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Hareng", "Clupea harengus", FishRarity.COMMON,
            weight = 0.1..0.3, preferredBait = listOf("Ver marin", "Crevette"),
            bestHours = listOf(5, 6, 7, 18, 19), bestWeather = listOf(WeatherType.CLOUDY)
        ),
        Fish(
            "Gobie", "Neogobius melanostomus", FishRarity.COMMON,
            weight = 0.05..0.2, preferredBait = listOf("Ver marin", "Crevette"),
            bestHours = listOf(7, 8, 9, 16, 17), bestWeather = listOf(WeatherType.ANY)
        ),
        Fish(
            "Éperlan", "Osmerus eperlanus", FishRarity.COMMON,
            weight = 0.05..0.15, preferredBait = listOf("Ver marin", "Petit poisson"),
            bestHours = listOf(6, 7, 8, 17, 18), bestWeather = listOf(WeatherType.CLOUDY)
        )
    )

    private val marineUncommon = listOf(
        Fish(
            "Plie", "Pleuronectes platessa", FishRarity.UNCOMMON,
            weight = 0.3..2.0, preferredBait = listOf("Ver marin", "Crevette", "Mollusque"),
            bestHours = listOf(6, 7, 8, 18, 19, 20), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Flet", "Platichthys flesus", FishRarity.UNCOMMON,
            weight = 0.2..1.5, preferredBait = listOf("Ver marin", "Crevette"),
            bestHours = listOf(7, 8, 9, 17, 18), bestWeather = listOf(WeatherType.CLOUDY)
        ),
        Fish(
            "Morue",
            "Gadus morhua",
            FishRarity.UNCOMMON,
            weight = 1.0..8.0,
            preferredBait = listOf("Poisson mort", "Crevette", "Calmar"),
            bestHours = listOf(5, 6, 7, 18, 19, 20),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Merlan", "Merlangius merlangus", FishRarity.UNCOMMON,
            weight = 0.3..2.0, preferredBait = listOf("Ver marin", "Petit poisson"),
            bestHours = listOf(6, 7, 8, 17, 18), bestWeather = listOf(WeatherType.CLOUDY)
        ),
        Fish(
            "Lieu noir", "Pollachius virens", FishRarity.UNCOMMON,
            weight = 1.0..6.0, preferredBait = listOf("Poisson mort", "Leurre marin"),
            bestHours = listOf(6, 7, 8, 17, 18, 19), bestWeather = listOf(WeatherType.OVERCAST)
        )
    )

    private val marineRare = listOf(
        Fish(
            "Turbot",
            "Scophthalmus maximus",
            FishRarity.RARE,
            weight = 2.0..15.0,
            preferredBait = listOf("Poisson mort", "Calmar", "Crevette géante"),
            bestHours = listOf(5, 6, 7, 19, 20, 21),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Cabillaud", "Gadus morhua major", FishRarity.RARE,
            weight = 3.0..20.0, preferredBait = listOf("Gros poisson mort", "Calmar"),
            bestHours = listOf(5, 6, 7, 18, 19, 20), bestWeather = listOf(WeatherType.OVERCAST)
        ),
        Fish(
            "Saumon de mer",
            "Salmo salar marinus",
            FishRarity.RARE,
            weight = 3.0..15.0,
            preferredBait = listOf("Cuillère marine", "Hareng"),
            bestHours = listOf(5, 6, 7, 17, 18, 19),
            bestWeather = listOf(WeatherType.CLOUDY, WeatherType.LIGHT_RAIN)
        ),
        Fish(
            "Maquereau",
            "Scomber scombrus",
            FishRarity.RARE,
            weight = 0.3..1.5,
            preferredBait = listOf("Petit leurre", "Ver marin"),
            bestHours = listOf(6, 7, 8, 16, 17, 18),
            bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        )
    )

    private val marineEpic = listOf(
        Fish(
            "Flétan",
            "Hippoglossus hippoglossus",
            FishRarity.EPIC,
            weight = 20.0..100.0,
            preferredBait = listOf("Gros poisson mort", "Calmar géant"),
            bestHours = listOf(5, 6, 7, 19, 20, 21),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.WIND)
        ),
        Fish(
            "Thon rouge",
            "Thunnus thynnus",
            FishRarity.EPIC,
            weight = 30.0..200.0,
            preferredBait = listOf("Gros leurre", "Maquereau"),
            bestHours = listOf(6, 7, 8, 17, 18, 19),
            bestWeather = listOf(WeatherType.SUNNY, WeatherType.CLOUDY)
        ),
        Fish(
            "Requin épineux",
            "Squalus acanthias",
            FishRarity.EPIC,
            weight = 5.0..20.0,
            preferredBait = listOf("Poisson mort", "Calmar"),
            bestHours = listOf(20, 21, 22, 5, 6),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.WIND)
        )
    )

    private val marineLegendary = listOf(
        Fish(
            "Esturgeon de mer",
            "Acipenser oxyrinchus",
            FishRarity.LEGENDARY,
            weight = 50.0..150.0,
            preferredBait = listOf("Ver géant", "Poisson mort spécial"),
            bestHours = listOf(5, 6, 7, 18, 19, 20),
            bestWeather = listOf(WeatherType.OVERCAST, WeatherType.FOG)
        ),
        Fish(
            "Thon géant", "Thunnus thynnus giganteus", FishRarity.LEGENDARY,
            weight = 100.0..300.0, preferredBait = listOf("Leurre légendaire", "Poisson géant"),
            bestHours = listOf(6, 7, 8, 17, 18), bestWeather = listOf(WeatherType.SUNNY)
        )
    )

    private fun getAllFish(): List<Fish> {
        return commonFresh + uncommonFresh + rareFresh + epicFresh + legendaryFresh +
                marineCommon + marineUncommon + marineRare + marineEpic + marineLegendary
    }

    private fun getFishByNames(names: List<String>): List<Fish> {
        return getAllFish().filter { it.name in names }
    }

    fun getAllLakes(): List<Lake> {
        return listOf(
            // ==========================================
            // NIVEAUX 1-10
            // ==========================================
            Lake(
                id = "mosquito_lake",
                name = "Lac aux moustiques",
                type = LakeType.POND,
                difficulty = Difficulty.BEGINNER,
                availableFish = getFishByNames(
                    listOf(
                        "Gardon",
                        "Carassin",
                        "Rotengle",
                        "Perche",
                        "Tanche",
                        "Brochet",
                        "Carpe commune",
                        "Carpe miroir"
                    )
                ),
                description = "Petit étang tranquille, idéal pour la pêche au coup et les premières carpes.",
                unlockLevel = 1,
                coordinates = mapOf(
                    "45:55" to "Nénuphars, spot à carpes.",
                    "21:25" to "Ponton en bois, idéal pour le gardon.",
                    "43:20" to "Zone de roseaux, tanches actives.",
                    "35:40" to "Fosse centrale pour les carassins."
                )
            ),
            Lake(
                id = "winding_rivulet",
                name = "Ruisseau sinueux",
                type = LakeType.CREEK,
                difficulty = Difficulty.BEGINNER,
                availableFish = getFishByNames(
                    listOf(
                        "Vairon",
                        "Épinoche",
                        "Goujon",
                        "Chevesne",
                        "Truite fario",
                        "Omble de fontaine"
                    )
                ),
                description = "Petit ruisseau de montagne. Pêche technique et précise à la truite.",
                unlockLevel = 1, // Niveau corrigé
                coordinates = mapOf(
                    "70:85" to "Près de la cascade, truites.",
                    "105:90" to "Fosse profonde, excellent pour les ombles.",
                    "111:117" to "Courant rapide, chevesnes.",
                    "145:110" to "Zone calme en aval."
                )
            ),

            // ==========================================
            // NIVEAUX 12-20
            // ==========================================
            Lake(
                id = "vieux_bourg",
                name = "Lac du Vieux Bourg",
                type = LakeType.LAKE,
                difficulty = Difficulty.INTERMEDIATE,
                availableFish = getFishByNames(
                    listOf(
                        "Gardon",
                        "Brème",
                        "Carassin",
                        "Grémille",
                        "Grenouille",
                        "Chevesne",
                        "Perche",
                        "Tanche",
                        "Carpe commune",
                        "Amour Blanc",
                        "Amour Noir",
                        "Carassin argenté",
                        "Goujon de l'Amour",
                        "Brochet",
                        "Anguille",
                        "Ide mélanote",
                        "Corégone d'Ostrozhsky",
                        "Lotte",
                        "Silure"
                    )
                ),
                description = "Lac technique avec de nombreuses espèces. Maîtrise des appâts requise.",
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
                        "Ablette",
                        "Gardon",
                        "Brème",
                        "Chevesne",
                        "Aspe",
                        "Barbeau",
                        "Sandre",
                        "Silure",
                        "Sterlet"
                    )
                ),
                description = "Une belle rivière avec un courant modéré, connue pour ses aspes et sterlets.",
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
                        "Gardon",
                        "Ablette",
                        "Brème",
                        "Rotengle",
                        "Carassin",
                        "Perche",
                        "Tanche",
                        "Brochet",
                        "Corégone",
                        "Lotte",
                        "Omble chevalier"
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
                name = "Lac des ours",
                type = LakeType.LAKE,
                difficulty = Difficulty.INTERMEDIATE,
                availableFish = getFishByNames(
                    listOf(
                        "Truite arc-en-ciel",
                        "Truite fario",
                        "Ombre arctique",
                        "Corégone",
                        "Truite de lac",
                        "Omble chevalier",
                        "Splake"
                    )
                ),
                description = "Lac de montagne cristallin. Spécialisé dans les salmonidés.",
                unlockLevel = 18,
                coordinates = mapOf(
                    "106:114" to "Zone la plus profonde, truites de lac.",
                    "45:124" to "Près du camp, idéal pour la truite Fario.",
                    "85:85" to "Tombant rocheux, repaire des ombles.",
                    "65:100" to "Plateau submergé pour les corégones."
                )
            ),
            Lake(
                id = "volkhov",
                name = "Rivière Volkhov",
                type = LakeType.RIVER,
                difficulty = Difficulty.ADVANCED,
                availableFish = getFishByNames(
                    listOf(
                        "Ablette",
                        "Brème",
                        "Perche",
                        "Brochet",
                        "Chevesne",
                        "Barbeau",
                        "Sandre",
                        "Anguille",
                        "Aspe",
                        "Silure",
                        "Saumon atlantique"
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

            // ==========================================
            // NIVEAUX 22-30
            // ==========================================
            Lake(
                id = "Donets",
                name = "Severski Donets",
                type = LakeType.RIVER,
                difficulty = Difficulty.EXPERT,
                availableFish = getFishByNames(
                    listOf(
                        "Gardon",
                        "Brème",
                        "Ablette",
                        "Sandre",
                        "Brochet",
                        "Carpe commune",
                        "Hotu",
                        "Aspe",
                        "Silure",
                        "Esturgeon"
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
                id = "sura",
                name = "Rivière Sura",
                type = LakeType.RIVER,
                difficulty = Difficulty.EXPERT,
                availableFish = getFishByNames(
                    listOf(
                        "Ablette",
                        "Goujon",
                        "Vairon",
                        "Chevesne",
                        "Ide mélanote",
                        "Perche",
                        "Brochet",
                        "Barbeau",
                        "Sandre",
                        "Silure",
                        "Esturgeon",
                        "Sterlet",
                        "Béluga"
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
                        "Carpe commune",
                        "Carpe miroir",
                        "Carpe fantôme",
                        "Amour Blanc",
                        "Carpe à grosse tête",
                        "Carassin",
                        "Tanche"
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
                id = "ladoga",
                name = "Lac Ladoga",
                type = LakeType.LAKE,
                difficulty = Difficulty.ADVANCED,
                availableFish = getFishByNames(
                    listOf(
                        "Gardon",
                        "Ablette",
                        "Brème",
                        "Perche",
                        "Brochet",
                        "Sandre",
                        "Anguille",
                        "Truite arc-en-ciel",
                        "Silure",
                        "Truite de lac",
                        "Saumon atlantique",
                        "Omble chevalier"
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
                id = "akhtuba",
                name = "Rivière Akhtouba",
                type = LakeType.RIVER,
                difficulty = Difficulty.EXPERT,
                availableFish = getFishByNames(
                    listOf(
                        "Brème",
                        "Carpe commune",
                        "Chevesne",
                        "Sandre",
                        "Silure",
                        "Esturgeon",
                        "Béluga",
                        "Aspe"
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
                        "Hareng",
                        "Plie",
                        "Flet",
                        "Morue",
                        "Merlan",
                        "Lieu noir",
                        "Turbot",
                        "Cabillaud",
                        "Maquereau",
                        "Flétan",
                        "Requin épineux"
                    )
                ),
                description = "Pêche en mer norvégienne. Espèces uniques et conditions difficiles.",
                unlockLevel = 30
            ),

            // ==========================================
            // NIVEAUX 34+
            // ==========================================
            Lake(
                id = "elk_lake",
                name = "Elk Lake",
                type = LakeType.LAKE,
                difficulty = Difficulty.BEGINNER,
                availableFish = getFishByNames(
                    listOf(
                        "Touladi (Truite grise)",
                        "Splake",
                        "Grand brochet",
                        "Brochet",
                        "Sandre",
                        "Perche",
                        "Lotte",
                        "Corégone de lac",
                        "Corégone"
                    )
                ),
                description = "Un lac profond et froid situé dans un parc national canadien. Réputé pour ses salmonidés trophées.",
                unlockLevel = 1,
                coordinates = mapOf(
                    "60:110" to "Le 'deep hole', touladis.",
                    "85:50" to "Tombant rocheux, brochets.",
                    "45:60" to "Baie avec herbiers, sandres.",
                    "110:80" to "Plateau submergé."
                )
            )
        )
    }
}