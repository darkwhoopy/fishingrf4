# Add project specific ProGuard rules here.
# RF4 Fishing Assistant - Configuration ProGuard

# ==========================================
# RÈGLES GÉNÉRALES
# ==========================================

# Conserve les numéros de ligne pour les crash reports
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes *Annotation*

# Conserve les classes d'exception
-keep public class * extends java.lang.Exception

# ==========================================
# CONSERVATION APPLICATION RF4
# ==========================================

# Conserve toutes les classes de l'application
-keep class com.rf4.fishingrf4.** { *; }

# Conserve les modèles de données
-keep class com.rf4.fishingrf4.data.** { *; }
-keep class com.rf4.fishingrf4.model.** { *; }

# Conserve les classes UI Compose
-keep class com.rf4.fishingrf4.ui.** { *; }

# ==========================================
# COMPOSE SPÉCIFIQUE
# ==========================================

# Conserve Compose
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.material3.** { *; }

# Navigation Compose
-keep class androidx.navigation.compose.** { *; }

# ==========================================
# KOTLIN ET COROUTINES
# ==========================================

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

# Coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ==========================================
# ANDROID JETPACK
# ==========================================

# ViewModel
-keep class androidx.lifecycle.** { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }

# DataStore
-keep class androidx.datastore.** { *; }
-keep class androidx.datastore.preferences.** { *; }

# ==========================================
# RESSOURCES
# ==========================================

# Conserve les classes R
-keep class **.R
-keep class **.R$* {
    <fields>;
}

# Conserve les drawables et layouts
-keep class androidx.appcompat.widget.** { *; }

# ==========================================
# OPTIMISATIONS DE SÉCURITÉ
# ==========================================

# Supprime les logs en production (optionnel)
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# ==========================================
# RÈGLES POUR DEBUG SYMBOLS
# ==========================================

# Conserve les noms de méthodes natives
-keepclasseswithmembernames class * {
    native <methods>;
}

# Conserve les informations de débogage
-keepattributes SourceFile,LineNumberTable,LocalVariableTable

# ==========================================
# RÈGLES POUR CRASH REPORTING
# ==========================================

# Firebase Crashlytics (si utilisé plus tard)
# -keep class com.google.firebase.crashlytics.** { *; }
# -dontwarn com.google.firebase.crashlytics.**