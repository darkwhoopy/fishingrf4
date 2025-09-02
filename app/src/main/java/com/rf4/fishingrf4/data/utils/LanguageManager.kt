package com.rf4.fishingrf4.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.*

/**
 * Gestionnaire de langues pour l'application RF4 Assistant
 * Gère le changement de langue en temps réel
 */
object LanguageManager {

    private const val PREF_LANGUAGE = "app_language"
    private const val PREF_NAME = "language_settings"

    /**
     * Énumération des langues supportées
     */
    enum class Language(
        val code: String,
        val displayName: String,
        val nativeName: String,
        val emoji: String
    ) {
        FRENCH("fr", "Français", "Français", "🇫🇷"),
        ENGLISH("en", "English", "English", "🇺🇸");

        companion object {
            fun fromCode(code: String): Language {
                return values().find { it.code == code } ?: FRENCH
            }

            fun getSystemLanguage(): Language {
                val systemLanguage = Locale.getDefault().language
                return when (systemLanguage) {
                    "en" -> ENGLISH
                    "fr" -> FRENCH
                    else -> ENGLISH // Anglais par défaut pour les autres langues
                }
            }
        }
    }

    /**
     * Obtient la langue sauvegardée ou celle du système
     */
    fun getSavedLanguage(context: Context): Language {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedCode = prefs.getString(PREF_LANGUAGE, null)

        return if (savedCode != null) {
            Language.fromCode(savedCode)
        } else {
            Language.getSystemLanguage()
        }
    }

    /**
     * Sauvegarde la langue choisie
     */
    private fun saveLanguage(context: Context, language: Language) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_LANGUAGE, language.code).apply()
    }

    /**
     * Change la langue de l'application
     */
    fun setAppLanguage(context: Context, language: Language) {
        saveLanguage(context, language)
        updateContextLanguage(context, language)

        // Redémarre l'activité pour appliquer la langue
        if (context is Activity) {
            context.recreate()
        }
    }

    /**
     * Met à jour la configuration linguistique du contexte
     */
    private fun updateContextLanguage(context: Context, language: Language) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    /**
     * Applique la langue sauvegardée au démarrage de l'activité
     * À appeler dans onCreate() de MainActivity
     */
    fun applyLanguage(context: Context) {
        val savedLanguage = getSavedLanguage(context)
        updateContextLanguage(context, savedLanguage)
    }

    /**
     * Obtient la langue actuelle
     */
    fun getCurrentLanguage(context: Context): Language {
        return getSavedLanguage(context)
    }

    /**
     * Vérifie si une langue est actuellement sélectionnée
     */
    fun isLanguageSelected(context: Context, language: Language): Boolean {
        return getCurrentLanguage(context) == language
    }
}

/**
 * Extension pour Context pour faciliter l'utilisation
 */
fun Context.getCurrentLanguage(): LanguageManager.Language {
    return LanguageManager.getCurrentLanguage(this)
}

fun Context.setLanguage(language: LanguageManager.Language) {
    LanguageManager.setAppLanguage(this, language)
}

/**
 * Composable pour gérer la langue dans l'interface
 */
@Composable
fun rememberCurrentLanguage(): LanguageManager.Language {
    val context = LocalContext.current
    return remember { LanguageManager.getCurrentLanguage(context) }
}

/**
 * Hook Composable pour détecter les changements de langue
 */
@Composable
fun LanguageProvider(
    content: @Composable (LanguageManager.Language) -> Unit
) {
    val context = LocalContext.current
    var currentLanguage by remember { mutableStateOf(LanguageManager.getCurrentLanguage(context)) }

    content(currentLanguage)
}