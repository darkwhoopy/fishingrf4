plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // ✅ CORRECTION : Version compatible avec Kotlin 2.0.21
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.gms.google-services")
}

android {
    namespace = "com.rf4.fishingrf4"
    compileSdk = 36

    // Configuration de signature
    signingConfigs {
        create("release") {
            storeFile = file("C:/Users/darkw/Documents/rf4-final.jks")
            storePassword = "#Whoopy62"
            keyAlias = "rf4"
            keyPassword = "#Whoopy62"
        }
    }

    defaultConfig {
        applicationId = "com.rf4.fishingrf4"
        minSdk = 26
        targetSdk = 36
        versionCode = 20
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ✅ AJOUT : Support multilingue pour AAB
        resourceConfigurations += listOf("fr", "en")
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            // Enables resource shrinking.
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // 🚀 SOLUTION : Génération des symboles de débogage natifs
            ndk {
                debugSymbolLevel = "FULL"
            }

            // ✅ OPTIONNEL : Désactiver le débogage en release
            isDebuggable = false
        }

        // ✅ AJOUT : Configuration debug pour les symboles aussi
        debug {
            isDebuggable = true
            ndk {
                debugSymbolLevel = "SYMBOL_TABLE"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        // ✅ AJOUT : Pour accéder à BuildConfig.VERSION_NAME
        buildConfig = true
    }

    // ✅ AJOUT : Configuration AAB pour garder toutes les langues
    bundle {
        language {
            enableSplit = false
        }
    }

    // 🎯 AJOUT : Optimisations pour les bibliothèques natives
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }

        // Pour les bibliothèques natives (si vous en ajoutez plus tard)
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    // Android Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.foundation)
    implementation(libs.androidx.media3.common.ktx)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ViewModel & Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.9.0")

    // ✅ CORRECTION : Version compatible avec Kotlin 2.0.21
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Icons
    implementation("androidx.compose.material:material-icons-extended:1.6.8")

    // Firebase
    implementation("com.google.firebase:firebase-auth-ktx:23.1.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.1")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.4.0")
}