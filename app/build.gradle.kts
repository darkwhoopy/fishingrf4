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
        versionCode = 14
        versionName = "0.8b"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ✅ AJOUT : Support multilingue pour AAB
        resourceConfigurations += listOf("fr", "en")
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    }

    // ✅ AJOUT : Configuration AAB pour garder toutes les langues
    bundle {
        language {
            enableSplit = false
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