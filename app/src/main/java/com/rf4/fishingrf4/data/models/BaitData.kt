package com.rf4.fishingrf4.data.models

// BaitData.kt - Base de données complète des appâts Russian Fishing 4

data class BaitInfo(
    val name: String,                    // Nom français
    val englishName: String,             // Nom anglais
    val category: String,                // Catégorie d'appât
    val description: String,             // Description détaillée
    val effectiveness: String,           // Efficacité et conditions
    val targetFish: List<String>,        // Poissons cibles
    val acquisition: String,             // Méthode d'obtention
    val tips: String,                    // Conseils d'utilisation
    val price: String = "",              // Prix si disponible
    val level: String = "",              // Niveau requis si applicable
    val timeOfDay: String = "",          // Moment optimal (jour/nuit/crépuscule)
    val waterType: String = "",          // Type d'eau (douce/mer/saumâtre)
    val depth: String = "",              // Profondeur optimale
    val hookSize: String = ""            // Taille d'hameçon recommandée
)

object BaitDatabase {

    val allBaits = listOf(

        // ============= VERS ET LARVES =============
        BaitInfo(
            name = "Ver de terre",
            englishName = "Worm",
            category = "Appâts naturels - Vers",
            description = "L'appât de base par excellence. Universel et économique, il peut être acheté ou obtenu gratuitement en creusant avec une pelle.",
            effectiveness = "Particulièrement efficace la nuit (20:00-6:00) pour attraper une grande variété de poissons de petite et moyenne taille. Excellent taux de réussite.",
            targetFish = listOf("Gardon", "Ablette", "Carassin", "Brème", "Perche", "Chevesne", "Ide"),
            acquisition = "Achat (1,80 argent) ou creusage gratuit avec Fiskarna Shovel (38 argent)",
            tips = "Appât universel parfait pour débuter. Disponible gratuitement chaque jour au bâtiment administratif.",
            price = "1,80 argent",
            level = "Niveau 0",
            timeOfDay = "Nuit (20h-6h) optimal",
            waterType = "Eau douce",
            depth = "80-100cm",
            hookSize = "S8-S12"
        ),

        BaitInfo(
            name = "Ver rouge",
            englishName = "Redworm",
            category = "Appâts naturels - Vers",
            description = "Version premium du ver de terre. Ver segmenté rouge vif, plus attractif que le ver basique.",
            effectiveness = "Supérieure au ver basique avec un excellent taux de touches. Attire des spécimens plus gros.",
            targetFish = listOf("Gardon", "Brème", "Carassin", "Perche", "Chevesne", "Carpe"),
            acquisition = "Creusage avec pelle ou achat en magasins",
            tips = "Plus cher que le ver basique mais rentable pour de meilleurs résultats",
            price = "10,60 argent",
            level = "Niveau 5+",
            timeOfDay = "Nuit optimal",
            waterType = "Eau douce",
            depth = "60-120cm",
            hookSize = "S8-S15"
        ),

        BaitInfo(
            name = "Ver de nuit",
            englishName = "Nightcrawler",
            category = "Appâts naturels - Vers",
            description = "Gros ver de terre épais, ver premium excellent pour cibler les poissons trophées et prédateurs.",
            effectiveness = "Excellente pour cibler les poissons trophées. Filtre les petites prises et attire les beaux spécimens.",
            targetFish = listOf("Brème", "Tanche", "Carpe", "Carassin", "Brochet", "Silure", "Sandre"),
            acquisition = "Achat (17,80 argent) ou creusage avec compétence élevée",
            tips = "Appât premium pour pêcheurs sérieux visant les gros poissons. Pêche de jour à 60-70cm avec hameçons S12.",
            price = "17,80 argent",
            level = "Compétence récolte 65%+",
            timeOfDay = "Jour optimal",
            waterType = "Eau douce",
            depth = "60-70cm",
            hookSize = "S12"
        ),

        BaitInfo(
            name = "Ver de vase",
            englishName = "Bloodworm",
            category = "Appâts naturels - Larves",
            description = "Petites larves de chironomes rouge vif avec têtes noires. L'appât le plus rentable pour gagner de l'expérience.",
            effectiveness = "Extrêmement efficace pour la pêche de jour (6h-20h). Le meilleur appât pour l'expérience et les petits poissons.",
            targetFish = listOf("Gardon", "Ablette", "Carassin", "Perche", "Brème", "Vendace"),
            acquisition = "Récolte avec Fiskarna Scoop (289 argent) ou achat",
            tips = "Idéal pour l'expérience et les petits poissons. Compétence 45%+ requise pour récolte efficace.",
            price = "Variable",
            level = "Compétence 45%+",
            timeOfDay = "Jour (6h-20h)",
            waterType = "Eau douce",
            depth = "18-30cm",
            hookSize = "S2"
        ),

        BaitInfo(
            name = "Casticot",
            englishName = "Caster",
            category = "Appâts naturels - Larves",
            description = "Larve de mouche, très attractive pour les cyprinidés. Excellente alternative au ver de vase.",
            effectiveness = "Une excellente alternative au ver de vase, particulièrement efficace pour la brème et les cyprinidés.",
            targetFish = listOf("Brème", "Gardon", "Ide", "Chevesne", "Carassin"),
            acquisition = "Achat en magasins spécialisés",
            tips = "Très efficace sur les cyprinidés. Parfait pour les montages feeder.",
            price = "Variable selon localisation",
            level = "Niveau 10+",
            timeOfDay = "Jour et crépuscule",
            waterType = "Eau douce",
            depth = "30-80cm",
            hookSize = "S6-S10"
        ),

        BaitInfo(
            name = "Asticots",
            englishName = "Maggots",
            category = "Appâts naturels - Larves",
            description = "Larves polyvalentes, excellentes pour l'amorçage et comme appât principal.",
            effectiveness = "Très efficaces pour attirer les cyprinidés. Excellent composant d'amorce.",
            targetFish = listOf("Brème", "Gardon", "Ablette", "Carassin", "Perche"),
            acquisition = "Achat dans les magasins d'appâts",
            tips = "Parfait en mélange avec l'amorce brème. Usage polyvalent.",
            price = "Bon marché",
            level = "Niveau 5+",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "20-60cm",
            hookSize = "S4-S8"
        ),

        // ============= APPÂTS VIVANTS =============
        BaitInfo(
            name = "Vif",
            englishName = "Baitfish",
            category = "Appâts vivants",
            description = "Petit poisson utilisé comme appât vivant. Incontournable pour les gros prédateurs.",
            effectiveness = "Nécessite un montage spécifique. Un vif légèrement avarié est parfois meilleur. Excellente efficacité sur les prédateurs.",
            targetFish = listOf("Brochet", "Silure", "Lotte", "Sandre", "Perche", "Loup de mer"),
            acquisition = "Pêche de petits poissons (Goujon, Ablette, Gardon) ou achat",
            tips = "Incontournable pour les gros prédateurs. Montage spécial requis avec bas de ligne renforcé.",
            price = "Variable selon espèce",
            level = "Niveau 15+",
            timeOfDay = "Tôt matin et soirée",
            waterType = "Eau douce et mer",
            depth = "Variable selon prédateur",
            hookSize = "S1/0-S5/0"
        ),

        BaitInfo(
            name = "Grenouille",
            englishName = "Frog",
            category = "Appâts vivants",
            description = "Appât spécifique pour certains prédateurs. Se pêche avec technique particulière.",
            effectiveness = "Très efficace sur les gros prédateurs en surface et mi-eau. Technique spécialisée requise.",
            targetFish = listOf("Brochet", "Silure", "Sandre", "Perche"),
            acquisition = "Pêche près des nénuphars avec petit hameçon (taille 20) et mouche",
            tips = "Technique de pêche particulière requise. Pêche près des nénuphars indispensable.",
            price = "Gratuit (pêche)",
            level = "Compétence spécialisée",
            timeOfDay = "Crépuscule et nuit",
            waterType = "Eau douce",
            depth = "Surface et mi-eau",
            hookSize = "S20 pour capture"
        ),

        BaitInfo(
            name = "Écrevisse",
            englishName = "Crayfish",
            category = "Appâts vivants",
            description = "Crustacé d'eau douce excellent pour les gros prédateurs de fond.",
            effectiveness = "Exceptionnelle pour les gros prédateurs de fond. Appât premium très attractif.",
            targetFish = listOf("Silure", "Lotte", "Brochet", "Sandre", "Carpe"),
            acquisition = "Pêche avec petit hameçon ou achat spécialisé",
            tips = "Excellent pour pêche de fond. Montage avec plombée importante recommandé.",
            price = "Cher mais efficace",
            level = "Niveau 20+",
            timeOfDay = "Nuit optimal",
            waterType = "Eau douce",
            depth = "Fond (2m+)",
            hookSize = "S1/0-S3/0"
        ),

        // ============= PÂTES ET APPÂTS FABRIQUÉS =============
        BaitInfo(
            name = "Boulette de pain",
            englishName = "Bread",
            category = "Appâts fabriqués",
            description = "Le premier appât que l'on apprend à fabriquer. Base de la progression en fabrication d'appâts.",
            effectiveness = "Excellent moyen de faire progresser la compétence au tout début. Efficace sur les cyprinidés.",
            targetFish = listOf("Carpe", "Gardon", "Brème", "Carassin", "Ide"),
            acquisition = "Fabrication (pain + eau) - Compétence 0-20%",
            tips = "Parfait pour débuter la fabrication d'appâts. Très bon marché à produire en masse.",
            price = "Très bon marché",
            level = "Compétence 0-20%",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "30-100cm",
            hookSize = "S8-S12"
        ),

        BaitInfo(
            name = "Pâte à l'ail",
            englishName = "Garlic Paste",
            category = "Appâts fabriqués",
            description = "Pâte parfumée plus attractive que la pâte de base. Progression intermédiaire en fabrication.",
            effectiveness = "Efficacité améliorée par rapport aux pâtes standard. Parfum attractif pour cyprinidés.",
            targetFish = listOf("Carpe", "Brème", "Tanche", "Carassin", "Gardon"),
            acquisition = "Fabrication - Compétence 35-50%",
            tips = "Bon compromis prix/efficacité pour progression compétence.",
            price = "Économique",
            level = "Compétence 35-50%",
            timeOfDay = "Jour et crépuscule",
            waterType = "Eau douce",
            depth = "40-120cm",
            hookSize = "S10-S15"
        ),

        BaitInfo(
            name = "Pâte aux œufs",
            englishName = "Egg Paste",
            category = "Appâts fabriqués",
            description = "Pâte avancée très populaire pour craft en masse. Ingrédients bon marché pour progression rapide.",
            effectiveness = "Très efficace sur cyprinidés. Parfaite pour progression skill 50-100%. Populaire pour craft masse.",
            targetFish = listOf("Carpe", "Brème", "Tanche", "Carassin", "Chevesne"),
            acquisition = "Fabrication - Compétence 50-100%",
            tips = "Nécessite milliers de pièces pour progression significative. Ingrédients très bon marché.",
            price = "Bon marché en masse",
            level = "Compétence 50-100%",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "50-150cm",
            hookSize = "S12-S18"
        ),

        BaitInfo(
            name = "Cube de pomme de terre",
            englishName = "Potato Cubes",
            category = "Appâts fabriqués",
            description = "Un des meilleurs appâts pour la carpe. Nécessite voyage au Ruisselet qui Serpente.",
            effectiveness = "Extrêmement efficace sur les carpes de toutes tailles. Appât spécialisé de premier choix.",
            targetFish = listOf("Carpe", "Carpe miroir", "Carpe cuir", "Carpe koï"),
            acquisition = "Fabrication (pommes de terre du marché fermier au Ruisselet qui Serpente)",
            tips = "Voyage au Ruisselet qui Serpente nécessaire pour acheter les pommes de terre.",
            price = "Cher (voyage requis)",
            level = "Accès Ruisselet",
            timeOfDay = "Crépuscule et nuit",
            waterType = "Eau douce",
            depth = "100-200cm",
            hookSize = "S15-S1/0"
        ),

        BaitInfo(
            name = "Orge perlé",
            englishName = "Pearl Barley",
            category = "Appâts fabriqués",
            description = "Très efficace sur la brème et autres cyprinidés. Ne pas confondre avec l'additif pour amorce.",
            effectiveness = "Excellente efficacité sur brème et cyprinidés. Composant essentiel des amorces brème.",
            targetFish = listOf("Brème", "Gardon", "Carassin", "Ide", "Chevesne"),
            acquisition = "Fabrication (orge perlé de l'épicerie) - Compétence 20-30%",
            tips = "Attention à ne pas acheter l'additif pour amorce. 50 portions recommandées pour mélange brème.",
            price = "Économique",
            level = "Compétence 20-30%",
            timeOfDay = "Jour optimal",
            waterType = "Eau douce",
            depth = "80-150cm",
            hookSize = "S8-S12"
        ),

        // ============= GRAINES ET CÉRÉALES =============
        BaitInfo(
            name = "Maïs",
            englishName = "Corn",
            category = "Graines et céréales",
            description = "Graine polyvalente, très attractive pour les cyprinidés de moyenne et grande taille.",
            effectiveness = "Excellente pour carpes et cyprinidés. Très visible et attractif.",
            targetFish = listOf("Carpe", "Brème", "Tanche", "Ide", "Chevesne", "Carassin"),
            acquisition = "Achat magasins ou fabrication",
            tips = "Très polyvalent, excellent pour amorçage également.",
            price = "Bon marché",
            level = "Niveau 10+",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "60-180cm",
            hookSize = "S10-S15"
        ),

        BaitInfo(
            name = "Blé",
            englishName = "Wheat",
            category = "Graines et céréales",
            description = "Céréale classique pour cyprinidés, excellent rapport qualité-prix.",
            effectiveness = "Bonne efficacité générale sur cyprinidés. Économique pour longues sessions.",
            targetFish = listOf("Gardon", "Brème", "Carassin", "Ide", "Chevesne"),
            acquisition = "Achat ou fabrication",
            tips = "Excellent pour débuter avec les graines. Très économique.",
            price = "Très bon marché",
            level = "Niveau 8+",
            timeOfDay = "Jour optimal",
            waterType = "Eau douce",
            depth = "40-120cm",
            hookSize = "S8-S12"
        ),

        // ============= APPÂTS MARINS =============
        BaitInfo(
            name = "Calmar",
            englishName = "Squid",
            category = "Appâts marins",
            description = "Appât marin premium pour gros prédateurs. Versions colorées disponibles.",
            effectiveness = "Exceptionnelle pour thons rouge Atlantique, Alfonsinos et autres prédateurs marins.",
            targetFish = listOf("Thon rouge", "Alfonsin", "Loup de mer", "Lieu", "Morue"),
            acquisition = "Achat pièces d'or ou craft spécialisé",
            tips = "Appât premium pour pêche haute mer. Plusieurs variantes colorées disponibles.",
            price = "Cher (pièces d'or)",
            level = "Accès mer",
            timeOfDay = "Variable selon espèce",
            waterType = "Mer",
            depth = "Variable (souvent profond)",
            hookSize = "S3/0-S8/0"
        ),

        BaitInfo(
            name = "Anchois",
            englishName = "Anchovy",
            category = "Appâts marins",
            description = "Petit poisson marin excellent pour prédateurs de taille moyenne.",
            effectiveness = "Très efficace pour prédateurs marins de taille moyenne. Appât naturel attractif.",
            targetFish = listOf("Loup de mer", "Lieu", "Morue", "Maquereau", "Chinchard"),
            acquisition = "Pêche ou achat",
            tips = "Excellent appât naturel marin. Peut être utilisé entier ou en morceaux.",
            price = "Modéré",
            level = "Accès mer",
            timeOfDay = "Tôt matin et soirée",
            waterType = "Mer",
            depth = "10-40m",
            hookSize = "S1/0-S4/0"
        ),

        BaitInfo(
            name = "Sardine",
            englishName = "Sardine",
            category = "Appâts marins",
            description = "Poisson marin gras, très attractif pour les gros prédateurs.",
            effectiveness = "Excellente pour gros prédateurs marins. Huiles naturelles très attractives.",
            targetFish = listOf("Thon", "Loup de mer", "Lieu", "Congre", "Requins"),
            acquisition = "Pêche ou achat spécialisé",
            tips = "Appât gras très attractif. Peut être utilisé frais ou légèrement avarié.",
            price = "Modéré à cher",
            level = "Accès mer",
            timeOfDay = "Crépuscule et nuit",
            waterType = "Mer",
            depth = "15-60m",
            hookSize = "S2/0-S6/0"
        ),

        // ============= LEURRES ARTIFICIELS =============
        BaitInfo(
            name = "Cuiller tournante",
            englishName = "Spinner",
            category = "Leurres - Cuillers",
            description = "Leurre rotatif classique pour prédateurs. Disponible en diverses tailles et couleurs.",
            effectiveness = "Très efficace en récupération constante. Vitesse 18-23 pour cuillères optimale.",
            targetFish = listOf("Brochet", "Perche", "Sandre", "Truite", "Saumon"),
            acquisition = "Achat magasins spécialisés",
            tips = "Technique stop and go recommandée. 4-5 révolutions, pause 1-2 secondes.",
            price = "Variable selon modèle",
            level = "Niveau 20+",
            timeOfDay = "Jour optimal",
            waterType = "Eau douce et mer",
            depth = "Surface à mi-eau",
            hookSize = "Intégré"
        ),

        BaitInfo(
            name = "Poisson nageur",
            englishName = "Crankbait",
            category = "Leurres - Poissons nageurs",
            description = "Leurre imitant un petit poisson. Excellente action de nage.",
            effectiveness = "Très efficace en récupération variée. Imite parfaitement un poisson blessé.",
            targetFish = listOf("Brochet", "Sandre", "Perche", "Chevesne", "Loup de mer"),
            acquisition = "Achat magasins spécialisés",
            tips = "Varier les vitesses de récupération. Excellente en bordure et obstacles.",
            price = "Modéré à cher",
            level = "Niveau 25+",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce et mer",
            depth = "Variable selon modèle",
            hookSize = "Triples intégrés"
        ),

        BaitInfo(
            name = "Jig",
            englishName = "Jig",
            category = "Leurres - Jigs",
            description = "Leurre plombé pour pêche verticale et animation saccadée.",
            effectiveness = "Excellent pour pêche verticale. Technique jig step très efficace.",
            targetFish = listOf("Sandre", "Perche", "Brochet", "Lieu", "Morue"),
            acquisition = "Achat magasins spécialisés",
            tips = "Technique lever et laisser tomber. Animation saccadée indispensable.",
            price = "Variable selon poids",
            level = "Niveau 30+",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce et mer",
            depth = "Fond et mi-eau",
            hookSize = "Simple intégré"
        ),

        // ============= APPÂTS SPÉCIALISÉS =============
        BaitInfo(
            name = "Bouillette",
            englishName = "Boilie",
            category = "Appâts spécialisés",
            description = "Appât sphérique dur spécialement conçu pour les carpes. Disponible en nombreux parfums.",
            effectiveness = "Extrêmement efficace pour carpes. Sélectif, évite les petits poissons indésirables.",
            targetFish = listOf("Carpe", "Carpe miroir", "Carpe cuir", "Amour blanc", "Brème"),
            acquisition = "Achat spécialisé ou fabrication avancée",
            tips = "Appât sélectif premium. Trempage préalable souvent bénéfique.",
            price = "Cher",
            level = "Niveau 35+",
            timeOfDay = "Nuit et crépuscule",
            waterType = "Eau douce",
            depth = "100-250cm",
            hookSize = "S15-S3/0"
        ),

        BaitInfo(
            name = "Pellets",
            englishName = "Pellets",
            category = "Appâts spécialisés",
            description = "Granulés industriels très nutritifs. Excellent pour amorçage et comme appât.",
            effectiveness = "Très efficaces pour cyprinidés. Excellent pour amorçage de précision.",
            targetFish = listOf("Carpe", "Brème", "Tanche", "Amour blanc", "Carassin"),
            acquisition = "Achat magasins spécialisés",
            tips = "Parfait pour sacs PVA. Combinaison amorçage/appât très efficace.",
            price = "Modéré",
            level = "Niveau 25+",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "80-200cm",
            hookSize = "S12-S1/0"
        ),

        BaitInfo(
            name = "Viande de moule",
            englishName = "Mussel Meat",
            category = "Appâts spécialisés",
            description = "Chair de mollusque premium. Nécessite Fiskarna Professional Fillet Knife pour préparation.",
            effectiveness = "Excellente pour prédateurs et cyprinidés. Appât naturel très attractif.",
            targetFish = listOf("Tanche", "Brème", "Carpe", "Lotte", "Silure"),
            acquisition = "Craft avec Fiskarna Professional Fillet Knife (5430,67 argent)",
            tips = "Appât premium nécessitant outil spécialisé. Très efficace en eaux froides.",
            price = "Cher (outil requis)",
            level = "Outil spécialisé",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "100-300cm",
            hookSize = "S15-S2/0"
        ),

        // ============= APPÂTS EXOTIQUES =============
        BaitInfo(
            name = "Larve de scarabée rhinocéros",
            englishName = "Rhinoceros Beetle Larva",
            category = "Appâts exotiques",
            description = "Larve rare et très efficace. Récolte avec compétence très élevée uniquement.",
            effectiveness = "Efficacité exceptionnelle 80-120%. Appât rare très recherché.",
            targetFish = listOf("Gros cyprinidés", "Carpe trophée", "Tanche", "Brème géante"),
            acquisition = "Récolte skill 70%+ dans certaines localisations",
            tips = "Appât rare à utiliser avec parcimonie. Réservé aux sessions trophées.",
            price = "Très cher/rare",
            level = "Compétence 70%+",
            timeOfDay = "Crépuscule et nuit",
            waterType = "Eau douce",
            depth = "150-300cm",
            hookSize = "S1/0-S3/0"
        ),

        BaitInfo(
            name = "Sangsue",
            englishName = "Leech",
            category = "Appâts exotiques",
            description = "Appât naturel ondulant très attractif pour prédateurs.",
            effectiveness = "Mouvement naturel très attractif. Excellente pour prédateurs difficiles.",
            targetFish = listOf("Perche", "Sandre", "Brochet", "Lotte", "Silure"),
            acquisition = "Récolte spécialisée ou achat rare",
            tips = "Animation lente recommandée. Appât vivant très efficace.",
            price = "Cher",
            level = "Compétence spécialisée",
            timeOfDay = "Crépuscule et nuit",
            waterType = "Eau douce",
            depth = "Variable",
            hookSize = "S8-S15"
        )
    )

    // Fonction pour rechercher par nom d'appât
    fun searchByName(query: String): List<BaitInfo> {
        return allBaits.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.englishName.contains(query, ignoreCase = true)
        }
    }

    // Fonction pour rechercher par poisson cible
    fun searchByFish(fishName: String): List<BaitInfo> {
        return allBaits.filter { bait ->
            bait.targetFish.any { it.contains(fishName, ignoreCase = true) }
        }
    }

    // Fonction pour rechercher par catégorie
    fun searchByCategory(category: String): List<BaitInfo> {
        return allBaits.filter { it.category.contains(category, ignoreCase = true) }
    }

    // Fonction pour obtenir toutes les catégories
    fun getAllCategories(): List<String> {
        return allBaits.map { it.category }.distinct().sorted()
    }

    // Fonction pour obtenir tous les poissons cibles
    fun getAllTargetFish(): List<String> {
        return allBaits.flatMap { it.targetFish }.distinct().sorted()
    }

    // Fonction de recherche avancée
    fun advancedSearch(
        nameQuery: String = "",
        fishQuery: String = "",
        categoryQuery: String = "",
        timeOfDay: String = "",
        waterType: String = ""
    ): List<BaitInfo> {
        return allBaits.filter { bait ->
            (nameQuery.isEmpty() || bait.name.contains(nameQuery, ignoreCase = true) ||
                    bait.englishName.contains(nameQuery, ignoreCase = true)) &&
                    (fishQuery.isEmpty() || bait.targetFish.any { it.contains(fishQuery, ignoreCase = true) }) &&
                    (categoryQuery.isEmpty() || bait.category.contains(categoryQuery, ignoreCase = true)) &&
                    (timeOfDay.isEmpty() || bait.timeOfDay.contains(timeOfDay, ignoreCase = true)) &&
                    (waterType.isEmpty() || bait.waterType.contains(waterType, ignoreCase = true))
        }
    }
}