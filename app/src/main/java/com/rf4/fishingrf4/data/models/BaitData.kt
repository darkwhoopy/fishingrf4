package com.rf4.fishingrf4.data.models

// BaitData.kt - Base de données complète des appâts Russian Fishing 4
import android.content.Context
import com.rf4.fishingrf4.utils.LanguageManager

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
        // APPÂTS MANQUANTS À AJOUTER dans BaitDatabase.allBaits

        // ============= VERS ET LARVES MANQUANTS =============
        BaitInfo(
            name = "Lombric",
            englishName = "Earthworm Large",
            category = "Appâts naturels - Vers",
            description = "Gros ver de terre épais, plus grand que le ver classique. Excellent pour filtrer les petites prises.",
            effectiveness = "Très efficace pour cibler les poissons de taille moyenne à grande. Filtre les petites espèces indésirables.",
            targetFish = listOf("Brème", "Carpe", "Tanche", "Carassin", "Chevesne"),
            acquisition = "Creusage avec pelle ou achat",
            tips = "Alternative au ver de nuit pour cibler les gros poissons sans payer le prix premium.",
            price = "Économique",
            level = "Niveau 8+",
            timeOfDay = "Nuit optimal",
            waterType = "Eau douce",
            depth = "70-120cm",
            hookSize = "S10-S15"
        ),

        BaitInfo(
            name = "Chrysalides",
            englishName = "Chrysalis",
            category = "Appâts naturels - Larves",
            description = "Cocons d'insectes en transformation. Appât naturel prisé par les poissons insectivores.",
            effectiveness = "Excellente pour perches et poissons insectivores. Appât saisonnier très efficace.",
            targetFish = listOf("Perche", "Gardon", "Chevesne", "Ide", "Truite"),
            acquisition = "Récolte spécialisée ou achat",
            tips = "Particulièrement efficace au printemps et en été. Imite parfaitement la nourriture naturelle.",
            price = "Modéré",
            level = "Niveau 15+",
            timeOfDay = "Matin et soir",
            waterType = "Eau douce",
            depth = "30-80cm",
            hookSize = "S6-S10"
        ),

        BaitInfo(
            name = "Ver haché",
            englishName = "Chopped Worm",
            category = "Appâts naturels - Vers",
            description = "Morceaux de vers préparés. Excellent pour amorçage et pêche fine.",
            effectiveness = "Très efficace en complément d'amorce. Libère des attractifs naturels dans l'eau.",
            targetFish = listOf("Gardon", "Ablette", "Brème", "Carassin"),
            acquisition = "Préparation maison ou achat",
            tips = "Parfait pour mélanger à l'amorce. Créé un nuage attractif.",
            price = "Bon marché",
            level = "Niveau 5+",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "20-60cm",
            hookSize = "S4-S8"
        ),

        BaitInfo(
            name = "Larve de scolyte",
            englishName = "Bark Beetle Larvae",
            category = "Appâts naturels - Larves",
            description = "Larve trouvée sous l'écorce des arbres. Appât naturel excellent pour les poissons de rivière.",
            effectiveness = "Très efficace sur les poissons sauvages habitués à cette nourriture naturelle.",
            targetFish = listOf("Chevesne", "Ide", "Perche", "Truite", "Ombre"),
            acquisition = "Récolte sous écorces ou achat spécialisé",
            tips = "Appât premium pour pêche naturelle. Irrésistible pour les poissons sauvages.",
            price = "Cher",
            level = "Compétence spécialisée",
            timeOfDay = "Jour optimal",
            waterType = "Eau douce courante",
            depth = "50-150cm",
            hookSize = "S8-S12"
        ),

        BaitInfo(
            name = "Larve d'éphémère",
            englishName = "Mayfly Larvae",
            category = "Appâts naturels - Larves",
            description = "Larve aquatique d'éphémère. Nourriture naturelle premium des truites et ombres.",
            effectiveness = "Exceptionnelle pour truites et salmonidés. Imite parfaitement la nourriture naturelle.",
            targetFish = listOf("Truite", "Ombre", "Saumon", "Perche", "Chevesne"),
            acquisition = "Récolte en rivière ou achat premium",
            tips = "Appât de choix pour la pêche à la truite en rivière. Très fragile mais très efficace.",
            price = "Très cher",
            level = "Compétence aquatique 60%+",
            timeOfDay = "Matin et crépuscule",
            waterType = "Eau courante froide",
            depth = "30-100cm",
            hookSize = "S6-S12"
        ),

        BaitInfo(
            name = "Larve de plécoptère",
            englishName = "Stonefly Larvae",
            category = "Appâts naturels - Larves",
            description = "Larve de perle trouvée sous les pierres des rivières froides. Appât premium pour salmonidés.",
            effectiveness = "Excellente pour pêche en eau froide. Appât naturel très prisé des truites.",
            targetFish = listOf("Truite", "Saumon", "Ombre", "Perche"),
            acquisition = "Récolte sous pierres rivières ou achat rare",
            tips = "Réservé aux eaux froides et oxygénées. Appât fragile mais très efficace.",
            price = "Très cher",
            level = "Compétence aquatique 70%+",
            timeOfDay = "Tôt matin",
            waterType = "Eau froide oxygénée",
            depth = "40-120cm",
            hookSize = "S8-S14"
        ),

        BaitInfo(
            name = "Larve de phrygane",
            englishName = "Caddisfly Larvae",
            category = "Appâts naturels - Larves",
            description = "Larve aquatique construisant un fourreau. Excellent appât pour poissons de rivière.",
            effectiveness = "Très efficace sur les poissons habitués à ce type de nourriture naturelle.",
            targetFish = listOf("Truite", "Perche", "Chevesne", "Ide", "Ombre"),
            acquisition = "Récolte aquatique ou achat spécialisé",
            tips = "Appât naturel excellent. Le fourreau peut être retiré ou conservé selon les conditions.",
            price = "Cher",
            level = "Compétence aquatique 50%+",
            timeOfDay = "Jour et crépuscule",
            waterType = "Eau douce courante",
            depth = "40-100cm",
            hookSize = "S8-S12"
        ),

        // ============= PÂTES MANQUANTES =============
        BaitInfo(
            name = "Pain mouillé",
            englishName = "Wet Bread",
            category = "Appâts fabriqués",
            description = "Pain trempé dans l'eau, premier appât artisanal. Base absolue pour débuter.",
            effectiveness = "Efficacité basique mais suffisante pour débuter. Très économique pour l'apprentissage.",
            targetFish = listOf("Gardon", "Carassin", "Carpe", "Brème"),
            acquisition = "Fabrication maison (pain + eau)",
            tips = "Le plus simple des appâts fabriqués. Idéal pour comprendre les bases de la fabrication.",
            price = "Très bon marché",
            level = "Niveau 0",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "30-80cm",
            hookSize = "S10-S15"
        ),

        BaitInfo(
            name = "Pâte sucrée",
            englishName = "Sweet Paste",
            category = "Appâts fabriqués",
            description = "Pâte additionnée de sucre. Attractive pour les cyprinidés gourmands.",
            effectiveness = "Bonne efficacité sur carpes et gros cyprinidés. Le sucre attire de loin.",
            targetFish = listOf("Carpe", "Brème", "Tanche", "Carassin"),
            acquisition = "Fabrication avec sucre ajouté",
            tips = "Excellente en eau froide où les poissons cherchent des calories. Attention aux écrevisses.",
            price = "Économique",
            level = "Compétence 25-40%",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "60-150cm",
            hookSize = "S12-S18"
        ),

        BaitInfo(
            name = "Pâte au miel",
            englishName = "Honey Paste",
            category = "Appâts fabriqués",
            description = "Pâte parfumée au miel. Très attractive pour les gros cyprinidés.",
            effectiveness = "Excellente efficacité grâce au parfum sucré naturel. Attractive de loin.",
            targetFish = listOf("Carpe", "Tanche", "Brème", "Amour blanc"),
            acquisition = "Fabrication avec miel premium",
            tips = "Appât haut de gamme pour sessions importantes. Le miel attire mais attire aussi les petites espèces.",
            price = "Cher",
            level = "Compétence 45-65%",
            timeOfDay = "Crépuscule et nuit",
            waterType = "Eau douce",
            depth = "80-200cm",
            hookSize = "S15-S1/0"
        ),

        BaitInfo(
            name = "Pâte au fromage blanc",
            englishName = "Cottage Cheese Paste",
            category = "Appâts fabriqués",
            description = "Pâte riche en protéines à base de fromage blanc. Très nutritive et attractive.",
            effectiveness = "Excellente pour gros poissons. Haute valeur nutritive et goût attractif.",
            targetFish = listOf("Carpe", "Amour blanc", "Tanche", "Brème géante"),
            acquisition = "Fabrication avec fromage blanc frais",
            tips = "Appât riche idéal pour l'hiver. Se conserve mal par forte chaleur.",
            price = "Modéré à cher",
            level = "Compétence 40-60%",
            timeOfDay = "Nuit et tôt matin",
            waterType = "Eau douce",
            depth = "100-250cm",
            hookSize = "S15-S2/0"
        ),

        // ============= GRAINES ET CÉRÉALES MANQUANTES =============
        BaitInfo(
            name = "Semoule",
            englishName = "Semolina",
            category = "Graines et céréales",
            description = "Semoule de blé fine. Excellent pour progression compétence et pêche fine.",
            effectiveness = "Bonne efficacité pour progression compétence. Économique et polyvalente.",
            targetFish = listOf("Gardon", "Brème", "Carassin", "Ide"),
            acquisition = "Fabrication à partir semoule brute",
            tips = "Progression 30-35% idéale. Peut être mélangée à d'autres appâts.",
            price = "Bon marché",
            level = "Compétence 30-35%",
            timeOfDay = "Jour optimal",
            waterType = "Eau douce",
            depth = "40-100cm",
            hookSize = "S8-S12"
        ),

        BaitInfo(
            name = "Flocons d'avoine",
            englishName = "Oat Flakes",
            category = "Graines et céréales",
            description = "Flocons d'avoine nutritifs. Excellent pour l'amorçage et comme appât de fond.",
            effectiveness = "Très bon pour amorçage. Reste longtemps sur le fond et attire durablement.",
            targetFish = listOf("Carpe", "Brème", "Tanche", "Carassin", "Amour blanc"),
            acquisition = "Achat épicerie ou fabrication",
            tips = "Excellent composant d'amorce. Gonfle dans l'eau et crée un tapis nourricier.",
            price = "Bon marché",
            level = "Niveau 12+",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "80-180cm",
            hookSize = "S10-S15"
        ),

        BaitInfo(
            name = "Grains de blé",
            englishName = "Wheat Grains",
            category = "Graines et céréales",
            description = "Grains de blé entiers préparés. Classique pour cyprinidés, résistant aux écrevisses.",
            effectiveness = "Excellente tenue à l'hameçon. Résiste bien aux écrevisses et petits poissons.",
            targetFish = listOf("Carpe", "Brème", "Tanche", "Ide", "Chevesne"),
            acquisition = "Préparation grains entiers ou achat",
            tips = "Nécessite trempage et cuisson. Très résistant, parfait en zone à écrevisses.",
            price = "Économique",
            level = "Niveau 15+",
            timeOfDay = "Nuit et tôt matin",
            waterType = "Eau douce",
            depth = "60-150cm",
            hookSize = "S10-S15"
        ),

        BaitInfo(
            name = "Tournesol",
            englishName = "Sunflower Seeds",
            category = "Graines et céréales",
            description = "Graines de tournesol décortiquées. Riches en huile, très attractives.",
            effectiveness = "Très attractives grâce aux huiles naturelles. Excellent pour carpes et gros poissons.",
            targetFish = listOf("Carpe", "Amour blanc", "Brème", "Tanche"),
            acquisition = "Préparation graines décortiquées ou achat",
            tips = "Les huiles se diffusent bien dans l'eau. Attention à bien décortiquer.",
            price = "Modéré",
            level = "Niveau 18+",
            timeOfDay = "Crépuscule et nuit",
            waterType = "Eau douce",
            depth = "100-200cm",
            hookSize = "S12-S18"
        ),

        BaitInfo(
            name = "Graines de lin",
            englishName = "Flax Seeds",
            category = "Graines et céréales",
            description = "Petites graines riches en huile. Premium pour cyprinidés difficiles.",
            effectiveness = "Très efficace sur poissons méfiants. Taille réduite mais très attractive.",
            targetFish = listOf("Carpe", "Tanche", "Brème", "Ide"),
            acquisition = "Achat spécialisé ou préparation",
            tips = "Appât sélectif pour gros poissons méfiants. Libère des huiles attractives.",
            price = "Cher",
            level = "Niveau 25+",
            timeOfDay = "Nuit optimal",
            waterType = "Eau douce",
            depth = "120-250cm",
            hookSize = "S10-S15"
        ),

        BaitInfo(
            name = "Chènevis",
            englishName = "Hemp Seeds",
            category = "Graines et céréales",
            description = "Graines de chanvre. Appât traditionnel très prisé des carpes et gros cyprinidés.",
            effectiveness = "Efficacité légendaire sur carpes. Appât traditionnel incontournable.",
            targetFish = listOf("Carpe", "Brème", "Tanche", "Barbeau", "Amour blanc"),
            acquisition = "Achat spécialisé pêche ou préparation",
            tips = "Nécessite préparation spéciale. Un des meilleurs appâts pour carpes difficiles.",
            price = "Cher",
            level = "Niveau 30+",
            timeOfDay = "Nuit et très tôt matin",
            waterType = "Eau douce",
            depth = "150-300cm",
            hookSize = "S12-S1/0"
        ),

        // ============= BOUILLIES MANQUANTES =============
        BaitInfo(
            name = "Bouillie de pois",
            englishName = "Pea Porridge",
            category = "Appâts fabriqués",
            description = "Bouillie épaisse à base de pois cassés. Nutritive et très attractive.",
            effectiveness = "Excellente pour gros poissons gourmands. Très nutritive et rassasiante.",
            targetFish = listOf("Carpe", "Brème géante", "Amour blanc", "Tanche"),
            acquisition = "Cuisson pois cassés avec épices",
            tips = "Bouillie épaisse qui tient bien. Parfaite pour sessions longues en eau froide.",
            price = "Économique",
            level = "Compétence 35-50%",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "80-200cm",
            hookSize = "S15-S1/0"
        ),

        BaitInfo(
            name = "Bouillie de semoule",
            englishName = "Semolina Porridge",
            category = "Appâts fabriqués",
            description = "Bouillie crémeuse à base de semoule. Facile à faire et économique.",
            effectiveness = "Bonne efficacité générale. Excellent rapport qualité-prix pour débuter.",
            targetFish = listOf("Gardon", "Brème", "Carassin", "Ide", "Chevesne"),
            acquisition = "Cuisson semoule avec lait ou eau",
            tips = "Une des premières bouillies à maîtriser. Consistance ajustable selon les besoins.",
            price = "Très bon marché",
            level = "Compétence 20-35%",
            timeOfDay = "Jour optimal",
            waterType = "Eau douce",
            depth = "40-120cm",
            hookSize = "S10-S15"
        ),

        BaitInfo(
            name = "Bouillie de millet",
            englishName = "Millet Porridge",
            category = "Appâts fabriqués",
            description = "Bouillie fine à base de millet. Digestible et attractive pour petits et moyens poissons.",
            effectiveness = "Très bonne pour poissons de taille moyenne. Digestion facile, usage prolongé possible.",
            targetFish = listOf("Gardon", "Brème", "Carassin", "Perche", "Ide"),
            acquisition = "Cuisson graines de millet",
            tips = "Bouillie légère idéale en été. Ne sature pas rapidement les poissons.",
            price = "Bon marché",
            level = "Compétence 25-40%",
            timeOfDay = "Jour et crépuscule",
            waterType = "Eau douce",
            depth = "50-120cm",
            hookSize = "S8-S12"
        ),

        // ============= AUTRES APPÂTS VIVANTS MANQUANTS =============
        BaitInfo(
            name = "Morceaux de poisson",
            englishName = "Fish Pieces",
            category = "Appâts vivants",
            description = "Morceaux de poisson frais. Excellent pour prédateurs et gros poissons de fond.",
            effectiveness = "Très efficace pour prédateurs. Libère des attractifs naturels dans l'eau.",
            targetFish = listOf("Silure", "Lotte", "Brochet", "Sandre", "Anguille"),
            acquisition = "Découpe de poissons pêchés ou achat",
            tips = "Utiliser poissons gras de préférence. Excellent pour pêche de nuit au silure.",
            price = "Gratuit si auto-produit",
            level = "Niveau 20+",
            timeOfDay = "Nuit optimal",
            waterType = "Eau douce",
            depth = "200-400cm",
            hookSize = "S2/0-S8/0"
        ),

        BaitInfo(
            name = "Moule de rivière",
            englishName = "River Mussel",
            category = "Appâts vivants",
            description = "Mollusque d'eau douce. Chair tendre très appréciée des gros poissons de fond.",
            effectiveness = "Excellente pour gros poissons de fond. Chair tendre et parfumée naturellement.",
            targetFish = listOf("Tanche", "Brème géante", "Carpe", "Barbeau", "Lotte"),
            acquisition = "Récolte en rivière ou achat spécialisé",
            tips = "Ouvrir la coquille au dernier moment. Chair très fragile mais très attractive.",
            price = "Modéré",
            level = "Niveau 25+",
            timeOfDay = "Nuit et tôt matin",
            waterType = "Eau douce",
            depth = "150-300cm",
            hookSize = "S15-S2/0"
        ),

        BaitInfo(
            name = "Moule zébrée",
            englishName = "Zebra Mussel",
            category = "Appâts vivants",
            description = "Petite moule invasive. Chair délicate très appréciée, notamment des perches.",
            effectiveness = "Très efficace sur perches et poissons de taille moyenne. Goût très attractif.",
            targetFish = listOf("Perche", "Sandre", "Brème", "Gardon", "Ide"),
            acquisition = "Récolte sur structures immergées ou achat",
            tips = "Taille réduite parfaite pour perches. Coquille coupante, manipulation prudente.",
            price = "Bon marché",
            level = "Niveau 15+",
            timeOfDay = "Jour optimal",
            waterType = "Eau douce",
            depth = "80-180cm",
            hookSize = "S8-S12"
        ),

        // ============= APPÂTS SPÉCIAUX MANQUANTS =============
        BaitInfo(
            name = "Cubes de fromage",
            englishName = "Cheese Cubes",
            category = "Appâts spécialisés",
            description = "Cubes de fromage dur. Appât original très efficace sur cyprinidés gourmands.",
            effectiveness = "Très efficace sur carpes et gros cyprinidés. Goût fort et attractif.",
            targetFish = listOf("Carpe", "Chevesne", "Ide", "Barbeau", "Amour blanc"),
            acquisition = "Découpe fromage dur en cubes",
            tips = "Utiliser fromage dur qui tient à l'hameçon. Odeur forte très attractive.",
            price = "Modéré",
            level = "Niveau 20+",
            timeOfDay = "Nuit et crépuscule",
            waterType = "Eau douce",
            depth = "100-200cm",
            hookSize = "S12-S18"
        ),

        BaitInfo(
            name = "Polenta",
            englishName = "Polenta",
            category = "Appâts fabriqués",
            description = "Bouillie de maïs italienne. Consistance parfaite pour l'eschage, très nutritive.",
            effectiveness = "Excellente tenue à l'hameçon. Très nutritive et attractive pour cyprinidés.",
            targetFish = listOf("Carpe", "Brème", "Tanche", "Carassin", "Amour blanc"),
            acquisition = "Cuisson farine de maïs spécialisée",
            tips = "Consistance idéale pour l'eschage. Se travaille facilement, tient parfaitement.",
            price = "Économique",
            level = "Compétence 40-60%",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce",
            depth = "80-180cm",
            hookSize = "S12-S18"
        ),

        BaitInfo(
            name = "Poisson mort",
            englishName = "Dead Fish",
            category = "Appâts spécialisés",
            description = "Poisson entier mort. Appât naturel premium pour gros prédateurs et silures.",
            effectiveness = "Exceptionnelle pour silures et gros prédateurs. Libère des attractifs puissants.",
            targetFish = listOf("Silure", "Lotte", "Brochet géant", "Anguille"),
            acquisition = "Poissons pêchés conservés ou achat spécialisé",
            tips = "Laisser légèrement avarier pour plus d'efficacité. Réservé aux très gros prédateurs.",
            price = "Variable",
            level = "Niveau 35+",
            timeOfDay = "Nuit exclusive",
            waterType = "Eau douce",
            depth = "300-600cm",
            hookSize = "S5/0-S12/0"
        ),

        BaitInfo(
            name = "Viande de pétoncle",
            englishName = "Scallop Meat",
            category = "Appâts marins",
            description = "Chair de pétoncle premium. Délicatesse marine pour poissons fins.",
            effectiveness = "Excellente pour poissons marins gourmets. Chair ferme et parfumée.",
            targetFish = listOf("Daurade", "Loup de mer", "Lieu", "Turbot", "Sole"),
            acquisition = "Achat poissonnerie spécialisée",
            tips = "Appât premium pour pêche fine marine. Chair ferme qui tient bien à l'hameçon.",
            price = "Très cher",
            level = "Accès mer + niveau 30+",
            timeOfDay = "Marée montante",
            waterType = "Mer",
            depth = "20-50m",
            hookSize = "S1/0-S3/0"
        ),

        BaitInfo(
            name = "Crabe",
            englishName = "Crab",
            category = "Appâts marins",
            description = "Crabe entier ou en morceaux. Appât naturel marin très attractif pour les gros poissons.",
            effectiveness = "Exceptionnelle pour gros poissons marins. Appât naturel très recherché.",
            targetFish = listOf("Loup de mer", "Lieu", "Congre", "Raie", "Turbot"),
            acquisition = "Pêche aux crabes ou achat spécialisé",
            tips = "Utiliser entier pour très gros poissons, en morceaux pour autres espèces.",
            price = "Cher",
            level = "Accès mer + niveau 25+",
            timeOfDay = "Nuit et marée",
            waterType = "Mer",
            depth = "30-80m",
            hookSize = "S2/0-S6/0"
        ),

        BaitInfo(
            name = "Ver marin",
            englishName = "Marine Worm",
            category = "Appâts marins",
            description = "Ver spécialisé pour pêche marine. Très résistant et attractif en milieu salé.",
            effectiveness = "Excellente pour pêche marine généraliste. Résiste bien au milieu salé.",
            targetFish = listOf("Lieu", "Morue", "Merlan", "Plie", "Maquereau"),
            acquisition = "Achat magasins pêche marine ou récolte",
            tips = "Appât de base incontournable pour pêche marine. Très polyvalent.",
            price = "Modéré",
            level = "Accès mer",
            timeOfDay = "Toute la journée",
            waterType = "Mer",
            depth = "10-60m",
            hookSize = "S1/0-S4/0"
        ),

        BaitInfo(
            name = "Crevette",
            englishName = "Shrimp",
            category = "Appâts marins",
            description = "Crevette fraîche ou congelée. Appât marin classique très attractif.",
            effectiveness = "Très efficace pour nombreuses espèces marines. Odeur et goût très attractifs.",
            targetFish = listOf("Loup de mer", "Daurade", "Lieu", "Maquereau", "Congre"),
            acquisition = "Achat poissonnerie ou pêche aux crevettes",
            tips = "Décortiquer ou non selon l'espèce visée. Très polyvalent et efficace.",
            price = "Modéré à cher",
            level = "Accès mer",
            timeOfDay = "Marée et crépuscule",
            waterType = "Mer",
            depth = "15-50m",
            hookSize = "S1/0-S3/0"
        ),

        // ============= LEURRES DURS MANQUANTS =============
        BaitInfo(
            name = "Wobblers",
            englishName = "Wobbler",
            category = "Leurres - Poissons nageurs",
            description = "Leurre dur oscillant. Action de nage hypnotique très efficace sur prédateurs.",
            effectiveness = "Très efficace en récupération lente à moyenne. Action oscillante irrésistible.",
            targetFish = listOf("Brochet", "Sandre", "Perche", "Loup de mer", "Lieu"),
            acquisition = "Achat magasins spécialisés leurres",
            tips = "Varier vitesse récupération. Excellent pour prospecter zones encombrées.",
            price = "Modéré à cher",
            level = "Niveau 25+",
            timeOfDay = "Jour optimal",
            waterType = "Eau douce et mer",
            depth = "Variable selon modèle",
            hookSize = "Triples intégrés"
        ),

        BaitInfo(
            name = "Poppers",
            englishName = "Popper",
            category = "Leurres - Surface",
            description = "Leurre de surface créant des éclaboussures. Technique spectaculaire pour prédateurs actifs.",
            effectiveness = "Exceptionnelle en surface par temps calme. Attaque visuelle spectaculaire.",
            targetFish = listOf("Brochet", "Loup de mer", "Perche", "Black Bass"),
            acquisition = "Achat spécialisé leurres surface",
            tips = "Animation saccadée obligatoire. Réservé aux poissons actifs en surface.",
            price = "Cher",
            level = "Niveau 30+",
            timeOfDay = "Matin et soir",
            waterType = "Eau douce et mer",
            depth = "Surface exclusive",
            hookSize = "Triples intégrés"
        ),

        BaitInfo(
            name = "Jerkbaits",
            englishName = "Jerkbait",
            category = "Leurres - Poissons nageurs",
            description = "Leurre dur pour animation saccadée. Imite parfaitement un poisson blessé.",
            effectiveness = "Très efficace avec animation stop and go. Déclenche l'instinct de prédation.",
            targetFish = listOf("Brochet", "Sandre", "Perche", "Loup de mer"),
            acquisition = "Achat magasins leurres avancés",
            tips = "Animation jerking obligatoire. Technique exigeante mais très efficace.",
            price = "Cher",
            level = "Niveau 35+",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce et mer",
            depth = "Variable",
            hookSize = "Triples renforcés"
        ),

        BaitInfo(
            name = "Cuillères",
            englishName = "Spoon",
            category = "Leurres - Cuillers",
            description = "Cuiller ondulante classique. Action hypnotique efficace à toutes vitesses.",
            effectiveness = "Très polyvalente, efficace à toutes profondeurs. Action attractive universelle.",
            targetFish = listOf("Brochet", "Perche", "Sandre", "Truite", "Saumon"),
            acquisition = "Achat magasins pêche généraliste",
            tips = "Récupération variée possible. Leurre incontournable pour débuter aux leurres.",
            price = "Bon marché à modéré",
            level = "Niveau 20+",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce et mer",
            depth = "Variable selon poids",
            hookSize = "Simple ou triple"
        ),

        BaitInfo(
            name = "Spinners",
            englishName = "Spinner",
            category = "Leurres - Cuillers tournantes",
            description = "Cuiller à palette tournante. Vibrations irrésistibles pour prédateurs actifs.",
            effectiveness = "Excellent pour prédateurs actifs. Vibrations perçues de loin.",
            targetFish = listOf("Brochet", "Perche", "Truite", "Sandre", "Saumon"),
            acquisition = "Achat magasins pêche aux leurres",
            tips = "Récupération constante obligatoire. Stop and go très efficace.",
            price = "Modéré",
            level = "Niveau 22+",
            timeOfDay = "Jour optimal",
            waterType = "Eau douce",
            depth = "Surface à mi-eau",
            hookSize = "Triple intégré"
        ),

        BaitInfo(
            name = "Plastique Souple",
            englishName = "Soft Plastic",
            category = "Leurres - Souples",
            description = "Leurre souple polyvalent. Texture réaliste et animation naturelle.",
            effectiveness = "Très efficace avec animation variée. Texture réaliste trompe les poissons méfiants.",
            targetFish = listOf("Sandre", "Perche", "Brochet", "Lieu", "Loup de mer"),
            acquisition = "Achat magasins leurres souples",
            tips = "Montage avec tête plombée. Animation lente et naturelle recommandée.",
            price = "Bon marché",
            level = "Niveau 25+",
            timeOfDay = "Toute la journée",
            waterType = "Eau douce et mer",
            depth = "Fond à surface",
            hookSize = "Simple texan"
        ),

        BaitInfo(
            name = "Swimbaits",
            englishName = "Swimbait",
            category = "Leurres - Souples",
            description = "Leurre imitant parfaitement un poisson nageur. Réalisme extrême pour gros prédateurs.",
            effectiveness = "Exceptionnelle pour très gros prédateurs. Réalisme troublant.",
            targetFish = listOf("Brochet géant", "Silure", "Sandre trophée", "Loup de mer"),
            acquisition = "Achat spécialisé leurres premium",
            tips = "Récupération très lente. Réservé aux gros poissons et pêcheurs expérimentés.",
            price = "Très cher",
            level = "Niveau 40+",
            timeOfDay = "Crépuscule et nuit",
            waterType = "Eau douce et mer",
            depth = "Mi-eau",
            hookSize = "Hameçons renforcés"
        ),

        BaitInfo(
            name = "Spinnerbaits",
            englishName = "Spinnerbait",
            category = "Leurres - Spécialisés",
            description = "Leurre à palettes multiples avec jupe. Anti-accroc parfait pour zones encombrées.",
            effectiveness = "Excellent en zones encombrées. Palettes attractives et montage anti-accroc.",
            targetFish = listOf("Brochet", "Black Bass", "Perche", "Sandre"),
            acquisition = "Achat magasins spécialisés bass fishing",
            tips = "Parfait pour herbiers et obstacles. Récupération constante avec pauses.",
            price = "Cher",
            level = "Niveau 35+",
            timeOfDay = "Jour et crépuscule",
            waterType = "Eau douce",
            depth = "Surface à mi-eau",
            hookSize = "Simple intégré"
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
    fun BaitInfo.getLocalizedName(context: Context): String {
        return when (LanguageManager.getCurrentLanguage(context)) {
            LanguageManager.Language.ENGLISH -> this.englishName
            LanguageManager.Language.FRENCH -> this.name
        }
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